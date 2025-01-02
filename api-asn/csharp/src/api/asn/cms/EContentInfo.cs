using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.util;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EContentInfo : BaseASNWrapper<ContentInfo>
    {
        public EContentInfo(ContentInfo aObject)
            : base(aObject)
        {
        }

        /**
         * Create EContentInfo
         * @param contentType
         * @param content
         */
        public EContentInfo(Asn1ObjectIdentifier contentType, Asn1OpenType content)
            : base(new ContentInfo(contentType, content))
        {
            //super(new ContentInfo(contentType, content));
        }

        /**
         * Create EContentInfo from byte array
         * @param aBytes byte[]
         */
        public EContentInfo(byte[] aBytes)
            : base(aBytes, new ContentInfo())
        {
        }

        /**
         * Create EContentInfo from Stream
         * @param aBytes byte[]
         */
        public EContentInfo(Stream aContentInfoStream)
            : base(aContentInfoStream, new ContentInfo())
        {
        }

        /**
         * Returns content type of ContentInfo
         * @return
         */
        public Asn1ObjectIdentifier getContentType()
        {
            return mObject.contentType;
        }
        /**
         * Set content type
         * @param aContentType  Asn1ObjectIdentifier
         */
        public void setContentType(Asn1ObjectIdentifier aContentType)
        {
            mObject.contentType = aContentType;
        }
        /**
         * Returns content value
         * @return 
         */
        public byte[] getContent()
        {
            return mObject.content.mValue;
        }
        /**
         * Set content value
         * @param aContent  byte[]
         */
        public void setContent(byte[] aContent)
        {
            mObject.content = new Asn1OpenType(aContent);
        }
        /**
         * Returns byte array of ContentInfo's content 
         * @return  byte[]
         */
        public byte[] getContentStringByteValue()
        {
            Asn1OctetString str = new Asn1OctetString();
            UtilOpenType.fromOpenType(mObject.content, str);
            return str.mValue;
        }

    }
}
