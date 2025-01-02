package dev.esya.api.cmssignature.archive;

import bundle.esya.api.cmssignature.testconstans.TestConstants;
import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.CRLFinderFromCRLFinderService;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.CRLFinderFromFile;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyClassInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.RevocationPolicyClassInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampInfo;

import java.util.*;

public class TimeoutESA extends CMSSignatureTest {

    String parentFolder = "T:\\api-cmssignature\\test-output\\java\\ma3\\timoutesa";
    String toBeArchivedFile = parentFolder + "\\toBeArchivedFile.pdf";
    String externalFile = parentFolder + "\\externalFile.pdf"; //ayrık imzalar için orijinal dosya yolu
    String archivedFile = parentFolder + "\\archivedFile.pdf";
    String crlFolder = parentFolder + "\\crls";
    String certFolder = parentFolder + "\\certs";

    @Test
    public void getTSCertificatesFromSignedFile() throws Exception
    {
        FileUtil.cleanDirectory(certFolder);

        byte[] sign = FileUtil.readBytes(toBeArchivedFile);
        BaseSignedData bs = new BaseSignedData(sign);

        List<Signer> signerList = bs.getSignerList();
        for(int i=0; i <signerList.size(); i++)
        {
            List<TimestampInfo> allTimeStamps = signerList.get(i).getAllTimeStamps();
            TimestampInfo timestampInfo = allTimeStamps.get(allTimeStamps.size() - 1);
            ECertificate tsCertificate = timestampInfo.getTSCertificate();
            FileUtil.writeBytes(certFolder + i + "_" + tsCertificate.getSubject().getCommonNameAttribute() + ".cer", tsCertificate.getEncoded());
        }
    }

    @Test
    public void downloadCRLsFromCRLFinderService() throws Exception
    {
        String certificatePath = "Certificate file path for CRL";

        ValidationPolicy policy = TestConstants.getPolicy();
        ValidationSystem validationSystem = CertificateValidation.createValidationSystem(policy);

        ECertificate eCertificate = ECertificate.readFromFile(certificatePath);
        Calendar notAfter = eCertificate.getNotAfter();
        notAfter.add(Calendar.DATE, -1);

        validationSystem.setBaseValidationTime(notAfter);

        CRLFinderFromCRLFinderService crlService = new CRLFinderFromCRLFinderService();
        crlService.setParentSystem(validationSystem);
        List<ECRL> crl = crlService.findCRL(eCertificate);

        for (int i = 0; i < crl.size(); i++) {
            ECRL ecrl = crl.get(i);
            String fileName = crlFolder + i +  "_" + ecrl.getIssuer().getCommonNameAttribute() + "_No " + ecrl.getCRLNumber().toString(10) +".crl";
            FileUtil.writeBytes(fileName, ecrl.getEncoded());
        }

        System.out.println(crl.size());
    }

    @Test
    public void convertToESA() throws Exception {

        ValidationPolicy policy = getPolicy();

        Optional<RevocationPolicyClassInfo> revocationFromCRLChecker = policy.getRevocationCheckers().stream()
                .filter(r -> r.getClassName().equals(RevocationFromCRLChecker.class.getName())).findFirst();
        if(revocationFromCRLChecker.isPresent() == false)
            throw new ESYAException("RevocationFromCRLChecker could not found!");

        ParameterList crlFinderFromFileParameters = new ParameterList();
        crlFinderFromFileParameters.addParameter("dizin", crlFolder);

        PolicyClassInfo crlFinder = new PolicyClassInfo(CRLFinderFromFile.class.getName(), crlFinderFromFileParameters);
        revocationFromCRLChecker.get().addFinder(crlFinder);

        //File file = new File("externalFile");//ayrık imza için
        //ISignable signable = new SignableFile(file, 2048);//ayrık imza için

        byte[] toBeArchivedFileBytes = FileUtil.readBytes(toBeArchivedFile);
        BaseSignedData baseSignedData = new BaseSignedData(toBeArchivedFileBytes);

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(EParameters.P_TSS_INFO, getTSSettings());
        parameters.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
        //parameters.put(EParameters.P_EXTERNAL_CONTENT, signable);//ayrık imza için

        List<Signer> signerList = baseSignedData.getSignerList();
        for(Signer signer : signerList) {
            //Sadece gerekli olan imzaların arşive çevrilmesi gerekiyor. Kullanıcı var olan arşiv kontrolünü eklemeli.
            signer.convert(ESignatureType.TYPE_ESA, parameters);
        }

        ValidationUtil.checkSignatureIsValid(baseSignedData.getEncoded(), null);

        FileUtil.writeBytes(archivedFile, baseSignedData.getEncoded());
    }
}
