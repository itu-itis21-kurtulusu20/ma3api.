using NUnit.Framework;
using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.performance
{
    public class BaseSignedDataMemoryTest
    {
        String P7S_PATH = "C:\\a\\files\\100MB.txt.p7s";

        [Test]
        public void testNewBaseSignedData() 
        {
            FileStream fs = new FileStream(P7S_PATH,FileMode.Open);
            BaseSignedData bsd = new BaseSignedData(fs);
            Console.WriteLine(bsd.ToString());   // 118 MB
        }

        [Test]
        public void testOldBaseSignedData()
        {
            byte[] p7sBytes = AsnIO.dosyadanOKU(P7S_PATH);
            BaseSignedData bsd = new BaseSignedData(p7sBytes);
            Console.WriteLine(bsd.ToString()); // 323 MB
        }
        
    }
}
