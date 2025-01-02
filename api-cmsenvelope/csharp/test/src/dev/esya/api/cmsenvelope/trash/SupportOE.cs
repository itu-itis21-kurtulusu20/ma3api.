using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace dev.esya.api.cmsenvelope
{
    [TestFixture]
    class SupportOE
    {
        [Test]
        public void testSC()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long sessionID = sc.openSession(1);
            byte[] randBytes = sc.getRandomData(sessionID, 32);

            Console.WriteLine(StringUtil.ToHexString(randBytes));
        }
    }
}
