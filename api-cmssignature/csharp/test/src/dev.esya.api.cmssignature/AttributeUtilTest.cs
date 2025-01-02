using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

namespace api_cmssignature_test.dev.esya.api.cmssignature
{
    [TestFixture]
    public class AttributeUtilTest
    {

        public static Object[] TestCases =
        {
            new object[] {"QCA1_1", "QCA1_1"},
            new object[] {new char[] { 'Q', 'C', 'A', '1', '_', '1' }, "QCA1_1"},
            new object[] {new byte[] { 0x51, 0x43, 0x41, 0x31, 0x5f, 0x31 }, "QCA1_1"},
            new object[] {null, null}
        };

        [Test, TestCaseSource("TestCases")]
        public void getStringValue_ByStringParam(Object value, Object expectedResult)
        {
            Assert.AreEqual(expectedResult,tr.gov.tubitak.uekae.esya.api.smartcard.src.util.AttributeUtil.getStringValue(value));
        }
    }
}