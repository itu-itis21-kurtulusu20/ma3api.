using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.signature.profile
{
    public interface ProfileDocInfo
    {
        String getURI();

        byte[] getDigestOfProfile(DigestAlg digestAlg);

        Stream getProfile();
    }
}
