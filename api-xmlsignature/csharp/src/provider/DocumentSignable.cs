using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.provider
{
    public class DocumentSignable : Document
    {
        readonly Signable signable;

        public DocumentSignable(Signable signable)
            : base(signable.getURI(), signable.getMimeType(), null)
        {
            this.signable = signable;
        }

        /* kullanilmiyor
        public Stream getStream()
        {
            try {
                return signable.getContent();
            } catch (Exception x){
                throw new XMLSignatureException(x, x.Message);
            }
        }*/

        public override Stream Stream
        {
            get
            {
                try
                {
                    return signable.getContent();
                }
                catch (Exception x)
                {
                    throw new XMLSignatureException(x, x.Message);
                }
            }
        }

        /* kullanilmiyor zaten
        public DataType getType()
        {
            return DataType.OCTETSTREAM;
        }*/

        public override DataType Type
        {
            get { return DataType.OCTETSTREAM; }
        }
    }
}
