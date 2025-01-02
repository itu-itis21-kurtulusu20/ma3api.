using System;
using System.IO;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.signature.impl
{
    /**
     * Signable file which its content will be added to a signature.
     *
     * @see tr.gov.tubitak.uekae.esya.api.signature.Signable
     * @see tr.gov.tubitak.uekae.esya.api.signature.Signature#addContent(tr.gov.tubitak.uekae.esya.api.signature.Signable, boolean)
     *
     * @author ayetgin
     */
    public class SignableFile : BaseSignable
    {
        private readonly FileInfo mFile;
        private readonly String mMime;
       

        // todo mime type detection?

        public SignableFile(String aPath, String aMimeType)
            : base()
        {
            mFile = new FileInfo(aPath);
            //if (mFile.isDirectory())
            if(Directory.Exists(aPath))
                throw new SignatureRuntimeException("Directory cant be signed!");
            //if (!mFile.Exists)
            if(!File.Exists(aPath))
                throw new SignatureRuntimeException("Signable file "+aPath+" not exists!");
            mMime = aMimeType;
        }

        public SignableFile(FileInfo aFile, String aMimeType)
            : base()
        {
            mFile = aFile;
            mMime = aMimeType;
        }

        public override Stream getContent()
        {
            try {
                content = new FileStream(mFile.FullName, FileMode.Open, FileAccess.Read);
                return content;
            } catch (Exception x){
                throw new SignatureException(x);
            }
        }
        //emin değilim
        public override String getURI()
        {
            Uri uri=new Uri(mFile.FullName);
            String s = new Uri(uri.AbsoluteUri).ToString();
            return System.Text.Encoding.ASCII.GetString(Encoding.ASCII.GetBytes(s));
            //return mFile.toURI().toASCIIString();

        }

        public override String getMimeType()
        {
            return mMime;
        }
    }
}
