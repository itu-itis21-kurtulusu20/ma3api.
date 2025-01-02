using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common.crypto;

namespace tr.gov.tubitak.uekae.esya.api.crypto.src
{
    public interface IDTBSRetrieverSigner : BaseSigner
    {
        byte[] getDtbs();
    }
}
