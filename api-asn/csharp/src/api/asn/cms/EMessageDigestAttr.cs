using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.cms;


namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EMessageDigestAttr : BaseASNWrapper<MessageDigest>
    {
        public EMessageDigestAttr(byte[] aBytes)
            : base(aBytes, new MessageDigest())
        {
        }  

        public byte[] getHash()
        {
            return mObject.mValue;
        }
    }
}
