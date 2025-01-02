using System;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;
using System.Threading;

namespace NETAPI_TEST.src.timestamp
{
    public class TimeStampTest
    {
        public static int count = 1;
        public static void ZamanDamgasiTest()
        {
            Thread[] MyThreads = new Thread[10];
            for (int I = 0; I < 10; I++)
            {
                MyThreads[I] = new Thread(new ThreadStart(ThreadProc));

            }
            for (int I = 0; I < 10; I++)
            {
                MyThreads[I].Start();
            }


        }

        protected static void ThreadProc()
        {
            while (true)
            {
               // Console.WriteLine("timestam isteği yapılıyor " + count++);
               // TSSettings tsSettings = new TSSettings("http://zd.ug.net", 21, "12345678".ToCharArray());

               // byte[] hash = new byte[20];
               // Random rand = new Random();
               // rand.NextBytes(hash);


               //// TSClient tsClient = TSClient.getInstance();
               // ETimeStampResponse timestamp = tsClient.timestamp(hash, tsSettings);


               // String str = timestamp.getStatusString();
               // EContentInfo contInfo = timestamp.getContentInfo();
               // ESignedData signed = new ESignedData(contInfo.getContent());

               // ETSTInfo tstInfo = new ETSTInfo(signed.getEncapsulatedContentInfo().getContent());
               // DateTime dateTime = tstInfo.getTime();

               // DateTime localTime = dateTime.ToLocalTime();
               // Console.WriteLine("timestam isteği yapıdı " + count++);
            }
        }


    }
}
