using System;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common.license;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match
{
    /**
     * Base classe for matcher classes.
     *
     * <p>When an associatiated item like CA certificate of a certificate or CRL of
     * a certificate is found, the matching between these two items must be done
     * according to several criteria specified in standars. Those matching criteria
     * are defined in matcher classes. 
     * 
     * @author IH
     */
    public abstract class Matcher
    {
        protected ILog LOGGER = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        
        protected Matcher mNextMatcher;

        public Matcher()
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

        public void addNextMatcher(Matcher aNextMatcher)
        {
            Matcher matcher = this;
            Matcher nextMatcher = getNextMatcher();
            while (nextMatcher != null)
            {
                matcher = nextMatcher;
                nextMatcher = matcher.getNextMatcher();
            }
            matcher.setNextMatcher(aNextMatcher);
        }

        public Matcher getNextMatcher()
        {
            return mNextMatcher;
        }

        public void setNextMatcher(Matcher aNextMatcher)
        {
            mNextMatcher = aNextMatcher;
        }
    }
}
