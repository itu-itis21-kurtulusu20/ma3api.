using System;
using System.Collections.Generic;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.manualExamples
{
    [TestFixture]
    public class ImzaZamaniAlma
    {
        private readonly String BESwithSIGNING_TIME = TestConstants.getDirectory() + "testdata\\support\\manual\\BESwithSigningTime.p7s";
        private readonly String ESA = TestConstants.getDirectory() + "testdata\\support\\manual\\ESA1.p7s";


        [Test]
        public void testImzaZamaniAlma1()
        {
            byte[] input = AsnIO.dosyadanOKU(ESA);
            BaseSignedData bs = new BaseSignedData(input);
            EST estSign = (EST)bs.getSignerList()[0];
            DateTime? time = estSign.getTime();
        }

        [Test]
        public void testImzaZamaniAlma2()
        {
            byte[] input = AsnIO.dosyadanOKU(BESwithSIGNING_TIME);
            BaseSignedData bs = new BaseSignedData(input);
            List<EAttribute> attrs = bs.getSignerList()[0].getSignedAttribute(AttributeOIDs.id_signingTime);
            DateTime? time = SigningTimeAttr.toTime(attrs[0]);
            Console.WriteLine(time.Value.ToString());

        }

        [Test]
        public void testImzaZamaniAlma3()
        {
            byte[] input = AsnIO.dosyadanOKU(ESA);
            BaseSignedData bs = new BaseSignedData(input);
            List<EAttribute> attrs = bs.getSignerList()[0].getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);
            List<EAttribute> attrsV2 = bs.getSignerList()[0].getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);
            attrs.AddRange(attrsV2);
            foreach (EAttribute attribute in attrs)
            {
                DateTime? time = ArchiveTimeStampAttr.toTime(attribute);
                Console.WriteLine(time.Value.ToString());
            }

        }
    }
}