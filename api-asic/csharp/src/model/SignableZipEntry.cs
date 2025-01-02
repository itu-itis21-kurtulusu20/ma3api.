/**
 * @author yavuz.kahveci
 */
using System;
using System.IO;
using ICSharpCode.SharpZipLib.Zip;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;

namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    public class SignableZipEntry : BaseSignable
    {
        private readonly ZipFile file;
        private readonly ZipEntry entry;

        public SignableZipEntry(ZipFile aFile, ZipEntry aEntry)
        {
            file = aFile;
            entry = aEntry;
        }

        public override Stream getContent()
        {
            try {
                content = file.GetInputStream(entry);
                return content;
            } catch (Exception x){
                throw new SignatureException(x);
            }
        }

        public override String getURI()
        {
            return entry.Name;
        }

        public override String getMimeType()
        {
            return null;
        }
    }
}