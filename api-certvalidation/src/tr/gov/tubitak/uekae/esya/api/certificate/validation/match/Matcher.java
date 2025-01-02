package tr.gov.tubitak.uekae.esya.api.certificate.validation.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;

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
public abstract class Matcher {

    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected Matcher mNextMatcher;

    public Matcher()
    {
    	try
    	{
    		LV.getInstance().checkLD(Urunler.SERTIFIKADOGRULAMA);
    	}
    	catch(LE ex)
    	{
    		throw new RuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
    }

    public void addNextMatcher(Matcher aNextMatcher)
    {
        Matcher matcher = this;
        Matcher nextMatcher = getNextMatcher();
        while (nextMatcher != null) {
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
