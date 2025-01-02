using System;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common.license;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    /**
     * sets configurations about license
     * @author orcun.ertugrul
     *
     */
    public static class LicenseUtil
    {
        private static string mPassword;
        private static byte[] mLicenseData;
        
        public static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static void setLicenseXml(Stream aIs, String aPassword)
        {
            setLicensePassword(aPassword);
            setLicenseXml(aIs);
        }

        /**
         * Gets the expiration date of the current licence.
         *
         * @return the expiration date of the current licence.
         */

        public static DateTime getExpirationDate()
        {
            try
            {
                Type t = Type.GetType("tr.gov.tubitak.uekae.esya.api.common.license.LV");
                if (t == null)
                    throw new ESYAException("common.license.LV lisans sınıfı bulunamadı");
                MethodInfo method1 = t.GetMethod("getInstance", BindingFlags.Static | BindingFlags.Public);

                object o = method1.Invoke(null, null);

                MethodInfo method2 = t.GetMethod("getLicenseExpirationDate");

                int ortakAPI = 40;
                object date = method2.Invoke(o, new object[] { ortakAPI });

                return ((DateTime?)date).Value;
            }
            catch (TargetInvocationException ex)
            {
                Exception cause = ex.InnerException;
                string message = (cause != null && cause.Message != null) ? cause.Message : ex.Message;

                throw new ESYAException("Problem in getting expiration date. " + message, ex);
            }
            catch (Exception ex)
            {
                throw new ESYAException("Problem in getting expiration date. " + ex.Message, ex);
            }

        }

        /**
        * Gets the start date of the current licence.
        *
        * @return the start date of the current licence.
        */
        public static DateTime getStartDate()
        {
            Type t = Type.GetType("tr.gov.tubitak.uekae.esya.api.common.license.LV");
            if (t == null)
                throw new ESYAException("common.license.LV lisans sınıfı bulunamadı");
            MethodInfo method1 = t.GetMethod("getInstance", BindingFlags.Static | BindingFlags.Public);

            object o = method1.Invoke(null, null);

            MethodInfo method2 = t.GetMethod("getLicenseStartDate");

            int ortakAPI = 40;
            object date = method2.Invoke(o, new object[] { ortakAPI });

            return ((DateTime?)date).Value;
        }

        /**
        * Sets license.
        * 
        * @param aIs
        * @throws ESYAException
        */
        public static void setLicenseXml(Stream aIs)
        {
            try
            {
                mLicenseData = new byte[aIs.Length];
                aIs.Read(mLicenseData, 0, (int)aIs.Length);

              	LV.reset();
                LV.getInstance();
            }
            catch (Exception ex)
            {
                logger.Error("License Error: " + ex.Message, ex);
                throw new ESYAException("License Error: " + ex.Message);
            }
        }

        public static void setLicensePassword(String aPassword)
        {
            mPassword = aPassword;
        }
        
        public static byte [] getLicense()
        {
            return mLicenseData;
        }

        public static String getPassword()
        {
            return mPassword;
        }
    }
}
