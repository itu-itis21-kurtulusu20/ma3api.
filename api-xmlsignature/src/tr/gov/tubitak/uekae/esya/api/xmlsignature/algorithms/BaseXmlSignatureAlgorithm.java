package tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms;

import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.Verifier;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ahmety
 * date: Aug 27, 2009
 */
public class BaseXmlSignatureAlgorithm implements XmlSignatureAlgorithm
{
    protected Signer mSigner;
    protected Verifier mVerifier;
    protected SignatureAlg mSignatureAlg;
    protected boolean mSign = false;

    public BaseXmlSignatureAlgorithm(SignatureAlg aSignatureAlg)
    {
        mSignatureAlg = aSignatureAlg;
    }

    public String getAlgorithmName()
    {
        return mSignatureAlg.getName();   
    }

    public void initSign(Key aKey, AlgorithmParams aParameters) throws XMLSignatureException
    {
        if (!(aKey instanceof PrivateKey)) {
            throw new XMLSignatureException("core.invalid.privatekey", "ECDSA", aKey.toString());
        }
        try {
            mSigner = Crypto.getSigner(mSignatureAlg);
            mSigner.init((PrivateKey)aKey,aParameters);
            mSign = true;
        }catch (CryptoException x){
            throw new XMLSignatureException(x, "errors.cantInit", "Signer");
        }
    }

    public void initVerify(Key aKey, AlgorithmParams aParameters) throws XMLSignatureException
    {
        if (!(aKey instanceof PublicKey)) {
            throw new XMLSignatureException("core.invalid.publickey", "ECDSA", aKey.toString());
        }
        try {
            mVerifier = Crypto.getVerifier(mSignatureAlg);
            mVerifier.init((PublicKey)aKey, aParameters);
            mSign = false;
        } catch (CryptoException x){
            throw new XMLSignatureException(x, "errors.cantInit", I18n.translate("signer"));
        }

    }

    public void update(byte[] aData) throws XMLSignatureException
    {
        try {
            if (mSign)
                mSigner.update(aData, 0, aData.length);
            else
                mVerifier.update(aData, 0, aData.length);
        } catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantUpdate", "Signature");
        }

    }

    public byte[] sign() throws XMLSignatureException
    {
        try {
            byte[] asn1Bytes = mSigner.sign(null);
            return asn1Bytes;
        } catch (CryptoException x){
            throw new XMLSignatureException(x, "errors.Sign error");
        }
    }

    public boolean verify(byte[] aSignatureValue) throws XMLSignatureException
    {
        try {
            return mVerifier.verifySignature(aSignatureValue);
        } catch (Exception x){
            throw new XMLSignatureException(x, "errors.verify");
        }
    }


}