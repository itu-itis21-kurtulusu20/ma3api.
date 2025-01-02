using System;
using System.Collections;
using System.IO;
using System.Linq;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common.tools;

namespace test.esya.api.cmssignature.common
{
    [TestFixture]
    class CombinedInputStreamTest
    {
        //DotNet Stream Referans 
        [Test]
        public void testMemoryStream()
        {
            byte[] buff = new byte[3];

            MemoryStream ms = new MemoryStream(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0});

            int readLen = 0;
            for (int i = 0; i < 10; i++)
            {
                readLen = ms.Read(buff, 0, buff.Length);
                Console.WriteLine(readLen);
            }
        }


        public CombinedInputStream BuildCombinedInputStream()
        {
            MemoryStream ms1 = new MemoryStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 });
            MemoryStream ms2 = new MemoryStream(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 });

            CombinedInputStream stream = new CombinedInputStream();
            stream.addInputStream(ms1);
            stream.addInputStream(ms2);

            return stream;
        }


        public void testCombinedInputStream_Size10(int buffSize, int [] output)
        {
            byte[] buff = new byte[buffSize];

            ArrayList readCounts = new ArrayList();
            CombinedInputStream stream = BuildCombinedInputStream();

            for (int i = 0; i < 10; i++)
            {
                int readLen = stream.Read(buff, 0, buff.Length);
                Console.WriteLine(readLen);
                readCounts.Add(readLen);
            }

            int [] readCountArr = (int []) readCounts.ToArray(typeof(int));

            Assert.IsTrue(output.SequenceEqual(readCountArr));
        }

        [Test]
        public void test_buff_15()
        {
            testCombinedInputStream_Size10(15, new int[] { 15, 5, 0, 0, 0, 0, 0, 0, 0, 0 });
        }


        [Test]
        public void test_buff_10()
        { 
            testCombinedInputStream_Size10(10, new int[] {10, 10, 0, 0, 0, 0, 0, 0, 0, 0});
        }

        [Test]
        public void test_buff_9()
        {
            testCombinedInputStream_Size10(9, new int[] { 9, 9, 2, 0, 0, 0, 0, 0, 0, 0 });
        }

        [Test]
        public void test_buff_8()
        {
            testCombinedInputStream_Size10(8, new int[] { 8, 8, 4, 0, 0, 0, 0, 0, 0, 0 });
        }

        [Test]
        public void test_buff_7()
        {
            testCombinedInputStream_Size10(7, new int[] { 7, 7, 6, 0, 0, 0, 0, 0, 0, 0 });
        }

        [Test]
        public void test_buff_6()
        {
            testCombinedInputStream_Size10(6, new int[] { 6, 6, 6, 2, 0, 0, 0, 0, 0, 0 });
        }

        [Test]
        public void test_buff_5()
        {
            testCombinedInputStream_Size10(5, new int[] { 5, 5, 5, 5, 0, 0, 0, 0, 0, 0 });
        }

        [Test]
        public void test_buff_4()
        {
            testCombinedInputStream_Size10(4, new int[] { 4, 4, 4, 4, 4, 0, 0, 0, 0, 0 });
        }

        [Test]
        public void test_buff_3()
        {
            testCombinedInputStream_Size10(3, new int[] { 3, 3, 3, 3, 3, 3, 2, 0, 0, 0 });
        }

        [Test]
        public void test_buff_2()
        {
            testCombinedInputStream_Size10(2, new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        }

        [Test]
        public void test_buff_1()
        {
            int [] output = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
            byte[] buff = new byte[1];

            ArrayList readCounts = new ArrayList();
            CombinedInputStream stream = BuildCombinedInputStream();

            for (int i = 0; i < 20; i++)
            {
                int readLen = stream.Read(buff, 0, buff.Length);
                Console.WriteLine(readLen);
                readCounts.Add(readLen);
            }

            int[] readCountArr = (int[])readCounts.ToArray(typeof(int));

            Assert.IsTrue(output.SequenceEqual(readCountArr));
        }

        
    }
}
