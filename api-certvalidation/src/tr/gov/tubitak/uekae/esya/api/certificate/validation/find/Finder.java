package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;

import java.net.URLConnection;

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
    public static final String PARAM_REMOTE  = "remote";
    
    public static final String PARAM_TIMEOUT = "timeout";
    
    public static final String PARAM_CRLSERVICE_ADDRESS = "address";
    
    public static final String DOSYA_YOLU = "dosyayolu";
    public static final String DIZIN = "dizin";
    public static final String PARAM_STOREPATH = "storepath";
    public static final String PARAM_TA = "ta"; // Trust Anchor
    public static final String PARAMA_STORE_STREAM="storestream";

    public static final String CACHE = "cache";
    
    public static String DEFAULT_CRLSERVICE_ADDRESS = "http://silsorgusu.kamusm.gov.tr";
    

    protected ValidationSystem mParentSystem;

    //Zincir üzerindeki sonraki bulucu
    //protected Finder mNextFinder;

    //Bulucu classa dışarıdan politika ile verilebilecek parametreler
    protected ParameterList mParameters;

    //Bulma işleminde bulunan nesnenin kontrolünün yapılıp yapılmayacağı bilgisi
    protected boolean mToBeChecked = true;

    //Bulma işleminde bulunan nesnenin eşleştirilip eşleştirilmeyeceği bilgisi
    protected boolean mToBeMatched = true;
    
    private static Logger logger = LoggerFactory.getLogger(Finder.class);

    
    protected Finder()
    {
    	mParameters = new ParameterList();
    	try
    	{
    		LV.getInstance().checkLD(Urunler.SERTIFIKADOGRULAMA);
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
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
    }    
    */

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

    public boolean isToBeChecked()
    {
        return mToBeChecked;
    }

    public boolean isRemote()
    {
        String val = (String) mParameters.getParameter(PARAM_REMOTE);
        return val != null && (val.equals("TRUE"));
    }
    
    protected void setTimeOut(URLConnection conn) 
    {
    	String timeout = mParameters.getParameterAsString(PARAM_TIMEOUT);
    	if(timeout != null)
    	{
    		logger.debug("Setting timeout: " + timeout);
    		int timeoutValue = Integer.parseInt(timeout);
    		conn.setConnectTimeout(timeoutValue);
    		conn.setReadTimeout(timeoutValue);
    	}
	}

    protected String getCRLServiceAddress()
    {
    	String address = mParameters.getParameterAsString(PARAM_CRLSERVICE_ADDRESS);
    	if(address == null)
    		return DEFAULT_CRLSERVICE_ADDRESS;
    	
    	return address;
    }
    
}
