package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import java.security.Key;
import java.security.PublicKey;

/**
 * @author ahmety
 * date: May 8, 2009
 */
public interface SignatureFormat
{

    XMLSignature createCounterSignature() throws XMLSignatureException;

    void addKeyInfo(ECertificate aCertificate);
    void addKeyInfo(PublicKey pk) throws XMLSignatureException;
    XMLSignature sign(BaseSigner aSigner) throws XMLSignatureException;
    //XMLSignature sign(ECertificate aCertificate, CardType aCardType, char[] aPass) throws XMLSignatureException;
    //XMLSignature sign(ECertificate aCertificate) throws XMLSignatureException;
    XMLSignature sign(Key aKey) throws XMLSignatureException;

    SignatureValidationResult validateCore() throws XMLSignatureException;
    SignatureValidationResult validateCore(Key aKey) throws XMLSignatureException;
    SignatureValidationResult validateCore(ECertificate aCertificate) throws XMLSignatureException;
    SignatureValidationResult validateSignatureValue(Key aKey) throws XMLSignatureException;

    SignatureFormat evolveToT() throws XMLSignatureException;
    SignatureFormat evolveToC() throws XMLSignatureException;
    SignatureFormat evolveToX1() throws XMLSignatureException;
    SignatureFormat evolveToX2() throws XMLSignatureException;
    SignatureFormat evolveToXL() throws XMLSignatureException;
    SignatureFormat evolveToA() throws XMLSignatureException;
    SignatureFormat addArchiveTimeStamp() throws XMLSignatureException;
}
