package dev.esya.api.xmlsignature.legacy.signerHelpers;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 10:28
 * To change this template use File | Settings | File Templates.
 */
public class SignerManagerFactory {

    private static ISignerManager signerManager = null;
    private static final boolean USE_SMART_CARD = false;

    public static ISignerManager getSignerMAnager()
    {
        if(signerManager == null)
        {
            if(USE_SMART_CARD)
            {
                signerManager = new ESmartCardSignerManager();
            }
            else
            {
                signerManager = EPfxSignerManager.getInstance(null);
            }
        }
        return signerManager;
    }

    public static void reset()
    {
        signerManager = null;
    }
}
