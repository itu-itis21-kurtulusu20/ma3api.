package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template.DefaultCardTemplate;

import java.io.IOException;

/**
* <b>Author</b>    : zeldal.ozdemir <br>
* <b>Project</b>   : MA3   <br>
* <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
* <b>Date</b>: 12/14/12 - 8:44 PM <p>
* <b>Description</b>: <br>
 * CardType extension for NSS Softtoken. pkcs11 must be given
*/
public class NSSCardType extends CardType {

    public NSSCardType(PKCS11 pkcs11) {
        super("softokn3", "NSS");
        this.template = new NSSCardTemplate(pkcs11);
    }


    private class NSSCardTemplate extends DefaultCardTemplate {
        private PKCS11 pkcs11;

        public NSSCardTemplate(PKCS11 pkcs11) {
            super(NSSCardType.this);
            this.pkcs11 = pkcs11;
        }

        @Override
        public synchronized IPKCS11Ops getPKCS11Ops() {
            return new NSSPKCS11Ops(getCardType(),pkcs11);
        }

    }

    private class NSSPKCS11Ops extends PKCS11Ops {

        public NSSPKCS11Ops(CardType cardType, PKCS11 pkcs11) {
            super(cardType);
            this.mPKCS11 = pkcs11;
        }

        @Override
        public void initialize() throws PKCS11Exception, IOException {
            // we did
        }
    }
}
