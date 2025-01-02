using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;

namespace test.esya.api.cmssignature.smartcard
{
    public class HSMPoolTest
    {
        [Test]
        public void testPool() 
        {
            HSMPool.DEFAULT_INITIAL_CAPACITY = 5;
            HSMPool.DEFAULT_MAX_CAPACITY = 10;
            HSMPool hsmPool = new HSMPool(CardType.DIRAKHSM, 22, "123456");

            for(int i = 0; i<10; i++)
            {
                P11SmartCard p11SmartCard = hsmPool.checkOutItem();
                Console.WriteLine("Session: " + p11SmartCard.getSessionNo());
            }
            Console.WriteLine("Done");
        }


        [Test]
        public void testPoolAtOverRequest() 
        {
            HSMPool.DEFAULT_INITIAL_CAPACITY = 1;
            HSMPool.DEFAULT_MAX_CAPACITY = 2;
            HSMPool hsmPool = new HSMPool(CardType.DIRAKHSM, 22, "123456");

            P11SmartCard p11SmartCard = null;
            p11SmartCard = hsmPool.checkOutItem();
            p11SmartCard = hsmPool.checkOutItem();

            //Must wait for the timeout.
            Assert.Throws<ESYAException>(() => hsmPool.checkOutItem());
        }
    }
}
