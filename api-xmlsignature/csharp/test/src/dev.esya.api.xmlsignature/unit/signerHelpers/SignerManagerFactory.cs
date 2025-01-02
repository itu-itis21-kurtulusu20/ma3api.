using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.pfxsigner;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.pfxsigner
{
    public class SignerManagerFactory
    {
        private static ISignerManager signerManager = null;
        private static readonly bool USE_SMART_CARD=false;
        public static ISignerManager getSignerManager()
        {
            if (signerManager == null)
            {
                if (USE_SMART_CARD)
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
    
}
