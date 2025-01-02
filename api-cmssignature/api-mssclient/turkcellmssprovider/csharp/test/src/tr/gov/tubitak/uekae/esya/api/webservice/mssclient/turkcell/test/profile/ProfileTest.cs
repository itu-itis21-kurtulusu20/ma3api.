using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.test.profile
{
    [TestFixture]
    public class ProfileTest
    {
        MSSParams mobilParams = new MSSParams("http://MImzaTubitakBilgem", "zR4*9a2+78", "www.turkcelltech.com");
        [Test]
        public void testProfile()
        {
            EMSSPRequestHandler msspRequestHandler = new EMSSPRequestHandler(mobilParams);

            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator("05380892868",Operator.TURKCELL);
            msspRequestHandler.setCertificateInitials(phoneNumberAndOperator);

            byte[] dataForSign = new byte[1];
        }
    }
}
