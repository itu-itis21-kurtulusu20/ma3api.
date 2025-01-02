package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.ValidationDataCollector;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlSignUtil;

import java.util.Calendar;

/**
 * @author ahmety
 * date: Nov 4, 2009
 */
public class T extends BES
{
    private static Logger logger = LoggerFactory.getLogger(T.class);

    public T(Context aContext, XMLSignature aSignature)
    {
        super(aContext, aSignature);
    }

    @Override
    public SignatureFormat evolveToT() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_T);
    }

    public SignatureFormat evolveToC() throws XMLSignatureException
    {
        CertificateStatusInfo csi = mContext.getValidationResult(mSignature);

        if (csi == null) {
            ECertificate certificate = extractCertificate();
            if (certificate == null)
                throw new XMLSignatureException("errors.cantFindCertificate");


            ValidationResult r1 = new ValidationDataCollector().collect(mSignature, true);

            csi = _validateCertificate(certificate, mContext.getValidationTime(mSignature), true);
            mContext.setValidationResult(mSignature, csi);
        }
        if (csi.getCertificateStatus() == CertificateStatus.VALID) {
            addReferences(csi);
        }
        else {
            logger.warn(XmlSignUtil.verificationInfo(csi));
            throw new XMLSignatureException("validation.certificate.error");
        }
        return new C(mContext, mSignature);
    }


    @Override
    public SignatureFormat evolveToX1() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_T, SignatureType.XAdES_X, SignatureType.XAdES_C);
    }

    @Override
    public SignatureFormat evolveToX2() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_T, SignatureType.XAdES_X, SignatureType.XAdES_C);
    }

    @Override
    public SignatureFormat evolveToXL() throws XMLSignatureException
    {
        addTimestampValidationDataForSignatureTS();
        addTimestampValidationDataForAllDataObjectsTS();
        addTimestampValidationDataForIndividualDataObjectsTS();
        addValidationData();
        return new XL(mContext, mSignature);
    }

    @Override
    public SignatureFormat evolveToA() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_T, SignatureType.XAdES_A, SignatureType.XAdES_C);
    }

    protected void addTimestampValidationDataForAllDataObjectsTS()
            throws XMLSignatureException
    {
        SignedDataObjectProperties  sdop = mSignature.getQualifyingProperties().getSignedDataObjectProperties();
        if (sdop!=null){
            if (sdop.getAllDataObjectsTimeStampCount()>0){
                for (int i=0; i<sdop.getAllDataObjectsTimeStampCount();i++){
                    AllDataObjectsTimeStamp adots  = sdop.getAllDataObjectsTimeStamp(i);
                    addTimestampValidationData(adots, Calendar.getInstance());
                }
            }
        }
    }

    protected void addTimestampValidationDataForIndividualDataObjectsTS()
            throws XMLSignatureException
    {
        SignedDataObjectProperties sdop = mSignature.getQualifyingProperties().getSignedDataObjectProperties();
        if (sdop!=null){
            if (sdop.getIndividualDataObjectsTimeStampCount()>0){
                for (int i=0; i<sdop.getIndividualDataObjectsTimeStampCount();i++){
                    IndividualDataObjectsTimeStamp idots  = sdop.getIndividualDataObjectsTimeStamp(i);
                    addTimestampValidationData(idots, Calendar.getInstance());
                }
            }
        }
    }

    protected void addTimestampValidationDataForSignatureTS()
            throws XMLSignatureException
    {
        UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp !=null){
            if (usp.getSignatureTimeStampCount()>0){
                for (int i=0; i<usp.getSignatureTimeStampCount();i++){
                    SignatureTimeStamp sts  = usp.getSignatureTimeStamp(i);
                    addTimestampValidationData(sts, Calendar.getInstance());
                }
            }
        }
    }

}
