package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.AsnUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.EncapsulatedTimeStampValidator;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TSTInfo;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp._pkixtspValues;

import java.util.Calendar;

/**
 * Container for the time-stamp token in form of ASN.1 data object generated
 * by TSA. 
 *
 * @author ahmety
 * date: Sep 29, 2009
 *
 * todo ETSTInfo vs asn wrapper classlari kullan...
 */
public class EncapsulatedTimeStamp extends EncapsulatedPKIData {
    protected static Logger logger = LoggerFactory.getLogger(EncapsulatedTimeStamp.class);
    private XMLSignature signature;
    private GenericTimeStamp parent;
    private EContentInfo mContentInfo;
    private ESignedData mSignedData;
    private ETSTInfo mTSTInfo;
    private Calendar mCalendar;

    public EncapsulatedTimeStamp(Context aContext, XMLSignature signature, XAdESTimeStamp parent, TimeStampResp aResponse)
            throws XMLSignatureException
    {
        super(aContext);

        this.signature = signature;
        this.parent = parent;

        mContentInfo = new EContentInfo(aResponse.timeStampToken);
        try {
            setValue(AsnUtil.encode(aResponse.timeStampToken, mEncoding));
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "core.timestamp.cantEncode");
        }

        if (mContext.isValidateTimeStamps()){
            EncapsulatedTimeStampValidator validator = new EncapsulatedTimeStampValidator(signature, parent);
            ValidationResult vr = validator.verify(this, null);
            if (vr.getType()!= ValidationResultType.VALID){
                //throw new XMLSignatureException("Cant validate timestamp "+parent.getLocalName()+", reason : "+vr.getMessage());
                throw new XMLSignatureException(I18n.translate("validation.timestamp.failed", parent.getLocalName(), vr.getMessage()));
            }

        }
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public EncapsulatedTimeStamp(Element aElement, GenericTimeStamp parent, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        this.parent = parent;

        ContentInfo contentInfo = new ContentInfo();
        //TimeStampResp resp = new TimeStampResp();
        AsnUtil.decode(contentInfo, mValue, mEncoding);
        mContentInfo = new EContentInfo(contentInfo);
        //mContentInfo = resp.timeStampToken;
    }

    public EContentInfo getContentInfo()
    {
        return mContentInfo;
    }

    /*
    from RFC 3160 :
        TimeStampToken ::= ContentInfo
        -- contentType is id-signedData as defined in [CMS]
        -- content is SignedData as defined in([CMS])
        -- eContentType within SignedData is id-ct-TSTInfo
        -- eContent within SignedData is TSTInfo
    */

    public ESignedData getSignedData() throws XMLSignatureException
    {
        if (mSignedData==null){
            try {
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(mContentInfo.getContent());
                SignedData signedData = new SignedData();
                signedData.decode(decBuf);
                mSignedData = new ESignedData(signedData);

            } catch (Exception x){
                throw new XMLSignatureException(x, "core.timestamp.cantExtractSignedData");
            }
        }
        return mSignedData;
    }

    public boolean isTimeStamp() {
        try {
            return getSignedData().getEncapsulatedContentInfo().getContentType().equals(
                        new Asn1ObjectIdentifier(_pkixtspValues.id_ct_TSTInfo));
        }
        catch (Exception e){
            logger.warn("Warning in EncapsulatedTimeStamp", e);
            return false;
        }
    }

    public ETSTInfo getTSTInfo() throws XMLSignatureException
    {
        if (mTSTInfo==null){
            try {
                TSTInfo tstInfo = new TSTInfo();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(getSignedData().getEncapsulatedContentInfo().getContent());
                tstInfo.decode(decBuf);
                mTSTInfo = new ETSTInfo(tstInfo);
            }
            catch (Exception x){
                throw new XMLSignatureException(x, "core.timestamp.cantExtractTSTInfo");
            }
        }
        return mTSTInfo;
    }

    public byte[] getDigestValue() throws XMLSignatureException {
        return getTSTInfo().getHashedMessage();
    }

    public DigestAlg getDigestAlgorithm() throws XMLSignatureException {
        try {
            EAlgorithmIdentifier tsHashAlg = getTSTInfo().getHashAlgorithm();
            return DigestAlg.fromOID(tsHashAlg.getAlgorithm().value);
        } catch (Exception x){
            throw new XMLSignatureException(x, "core.timestamp.cantExtractDigestAlg");
        }
    }


    public Calendar getTime() throws XMLSignatureException {
        if (mCalendar==null){
            try {
                mCalendar = getTSTInfo().getTime();
            } catch (Exception x){
                throw new XMLSignatureException(x, "core.timestamp.cantExtractTime");
            }
        }
        return mCalendar;
    }

    public String getLocalName()
    {
        return Constants.TAGX_ENCAPSULATEDTIMESTAMP;
    }
}
