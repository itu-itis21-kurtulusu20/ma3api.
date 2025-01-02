using System;
using System.Reflection;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper
{
    public static class  EMSSPProviderFactory
    {
        private static String MSSP_PROVIDER_CLASS_NAME_TURKCELL = "tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.provider.TurkcellMSSProvider";
        private static String MSSP_PROVIDER_ASSEMBLY_NAME_TURKCELL = "ma3api-mssclient-turkcellprovider";

        private static String MSSP_PROVIDER_CLASS_NAME_TURKTELEKOM = "tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.provider.TurkTelekomMSSProvider";
        private static String MSSP_PROVIDER_ASSEMBLY_NAME_TURKTELEKOM = "ma3api-mssclient-turktelekomprovider";

        private static String MSSP_PROVIDER_CLASS_NAME_VODAFONE = "tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.provider.VodafoneMSSProvider";
        private static String MSSP_PROVIDER_ASSEMBLY_NAME_VODAFONE = "ma3api-mssclient-vodafoneprovider";
        public static IMSSProvider getProvider(Operator iOperator)
        {
            EMSSPClientUtil.CheckLicense();
            String msspProviderClassName = null;
            String msspAssemblyName = null;
            switch (iOperator)
            {
                case Operator.TURKCELL:
                    msspProviderClassName = MSSP_PROVIDER_CLASS_NAME_TURKCELL;
                    msspAssemblyName = MSSP_PROVIDER_ASSEMBLY_NAME_TURKCELL;
                    break;
                case Operator.TURKTELEKOM:
                    msspProviderClassName = MSSP_PROVIDER_CLASS_NAME_TURKTELEKOM;
                    msspAssemblyName = MSSP_PROVIDER_ASSEMBLY_NAME_TURKTELEKOM;
                    break;
                case Operator.VODAFONE:
                    msspProviderClassName = MSSP_PROVIDER_CLASS_NAME_VODAFONE;
                    msspAssemblyName = MSSP_PROVIDER_ASSEMBLY_NAME_VODAFONE;
                    break;
            }
            if (msspProviderClassName == null)
            {
                throw new ESYAException("Unknown operator");
            }

            IMSSProvider provider = null;
            try
            {
                provider = (IMSSProvider)Activator.CreateInstance(msspAssemblyName, msspProviderClassName).Unwrap();
            }
            catch (Exception t)
            {
                throw new ESYAException("Cant load mssp provider : " + "Assembly = "+msspAssemblyName+",Class ="+msspProviderClassName, t);
            }
            return provider;
        }
    }
}
