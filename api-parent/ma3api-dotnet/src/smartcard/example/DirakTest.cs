using System;
using ImzaApiTest.src.tr.gov.tubitak.uekae.esya.api;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.smartcard.src.pkcs11.card.ops;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.example
{
    public class DirakTest : SampleBase
    {
        String dirakUsername = null;
        String dirakPassword = null;

        [Test]
        public void testDirakUserLogin()
        {
            DirakLibOps.userLogin(dirakUsername, dirakPassword);
        }

        [Test]
        public void testDirakUserLogout()
        {
            DirakLibOps.userLogout(dirakUsername);
        }
    }
}
