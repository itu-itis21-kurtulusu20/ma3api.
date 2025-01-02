package dev.esya.api.cmssignature.ec.compressed;

import gnu.crypto.key.ecdsa.ECDSAKeyPairX509Codec;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by orcun.ertugrul on 07-Mar-18.
 */
public class X509CompressedECCertificate extends X509Certificate {

    ECertificate mCert;

    public X509CompressedECCertificate(ECertificate cert) {
        mCert = cert;
    }

    @Override
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {

    }

    @Override
    public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {

    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public BigInteger getSerialNumber() {
        return mCert.getSerialNumber();
    }


    @Override
    public Principal getIssuerDN() {
        return null;
    }

    @Override
    public Principal getSubjectDN() {
        return null;
    }

    @Override
    public Date getNotBefore() {
        return null;
    }

    @Override
    public Date getNotAfter() {
        return null;
    }

    @Override
    public byte[] getTBSCertificate() throws CertificateEncodingException {
        return mCert.getTBSEncodedBytes();
    }

    @Override
    public byte[] getSignature() {
        return mCert.getSignatureValue();
    }

    @Override
    public String getSigAlgName() {
        return null;
    }

    @Override
    public String getSigAlgOID() {
        return null;
    }

    @Override
    public byte[] getSigAlgParams() {
        return new byte[0];
    }

    @Override
    public boolean[] getIssuerUniqueID() {
        return new boolean[0];
    }

    @Override
    public boolean[] getSubjectUniqueID() {
        return new boolean[0];
    }

    @Override
    public boolean[] getKeyUsage() {
        return new boolean[0];
    }

    @Override
    public int getBasicConstraints() {
        return 0;
    }

    @Override
    public byte[] getEncoded() throws CertificateEncodingException {
        return mCert.getEncoded();
    }

    @Override
    public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {

    }

    @Override
    public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {

    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public PublicKey getPublicKey() {
        try {
            ECDSAPublicKey pKey = (ECDSAPublicKey) new ECDSAKeyPairX509Codec().decodePublicKey(mCert.getSubjectPublicKeyInfo().getEncoded());
            return new CustomECDSAPublicKey(pKey, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;

    }

    @Override
    public boolean hasUnsupportedCriticalExtension() {
        return false;
    }

    @Override
    public Set<String> getCriticalExtensionOIDs() {
        return null;
    }

    @Override
    public Set<String> getNonCriticalExtensionOIDs() {
        return null;
    }

    @Override
    public byte[] getExtensionValue(String oid) {
        return new byte[0];
    }
}
