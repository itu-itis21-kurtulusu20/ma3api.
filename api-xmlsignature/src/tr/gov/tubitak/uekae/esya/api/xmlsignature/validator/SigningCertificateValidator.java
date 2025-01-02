package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * ETSI TS 101 903 V1.4.1 :
 * <h3>G.2.2.5 Checking SigningCertificate</h3>
 *
 * <p>If the <code>CertificateValues</code> is present, the verifier could get
 * it from this property or from within the <code>ds:KeyInfo</code> element.
 * If the <code>CertificateValues</code> element is not present, the verifier
 * may gain access to the signer's certificate from within the
 * <code>ds:KeyInfo</code>, if present, or by other means that are out of the
 * scope of the present document. In addition, the means allowing the verifier
 * to identify the signer's certificate are out of scope of the present
 * document. </p>
 *
 * <p>Once the verifier has gotten the signing certificate, she should check it
 * against the references present in the <code>ds:SigningCertificate</code>
 * property, if present. For doing this, and for each reference present in the
 * property, the verifier should perform the following tasks:
 * <ol>
 * <li>Compare the name of the issuer and the serial number of the certificate
 * with those indicated in the <code>IssuerSerial</code> element, following the
 * indications given in in XMLDSIG clause 4.4.4 on how to generate the string
 * corresponding to the issuer"s distinguished name. If they do not match take
 * the next reference and re-start again in 1. If they match, continue with 2.
 *
 * <li> If the <code>ds:KeyInfo</code> contains the
 * <code>ds:X509IssuerSerial</code> element, check that the issuer and the
 * serial number indicated in both, that one and <code>IssuerSerial</code>
 * from <code>SigningCertificate</code>, are the same.
 *
 * <li>Check that the content of <code>ds:DigestValue</code> is the result of
 * digesting the certificate with the algorithm indicated in
 * <code>ds:DigestMethod</code> and base-64 encoding this digest.
 * </ol>
 *
 * <p> If the verifier does not find any reference matching the signing
 * certificate, the validation of this property should be taken as failed.</p>
 *
 * <p>If <code>SigningCertificate</code> contains references to other
 * certificates in the path, the verifier should proceed to check
 * each of the certificates in the certification path against them. </p>
 *
 * <p>Should this property contain one or more references to certificates other
 * than those present in the certification path, the verifier should assume
 * that a failure has occurred during the validation.</p>
 *
 * <p>Should one or more certificates in the certification path not be
 * referenced by this property, the verifier should assume that the
 * validation is successful unless the signature policy mandates that
 * references to all the certificates in the certification path
 * "must" be present. </p>
 *
 * @author ahmety
 * date: Oct 1, 2009
 *
 * todo this validator does not work with certificate chains!
 */
public class SigningCertificateValidator implements Validator
{
    private static Logger logger = LoggerFactory.getLogger(SigningCertificateValidator.class);

    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
            throws XMLSignatureException
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp!=null)
        {
            SignedProperties sp = qp.getSignedProperties();
            if (sp!=null)
            {
                SignedSignatureProperties ssp = sp.getSignedSignatureProperties();
                SigningCertificate sc = ssp.getSigningCertificate();
                if (sc!=null)
                {
                    logger.debug("Checking Signing Certificate signed property.");
                    CertID signingCertID = resolveSigningCertificate(sc.getCertIDListCopy());
                    DigestMethod dm = signingCertID.getDigestMethod();

                    // check certificate issuer
                    String issuerDeclared = signingCertID.getX509IssuerName();
                    //String issuerCert = RFC2253Parser.normalize(aCertificate.strSahibiAl());
                    String issuerCert = LDAPDNUtil.normalize(aCertificate.getIssuer().stringValue());
                    if (! issuerCert.equals(issuerDeclared))
                    {
                        String failMessage = I18n.translate("validation.signingCertificate.issuerMismatch", issuerDeclared, issuerCert);
                        logger.warn(failMessage);
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.signingCertificate"),
                                                    failMessage,null, getClass());
                    }

                    // check certificate serial number
                    BigInteger serialDeclared = signingCertID.getX509SerialNumber();
                    BigInteger serialCert = aCertificate.getSerialNumber();
                    if (!serialCert.equals(serialDeclared))
                    {
                        String failMessage = I18n.translate("validation.signingCertificate.serialMismatch", serialCert, serialDeclared);
                        logger.warn(failMessage);
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.signingCertificate"),
                                                    failMessage, null, getClass());
                    }

                    // check certificate digest
                    byte[] digest = KriptoUtil.digest(aCertificate.getEncoded(), dm);
                    boolean certDigestOK = Arrays.equals(digest, signingCertID.getDigestValue());
                    if (!certDigestOK)
                    {
                        String failMessage = I18n.translate("validation.signingCertificate.invalidDigest");
                        logger.warn(failMessage);
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.signingCertificate"),
                                                    failMessage, null, getClass());
                    }

                    return new ValidationResult(ValidationResultType.VALID,
                                                I18n.translate("validation.check.signingCertificate"),
                                                I18n.translate("validation.signingCertificate.valid"),
                                                null, getClass());
                }
            }
            return new ValidationResult(ValidationResultType.INVALID,
                                        I18n.translate("validation.check.signingCertificate"),
                                        I18n.translate("validation.signingCertificate.notFound"),
                                        null, getClass());
        }
        // if no qualifying props, XMLdSig
        return null;
    }

    private CertID resolveSigningCertificate(List<CertID> aList){
        return aList.get(0);
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }

}
