package tr.gov.tubitak.uekae.esya.api.common.lcns;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.asn1.TLVInfo;
import tr.gov.tubitak.uekae.esya.api.common.asn1.TLVUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

import java.security.MessageDigest;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Arrays;

// Use this class only for license file.
class SignerInfo {

    private byte [] signedAttrsBytes;
    private byte [] messageDigestAttrSignedHash;
    private byte [] signature;

    private String digestAlg;
    private String signatureAlg;

    private PKCS7 pkcs7;

    public SignerInfo(byte [] bytes, TLVInfo signerInfoTLVInfo) throws ESYAException {

        TLVInfo signedAttrs = TLVUtil.parseField(bytes, signerInfoTLVInfo.getDataStartIndex(), 3);
        signedAttrsBytes = TLVUtil.encode(0x31, bytes, signedAttrs);

        messageDigestAttrSignedHash = getMessageDigestAttr(signedAttrsBytes);

        TLVInfo signatureTLV = TLVUtil.parseField(bytes, signerInfoTLVInfo.getDataStartIndex(), 5);
        signature = Arrays.copyOfRange(bytes, signatureTLV.getDataStartIndex(), signatureTLV.getDataEndIndex());

        TLVInfo digestAlgWithParametersTLV = TLVUtil.parseField(bytes, signerInfoTLVInfo.getDataStartIndex(), 2);
        TLVInfo digestAlgTLV = TLVUtil.parseField(bytes, digestAlgWithParametersTLV.getDataStartIndex());
        byte[] digestAlgBytes = Arrays.copyOfRange(bytes, digestAlgTLV.getDataStartIndex(), digestAlgTLV.getDataEndIndex());

        digestAlg = getDigestAlg(StringUtil.toHexString(digestAlgBytes));

        TLVInfo signatureAlgWithParametersTLV = TLVUtil.parseField(bytes, signerInfoTLVInfo.getDataStartIndex(), 4);
        TLVInfo signatureAlgTLV = TLVUtil.parseField(bytes, signatureAlgWithParametersTLV.getDataStartIndex());
        byte[] signatureAlgBytes = Arrays.copyOfRange(bytes, signatureAlgTLV.getDataStartIndex(), signatureAlgTLV.getDataEndIndex());

        signatureAlg = getSignatureAlg(StringUtil.toHexString(signatureAlgBytes), digestAlg);
    }


    private byte[] getMessageDigestAttr(byte[] signedAttrsBytes) throws ESYAException {
        String messageDigestAttrOID = "06092A864886F70D010904";

        int endIndex = 0;
        int attIndex = 0;

        while(endIndex < signedAttrsBytes.length) {
            TLVInfo attrInfo = TLVUtil.parseField(signedAttrsBytes, 0, attIndex);
            endIndex = attrInfo.getDataEndIndex();

            //Debug
            //byte[] attrBytes = Arrays.copyOfRange(signedAttrsBytes, attrInfo.getTagStartIndex(), attrInfo.getDataEndIndex());
            //System.out.println(StringUtil.toHexString(attrBytes));

            TLVInfo oidInfo = TLVUtil.parseField(signedAttrsBytes, attrInfo.getTagStartIndex(), 0);
            byte[] oidBytes = Arrays.copyOfRange(signedAttrsBytes, oidInfo.getTagStartIndex(), oidInfo.getDataEndIndex());
            String oidStr = StringUtil.toHexString(oidBytes);

            //System.out.println(StringUtil.toHexString(oidBytes)); //Debug

            if(messageDigestAttrOID.equals(oidStr)) {
                TLVInfo attrValueInfo = TLVUtil.parseField(signedAttrsBytes, attrInfo.getTagStartIndex(), 1);
                TLVInfo digestValueInfo = TLVUtil.parseField(signedAttrsBytes, attrValueInfo.getTagStartIndex(), 0);
                byte[] attrValueBytes = Arrays.copyOfRange(signedAttrsBytes, digestValueInfo.getDataStartIndex(), digestValueInfo.getDataEndIndex());
                //String attrValueStr = StringUtil.toHexString(attrValueBytes);
                //System.out.println(attrValueStr);
                return attrValueBytes;
            }
            attIndex++;
        }
        throw new ESYAException("Can not find MessageDigestAttr in license!");
    }

    public void setPKCS7(PKCS7 pkcs7) {
        this.pkcs7 = pkcs7;
    }

    private String getDigestAlg(String digestAlgBytesInHex) throws ESYAException {
        if("2B0E03021A".equals(digestAlgBytesInHex)) {
            return "SHA1";
        } else if("608648016503040201".equals(digestAlgBytesInHex)) {
            return "SHA-256";
        } else if ("608648016503040202".equals(digestAlgBytesInHex)) {
            return "SHA-384";
        } else if ("608648016503040203".equals(digestAlgBytesInHex)) {
            return "SHA-512";
        }
        throw new ESYAException("Unknowm hash algorithm!");
    }

    private String getSignatureAlg(String signatureAlgBytesInHex, String digestAlg) throws ESYAException {

        if(signatureAlgBytesInHex.startsWith("2A8648CE3D0403"))
            signatureAlg = digestAlg.replace("-", "") + "withECDSA";
        else
            signatureAlg = digestAlg.replace("-", "") + "withRSA";

        return signatureAlg;
    }

    // Sertifika dışardan verildiği için güvenilir kabul ediliyor.
    // Dokümanın o sertifika ile imzalanmış olması gerekiyor.
    public boolean verify(X509Certificate cert) throws ESYAException {
        try{
            Signature verifier = Signature.getInstance(signatureAlg);
            verifier.initVerify(cert);
            verifier.update(signedAttrsBytes);
            boolean cryptoCheck = verifier.verify(signature);

            byte[] content = pkcs7.getContent();
            MessageDigest md = MessageDigest.getInstance(digestAlg);
            md.update(content);
            byte[] contentHash = md.digest();

            boolean hashCheck = Arrays.equals(contentHash, messageDigestAttrSignedHash);

            return cryptoCheck && hashCheck;
        } catch (Exception ex){
            throw new ESYAException("Can not verify signature", ex);
        }
    }

}
