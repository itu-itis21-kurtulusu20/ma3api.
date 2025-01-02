package dev.esya.api.xmlsignature.legacy.plugtests2010.create;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transform;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.Include;
import dev.esya.api.xmlsignature.legacy.plugtests2010.PT2010BaseTest;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author ayetgin
 */
public class CreateTest extends PT2010BaseTest
{
    static String BES_BASE = BASELOC +"XAdES-BES.SCOK\\TU\\";
    static String EPES_BASE = BASELOC +"XAdES-EPES.SCOK\\TU\\";
    static String T_BASE = BASELOC +"XAdES-T.SCOK\\TU\\";
    static String C_BASE = BASELOC +"XAdES-C.SCOK\\TU\\";
    static String X_BASE = BASELOC +"XAdES-X.SCOK\\TU\\";
    static String XL_BASE = BASELOC +"XAdES-XL.SCOK\\TU\\";
    static String A_BASE = BASELOC +"XAdES-A.SCOK\\TU\\";

    static String RELATIVE_DATA_DIR = "../../Data/";
    static String SAMPLE_DATA = RELATIVE_DATA_DIR + "ts_101903v010302p.txt";


    // 1 signing certificate & URI
    public void testCreateBES1() throws Exception {
        XMLSignature signature = new XMLSignature(new Context(BES_BASE));
        signature.addDocument(SAMPLE_DATA, null, false);
        signature.addKeyInfo(CERTIFICATE);
        FileOutputStream fos = new FileOutputStream(BES_BASE+"ahmet.crt");
        fos.write(CERTIFICATE.getEncoded());
        fos.close();
        signature.getQualifyingProperties().getSignedSignatureProperties().getSigningCertificate().getCertID(0).setURI("ahmet.crt");
        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-1.xml"));
    }

    // 2 signing time
    public void testCreateBES2() throws Exception {
        XMLSignature signature = new XMLSignature(new Context(BES_BASE));
        signature.addDocument(SAMPLE_DATA, null, false);
        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.getSigningCertificate().getCertID(0).setURI("ahmet.crt");
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-2.xml"));
    }

    // 3 signature production place
    public void testCreateBES3() throws Exception {
        XMLSignature signature = new XMLSignature(new Context(BES_BASE));
        signature.addDocument(SAMPLE_DATA, null, false);
        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));
        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-3.xml"));
    }

    // 4 claimed role
    public void testCreateBES4() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        signature.addDocument(SAMPLE_DATA, null, null, false);
        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(c, "Istanbul", null, null, null));
        ClaimedRole claimedRole = new ClaimedRole(c);
        claimedRole.addContent(getClaimedRoleContent());
        ssp.setSignerRole(new SignerRole(c, new ClaimedRole[]{claimedRole}));
        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-4.xml"));
    }

    // 5 
    public void testCreateBES5() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        signature.addDocument(SAMPLE_DATA, null, false);
        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(c, "Istanbul", null, null, null));

        CertifiedRole r1 = new CertifiedRole(c, ROLE_CERTIFICATE, PKIEncodingType.DER);
        CertifiedRole r2 = new CertifiedRole(c, ROLE_CERTIFICATE, PKIEncodingType.BER);

        ssp.setSignerRole(new SignerRole(c, new CertifiedRole[]{r1, r2}));
        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-5.xml"));
    }


    // 6 data object format
    public void testCreateBES6() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        String ref = "#" + signature.addDocument(SAMPLE_DATA, null, false);
        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        DataObjectFormat dof = new DataObjectFormat(c, ref, "Technical Specification",
                new ObjectIdentifier(c, new Identifier(c, "http://uri.etsi.org/01903/v1.3.2#", null),
                "ETSI TS 101 903 V1.3.2 (2006-03)",
                Arrays.asList("http://www.w3.org/TR/XAdES/",
                              "file:///test.test/schemas/xades.xsd")),
                "text/plain", "http://www.ietf.org/rfc/rfc2279.txt");

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();
        sdop.addDataObjectFormat(dof);

        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-6.xml"));
    }

    // 7 data object format 2, one of them refers to embebdded object
    public void testCreateBES7() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        String ref = "#"+ signature.addDocument(SAMPLE_DATA, null, false);

        Document fileDocument = new FileDocument(new File(DATA_DIR +"ts_101903v010302p.txt"));

        String docId = "#"+signature.addPlainObject(new String(fileDocument.getBytes()), "text/plain", "http://www.ietf.org/rfc/rfc2279.txt");
        String ref2 = "#" + signature.addDocument(docId, null, false);

        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();
        sdop.addDataObjectFormat(createTestDataObjectFormat(c, ref));
        sdop.addDataObjectFormat(createTestDataObjectFormat(c, ref2));

        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-7.xml"));
    }

    // 8 commitment type indication
    public void testCreateBES8() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);

        String objId = signature.addPlainObject("Hi there!", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId, null, false);

        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();
        sdop.addCommitmentTypeIndication(createTestCTI(c, ref1, ref2));

        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-8.xml"));
    }



    // 9 individual data objects timestamp
    public void testCreateBES9() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);
        String objId3 = signature.addPlainObject("Test data 2.", "text/plain", null);
        String ref3 = "#"+signature.addDocument("#"+objId3, null, false);
        String objId4 = signature.addPlainObject("Test data 3.", "text/plain", null);
        String ref4 = "#"+signature.addDocument("#"+objId4, null, false);
        String objId5 = signature.addPlainObject("Test data 4.", "text/plain", null);
        String ref5 = "#"+signature.addDocument("#"+objId5, null, false);

        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();

        IndividualDataObjectsTimeStamp timeStamp = new IndividualDataObjectsTimeStamp(c);
        timeStamp.addInclude(new Include(c, ref1, Boolean.TRUE));
        timeStamp.addInclude(new Include(c, ref3, Boolean.TRUE));

        timeStamp.addEncapsulatedTimeStamp(signature);

        sdop.addIndividualDataObjectsTimeStamp(timeStamp);

        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-9.xml"));
    }

    // 10 all data objects timestamp
    public void testCreateBES10() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();

        AllDataObjectsTimeStamp timeStamp = new AllDataObjectsTimeStamp(c, signature);
        sdop.addAllDataObjectsTimeStamp(timeStamp);

        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-10.xml"));
    }

    // 11 counter signature
    public void testCreateBES11() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);

        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        signature.sign(PRIVATEKEY);

        XMLSignature counterSig = signature.createCounterSignature();
        counterSig.addKeyInfo(CERTIFICATE);
        counterSig.sign(PRIVATEKEY);

        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-11.xml"));
    }

    // 15 all except counter
    public void testCreateBES15() throws Exception {
        Context c = new Context(BES_BASE);
        XMLSignature signature = new XMLSignature(c);
        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();

        sdop.addDataObjectFormat(createTestDataObjectFormat(c, ref1));
        sdop.addCommitmentTypeIndication(createTestCTI(c, ref1, ref2));

        AllDataObjectsTimeStamp timeStamp = new AllDataObjectsTimeStamp(c, signature);
        sdop.addAllDataObjectsTimeStamp(timeStamp);

        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X-BES-15.xml"));
    }

    // 16 todo
    // 17 todo

    //  Individual TimeStamp And ValidationData
    public void testCreateBES141_1() throws Exception 
    {
        Context c = new Context(BES_BASE);
        c.getConfig().getParameters().setAddTimestampValidationData(true);
        
        XMLSignature signature = new XMLSignature(c);
        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);
        String objId3 = signature.addPlainObject("Test data 2.", "text/plain", null);
        String ref3 = "#"+signature.addDocument("#"+objId3, null, false);
        String objId4 = signature.addPlainObject("Test data 3.", "text/plain", null);
        String ref4 = "#"+signature.addDocument("#"+objId4, null, false);
        String objId5 = signature.addPlainObject("Test data 4.", "text/plain", null);
        String ref5 = "#"+signature.addDocument("#"+objId5, null, false);

        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();

        IndividualDataObjectsTimeStamp timeStamp = new IndividualDataObjectsTimeStamp(c);
        timeStamp.addInclude(new Include(c, ref1, Boolean.TRUE));
        timeStamp.addInclude(new Include(c, ref3, Boolean.TRUE));

        timeStamp.addEncapsulatedTimeStamp(signature);

        sdop.addIndividualDataObjectsTimeStamp(timeStamp);

        signature.addTimeStampValidationData(timeStamp, Calendar.getInstance());

        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X141-BES-1.xml"));

    }
    
    // 10 all data objects TimeStamp and ValidationData
    public void testCreateBES141_2() throws Exception {
        Context c = new Context(BES_BASE);
        c.getConfig().getParameters().setAddTimestampValidationData(true);
        c.getConfig().getValidationConfig().setCertValidationPolicies(OCSP_POLICIES);

        XMLSignature signature = new XMLSignature(c);
        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        QualifyingProperties qp = signature.getQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();

        AllDataObjectsTimeStamp timeStamp = new AllDataObjectsTimeStamp(c, signature);
        sdop.addAllDataObjectsTimeStamp(timeStamp);

        signature.addTimeStampValidationData(timeStamp, Calendar.getInstance());

        signature.sign(PRIVATEKEY);
        signature.write(new FileOutputStream(BES_BASE + "Signature-X141-BES-2.xml"));
    }
    
    
    /* -------------------------------------
                T Tests
    --------------------------------------- */
    // todo make it work
    public void testCreateT1() throws Exception {
        XMLSignature signature = new XMLSignature(new Context(T_BASE));

        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);
        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();

        signature.write(new FileOutputStream(T_BASE + "Signature-X-T-1.xml"));
    }

    // todo 2
    /* -------------------------------------
                EPES 
    --------------------------------------- */

    public void testCreateEpes1() throws Exception
    {
        Context context = new Context(EPES_BASE);
        XMLSignature signature = new XMLSignature(context);

        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);
        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);


        PolicyId policyId = new PolicyId(context,
                                         new Identifier(context, "http://xades-portal.etsi.org/protected/XAdES/TestCases/Data/PolicyData.xml", null),
                                         "ETSI foo signature policy",
                                         // documentation references
                                         Arrays.asList("http://xades.org/testPolicy1.txt",
                                                       "file:///test/data/xml/testPolicy2.txt",
                                                       "http://test.test/testPolicy3.txt")
                            );
        Transforms transforms = new Transforms(context);
        Map<String, String> prefixNamespace = new HashMap<String, String>();
        prefixNamespace.put("ietf", "http://www.ietf.org");
        prefixNamespace.put("pl", "http://test.test");
        transforms.addTransform(new Transform(context,
                                              TransformType.XPATH.getUrl(),
                                              "self::pl:policy1",
                                              prefixNamespace));

        NoticeReference nr = new NoticeReference(context, "XadesLabaratories Inc.", new Integer[]{1, 5, 26, 50});
        SPUserNotice notice = new SPUserNotice(context, Arrays.asList(nr), Arrays.asList("This is a test policy."));

        SignaturePolicyQualifier spq1 = new SignaturePolicyQualifier(context,"../../Data/PolicyData.xml", null);
        SignaturePolicyQualifier spq2 = new SignaturePolicyQualifier(context,null, notice);

        SignaturePolicyId pid = new SignaturePolicyId(context,
                                                      policyId,
                                                      transforms,
                                                      DigestMethod.SHA_1,
                                                      Arrays.asList(spq1, spq2));

        SignaturePolicyIdentifier spi = new SignaturePolicyIdentifier(context, pid);

        signature.createOrGetQualifyingProperties().getSignedProperties().getSignedSignatureProperties().setSignaturePolicyIdentifier(spi);

        signature.addKeyInfo(CERTIFICATE);
        signature.sign(PRIVATEKEY);

        signature.write(new FileOutputStream(EPES_BASE + "Signature-X-EPES-1.xml"));

    }

    public void testCreateEpes2() throws Exception
    {
        Context context = new Context(EPES_BASE);
        XMLSignature signature = new XMLSignature(context);

        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);
        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        QualifyingProperties qp = signature.createOrGetQualifyingProperties();
        SignedSignatureProperties ssp = qp.getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(ssp.getContext(), "Istanbul", null, null, null));

        SignedDataObjectProperties sdop = qp.getSignedProperties().createOrGetSignedDataObjectProperties();

        sdop.addDataObjectFormat(createTestDataObjectFormat(context, ref1));
        sdop.addCommitmentTypeIndication(createTestCTI(context, ref1, ref2));

        ClaimedRole claimedRole1 = new ClaimedRole(context, "witness for signing the contract");
        ClaimedRole claimedRole2 = new ClaimedRole(context);
        claimedRole2.addContent(getClaimedRoleContent());
        ssp.setSignerRole(new SignerRole(context, new ClaimedRole[]{claimedRole1, claimedRole2}));

        
        PolicyId policyId = new PolicyId(context,
                                         new Identifier(context, "http://xades-portal.etsi.org/protected/XAdES/TestCases/Data/testPolicyBase64.txt", null),
                                         "ETSI foo signature policy",
                                         // documentation references
                                         Arrays.asList("http://xades.org/testPolicy1.txt",
                                                       "file:///test/data/xml/testPolicy2.txt",
                                                       "http://test.test/testPolicy3.txt")
                            );
        Transforms transforms = new Transforms(context);
        transforms.addTransform(new Transform(context,TransformType.BASE64.getUrl()));

        NoticeReference nr = new NoticeReference(context, "XadesLabaratories Inc.", new Integer[]{1, 5, 26, 50});
        SPUserNotice notice = new SPUserNotice(context, Arrays.asList(nr), Arrays.asList("This is a test policy."));

        SignaturePolicyQualifier spq1 = new SignaturePolicyQualifier(context,"../../Data/testPolicyBase64.txt", null);
        SignaturePolicyQualifier spq2 = new SignaturePolicyQualifier(context,null, notice);

        SignaturePolicyId pid = new SignaturePolicyId(context,
                                                      policyId,
                                                      transforms,
                                                      DigestMethod.SHA_1,
                                                      Arrays.asList(spq1, spq2));

        SignaturePolicyIdentifier spi = new SignaturePolicyIdentifier(context, pid);

        signature.createOrGetQualifyingProperties().getSignedProperties().getSignedSignatureProperties().setSignaturePolicyIdentifier(spi);

        signature.addKeyInfo(CERTIFICATE);
        signature.sign(PRIVATEKEY);

        signature.write(new FileOutputStream(EPES_BASE + "Signature-X-EPES-2.xml"));
    }

    /* -------------------------------------
                C Tests
    --------------------------------------- */
    public void testCreateC1() throws Exception
    {
        XMLSignature signature = new XMLSignature(new Context(C_BASE));

        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);
        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();

        signature.upgradeToXAdES_C();

        signature.write(new FileOutputStream(C_BASE + "Signature-X-C-1.xml"));
    }

    public void testCreateC2() throws Exception {
        Context context = new Context(C_BASE);
        context.getConfig().getValidationConfig().setCertValidationPolicies(OCSP_POLICIES);

        XMLSignature signature = new XMLSignature(context);

        String ref1 = "#"+signature.addDocument(SAMPLE_DATA, null, true);
        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();

        signature.upgradeToXAdES_C();

        signature.write(new FileOutputStream(C_BASE + "Signature-X-C-2.xml"));
    }

    // todo c3 with attr. certificate refs


    /*  ------------------------------------------------
            X Tests
     --------------------------------------------------- */
    public void testCreateX1() throws Exception {
        Context context = new Context(X_BASE);
        //context.getConfig().getValidationConfig().setCertificateValidationPolicyFile(CERT_VAL_POLICY_OCSP);

        XMLSignature signature = new XMLSignature(context);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();
        signature.upgradeToXAdES_C();
        signature.upgradeToXAdES_X1();

        signature.write(new FileOutputStream(X_BASE + "Signature-X-X-1.xml"));
    }

    public void testCreateX2() throws Exception {
        Context context = new Context(X_BASE);
        //context.getConfig().getValidationConfig().setCertificateValidationPolicyFile(CERT_VAL_POLICY_OCSP);

        XMLSignature signature = new XMLSignature(context);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();
        signature.upgradeToXAdES_C();
        signature.upgradeToXAdES_X2();

        signature.write(new FileOutputStream(X_BASE + "Signature-X-X-2.xml"));
    }

    public void testCreateX3() throws Exception {
        Context context = new Context(X_BASE);
        context.getConfig().getValidationConfig().setCertValidationPolicies(OCSP_POLICIES);

        XMLSignature signature = new XMLSignature(context);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();
        signature.upgradeToXAdES_C();
        signature.upgradeToXAdES_X1();

        signature.write(new FileOutputStream(X_BASE + "Signature-X-X-3.xml"));
    }

    public void testCreateX4() throws Exception {
        Context context = new Context(X_BASE);
        context.getConfig().getValidationConfig().setCertValidationPolicies(OCSP_POLICIES);

        XMLSignature signature = new XMLSignature(context);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();
        signature.upgradeToXAdES_C();
        signature.upgradeToXAdES_X2();

        signature.write(new FileOutputStream(X_BASE + "Signature-X-X-4.xml"));
    }

    /*  ------------------------------------------------
            XL Tests
     --------------------------------------------------- */

    public void testCreateXL1() throws Exception {
        Context context = new Context(XL_BASE);
        //context.getConfig().getValidationConfig().setCertificateValidationPolicyFile(CERT_VAL_POLICY_OCSP);

        XMLSignature signature = new XMLSignature(context);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();
        signature.upgradeToXAdES_C();
        signature.upgradeToXAdES_X1();
        signature.upgradeToXAdES_XL();

        signature.write(new FileOutputStream(XL_BASE + "Signature-X-XL-1.xml"));
    }

    public void testCreateXL2() throws Exception {
        Context context = new Context(XL_BASE);
        //context.getConfig().getValidationConfig().setCertificateValidationPolicyFile(CERT_VAL_POLICY_OCSP);

        XMLSignature signature = new XMLSignature(context);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();
        signature.upgradeToXAdES_C();
        signature.upgradeToXAdES_X2();
        signature.upgradeToXAdES_XL();

        signature.write(new FileOutputStream(XL_BASE + "Signature-X-XL-2.xml"));
    }

    public void testCreateXL3() throws Exception {
        Context context = new Context(XL_BASE);
        context.getConfig().getValidationConfig().setCertValidationPolicies(OCSP_POLICIES);

        XMLSignature signature = new XMLSignature(context);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();
        signature.upgradeToXAdES_C();
        signature.upgradeToXAdES_X1();
        signature.upgradeToXAdES_XL();

        signature.write(new FileOutputStream(XL_BASE + "Signature-X-XL-3.xml"));
    }

    public void testCreateXL4() throws Exception {
        Context context = new Context(XL_BASE);
        context.getConfig().getValidationConfig().setCertValidationPolicies(OCSP_POLICIES);

        XMLSignature signature = new XMLSignature(context);

        String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
        String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

        signature.addKeyInfo(CERTIFICATE);
        SignedSignatureProperties ssp = signature.getQualifyingProperties().getSignedSignatureProperties();
        ssp.setSigningTime(getTime());
        signature.sign(PRIVATEKEY);

        signature.upgradeToXAdES_T();
        signature.upgradeToXAdES_C();
        signature.upgradeToXAdES_X2();
        signature.upgradeToXAdES_XL();

        signature.write(new FileOutputStream(XL_BASE + "Signature-X-XL-4.xml"));
    }


    






    // -------------------------------------------------------------------------

    private DataObjectFormat createTestDataObjectFormat(Context c, String ref) throws Exception {
        return new DataObjectFormat(c, ref, "Technical Specification",
                new ObjectIdentifier(c, new Identifier(c, "http://uri.etsi.org/01903/v1.3.2#", null),
                "ETSI TS 101 903 V1.3.2 (2006-03)",
                Arrays.asList("http://www.w3.org/TR/XAdES/",
                              "file:///test.test/schemas/xades.xsd")),
                "text/plain", "http://www.ietf.org/rfc/rfc2279.txt");
    }

    private CommitmentTypeIndication createTestCTI(Context c, String ref1, String ref2)
            throws Exception
    {
        CommitmentTypeId typeId = new CommitmentTypeId(
                c,
                new Identifier(c, "http://uri.etsi.org/01903/v1.2.2#ProofOfOrigin", null),
                "Proof of origin indicates that the signer recognizes to have created, approved and sent the signed data object.",
                Arrays.asList(
                        "http://test.test/commitment1.txt</xades:DocumentationReference",
                        "file:///test/data/xml/commitment2.txt</xades:DocumentationReference",
                        "http://test.test/commitment3.txt</xades:DocumentationReference")
                );

        CommitmentTypeQualifier q1 = new CommitmentTypeQualifier(c);
        q1.addContent("test commitment a");
        q1.addContent(getQualifierSampleContent());
        q1.addContent("test commitment b");
        CommitmentTypeQualifier q2 = new CommitmentTypeQualifier(c, "commitment 2");
        CommitmentTypeQualifier q3 = new CommitmentTypeQualifier(c, "commitment 2");

        return new CommitmentTypeIndication(c, typeId,
                                            Arrays.asList(ref1, ref2), false,
                                            Arrays.asList(q1, q2, q3));

    }

    private Element getClaimedRoleContent() throws Exception
    {
            return stringToElement(
                      "<xl:XadesLabs xmlns:xl=\"http://xadeslabs.com/xades\"> \n" +
                      "          <xl:Roles type=\"signer\">\n" +
                      "            <xl:ExecutiveDirector>true</xl:ExecutiveDirector>\n" +
                      "            <xl:ShareHolder>true</xl:ShareHolder>\n" +
                      "            <xl:Vendor>false</xl:Vendor>\n" +
                      "            <xl:Purchaser>false</xl:Purchaser>\n" +
                      "            <xl:Engineer>false</xl:Engineer>\n" +
                      "          </xl:Roles>\n" +
                      "        </xl:XadesLabs>");
    }

    private Element getQualifierSampleContent() throws Exception
    {
        return stringToElement(
                "<xl:XadesLabs xmlns:xl=\"http://xadeslabs.com/xades\"> \n" +
                "          <xl:Commitments type=\"ProofOfOrigin\">\n" +
                "            <xl:Commitment>commitment 1</xl:Commitment>\n" +
                "            <xl:Commitment>commitment 2</xl:Commitment>\n" +
                "            <xl:Commitment>commitment 3</xl:Commitment>\n" +
                "            <xl:Commitment>commitment 4</xl:Commitment>\n" +
                "          </xl:Commitments>\n" +
                "</xl:XadesLabs>");
    }

    private Element stringToElement(String aStr) throws Exception{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new ByteArrayInputStream(aStr.getBytes())).getDocumentElement();
    }

    private static XMLGregorianCalendar getTime(){
        Calendar fake = new GregorianCalendar();
        fake.add(Calendar.SECOND, -10000);
        return XmlUtil.createDate(fake);
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println(""+XmlUtil.createDate().toGregorianCalendar().getTime().getTime());
        System.out.println(""+getTime().toGregorianCalendar().getTime().getTime());
        
        //TestRunner.run(CreateTest.class);
        new CreateTest().testCreateC2();
    }
}
