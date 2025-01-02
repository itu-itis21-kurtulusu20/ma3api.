package tr.gov.tubitak.uekae.esya.api.infra.crl.crlincache;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.EAsnUtil;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.PublicKey;

/**
 * Created by IntelliJ IDEA.
 * User: zeldalo
 * Date: 20.Nis.2009
 * Time: 10:59:24
 * To change this template use File | Settings | File Templates.
 */
public class CrlInCache {
    private static final Logger logger = LoggerFactory.getLogger(CrlInCache.class);
    private CertificateRevocationTree crt = new CertificateRevocationTree();
    private Certificate crlSignerCA;
    private PublicKey signerCAPublicKey;
    private Version version;  // optional
    private Name issuer;
    private Time thisUpdate;
    private Time nextUpdate;
    private EExtension crlNumberExtension;

    protected CrlInCache() {
        crlSignerCA = null;
    }

    public CrlInCache(Certificate crlSignerCA) throws Exception {
        this.crlSignerCA = crlSignerCA;
        this.signerCAPublicKey = getSignerCAPublicKey(crlSignerCA);
    }

    public CrlInCache(FileInputStream fis, Certificate crlSignerCA) throws Exception {
        this(crlSignerCA);
        feedCrlCache(fis);
    }

    public void feedCrlCache(FileInputStream fis) throws Exception {
        ISignChecker signChecker = createCrlSignChecker(fis, signerCAPublicKey);
        CertificateList certificateList = extractRevocatedListElements(fis, signChecker);
        this.issuer = certificateList.getTbsCertList().issuer;
        EExtensions extensions = new EExtensions(certificateList.getTbsCertList().crlExtensions);
        this.crlNumberExtension = extensions.getCRLNumber();
        this.thisUpdate = certificateList.getTbsCertList().thisUpdate;
        this.nextUpdate = certificateList.getTbsCertList().nextUpdate;
        this.version = certificateList.getTbsCertList().version;
        crt.clear();
        crt.putAll(certificateList.getRevokedCertificateList());
        verifyCrlSignature(signChecker);
    }

    private CertificateList extractRevocatedListElements(FileInputStream inputStream, ISignChecker signChecker) throws ESYAException {
        CertificateList certificateList = new CertificateList(signChecker);
        try {
             certificateList.decode(new Asn1DerInputStream(inputStream));
        } catch (Asn1Exception e) {
            logger.error("CertificateRevocationTree oluştururken Asn1Exception Hatası: ",e);
            throw e;
        } catch (IOException e) {
            logger.error("CertificateRevocationTree oluştururken InputStream Hatası: ",e);
            throw new ESYAException(e);
        }
        return certificateList;
    }

    private void verifyCrlSignature(ISignChecker signChecker) throws ESYAException {
        if (signChecker.verify()) {
            logger.info(" Valid CRL Sign ... ");
        } else {
            logger.error("Invalid CRL Sign !!!!");
            throw new ESYAException("Invalid CRL Sign !!!!");
        }


    }

    private PublicKey getSignerCAPublicKey(Certificate crlSignerCA) throws ESYAException {
/*        Asn1DerEncodeBuffer buffer = new Asn1DerEncodeBuffer();
        crlSignerCA.tbsCertificate.subjectPublicKeyInfo.encode(buffer);*/
        return KeyUtil.decodePublicKey(new ESubjectPublicKeyInfo(crlSignerCA.tbsCertificate.subjectPublicKeyInfo));

    }

    private ISignChecker createCrlSignChecker(FileInputStream inputStream, PublicKey publicKey) throws Exception {
        if (crlSignerCA == null)
            return getDummySignChecker();
        long firstPosition = inputStream.getChannel().position();

        Asn1DerInputStream crlStream = new Asn1DerInputStream(inputStream);

        ISignChecker signChecker = readAndPrepareSignChecker(crlStream, publicKey);
        inputStream.getChannel().position(firstPosition);
        return signChecker;

    }

    private ISignChecker readAndPrepareSignChecker(Asn1DerInputStream crlStream, PublicKey publicKey) throws ESYAException {
        try {
            EAsnUtil.decodeTagAndLengthWithCheckingTag(crlStream, Asn1Tag.SEQUENCE);
            // skip TBSCertList
            int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(crlStream, Asn1Tag.SEQUENCE);
            while (len > 0) {
                int read = (int) crlStream.skip(len);
                logger.debug("trying to read:" + len + " Actual Read :" + read);
                len = len - read;
            }

            AlgorithmIdentifier algID = new AlgorithmIdentifier();
            algID.decode(crlStream);

            Asn1BitString signature = new Asn1BitString();
            signature.decode(crlStream);

            return new SignChecker(algID, publicKey, signature.value);
        }catch (Exception e){
            throw new ESYAException(e);
        }
    }

    public RevokedCertificateInfo checkCertificate(Certificate certificate) throws ESYAException {
        if(!UtilEsitlikler.esitMi(crlSignerCA.tbsCertificate.subject, certificate.tbsCertificate.issuer)){
            logger.error(" checkCertificate, crl signer and certicate issuer are not same !!!.");
            throw new ESYAException("Wrong CA");
        }
        return this.crt.get(certificate.tbsCertificate.serialNumber.value.toByteArray());
    }

    public RevokedCertificateInfo checkCertificate(byte[] serial) {
        return this.crt.get(serial);
    }

    public int getCacheSize() {
        return crt.size();
    }

    public Version getVersion() {
        return version;
    }

    public Name getIssuer() {
        return issuer;
    }

    public Time getThisUpdate() {
        return thisUpdate;
    }

    public Time getNextUpdate() {
        return nextUpdate;
    }

    private ISignChecker getDummySignChecker() {
        ISignChecker signChecker = new ISignChecker() {
            public void collect(Asn1Type aAsn) {

            }
            public void collect(byte[] data) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
            public boolean verify() {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        return signChecker;
    }

    public EExtension getCrlNumberExtension() {
        return crlNumberExtension;
    }
}
