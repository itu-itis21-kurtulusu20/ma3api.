using System;
using System.Security.Cryptography.X509Certificates;
using System.Security.Cryptography;
using tr.gov.tubitak.uekae.esya.api.common.asn1;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.common.lcns
{
    class SignerInfo
    {

        private byte[] signedAttrsBytes;
        private byte[] messageDigestAttrSignedHash;
        private byte[] signature;

        private String digestAlg;
        private String signatureAlg;

        private PKCS7 pkcs7;

        public SignerInfo(byte[] bytes, TLVInfo tlvInfo) 
        {
            TLVInfo signedAttrs = TLVUtil.parseField(bytes, tlvInfo.getDataStartIndex(), 3);
            signedAttrsBytes = TLVUtil.encode(0x31, bytes, signedAttrs);

            messageDigestAttrSignedHash = getMessageDigestAttr(signedAttrsBytes);

            TLVInfo signatureTLV = TLVUtil.parseField(bytes, tlvInfo.getDataStartIndex(), 5);
            signature = TLVUtil.getData(bytes, signatureTLV);
               
            TLVInfo digestAlgWithParametersTLV = TLVUtil.parseField(bytes, tlvInfo.getDataStartIndex(), 2);
            
            TLVInfo digestAlgTLV = TLVUtil.parseField(bytes, digestAlgWithParametersTLV.getDataStartIndex());
            byte[] digestAlgBytes = TLVUtil.getData(bytes, digestAlgTLV);
            digestAlg = getDigestAlg(StringUtil.ToHexString(digestAlgBytes));

            TLVInfo signatureAlgWithParametersTLV = TLVUtil.parseField(bytes, tlvInfo.getDataStartIndex(), 4);
            TLVInfo signatureAlgTLV = TLVUtil.parseField(bytes, signatureAlgWithParametersTLV.getDataStartIndex());

            byte[] signatureAlgBytes = TLVUtil.getData(bytes, signatureAlgTLV);
            signatureAlg = getSignatureAlg(StringUtil.ToHexString(signatureAlgBytes), digestAlg);
        }

        public void setPKCS7(PKCS7 pkcs7)
        {
            this.pkcs7 = pkcs7;
        }

        private byte[] getMessageDigestAttr(byte[] signedAttrsBytes) 
        {
            String messageDigestAttrOID = "2A864886F70D010904";

            int endIndex = 0;
            int attIndex = 0;

            while( endIndex < signedAttrsBytes.Length) {
                TLVInfo attrInfo = TLVUtil.parseField(signedAttrsBytes, 0, attIndex);
                endIndex = attrInfo.getDataEndIndex();

                //Debug
                //byte[] attrBytes = Arrays.copyOfRange(signedAttrsBytes, attrInfo.getTagStartIndex(), attrInfo.getDataEndIndex());
                //System.out.println(StringUtil.toHexString(attrBytes));

                TLVInfo oidInfo = TLVUtil.parseField(signedAttrsBytes, attrInfo.getTagStartIndex(), 0);
                byte[] oidBytes = TLVUtil.getData(signedAttrsBytes, oidInfo);
                String oidStr = StringUtil.ToHexString(oidBytes);

                //System.out.println(StringUtil.toHexString(oidBytes)); //Debug

                if(messageDigestAttrOID.Equals(oidStr)) {
                    TLVInfo attrValueInfo = TLVUtil.parseField(signedAttrsBytes, attrInfo.getTagStartIndex(), 1);
                    TLVInfo digestValueInfo = TLVUtil.parseField(signedAttrsBytes, attrValueInfo.getTagStartIndex(), 0);
                    byte[] attrValueBytes = TLVUtil.getData(signedAttrsBytes, digestValueInfo);
                    //String attrValueStr = StringUtil.toHexString(attrValueBytes); 
                    //System.out.println(attrValueStr);
                    return attrValueBytes;
                }
                attIndex++;
            }
            throw new ESYAException("Can not find MessageDigestAttr in license!");
    }

        private String getDigestAlg(String digestAlgBytesInHex) 
        {
            if ("2B0E03021A".Equals(digestAlgBytesInHex)) 
            {
                return "SHA1";
            }
            else if ("608648016503040201".Equals(digestAlgBytesInHex))
            {
                return "SHA256";
            }
            else if ("608648016503040202".Equals(digestAlgBytesInHex)) 
            {
                return "SHA384";
            }
            else if ("608648016503040203".Equals(digestAlgBytesInHex))
            {
                return "SHA512";
            }

            throw new ESYAException("Unknowm hash algorithm!");
        }

        private String getSignatureAlg(String signatureAlgBytesInHex, String digestAlg) 
        {
            if(signatureAlgBytesInHex.StartsWith("2A8648CE3D0403"))
            {
                return digestAlg + "withECDSA";
            }
            else
            {
                return digestAlg + "withRSA"; 
            }
        }

        // Sertifika dışardan verildiği için güvenilir kabul ediliyor.
        // Dokümanın o sertifika ile imzalanmış olması gerekiyor.
        public bool verify(X509Certificate2 cert)
        {
            try
            {
                
                bool crypto_OK = false;

                if (signatureAlg.Contains("ECDSA"))
                {
                    throw new ESYAException("ECDSA is not supported in .NET 3.5");
                    //.NET 4.6+ veya .NET Standart 2.0 ile kullanılabilir. .NET 4.6+ ile test edildi. .NET Standart 2.0'da test edilmedi.
                    //HashAlgorithmName hashAlgorithm = new HashAlgorithmName(digestAlg);
                    //ECDsa key = cert.GetECDsaPublicKey();
                    //crypto_OK = key.VerifyData(signedAttrsBytes, signature, hashAlgorithm);
                }
                else
                {
                    string publicKeyXML = cert.PublicKey.Key.ToXmlString(false);
                    RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
                    rsa.FromXmlString(publicKeyXML);

                    crypto_OK = rsa.VerifyData(signedAttrsBytes, digestAlg, signature);
                }
                                
                byte[] content = pkcs7.getContent();
                byte[] hashedData = HashAlgorithm.Create(digestAlg).ComputeHash(content); 
                
                bool hash_OK = ArrayUtil.Equals(hashedData, messageDigestAttrSignedHash);
                return crypto_OK && hash_OK;

            }
            catch (Exception e)
            {
                throw new ESYAException("Can not verify signature", e);
            }
        }
    }
}
