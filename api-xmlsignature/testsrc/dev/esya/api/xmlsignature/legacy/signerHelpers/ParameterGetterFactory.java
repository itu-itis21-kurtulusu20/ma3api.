package dev.esya.api.xmlsignature.legacy.signerHelpers;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class ParameterGetterFactory {

    private static BaseParameterGetter bpg = null;

    public static BaseParameterGetter getParameterGetter()
    {
        if(bpg == null)
        {
            bpg = new UgTestParameterGetter();
        }
        return bpg;
    }
}
