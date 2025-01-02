
using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

namespace test.esya.api.cmssignature.structural
{
    [TestFixture]
    class ContentTest : CMSSignatureTest
    {
        [Test]
        public void testAddTwoInternal()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            try
            {
                bs.addContent(getSimpleContent());
                throw new Exception("AddContent must throw Exception in the second time!");
            }
            catch (CMSSignatureException Ex)
            {
                Assert.IsTrue(true, "Second AddContent should throw CMSSignatureException");
            }
        }

        [Test]
        public void testAddTwoExternal()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent(), false);

            try
            {
                bs.addContent(getSimpleContent(), false);
                throw new Exception("AddContent must throw Exception in the second time!");
            }
            catch (CMSSignatureException Ex)
            {
                Assert.IsTrue(true, "Second AddContent should throw CMSSignatureException");
            }
        }

        [Test]
        public void testAddOneInternalOneExternal()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            try
            {
                bs.addContent(getSimpleContent(), false);
                throw new Exception("AddContent must throw Exception in the second time!");
            }
            catch (CMSSignatureException Ex)
            {
                Assert.IsTrue(true, "Second AddContent should throw CMSSignatureException");
            }
        }

        [Test]
        public void testAddOneExternalOneInternal()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent(), false);

            try
            {
                bs.addContent(getSimpleContent());
                throw new Exception("AddContent must throw Exception in the second time!");
            }
            catch (CMSSignatureException Ex)
            {
                Assert.IsTrue(true, "Second AddContent should throw CMSSignatureException");
            }
        }
    }
}
