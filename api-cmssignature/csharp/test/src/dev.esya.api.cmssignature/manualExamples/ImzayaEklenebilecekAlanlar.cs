using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;

namespace dev.esya.api.cmssignature.manualExamples
{
    [TestFixture]
    public class ImzayaEklenebilecekAlanlar
    {
        String SIGNING_CERTIFICATE_PATH = TestConstants.getDirectory() + "testdata\\support\\yasemin_sign.cer";
        String POLICY_FILE = TestConstants.getDirectory() + "testdata\\support\\policy.xml";
        [Test]
        public void testImzayaEkle()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableByteArray(ASCIIEncoding.ASCII.GetBytes("test")));

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            ValidationPolicy policy = PolicyReader.readValidationPolicy(POLICY_FILE);
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;
            params_[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = SmartOp.findSlotNumber(CardType.AKIS, null);
            long session = sc.openSession(slot);
            sc.login(session, "12345");

            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slot, cert.getSerialNumber().GetData(),
                    SignatureAlg.RSA_SHA1.getName());

            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new SignerLocationAttr("TURKEY", "KOCAELI", new String[] { "TUBITAK UEKAE", "GEBZE" }));
            optionalAttributes.Add(new CommitmentTypeIndicationAttr(CommitmentType.CREATION));
            optionalAttributes.Add(new ContentIdentifierAttr(ASCIIEncoding.ASCII.GetBytes("PL123456789")));

            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, optionalAttributes, params_);

            sc.logout(session);
            sc.closeSession(session);

            //reading Attributes
            BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
            List<EAttribute> attrs;
            Signer aSigner = bs2.getSignerList()[0];

            attrs = aSigner.getAttribute(SigningTimeAttr.OID);
            DateTime? st = SigningTimeAttr.toTime(attrs[0]);
            Console.WriteLine("Signing time: " + st.Value.ToLocalTime().ToString());

            attrs = aSigner.getAttribute(SignerLocationAttr.OID);
            ESignerLocation sl = SignerLocationAttr.toSignerLocation(attrs[0]);
            StringBuilder sb = new StringBuilder();
            foreach (String address in sl.getPostalAddress())
                sb.Append(" " + address);
            Console.WriteLine("\nCountry: " + sl.getCountry() +
                               "\nCity: " + sl.getLocalityName() +
                               "\nAdress: " + sb);

            attrs = aSigner.getAttribute(ContentIdentifierAttr.OID);
            byte[] ci = ContentIdentifierAttr.toIdentifier(attrs[0]);
            Console.WriteLine("\n" + BitConverter.ToString(ci));

            attrs = aSigner.getAttribute(CommitmentTypeIndicationAttr.OID);
            CommitmentType ct = CommitmentTypeIndicationAttr.toCommitmentType(attrs[0]);
            Console.WriteLine("\n" + ct);
        }

    }
}
