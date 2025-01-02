package tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes;

import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfo;

/**
 * Created by sura.emanet on 12.04.2019.
 */
public class EnvelopeRecipient extends RecipientInfo {

    CryptoProvider cryptoProvider;

    public CryptoProvider getCryptoProvider() {
        if(cryptoProvider != null)
            return cryptoProvider;
        else
            return Crypto.getProvider();
    }
}
