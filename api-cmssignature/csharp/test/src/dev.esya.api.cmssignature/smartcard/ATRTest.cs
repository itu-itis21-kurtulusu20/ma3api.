
using System;
using System.Collections.Generic;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.config;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;
using tr.gov.tubitak.uekae.esya.api.smartcard.winscard;

namespace dev.esya.api.cmssignature.smartcard
{
    class ATR
    {
        [Test]
        public void getATRs()
        {
            string [] atrs = SmartOp.getCardATRs();
            foreach (string atr in atrs)
            {
                Console.WriteLine(atr);    
            }
            
        }

        [Test]
        public void testAkisATR()
        {
            List<String> ATR_LIST = AkisTemplate.ATR_HASHES;
            foreach (string atrStr in ATR_LIST)
            {
                bool isAkis = AkisTemplate.isAkisATR(atrStr);
                if(isAkis == false)
                    throw new Exception("Problem");
                Console.WriteLine(isAkis);
            }

        }

        [Test]
        public void testSmartCardConfig()
        {
            List<CardTypeConfig> cards = new SmartCardConfigParser().readConfig();
            Console.WriteLine(cards);
            CardType.applyCardTypeConfig(cards);
            CardType found = CardType.getCardTypeFromATR("3B7F96000080318065B0846160FB120FFD829000", CardType.Application.ESIGNATURE);
            Console.WriteLine(found);
            Console.WriteLine(found.getLibName());
        }
    }
}
