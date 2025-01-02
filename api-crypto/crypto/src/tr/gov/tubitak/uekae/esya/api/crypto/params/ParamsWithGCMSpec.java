package tr.gov.tubitak.uekae.esya.api.crypto.params;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.io.InputStream;

/**
 * Created by sura.emanet on 25.04.2018.
 */
public class ParamsWithGCMSpec extends ParamsWithIV {

    private InputStream mAAD;
    protected byte[] mTag;

    public ParamsWithGCMSpec(byte[] aIV){
        this(aIV, null);
    }

    public ParamsWithGCMSpec(byte[] aIV, InputStream aAAD)
    {
        super(aIV);
        mAAD = aAAD;
    }

    public ParamsWithGCMSpec(byte[] aIV, InputStream aAAD, byte [] aTag)
    {
        super(aIV);
        mAAD = aAAD;
        mTag = aTag;
    }

    public InputStream getAAD()
    {
        return mAAD;
    }

    @Override
    public byte[] getEncoded() {
        throw new ESYARuntimeException("getEncoded is not applicable for GCM params");
    }

    public byte [] getTag(){
        return mTag;
    }

    public void setTag(byte []aTag){
        mTag = aTag;
    }
}
