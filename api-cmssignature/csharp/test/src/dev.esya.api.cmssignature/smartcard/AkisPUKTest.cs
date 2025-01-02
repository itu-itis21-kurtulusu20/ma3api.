using System;
using System.Text;
using iaik.pkcs.pkcs11.wrapper;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

namespace dev.esya.api.cmssignature.smartcard
{
    [TestFixture]
    class AkisPUKTest
    {

        [Test]
        public void testAuthenticatePUK()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long session = sc.openSession(1);

            string PUK = "123456";

            try
            {
                //((PKCS11Ops)sc.getPkcs11Ops()).getPKCS11Module().C_Login(session, PKCS11Constants_Fields.CKU_SO, PUK.ToCharArray());
            }
            catch (Exception ex2)
            {
                Console.WriteLine(ex2.ToString());
            }

        }


        [Test]
        public void testChangePUK()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long session = sc.openSession(1);

            string WRONG_PUK = "12345";
            string PUK = "123456";


            try
            {
                //((PKCS11Ops)sc.getPkcs11Ops()).getPKCS11Module().C_Login(session, PKCS11Constants_Fields.CKU_SO, WRONG_PUK.ToCharArray());
            }
            catch (Exception)
            {
                try
                {
                    //((PKCS11Ops)sc.getPkcs11Ops()).getPKCS11Module().C_Login(session, PKCS11Constants_Fields.CKU_SO, PUK.ToCharArray());
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.ToString());
                }
            }
        }

    }
}