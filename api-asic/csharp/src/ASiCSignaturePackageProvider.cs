using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.asic.core.impl;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.asic.util;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    /**
     * @author suleyman.uslu
     */

    public class ASiCSignaturePackageProvider : SignaturePackageProvider
    {
        public SignaturePackage createPackage(Context context, PackageType packageType, SignatureFormat format)
        {
            return SelectPackageImpl(context, packageType, format);
        }

        public SignaturePackage readPackage(Context context, FileInfo file)
        {
            try
            {
                ASiCReader reader = new ASiCReader();
                PackageContents contents = reader.read(file, context);

                AbstractSignaturePackage sp = SelectPackageImpl(context, file, contents);

                foreach (SignatureContainerEntryImpl entry in contents.getContainers())
                {
                    SignatureContainer sc = entry.getContainer();
                    var ex = sc as SignatureContainerEx;
                    if (ex != null)
                    {
                        ex.setSignaturePackage(sp);
                    }
                }
                // todo verify contents ?

                    return sp;
            }
            catch (Exception x)
            {
                throw new SignatureException(x);
            }
        }

        public bool supportsPackageType(PackageType packageType)
        {
            return true;
        }


        // internal
        private AbstractSignaturePackage SelectPackageImpl(Context context, PackageType? packageType,
            SignatureFormat? signatureFormat)
        {
            switch (packageType)
            {
                case PackageType.ASiC_S:
                    switch (signatureFormat)
                    {
                        case SignatureFormat.XAdES:
                            return new ASiCSXAdESPackage(context);
                        case SignatureFormat.CAdES:
                            return new ASiCSCAdESPackage(context);
                    }
                    break;
                case PackageType.ASiC_E:
                    switch (signatureFormat)
                    {
                        case SignatureFormat.XAdES:
                            return new ASiCEXAdESPackage(context);
                        case SignatureFormat.CAdES:
                            return new ASiCECAdESPackage(context);
                    }
                    break;
                default:
                    throw new SignatureRuntimeException(string.Format(Resource.message(Resource.HATA_IMZA_PAKETI_DESTEKLENMIYOR), packageType, signatureFormat));
                   
            }
            return null;
        }


        private readonly IDictionary<String, PackageType> _extensions = new Dictionary<String, PackageType>();
        private readonly IDictionary<String, PackageType> _mimetypes = new Dictionary<String, PackageType>();


        public ASiCSignaturePackageProvider()
        {
            _extensions.Add("asics", PackageType.ASiC_S);
            _extensions.Add("scs", PackageType.ASiC_S);
            _extensions.Add("asice", PackageType.ASiC_E);
            _extensions.Add("sce", PackageType.ASiC_E);

            _mimetypes.Add("application/vnd.etsi.asic-s+zip", PackageType.ASiC_S);
            _mimetypes.Add("application/vnd.etsi.asic-e+zip", PackageType.ASiC_E);
        }


        private AbstractSignaturePackage SelectPackageImpl(Context context, FileInfo file, PackageContents contents)
        {
            // todo determine type
            // detect mimetype
            PackageType? byExtension = null;
            PackageType? byMimetype = null;
            PackageType? decidedPackageType = null;
            SignatureFormat? format = null;

            String fileName = file.Name;
            String mimetype = contents.getMimetype();

            int ext = fileName.IndexOf('.');

            if (ext > 0)
            {
                String extension = fileName.Substring(ext + 1);
                if (_extensions.ContainsKey(extension.ToLower()))
                    byExtension = _extensions[extension.ToLower()];
            }

            if (mimetype != null)
            {
                byMimetype = _mimetypes[mimetype];
            }

            /** todo
             The comment field in the ZIP header may be used to identify the type of
             the data object within the container.
             If this field is present, it should be set with "mimetype=" followed by
             the mime type of the data object held in the signed data object.
             */

            // pick  according to
            // An optional "mimetype" inserted containing the mime type defined in
            // clause 5.2.1. If the file extension does not imply use of ASiC then
            // the "mimetype" SHALL be present.
            if (byExtension == null && byMimetype == null)
            {
                throw new SignatureException(Resource.message(Resource.HATA_IMZA_PAKETI_BILINMEYEN_UZANTI_EKSIK_MIMETYPE));
            }

            if (byMimetype != null && byExtension != null)
            {
                if (byExtension == byMimetype)
                {
                    decidedPackageType = byExtension;
                }
                else
                {
                    throw new SignatureException(string.Format(Resource.message(Resource.HATA_IMZA_PAKETI_UZANTI_MIMETYPE_UYUZMAZLIK), byExtension, byMimetype));
                }
            }
            else if (byExtension != null)
            {
                decidedPackageType = byExtension;
            }
            else
            {
                decidedPackageType = byMimetype;
            }


            if (contents.getContainers().Count < 1)
                throw new SignatureException(Resource.message(Resource.IMZA_PAKETI_IMZA_KONTEYNERI_BULUNAMADI));
            
            SignatureContainerEntryImpl entry = contents.getContainers()[0];
            String docName = entry.getASiCDocumentName().ToLower();
            if (docName.EndsWith("p7s"))
            {
                format = SignatureFormat.CAdES;
            }
            else if (docName.EndsWith("xml"))
            {
                format = SignatureFormat.XAdES;
            }

            AbstractSignaturePackage asp = SelectPackageImpl(context, decidedPackageType, format);
            asp.setContents(contents);
            return asp;
        }
    }
}
