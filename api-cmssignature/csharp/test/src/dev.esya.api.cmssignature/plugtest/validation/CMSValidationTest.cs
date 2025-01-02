using System;
using test.esya.api.cmssignature;

namespace dev.esya.api.cmssignature.plugtest.validation
{
    public class CMSValidationTest
    {
        public static String INPUT_DIRECTORY_PATH;

        static CMSValidationTest()
        {
            CMSSignatureTest test = new CMSSignatureTest();
            INPUT_DIRECTORY_PATH = test.getDirectory() + "validation\\";
        }
    }
}