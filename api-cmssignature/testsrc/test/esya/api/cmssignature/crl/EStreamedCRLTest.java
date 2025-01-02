package test.esya.api.cmssignature.crl;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyLabel;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.*;

public class EStreamedCRLTest {

    @Test
    public void creationCRLUsingEStreamedCRL_SignerPFX_RSASHA1() throws IOException, ESYAException {
        FileInputStream fis = new FileInputStream("T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_2.p12");
        PfxParser pfxParser = new PfxParser(fis, "123456");
        fis.close();
        PrivateKey privateKey = pfxParser.getFirstPrivateKey();

        ECertificate issuer = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");
        final Signer signer1 = Crypto.getSigner(SignatureAlg.RSA_NONE);
        signer1.init(privateKey);
        final Asn1BigInteger serialInt = new Asn1BigInteger(BigInteger.valueOf(123));
        EExtension eExtension = new EExtension(EExtensions.oid_ce_cRLNumber, false, serialInt);
        EExtensions aSilExtensions = new EExtensions(new EExtension[]{eExtension});

        EStreamedCRL streamedCRL = new EStreamedCRL(
                EVersion.v2, issuer.getIssuer(),
                Calendar.getInstance(), Calendar.getInstance(),
                aSilExtensions,
                new MyRevokedListFetcher(),
                signer1,
                DigestAlg.SHA1.getName(),
                new EAlgorithmIdentifier(DigestAlg.SHA1.getOID(), EAlgorithmIdentifier.ASN_NULL),
                new EAlgorithmIdentifier(SignatureAlg.RSA_SHA1.getOID(), EAlgorithmIdentifier.ASN_NULL)
        );

        ByteArrayOutputStream aOutputStream = new ByteArrayOutputStream();
        streamedCRL.streameYaz(aOutputStream);
        ECRL ecrl = new ECRL(aOutputStream.toByteArray());

        boolean verify = SignUtil.verify(SignatureAlg.RSA_SHA1, ecrl.getTBSEncodedBytes(), ecrl.getSignature(), issuer);
        Assert.assertTrue(verify);
    }

    @Test
    public void creationCRLUsingEStreamedCRL_SignerPFX_RSASHA256() throws IOException, ESYAException {
        FileInputStream fis = new FileInputStream("T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_2.p12");
        PfxParser pfxParser = new PfxParser(fis, "123456");
        fis.close();
        PrivateKey privateKey = pfxParser.getFirstPrivateKey();

        ECertificate issuer = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");
        final Signer signer1 = Crypto.getSigner(SignatureAlg.RSA_NONE);
        signer1.init(privateKey);
        final Asn1BigInteger serialInt = new Asn1BigInteger(BigInteger.valueOf(123));
        EExtension eExtension = new EExtension(EExtensions.oid_ce_cRLNumber, false, serialInt);
        EExtensions aSilExtensions = new EExtensions(new EExtension[]{eExtension});

        EStreamedCRL streamedCRL = new EStreamedCRL(
                EVersion.v2, issuer.getIssuer(),
                Calendar.getInstance(), Calendar.getInstance(),
                aSilExtensions,
                new MyRevokedListFetcher(),
                signer1,
                DigestAlg.SHA256.getName(),
                new EAlgorithmIdentifier(DigestAlg.SHA256.getOID(), EAlgorithmIdentifier.ASN_NULL),
                new EAlgorithmIdentifier(SignatureAlg.RSA_SHA256.getOID(), EAlgorithmIdentifier.ASN_NULL)
        );

        ByteArrayOutputStream aOutputStream = new ByteArrayOutputStream();
        streamedCRL.streameYaz(aOutputStream);
        ECRL ecrl = new ECRL(aOutputStream.toByteArray());

        boolean verify = SignUtil.verify(SignatureAlg.RSA_SHA256, ecrl.getTBSEncodedBytes(), ecrl.getSignature(), issuer);
        System.out.println("EStreamedCRL: " + verify);
        Assert.assertTrue(verify);
    }

    @Test
    public void creationCRLUsingEStreamedCRL_SignerSmartCard_RSAPSS() throws Exception {
        SmartCard sc = new SmartCard(CardType.DIRAKHSM);
        long slot = sc.getSlotList()[0];
        long session = sc.openSession(slot);
        sc.login(session, "12345678");

        // ----- PFX IMPORT
        FileInputStream fis = new FileInputStream("T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_2.p12");
        PfxParser pfxParser = new PfxParser(fis, "123456");
        fis.close();
        List<Pair<ECertificate, PrivateKey>> entries = pfxParser.getCertificatesAndKeys();

        Pair<ECertificate, PrivateKey> entry = entries.get(0);
        ECertificate cert = entry.first();

        String label = "QCA1_2";

        RSAPrivateKeyTemplate privateKeyTemplate = new RSAPrivateKeyTemplate(label, (RSAPrivateCrtKey) entry.second(), null);
        privateKeyTemplate.getAsTokenTemplate(true, true, false);

        RSAPublicKeyTemplate publicKeyTemplate = new RSAPublicKeyTemplate(label, (java.security.interfaces.RSAPublicKey) cert.asX509Certificate().getPublicKey());
        publicKeyTemplate.getAsTokenTemplate(true, true, false);

        RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(publicKeyTemplate, privateKeyTemplate);

        sc.importKeyPair(session, keyPairTemplate);
        sc.importCertificate(session, label,cert.asX509Certificate());
        // ----- PFX IMPORT

        try {
            BaseSigner signer1 = new SCSignerWithKeyLabel(sc, session, slot, label,
                    SignatureAlg.RSA_PSS_RAW.getName(),new RSAPSSParams(DigestAlg.SHA256));

            final Asn1BigInteger serialInt = new Asn1BigInteger(BigInteger.valueOf(123));
            EExtension eExtension = new EExtension(EExtensions.oid_ce_cRLNumber, false, serialInt);
            EExtensions aSilExtensions = new EExtensions(new EExtension[]{eExtension});

            EStreamedCRL streamedCRL = new EStreamedCRL(
                    EVersion.v2, cert.getIssuer(),
                    Calendar.getInstance(), Calendar.getInstance(),
                    aSilExtensions,
                    new MyRevokedListFetcher(),
                    signer1,
                    DigestAlg.SHA256.getName(),
                    new EAlgorithmIdentifier(DigestAlg.SHA256.getOID(), EAlgorithmIdentifier.ASN_NULL),
                    new EAlgorithmIdentifier(SignatureAlg.RSA_PSS_RAW.getOID(), new RSAPSSParams(DigestAlg.SHA256).getEncoded())
            );

            ByteArrayOutputStream aOutputStream = new ByteArrayOutputStream();
            streamedCRL.streameYaz(aOutputStream);
            ECRL ecrl = new ECRL(aOutputStream.toByteArray());

            boolean verify = SignUtil.verify(SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ecrl.getTBSEncodedBytes(), ecrl.getSignature(), cert);
            Assert.assertTrue(verify);
        }finally {
            sc.deleteCertificate(session, label);
            sc.deletePrivateObject(session, label);
            sc.deletePublicObject(session, label);
        }
    }

    // --------

    public static class MyRevokedListFetcher implements EStreamedCRL.RevokedListFetcher {
        private List<ERevokedCertificateElement> certificateElements = new ArrayList<ERevokedCertificateElement>();
        private Iterator<ERevokedCertificateElement> iterator;

        {
            certificateElements.add(get1("125"));
            certificateElements.add(get1("126"));
            iterator = certificateElements.iterator();
        }

        private static ERevokedCertificateElement get1(String number) {
            ETime revocationDate = new ETime();
            try {
                revocationDate.setGeneralTime(new GregorianCalendar());
            } catch (Asn1Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return new ERevokedCertificateElement(new BigInteger(number, 10), revocationDate);
        }

        public ERevokedCertificateElement getNext() throws ESYAException {
            return iterator.hasNext() ? iterator.next() : null;
        }

        public void reset() throws ESYAException {
            iterator = certificateElements.iterator();
        }
    }
}
