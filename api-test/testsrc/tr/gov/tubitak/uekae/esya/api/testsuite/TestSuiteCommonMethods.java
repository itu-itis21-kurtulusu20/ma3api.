package tr.gov.tubitak.uekae.esya.api.testsuite;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.PathValidationRecord;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevocationCheckResult;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.util.List;

public class TestSuiteCommonMethods {

    public static final String FOLDER = "T:\\api-test\\";

    public static final String PLAINFILENAME = "T:\\api-parent\\resources\\testdata\\sample.txt";
    public static final String PLAINPDFFILENAME = "T:\\api-parent\\resources\\testdata\\sample.pdf";
    public static final String PLAINFILEMIMETYPE = "text/plain";

    public static String rootFolder = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\";
    public static String pfxPin = "123456";

    public static Signer signer;
    public static ECertificate certificate;

    public static boolean runCertificateSelfController(List<CheckResult> selfCheckResultList, String checkerClass, String errorMessage) throws Exception {

        boolean checkerFailed= false;
        String checkText = getChecker(checkerClass).getCheckText();

        for(CheckResult cr: selfCheckResultList){
            if(compareCheckTexts(cr,checkText,errorMessage))
                checkerFailed = true;
        }

        return checkerFailed;
    }

    public static boolean runOCSPCertificateController(CertificateStatusInfo signingCertficateInfo, String checkerName, String errorMessage) throws Exception {

        RevocationStatus revocationStatus = null;
        RevocationStatusInfo revocationInfo = signingCertficateInfo.getRevocationInfo();
        if(revocationInfo != null)
            revocationStatus = revocationInfo.getRevocationStatus();

        boolean checkerFailed = false;
        String checkText = getChecker(checkerName).getCheckText();

        if(revocationStatus != RevocationStatus.REVOKED) {
            List<CheckResult> ocspCertificateCheckResultList = signingCertficateInfo.getDetails();
            for (CheckResult cr : ocspCertificateCheckResultList) {
                if (compareCheckTexts(cr,checkText,errorMessage))
                    checkerFailed = true;
            }
        }
        else {
            List<RevocationCheckResult> revocationCheckDetails = signingCertficateInfo.getRevocationCheckDetails();
            for (RevocationCheckResult rcr : revocationCheckDetails) {
                if (compareCheckTexts(rcr,checkText,errorMessage))
                    checkerFailed = true;
            }
        }
        return checkerFailed;
    }

    public static boolean runCertificateRevocationController(List<RevocationCheckResult> revocationCheckResultList, String checkerName, String errorMessage) throws Exception {
        boolean checkerFailed = false;
        String checkText = getChecker(checkerName).getCheckText();

        for (RevocationCheckResult rcr: revocationCheckResultList) {
            if(compareCheckTexts(rcr,checkText,errorMessage))
                checkerFailed = true;
        }
        return checkerFailed;
    }

    public static boolean runCertificateSubrootController(PathValidationRecord pathValidationRecord, String errorMessage) throws Exception {

        boolean checkerFailed = false;
        String result = pathValidationRecord.getResultCode().getMessage();

        if(pathValidationRecord.getErrorIndex() == 1 && result.equals(errorMessage))
            checkerFailed = true;

        return checkerFailed;
    }

    public static Checker getChecker(String className) throws Exception {
        Class checker = Class.forName(className);
        Checker certificateChecker = (Checker) checker.newInstance();

        return certificateChecker;
    }

    public static boolean compareCheckTexts(CheckResult checkResult, String checkText, String errorMessage) throws Exception {
        if (checkResult.getCheckText().equals(checkText)){
            String result = checkResult.getResultText();
            if (result.equals(errorMessage)){
                return true;
            }
        }
        return false;
    }

    public static void parsePfx(String pfx) throws Exception{

        FileInputStream fileInputStream = new FileInputStream(rootFolder + pfx);
        PfxParser pfxParser = new PfxParser(fileInputStream, pfxPin.toCharArray());
        List<Pair<ECertificate, PrivateKey>> entries = pfxParser.getCertificatesAndKeys();

        certificate = entries.get(0).getObject1();
        signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
        signer.init(entries.get(0).getObject2());
        fileInputStream.close();
    }
}


