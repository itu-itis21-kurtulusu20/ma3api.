package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.AllDataObjectsTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.IndividualDataObjectsTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RefsOnlyTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.SigAndRefsTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * <p>Base class for BES TimeStamp validator objects.
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.IndividualDataObjectsTimeStampValidator
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AllDataObjectsTimeStampValidator
 *
 * @author ahmety
 * date: Oct 9, 2009
 */
public abstract class BaseTimeStampValidator implements Validator
{
    public static final int MILLIS_IN_SECONDS = 1000;
    private static Logger logger = LoggerFactory.getLogger(BaseTimeStampValidator.class);

    public ValidationResult validate(XMLSignature aSignature,
                                     ECertificate aCertificate)
            throws XMLSignatureException
    {

        List<? extends XAdESTimeStamp> tsNodes = getTimeStampNodes(aSignature);
        // if no such thing exists return null = no validation
        if (tsNodes==null ||tsNodes.size()<1)
            return null;

        ValidationResult result = new ValidationResult(getClass());

        for (XAdESTimeStamp tsNode : tsNodes)
        {
            logger.debug("Validate "+ tsNode.getLocalName()+" for signature "+aSignature.getId());
            byte[] bytesForTimeStamp = tsNode.getContentForTimeStamp(aSignature);

            // todo? : xml timestampler su an degerlendirilmiyor!
            if (tsNode.getEncapsulatedTimeStampCount()<1){
                return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.timestamp", getTSNodeName()),
                                            I18n.translate("validation.timestamp.cantFind", getTSNodeName()),
                                            null, getClass());
            }

            //For each time-stamp token encapsulated by the property
            for (int index=0; index<tsNode.getEncapsulatedTimeStampCount();  index++){
                EncapsulatedTimeStamp ets = tsNode.getEncapsulatedTimeStamp(index);
                try {
                    /* todo
                    Verify the signature present within the time-stamp token. Rules
                    for acceptance of the validity of the signature within the
                    time-stamp, involving trust decisions, are out of the scope of
                    the present document.
                    */


                    // check if it is Time Stamp !
                    if(!ets.isTimeStamp())
                         return new ValidationResult(ValidationResultType.INVALID,
                                                     I18n.translate("validation.check.timestamp", getTSNodeName()),
                                                    I18n.translate("validation.timestamp.invalidFormat", getTSNodeName(), index),
                                                    null, getClass());


                    /*
                    compute the digest of the resulting octet stream using
                    the algorithm indicated in the time-stamp token and
                    check if it is the same as the digest present there.
                    */
                    byte[] hash = DigestUtil.digest(ets.getDigestAlgorithm(), bytesForTimeStamp);

                    if (!Arrays.equals(hash, ets.getDigestValue())){
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.timestamp", getTSNodeName()),
                                                    I18n.translate("validation.timestamp.invalidDigest", getTSNodeName(), index),
                                                    null, getClass());
                    }

                    /*
                    Check for coherence in the value of the times indicated in the
                    time-stamp tokens. All the time instants have to be previous to
                    the times indicated within the time-stamp tokens enclosed within
                    all the rest of time-stamp container properties except
                    IndividualDataObjectsTimeStamp.
                    */
                    boolean coherent = checkCoherence(result, ets.getTime(), ets, index, tsNode, aSignature, aCertificate);
                    if (!coherent){
                        result.setStatus(ValidationResultType.INVALID,
                                         I18n.translate("validation.check.timestamp", getTSNodeName()),
                                         I18n.translate("validation.timestamp.incoherent"), null);
                        return result;
                    }

                    ValidationResult vri = verifySignature(aSignature, tsNode, ets);
                    result.addItem(vri);
                    if (vri.getType()!=ValidationResultType.VALID){
                        result.setStatus(ValidationResultType.INVALID,
                                         I18n.translate("validation.check.timestamp", getTSNodeName()),
                                         I18n.translate("validation.timestamp.error", getTSNodeName(), index), null);
                        return vri;
                    }

                    index++;

                }
                catch (Exception x){
                    throw new XMLSignatureException(x, "validation.timestamp.error", getTSNodeName(), index);
                }
            }
        }

        result.setStatus(ValidationResultType.VALID,
                         I18n.translate("validation.check.timestamp", getTSNodeName()),
                         I18n.translate("validation.timestamp.valid", getTSNodeName()), null);
        return result;
    }

    protected ValidationResult verifySignature(XMLSignature signature, XAdESTimeStamp aXAdESTimeStamp, EncapsulatedTimeStamp aTimeStamp)
    {
        ValidationResult result = new ValidationResult(getClass());

        try {
            XAdESTimeStamp next = getNextTimeStamp(aXAdESTimeStamp, signature);
            EncapsulatedTimeStampValidator validator = new EncapsulatedTimeStampValidator(signature, aXAdESTimeStamp);
            return validator.verify(aTimeStamp, next != null ? next.getEncapsulatedTimeStamp(0).getTime() : null);

        } catch (Exception x){
            logger.error("Cant verify Timestamp signature", x);
            result.setStatus(ValidationResultType.INCOMPLETE,
                             I18n.translate("validation.check.timestamp", getTSNodeName()),
                             I18n.translate("validation.timestamp.signature.verificationError"), x.getMessage());
            return result;
        }
    }

    abstract List<? extends XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature);

    /**
     * Each timestamp is validated at next timestamp time.
     */
    abstract XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature) throws XMLSignatureException;

    /**
     * Compare current timestamp to other time instants. Note that by design,
     * time stamp validators only check time instants supposed to be before
     * checked timestamp
     *
     * @param aResult               check result will be added here
     * @param aTime                 current timestamps time
     * @param aTimeStampToCheck     current timestamp
     * @param aTimeStampIndex       current timestamps order in container timestamp node
     * @param aContainerTimeStamp   container node
     * @param aSignature            container signature
     * @param aCertificate          signer certificate
     * @return validity info about timesatmp time
     * @throws XMLSignatureException if unexpected  problems arise 
     */
    abstract boolean checkCoherence(ValidationResult aResult,
                                     Calendar aTime,
                                     EncapsulatedTimeStamp aTimeStampToCheck,
                                     int aTimeStampIndex,
                                     XAdESTimeStamp aContainerTimeStamp,
                                     XMLSignature aSignature,
                                     ECertificate aCertificate)
            throws XMLSignatureException;

    abstract String getTSNodeName();

    public String getName()
    {
        return getClass().getSimpleName();
    }

    // utility methods for overriders
    protected boolean checkTimestampVsSigningTime(Calendar aTime, int aTSIndex, XMLSignature aSignature, ValidationResult aResult){

        SignedSignatureProperties ssp = aSignature.getQualifyingProperties().getSignedSignatureProperties();
        int signingTimeTolerance = aSignature.getContext().getConfig().getValidationConfig().getSignatureTimeToleranceInSeconds();

        if (ssp!=null){
            if (ssp.getSigningTime()!=null){

                long stsTimeInLong = aTime.getTimeInMillis();
                long signingTimeInLong = ssp.getSigningTime().toGregorianCalendar().getTimeInMillis();
                int signingTimeToleranceInMilliSeconds = signingTimeTolerance * MILLIS_IN_SECONDS;
                long signingTimeFlex = signingTimeInLong - signingTimeToleranceInMilliSeconds;
                if(stsTimeInLong < signingTimeFlex ) {
                    aResult.addItem(new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.timestamp", getTSNodeName()),
                            I18n.translate("validation.timestamp.cantBeBeforeSigningTime",
                                    getTSNodeName(), aTSIndex, aTime.getTime(), ssp.getSigningTime().toGregorianCalendar().getTime()), null, getClass()));
                    return false;
                }
            }
        }
        // since this methods return only warning, return value is always true
        return true;
    }


    protected boolean checkTimestampVsSigningCertificate(Calendar aTime, int aTSIndex, ECertificate aCertificate, ValidationResult aResult){
        Calendar certStartDate = aCertificate.getNotBefore();
        Calendar certEndDate = aCertificate.getNotAfter();

        if (aTime.after(certEndDate) || aTime.before(certStartDate)){
            aResult.addItem(new ValidationResult(ValidationResultType.INVALID,
                                                 I18n.translate("validation.check.timestamp", getTSNodeName()),
                                        I18n.translate("validation.timestamp.notWithinCertificatePeriod", getTSNodeName(), aTSIndex, aTime.getTime()),
                                        null, getClass()));
            return false;
        }
        return true;
    }

    protected boolean checkTimestampVsDataObjectTimestamps(Calendar aTime, int aTSIndex, XMLSignature aSignature, ValidationResult aResult){

        boolean valid = true;

        SignedDataObjectProperties sdop = aSignature.getQualifyingProperties().getSignedDataObjectProperties();
        if (sdop==null)
            return valid;

        // all data object timestamps
        for (int i=0; i<sdop.getAllDataObjectsTimeStampCount(); i++){
            AllDataObjectsTimeStamp previousNode = sdop.getAllDataObjectsTimeStamp(i);
            for (int j =0; j< previousNode.getEncapsulatedTimeStampCount(); j++){
                EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
            }
        }
        // individual data object timestamps
        for (int i=0; i<sdop.getIndividualDataObjectsTimeStampCount(); i++){
            IndividualDataObjectsTimeStamp previousNode = sdop.getIndividualDataObjectsTimeStamp(i);
            for (int j =0; j< previousNode.getEncapsulatedTimeStampCount(); j++){
                EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
            }
        }
        return valid;
    }

    protected boolean checkTimestampVsRefTimestamps(Calendar aTime, int aTSIndex, XMLSignature aSignature, ValidationResult aResult){

        boolean valid = true;
        UnsignedSignatureProperties usp = aSignature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp==null)
            return valid;

        // all data object timestamps
        for (int i=0; i<usp.getRefsOnlyTimeStampCount(); i++){
            RefsOnlyTimeStamp previousNode = usp.getRefsOnlyTimeStamp(i);
            for (int j =0; j< previousNode.getEncapsulatedTimeStampCount(); j++){
                EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
            }
        }
        // individual data object timestamps
        for (int i=0; i<usp.getSigAndRefsTimeStampCount(); i++){
            SigAndRefsTimeStamp previousNode = usp.getSigAndRefsTimeStamp(i);
            for (int j =0; j< previousNode.getEncapsulatedTimeStampCount(); j++){
                EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
            }
        }
        return valid;
    }

    protected boolean checkTimestampVsSignatureTimestamp(Calendar aTime, int aTSIndex, XMLSignature aSignature, ValidationResult aResult){

        boolean valid = true;
        UnsignedSignatureProperties  usp = aSignature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp==null)
            return valid;

        for (int i=0; i<usp.getSignatureTimeStampCount(); i++){
            SignatureTimeStamp previousNode = usp.getSignatureTimeStamp(i);
            for (int j =0; j< previousNode.getEncapsulatedTimeStampCount(); j++){
                EncapsulatedTimeStamp previousTS = previousNode.getEncapsulatedTimeStamp(j);
                valid &= checkTimestampVsPreviousTimestamp(aTime, aTSIndex, previousTS, previousNode, aResult);
            }
        }
        return valid;
    }


    protected boolean checkTimestampVsPreviousTimestamp(Calendar aTime, int aTSIndex,
                                                        EncapsulatedTimeStamp aPreviousTS, XAdESTimeStamp aPreviousNode,
                                                        ValidationResult aResult)
    {
        Calendar previousTSTime;
        try {
            previousTSTime = aPreviousTS.getTime();
        }
        catch (Exception e){
            logger.warn("Warning in BaseTimeStampValidator", e);
            aResult.addItem(new ValidationResult(ValidationResultType.INVALID,
                                                 I18n.translate("validation.check.timestamp", getTSNodeName()),
                                                 // todo i18n
                                                 e.getMessage()+ " : "+aPreviousNode.getLocalName(),
                                                 null, getClass()));
            return false;
        }

        if (aTime.before(previousTSTime)){
            aResult.addItem(new ValidationResult(ValidationResultType.INVALID,
                                                 I18n.translate("validation.check.timestamp", getTSNodeName()),
                                        I18n.translate("validation.timestamp.cantBeBeforeTimestamp",
                                                       getTSNodeName(), aTSIndex, aTime.getTime(), aPreviousNode.getLocalName(), previousTSTime.getTime()),
                                        null, getClass()));
            return false;
        }
        return true;
    }

}
