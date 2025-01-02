using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common.license;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.save
{
    /**
     * <p>During Certificate/CRL Validation operations, the found items can be
     * saved in somewhere for further use. Saver classes can be defined to perform
     * such action. Saver is the base class for saver classes.
     *
     * Mainly three category of savers can be defined Certificate Savers CRL Savers
     * OCSP Response Savers Bulunan ve geçerlilik kontrolü yapılmış olan objelerin
     * istenilen yerde(sertifika deposu gibi)kaydedilerek ileride kullanılabilmesini
     * saplayan abstract class
     *
     * @author isilh
     */
    public abstract class Saver
    {       
        protected ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected ParameterList mParameters;

        protected ValidationSystem mParentSystem;

        protected Saver()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.SERTIFIKADOGRULAMA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        public void setParameters(ParameterList aParameterList)
        {
            mParameters = aParameterList;
        }

        public void setParentSystem(ValidationSystem aParentSystem)
        {
            mParentSystem = aParentSystem;
        }
    }
}
