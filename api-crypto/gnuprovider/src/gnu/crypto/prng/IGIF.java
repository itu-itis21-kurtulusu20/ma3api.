package gnu.crypto.prng;

import tr.gov.tubitak.uekae.esya.api.common.crypto.BasePRNG;
import tr.gov.tubitak.uekae.esya.api.common.crypto.IRandom;
import tr.gov.tubitak.uekae.esya.api.common.crypto.LimitReachedException;

import java.util.List;

/**
 * Created by ramazan.girgin on 2/26/2015.
 */
public interface IGIF {
    byte[] generateRng() throws LimitReachedException;
    int getOutputSize();
}
