
using System;
using System.Threading;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.infra.cache;

namespace test.esya.api.certvalidation
{
    [TestFixture]
    public class FixedSizedCacheTest
    {

        [Test]
        public void testGet()
        {
            FixedSizedCache<string, string> cache = new FixedSizedCache<string, string>(5, TimeSpan.FromHours(1));

            cache.Add("1", "1");
            cache.Add("2", "2");
            cache.Add("3", "3");
            cache.Add("4", "4");
            cache.Add("5", "5");
            cache.Add("6", "6");

            string item = cache.Get("3");

            Assert.AreEqual("3", item);
        }

        [Test]
        public void notAddedItemMustBeNull()
        {
            FixedSizedCache<string,string> cache = new FixedSizedCache<string, string>(5, TimeSpan.FromHours(1));

            string item = cache.Get("1");

            Assert.IsNull(item);
        }

        [Test]
        public void addSecondTime()
        {
            FixedSizedCache<string, string> cache = new FixedSizedCache<string, string>(5, TimeSpan.FromHours(1));

            cache.Add("1", "1");
            cache.Add("1", "2");

            string item = cache.Get("1");

            Assert.AreEqual("2", item);
        }

        [Test]
        public void testOverSize()
        {
            FixedSizedCache<string, string> cache = new FixedSizedCache<string, string>(5, TimeSpan.FromHours(1));

            cache.Add("1", "1");
            cache.Add("2", "2");
            cache.Add("3", "3");
            cache.Add("4", "4");
            cache.Add("5", "5");
            cache.Add("6", "6");

            string item = cache.Get("1");

            Assert.IsNull(item);
        }


        [Test]
        public void testOverSize2()
        {
            FixedSizedCache<string, string> cache = new FixedSizedCache<string, string>(5, TimeSpan.FromHours(1));

            cache.Add("1", "1");
            cache.Add("2", "2");
            cache.Add("3", "3");
            cache.Add("4", "4");
            cache.Add("1", "1");
            cache.Add("5", "5");
            cache.Add("6", "6");

            string item = cache.Get("2");

            Assert.IsNull(item);

            string item2 = cache.Get("3");

            Assert.AreEqual("3", item2);
        }




        [Test]
        public void testTimeout()
        {
            FixedSizedCache<string, string> cache = new FixedSizedCache<string, string>(5, TimeSpan.FromSeconds(1));

            cache.Add("1", "1");
            cache.Add("2", "2");
           
            Thread.Sleep(3000);

            string item = cache.Get("1");

            Assert.IsNull(item);
        }



    }
}
