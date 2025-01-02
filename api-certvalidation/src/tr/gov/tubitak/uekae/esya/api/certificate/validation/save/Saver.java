package tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;

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
	protected Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	protected ParameterList mParameters;
	
	protected ValidationSystem mParentSystem;
	
	protected Saver()
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
	
	public void setParameters(ParameterList aParameterList)
    {
        mParameters = aParameterList;
    }
	
	public void setParentSystem(ValidationSystem aParentSystem)
    {
        mParentSystem = aParentSystem;
    }

}
