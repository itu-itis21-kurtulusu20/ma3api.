package tr.gov.tubitak.uekae.esya.api.crypto.params;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.KeySpec;

/**
 * Created with IntelliJ IDEA.
 * User: murat.kubilay
 * Date: 7/31/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecretKeySpec  implements KeySpec {


    CipherAlg mAlgorithm;
    String mLabel;
    int mLen;

    public SecretKeySpec (CipherAlg algorithm, String label, int len){
        mAlgorithm = algorithm;
        mLabel = label;
        mLen = len ;
    }

    public SecretKeySpec (CipherAlg algorithm, int len){
        mAlgorithm = algorithm;
        mLen = len ;
    }

    public CipherAlg getmAlgorithm() {
        return mAlgorithm;
    }

    public String getmLabel() {
        return mLabel;
    }

    public int getmLen() {
        return mLen;
    }

}
