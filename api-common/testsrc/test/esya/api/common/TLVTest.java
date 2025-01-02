package test.esya.api.common;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.asn1.TLVInfo;
import tr.gov.tubitak.uekae.esya.api.common.asn1.TLVUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class TLVTest {

    @Test
    public void testToParseLicenseFile() throws Exception {
        String mLicenseCertStr = "-----BEGIN CERTIFICATE-----\n" +
                "MIIE6jCCAtKgAwIBAgIBLDANBgkqhkiG9w0BAQsFADAhMR8wHQYDVQQDDBZNQTMg" +
                "TGlzYW5zIFN1bnVjdXN1IDAxMB4XDTEwMDMxODExMzAxNloXDTMwMDMxODExMzAx" +
                "NlowITEfMB0GA1UEAwwWTUEzIExpc2FucyBTdW51Y3VzdSAwMTCCAiIwDQYJKoZI" +
                "hvcNAQEBBQADggIPADCCAgoCggIBAJ6ySMBTleYdNXt4kVPJEOWdWhDPXLPzrmNq" +
                "L/76mR1HzlSsV1NczcE+yuwcUN7lgnh24VhnSRq5mt0nr65GHHbHzILfrt600Obd" +
                "9NlhO+m71lsuMCIxmak6YPb7liQPlXbBAtKV3ZKQdsZ1tbeWm91xZOJWBGHp8SnG" +
                "Q2S7/OsYLpdWRo0m35XyiJeomEDonYTpM+lniws2AQQ4PTfyPoBSXkamQq8CE2ef" +
                "dcraCp3jEZMQ9U1BWqhuZ2s+INDHFFxMwf6a8EfKPgQi//g30DZq00yznE3Hm8Gz" +
                "t24qGnRoQxXyrbWYPxb11lAUDQ65jcv2qpUc3oqLsCWcMnmpgBQ/JZTdcg1QdovM" +
                "Jkiywqrx7ilGPXGh9m14+a8EOq9umdB12LqW1jXstdMYG4y/yBcZoy2tgshJ90ga" +
                "rz3sDOcDJCq5oetUI82YcteiG3MZC+zQPJti7sR4mYOi0oq66ohVyDXiOHfVjSvp" +
                "E4q0oWmvhbzyjep5ga480eEChTH2dDDfl2RaNuRTIf1ume4e1IESy9UbsxveG58c" +
                "QWskXfLOMyAvtLEsC4ck54tBrHEkB411UVx6+8AzNMN//DPE/bYrd5zo3UljklD6" +
                "svoMlDCnZ4ji2FiRVUheTD8am1COredm6lrz8TZk+LS/5SR6n13fXQ1e6UitzzkB" +
                "8R8vcSqNAgMBAAGjLTArMA4GA1UdDwEB/wQEAwIGwDAZBgNVHREEEjAQgQ5saXNh" +
                "bnNAbWEzLm5ldDANBgkqhkiG9w0BAQsFAAOCAgEAAWIBI3nx4TG757HS7tF2+8ZP" +
                "HFNvtRplUn2RlRUNK/HUBaPwasMYI+c+pcgZ+TjwMGSqUSQkyeuNWVQl9rJ7YIj7" +
                "Nwn2/1tbonaoLkRjam+31S8hF3kemmHqjeRDn0D7rGpXM+3ltptGiGFYAGw0T8uy" +
                "LrjrYQb1OlTuYjlHClp66wbn7aFZum/G9PqXM2MMxmRKC513nHVzCDVdWdXgMm+4" +
                "Gez7WggN0Lfj6FuIyxeMKaLTRkcZ6bsKu5sg226CUpZLULm0jiLAMyMH3LKEHxtn" +
                "AFYhm2701O5L6e6MzrXUFEMgGFH196liCfBr0l0qQ9GuSQB9L/7CxZ7mOa14th54" +
                "jLADIIRI95zTTrz2ic7NDCiZD71HXwPdfgoNsGauZc6DVKaTNR6uhRoHiadJCaMY" +
                "Qyaik5TMnXOKil3z6A1tci8JJcOVhzoC9e5ryZA/3mxkx16+UsrBQCGE3zl4GQ+h" +
                "HvlU2ROu0d+EFIeTXpq7qctNb3GGeaW2HCuzR5BvXYUdKzvpM48QdWKWwJsAf8T9" +
                "ewrj07QY9wG/wtrd38n2FdaARH8lAVCIyRPWCrxt6W6DdpanHJ0kOusZTgq02VLg" +
                "ZpN3N063ZCd4g1G/lL9BniiTduj8JZmBWlhA1tA3Sr/WnHpww9Xy+uyX7LsDVjKU" +
                "f+ew1LMAC62qHPSwxEE=" +
                "\n-----END CERTIFICATE-----";



        byte [] bytes = FileUtil.readBytes("T:\\api-parent\\lisans\\lisans.xml");

        TLVInfo signedDataTLVInfo = TLVUtil.parseField(bytes, 0, 1);
        TLVInfo encapsulatedContentInfoTLVInfo = TLVUtil.parseField(bytes, signedDataTLVInfo.getDataStartIndex(), 2);
        TLVInfo octetStringContentTLVInfo = TLVUtil.parseFieldInsideTag(bytes, encapsulatedContentInfoTLVInfo.getDataStartIndex(), 1);
        TLVInfo dataTLVInfo = TLVUtil.parseField(bytes, octetStringContentTLVInfo.getDataStartIndex());


        byte[] contentBytes = Arrays.copyOfRange(bytes, dataTLVInfo.getDataStartIndex(), dataTLVInfo.getDataEndIndex());


        TLVInfo signerInfoOne = TLVUtil.parseField(bytes, signedDataTLVInfo.getDataStartIndex(), 4);// CRLs is NULL

        TLVInfo signedAttrs = TLVUtil.parseField(bytes, signerInfoOne.getDataStartIndex(), 3);
        byte[] signedAttrsBytes = TLVUtil.encode(0x31, bytes, signedAttrs);


        TLVInfo messageDigestAttr = TLVUtil.parseField(bytes, signedAttrs.getDataStartIndex(), 2);
        TLVInfo messageDigestAttrValue = TLVUtil.parseFieldInsideTag(bytes, messageDigestAttr.getDataStartIndex(), 2);
        TLVInfo encapsulatedDigest = TLVUtil.parseFieldInsideTag(bytes, messageDigestAttrValue.getDataStartIndex(),1);
        TLVInfo digestTLVInfo = TLVUtil.parseField(bytes, encapsulatedDigest.getDataStartIndex());
        byte[] digestBytes = Arrays.copyOfRange(bytes, digestTLVInfo.getDataStartIndex(), digestTLVInfo.getDataEndIndex());


        TLVInfo signatureTLV = TLVUtil.parseField(bytes, signerInfoOne.getDataStartIndex(), 5);
        byte[] signatureBytes = Arrays.copyOfRange(bytes, signatureTLV.getDataStartIndex(), signatureTLV.getDataEndIndex());

        TLVInfo digestAlgWithParametersTLV = TLVUtil.parseField(bytes, signerInfoOne.getDataStartIndex(), 2);
        TLVInfo digestAlgTLV = TLVUtil.parseField(bytes, digestAlgWithParametersTLV.getDataStartIndex());
        byte[] digestAlgBytes = Arrays.copyOfRange(bytes, digestAlgTLV.getDataStartIndex(), digestAlgTLV.getDataEndIndex());


        TLVInfo signatureAlgWithParametersTLV = TLVUtil.parseField(bytes, signerInfoOne.getDataStartIndex(), 4);
        TLVInfo signatureAlgTLV = TLVUtil.parseField(bytes, signatureAlgWithParametersTLV.getDataStartIndex());
        byte[] signatureAlgBytes = Arrays.copyOfRange(bytes, signatureAlgTLV.getDataStartIndex(), signatureAlgTLV.getDataEndIndex());


        ByteArrayInputStream cerInputStream = new ByteArrayInputStream(mLicenseCertStr.getBytes("ASCII"));
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(cerInputStream);


        Signature verifier = Signature.getInstance("SHA1withRSA");
        verifier.initVerify(cert);
        verifier.update(signedAttrsBytes);
        boolean verify = verifier.verify(signatureBytes);

        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(contentBytes);
        byte[] digest = md.digest();
        boolean equals = Arrays.equals(digest, digestBytes);

        Assert.assertTrue(verify && equals);
    }



}
