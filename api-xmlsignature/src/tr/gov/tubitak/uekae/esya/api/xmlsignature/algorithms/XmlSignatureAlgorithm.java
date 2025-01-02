package tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms;

import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import java.security.Key;

/**
 * Signature algorithm for xml signatures.
 *
 * @author ahmety
 *         date: Aug 26, 2009
 */
public interface XmlSignatureAlgorithm
{
    String getAlgorithmName();

    void initSign(Key aKey, AlgorithmParams aParameters) throws XMLSignatureException;

    void initVerify(Key aKey, AlgorithmParams aParameters) throws XMLSignatureException;

    void update(byte[] aData) throws XMLSignatureException;

    byte[] sign() throws XMLSignatureException;

    boolean verify(byte[] aSignatureValue) throws XMLSignatureException;

}
