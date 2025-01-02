using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers
{
    public class ParameterGetterFactory
    {
        private static BaseParameterGetter parameterGetter = null;
        public static BaseParameterGetter getParameterGetter()
        {
            if (parameterGetter == null)
            {
                parameterGetter = new UgTestParameterGetter();
            }
            return parameterGetter;
        }
    }
}
