using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncySignerManager
    {
        public static ISigner GetSigner(SignatureAlg aSignatureAlg)
        {
            String signatureAlgName = aSignatureAlg.getName();
            
            if (signatureAlgName.Equals("RSA-RAW"))
            {
                return SignerUtilities.GetSigner(signatureAlgName);
            }
            else
            {
                return SignerUtilities.GetSigner(BouncyProviderUtil.resolveSignature(aSignatureAlg));
            }
       
        }
    }
}
