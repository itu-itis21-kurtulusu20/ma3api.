using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    class AladdinOps : PKCS11Ops
    {
        public AladdinOps(): base(CardType.ALADDIN)
        {

        }
    }
}
