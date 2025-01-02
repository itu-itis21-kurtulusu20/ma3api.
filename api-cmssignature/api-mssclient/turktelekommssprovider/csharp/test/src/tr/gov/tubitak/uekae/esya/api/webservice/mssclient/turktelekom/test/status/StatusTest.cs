using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.test.status
{
    [TestFixture]
    class StatusTest
    {
        MSSParams mobilParams = new MSSParams("http://MImzaTubitakBilgem", "zR4*9a2+78", "www.turkcelltech.com");
        [Test]
        public void testStatus(string[] args)
        {
            IStatusRequest request = new TurkTelekomStatusRequest(mobilParams);

            IStatusResponse response = request.sendRequest("_26484", "aaaa");
            Console.WriteLine(response.getStatus().StatusCode);
            Console.WriteLine(response.getStatus().StatusMessage);
            Console.WriteLine(response.getMSISDN());
            Console.WriteLine(BitConverter.ToString(response.getSignature()));
        }
    }
}
