using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.asn.profile
{
    public interface ProfileDocInfo
    {
        byte[] getDigestOfProfile(int[] aDigestAlgOid);

        Stream getProfile();

        String getURI();
    }
}
