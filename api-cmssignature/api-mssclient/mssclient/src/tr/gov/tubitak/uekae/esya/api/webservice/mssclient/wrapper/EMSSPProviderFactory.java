package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Operator;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider.IMSSProvider;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 7/31/12
 * Time: 8:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class EMSSPProviderFactory {

    static Logger logger = LoggerFactory.getLogger(EMSSPProviderFactory.class);
    private static String MSSP_PROVIDER_CLASS_NAME_TURKCELL="tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.provider.TurkcellMSSProvider";
    private static String MSSP_PROVIDER_CLASS_NAME_TURKTELEKOM="tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.provider.TurkTelekomMSSProvider";
    private static String MSSP_PROVIDER_CLASS_NAME_VODAFONE="tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.provider.VodafoneMSSProvider";

    public static IMSSProvider getProvider(Operator operator) throws ESYAException {
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
            boolean isTest = LV.getInstance().isTL(LV.Urunler.MOBILIMZA);
            if(isTest)
            {
                logger.debug("Test lisansı, mobil imza işlemlerinde 15 sn gecikme yaşanacak.");
                Thread.sleep(15000);
            }
        }
        catch(LE ex)
        {
            logger.debug("Lisans kontrolu basarisiz. ");
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        catch (InterruptedException e){
            throw new ESYAException(e);
        }

        String msspProviderClassName=null;
        switch (operator){
            case TURKCELL:
                msspProviderClassName = MSSP_PROVIDER_CLASS_NAME_TURKCELL;
                break;
            case TURKTELEKOM:
                msspProviderClassName = MSSP_PROVIDER_CLASS_NAME_TURKTELEKOM;
                break;
            case VODAFONE:
                msspProviderClassName = MSSP_PROVIDER_CLASS_NAME_VODAFONE;
                break;
        }
        if(msspProviderClassName == null){
            throw  new ESYAException("Unknown operator");
        }

        try {
            Class<?> aClass = Class.forName(msspProviderClassName);
            return (IMSSProvider) aClass.newInstance();

        }  catch (Exception e){
            throw new ESYAException(e);
        }
    }
}
