using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EContentHints : BaseASNWrapper<ContentHints>
    {
        public EContentHints(byte[] aBytes)
            : base(aBytes, new ContentHints())
        {
        }

        /**
         * 
         * @param aContentDescription defines content type. mandatory
         * @param aContentType defines explanation of hint. optional
         */

        public EContentHints(String aContentDescription, Asn1ObjectIdentifier aContentType)
            : base(new ContentHints(new Asn1UTF8String(aContentDescription), aContentType))
        {
        }
        /**
         * Returns Content Description of ContentHints attribute
         * @return
         */
        public String getContentDescription()
        {
            return mObject.contentDescription.mValue;
        }
        /**
         * Returns Content Type of ContentHints attribute
         * @return
         */
        public Asn1ObjectIdentifier getContentType()
        {
            return mObject.contentType;
        }
    }
}