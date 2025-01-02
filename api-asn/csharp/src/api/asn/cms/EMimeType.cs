using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EMimeType : BaseASNWrapper<MimeType>
    {
        public EMimeType(byte[] aBytes)
            : base(aBytes, new MimeType())
        {
        }

        /**
        *
        * @param aMimeType defines mime type
        */
        public EMimeType(String aMimeType)
            : base(new MimeType(aMimeType))
        {
        }
    }
}
