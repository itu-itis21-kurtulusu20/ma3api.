package dev.esya.api.xmlsignature.legacy.signerHelpers;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 */
public class EPfxSigner /*implements BaseSigner*/ {/*

    private String signatureAlgorithmStr;
    private Certificate signingCert;
    private Key signingBouncyKey;
    private ECertificate signingCertificate;
    private ECertificate signRequestCertificate = null;

    public EPfxSigner(String aSignatureAlgorithmStr, String aPfxFilePath, String aPassword)
    {
        signatureAlgorithmStr = aSignatureAlgorithmStr;
        init(aPfxFilePath,aPassword);
    }

    private void init(String aPfxFilePath, String aPassword)
    {
        try
        {
            FileInputStream fis = new FileInputStream(aPfxFilePath);
            PKCS12KeyStore pkcs12KeyStore = new PKCS12KeyStore();
            pkcs12KeyStore.engineLoad(fis,aPassword.toCharArray());
            fis.close();
            Enumeration aliases = pkcs12KeyStore.engineAliases();
            while(aliases.hasMoreElements())
            {
                String alias = (String)aliases.nextElement();
                signingCert = pkcs12KeyStore.engineGetCertificate(alias);
                ECertificate cert = new ECertificate(signingCert.getEncoded());
                EKeyUsage keyUsage = cert.getExtensions().getKeyUsage();
                boolean isDigitalSignature = keyUsage.isDigitalSignature();
                if(isDigitalSignature)
                {
                    signingBouncyKey = pkcs12KeyStore.engineGetKey(alias,aPassword.toCharArray());
                    signingCertificate = cert;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CertificateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ESYAException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setSignRequestCertificate(ECertificate aSignRequestCertificate)
    {
        signRequestCertificate = aSignRequestCertificate;
    }

    private byte[] signData(byte [] aData) throws ESYAException
    {
        //SUNSigner da olabilir hangisi olmali bilmiyorum=)
        GNUSigner signer = new GNUSigner(SignatureAlg.fromName(signatureAlgorithmStr));
        signer.init((PrivateKey) signingBouncyKey);
        return signer.sign(aData);
    }

    public byte[] sign(byte[] aData) throws ESYAException
    {
        return signData(aData);
    }

    public String getSignatureAlgorithmStr()
    {
        return signatureAlgorithmStr;
    }

    public ECertificate getSigningCertificate()
    {
        return signingCertificate;
    }

    public AlgorithmParameterSpec getAlgorithmParameterSpec()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }  */
}
