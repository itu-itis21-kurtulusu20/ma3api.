package gnu.crypto.prng;

import tr.gov.tubitak.uekae.esya.api.common.crypto.BasePRNG;
import tr.gov.tubitak.uekae.esya.api.common.crypto.LimitReachedException;

import java.util.Map;

/**
 * Created by ramazan.girgin on 2/26/2015.
 */
public class GifPRNG extends BasePRNG {
    IGIF gif =null;
    public GifPRNG(String name, IGIF gif) {
        super(name);
        this.gif = gif;
        setBufferSize(gif.getOutputSize());
    }

    public GifPRNG(GifPRNG other) {
        this(other.name(), other.gif);
        setBufferSize(other.bufferSize);
    }

    @Override
    public Object clone() {
        return new GifPRNG(this);
    }

    @Override
    public void setup(Map attributes) {

    }

    @Override
    public void fillBlock() throws LimitReachedException {
        buffer = gif.generateRng();
    }

    @Override
    public boolean isUseTRSU() {
        return false;
    }
}
