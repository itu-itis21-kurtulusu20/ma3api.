package tr.gov.tubitak.uekae.esya.api.signature.util;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author ayetgin
 */
public class PfxSigner implements BaseSigner
{
    private String signatureAlgorithmStr;
    private SignatureAlg signatureAlg;
    private ECertificate certificate;
    private PrivateKey privateKey;

    private CryptoProvider cryptoProvider;


    public PfxSigner(SignatureAlg aSignatureAlg, InputStream aPfxStream, char[] aPassword)
    {
        init(aSignatureAlg, aPfxStream, aPassword);
    }
    public PfxSigner(SignatureAlg aSignatureAlg, String aPfxFilePath, char[] aPassword)
    {
        try {
            init(aSignatureAlg, new FileInputStream(aPfxFilePath), aPassword );
        } catch (Exception x){
            throw new ESYARuntimeException(x);
        }
    }

    public PfxSigner(SignatureAlg aSignatureAlg, InputStream aPfxStream, char[] aPassword, CryptoProvider cryptoProvider)
    {
        init(aSignatureAlg, aPfxStream, aPassword);
        this.cryptoProvider = cryptoProvider;
    }
    public PfxSigner(SignatureAlg aSignatureAlg, String aPfxFilePath, char[] aPassword,CryptoProvider cryptoProvider)
    {
        try {
            init(aSignatureAlg, new FileInputStream(aPfxFilePath), aPassword );
            this.cryptoProvider = cryptoProvider;
        } catch (Exception x){
            throw new ESYARuntimeException(x);
        }
    }
    private void init(SignatureAlg aSignatureAlg, InputStream aPfxStream, char[] aPassword){
        try {
            signatureAlgorithmStr = aSignatureAlg.getName();
            signatureAlg = aSignatureAlg;

            PfxParser pfxParser = new PfxParser(aPfxStream,aPassword);
            Pair<ECertificate, PrivateKey> firstSigningKeyCertPair = pfxParser.getFirstSigningKeyCertPair();

            if(firstSigningKeyCertPair != null) {
                certificate = firstSigningKeyCertPair.first();
                privateKey = firstSigningKeyCertPair.second();
            } else
                certificate = null;
        }
        catch (Exception x){
            throw new ESYARuntimeException(x);
        }
        if (certificate==null)
            throw new ESYARuntimeException("No signing certificate found in PFX!");

    }

    public byte[] sign(byte[] aData) throws ESYAException
    {
        Signer signer = null;
        if(cryptoProvider != null) {
            signer = cryptoProvider.getSigner(signatureAlg);
        }
        else {
            signer = Crypto.getSigner(signatureAlg);
        }
        signer.init(privateKey, null);
        return signer.sign(aData);
    }

    public String getSignatureAlgorithmStr()
    {
        return signatureAlgorithmStr;
    }

    public AlgorithmParameterSpec getAlgorithmParameterSpec()
    {
        return null;
    }

    public ECertificate getSignersCertificate(){
        return certificate;
    }
}
