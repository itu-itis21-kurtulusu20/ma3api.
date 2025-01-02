package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RefsOnlyTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.SigAndRefsTimeStamp;

/**
 * XML Advanced Electronic Signature with Complete validation data references
 * (XAdES-C) in accordance with the present document adds to the XAdES-T the
 * <code>CompleteCertificateRefs</code> and <code>CompleteRevocationRefs</code>
 * unsigned properties as defined by the present document. If attribute
 * certificates appear in the signature, then XAdES-C also incorporates the
 * <code>AttributeCertificateRefs</code> and the
 * <code>AttributeRevocationRefs</code> elements.
 *
 * <p><code>CompleteCertificateRefs</code> element contains a sequence of
 * references to the full set of CA certificates that have been used to
 * validate the electronic signature up to (but not including) the signing
 * certificate.
 *
 * <p><code>CompleteRevocationRefs</code> element contains a full set of
 * references to the revocation data that have been used in the validation of
 * the signer and CA certificates.
 *
 * <p><code>AttributeCertificateRefs</code> and
 * <code>AttributeRevocationRefs</code> elements contain references to the full
 * set of Attribute Authorities certificates and references to the full set of
 * revocation data that have been used in the validation of the attribute
 * certificates present in the signature, respectively.
 *
 * <p>Storing the references allows the values of the certification path and
 * revocation data to be stored elsewhere, reducing the size of a stored
 * electronic signature format.
 *
 * <p>Below follows the structure for XAdES-C built by direct incorporation of
 * properties on a XAdES-T containing the <code>SignatureTimeStamp</code>
 * signed property. A XAdES-C form based on time-marks MAY exist without such
 * element.
 *
 * @author ahmety
 * date: Nov 9, 2009
 */
public class C extends T
{
    private static Logger logger = LoggerFactory.getLogger(C.class);

    public C(Context aContext, XMLSignature aSignature)
    {
        super(aContext, aSignature);
    }

    @Override
    public SignatureFormat evolveToT() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_C, SignatureType.XAdES_T);
    }

    @Override
    public SignatureFormat evolveToC() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_C);
    }

    @Override
    public SignatureFormat evolveToX1() throws XMLSignatureException
    {
        TimestampConfig tsConfig = mContext.getConfig().getTimestampConfig();
        TSSettings settings = tsConfig.getSettings();
        SigAndRefsTimeStamp sarts = new SigAndRefsTimeStamp(mContext, mSignature, tsConfig.getDigestMethod(), settings);
        UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties();
        usp.addSigAndRefsTimeStamp(sarts);

        // add TS validation data for previous timestamps
        addTimestampValidationDataForSignatureTS();
        addTimestampValidationDataForAllDataObjectsTS();
        addTimestampValidationDataForIndividualDataObjectsTS();

        return new X(mContext , mSignature);
    }

    @Override
    public SignatureFormat evolveToX2() throws XMLSignatureException
    {
        TimestampConfig tsConfig = mContext.getConfig().getTimestampConfig();
        TSSettings settings = tsConfig.getSettings();
        RefsOnlyTimeStamp refsOnlyTimeStamp = new RefsOnlyTimeStamp(mContext, mSignature, tsConfig.getDigestMethod(), settings);
        UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties();
        usp.addRefsOnlyTimeStamp(refsOnlyTimeStamp);

        // add TS validation data for previous timestamps
        addTimestampValidationDataForSignatureTS();
        addTimestampValidationDataForAllDataObjectsTS();
        addTimestampValidationDataForIndividualDataObjectsTS();

        return new X(mContext , mSignature);
    }

    @Override
    public SignatureFormat evolveToXL() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_C, SignatureType.XAdES_X_L, SignatureType.XAdES_X);
    }

    @Override
    public SignatureFormat evolveToA() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_C, SignatureType.XAdES_A, SignatureType.XAdES_X);
    }
}
