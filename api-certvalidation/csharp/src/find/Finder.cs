using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common.license;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
     * Base class for finder classes.
     *
     * <p>During validation process, some items such as issuer certificates or crls
     * must be found from some places. These external items to be found are searched
     * and found according to the finders. Each Finder specifies a location or means
     * of finding an item. For example to locate an issuer certificate according to
     * the Authority Info Access extension of the certificate ,
     * CertificateFinderFromAIA is used.
     *
     * @author IH
     */
    public abstract class Finder
    {
        public static readonly String PARAM_REMOTE = "remote";
        public static readonly String PARAM_STOREPATH = "storepath";
        public static readonly String PARAM_STORE_STREAM = "storestream";
        public static readonly String PARAM_TIMEOUT = "timeout";
        public static readonly String CACHE = "cache";

        public static readonly String PARAM_CRLSERVICE_ADDRESS = "address";
        
        public static String DEFAULT_CRLSERVICE_ADDRESS = "http://silsorgusu.kamusm.gov.tr";

        public static readonly String DOSYA_YOLU = "dosyayolu";
        public static readonly String DIZIN = "dizin";


        protected ValidationSystem mParentSystem;

        //Zincir üzerindeki sonraki bulucu
        //protected Finder mNextFinder;

        //Bulucu classa dışarıdan politika ile verilebilecek parametreler
        protected ParameterList mParameters;

        //Bulma işleminde bulunan nesnenin kontrolünün yapılıp yapılmayacağı bilgisi
        protected bool mToBeChecked = true;

        //Bulma işleminde bulunan nesnenin eşleştirilip eşleştirilmeyeceği bilgisi
        protected bool mToBeMatched = true;

        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected Finder()
        {
            mParameters = new ParameterList();
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.SERTIFIKADOGRULAMA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }
        /*
        public void addNextFinder(Finder aNextFinder)
        {
            Finder finder = this;
            Finder nextFinder = getNextFinder();
            while (nextFinder != null) {
                finder = nextFinder;
                nextFinder = finder.getNextFinder();
            }
            finder.setNextFinder(aNextFinder);
        }

        public Finder getNextFinder()
        {
            return mNextFinder;
        }

        public void setNextFinder(Finder aFinder)
        {
            mNextFinder = aFinder;
        }    */

        public void setParentSystem(ValidationSystem aParentSystem)
        {
            mParentSystem = aParentSystem;
        }

        public void setParameters(ParameterList aParameterList)
        {
            if(aParameterList == null)
                mParameters = new ParameterList();
            else
                mParameters = aParameterList;
        }

        public ParameterList getParameters()
        {
            return mParameters;
        }

        public bool isToBeChecked()
        {
            return mToBeChecked;
        }

        public bool isRemote()
        {
            String val = (String)mParameters.getParameter(PARAM_REMOTE);
            return val != null && (val.Equals("TRUE"));
        }

        protected String getCRLServiceAddress()
        {
            String address = mParameters.getParameterAsString(PARAM_CRLSERVICE_ADDRESS);
            if (address == null)
                return DEFAULT_CRLSERVICE_ADDRESS;

            return address;
        }
    }
}
