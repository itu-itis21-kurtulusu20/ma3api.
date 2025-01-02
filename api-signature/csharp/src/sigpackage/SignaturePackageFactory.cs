using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;

namespace tr.gov.tubitak.uekae.esya.api.signature.sigpackage
{
    /**
     * @author yavuz.kahveci
     */
    public class SignaturePackageFactory
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private static List<SignaturePackageProvider> providers = new List<SignaturePackageProvider>();

        private static String ASIC_PROVIDER = "tr.gov.tubitak.uekae.esya.api.asic.ASiCSignaturePackageProvider";

        static SignaturePackageFactory()
        {
            addToKnownProvidersIfInClasspath(ASIC_PROVIDER);
        }

        public static SignaturePackage createPackage(Context context, PackageType packageType, SignatureFormat format)
        {
            foreach (SignaturePackageProvider provider in providers)
            {
                if (provider.supportsPackageType(packageType))
                {
                    return provider.createPackage(context, packageType, format);
                }
            }
            throw new NotSupportedException("Provider not found for "+packageType);
        }

        public static SignaturePackage readPackage(Context context, FileInfo file)
        {
            // todo package type detection from file and use appropriate provider
            foreach (SignaturePackageProvider provider in providers)
            {
                return provider.readPackage(context, file);
            }
            throw new NotSupportedException("Provider not found for "+file);
        }

        // internal
        private static void addToKnownProvidersIfInClasspath(String providerClassName)
        {
            try
            {
                SignaturePackageProvider provider = createProvider(providerClassName);
                providers.Add(provider);
                logger.Info("Found provider : "+ASIC_PROVIDER);
            }
            catch (Exception x)
            {
                logger.Info("Provider is not in classpath : "+providerClassName);
            }
        }

        private static SignaturePackageProvider createProvider(String className)
        {
            try
            {
                return (SignaturePackageProvider) Activator.CreateInstance("ma3api-asic", className).Unwrap();
            } catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }

        /*public static SignaturePackage createPackage(Context context, PackageType packageType, SignatureFormat format){
            return selectPackageImpl(context, packageType, format);
        }

        public static SignaturePackage readPackage(Context context, FileInfo fileInfo)
        {
            try {
                ASiCReader reader = new ASiCReader();
                PackageContents contents = reader.read(fileInfo, context);

                AbstractSignaturePackage sp = selectPackageImpl(context, fileInfo, contents);

                 //todo verify contents ?

                return sp;
            }catch (Exception x){
                throw new SignatureException(x);
            }
        }

        private static AbstractSignaturePackage selectPackageImpl(Context context, PackageType? packageType, SignatureFormat? signatureFormat){
            switch (packageType){
                case PackageType.ASiC_S:
                   switch (signatureFormat){
                       case SignatureFormat.XAdES: return new ASiCSXAdESPackage(context);
                       case SignatureFormat.CAdES: return new ASiCSCAdESPackage(context);
                   }
                   break;
                case PackageType.ASiC_E:
                   switch (signatureFormat){
                       case SignatureFormat.XAdES: return new ASiCEXAdESPackage(context);
                       case SignatureFormat.CAdES: return new ASiCECAdESPackage(context);
                   }
                   break;
                default:
                    throw new SignatureRuntimeException("Not supported signature package: "+packageType+"-"+signatureFormat);

            }
            return null;
        }

        private static Dictionary<String, PackageType> extensions = new Dictionary<String, PackageType>()
                                                                        { {"asics", PackageType.ASiC_S},
                                                                          {"scs",   PackageType.ASiC_S},
                                                                          {"asice", PackageType.ASiC_E},
                                                                          {"sce",   PackageType.ASiC_E},
                                                                        };
        private static Dictionary<String, PackageType> mimetypes  = new Dictionary<String, PackageType>()
                                                                        { {"application/vnd.etsi.asic-s+zip", PackageType.ASiC_S},
                                                                          {"application/vnd.etsi.asic-e+zip", PackageType.ASiC_E},
                                                                        };*/

        /*static {
            extensions.put("asics", PackageType.ASiC_S);
            extensions.put("scs",   PackageType.ASiC_S);
            extensions.put("asice", PackageType.ASiC_E);
            extensions.put("sce",   PackageType.ASiC_E);

            mimetypes.put("application/vnd.etsi.asic-s+zip", PackageType.ASiC_S);
            mimetypes.put("application/vnd.etsi.asic-e+zip", PackageType.ASiC_E);
        }*/

        /*private static AbstractSignaturePackage selectPackageImpl(Context context, FileInfo fileInfo, PackageContents contents)
        {
             //todo determine type
             //detect mimetype
            
            PackageType? byExtension = null;
            PackageType? byMimetype = null;
            PackageType? decidedPackageType = null;
            SignatureFormat? format = null;

            String fileName = fileInfo.Name;
            String mimetype = contents.getMimetype();

            int ext = fileName.IndexOf('.');

            if (ext>0){
                String extension = fileName.Substring(ext+1);
                byExtension = extensions[extension.ToLower()];
            }

            if (mimetype!=null){
                byMimetype = mimetypes[mimetype];
            }*/

            /** todo
            The comment field in the ZIP header may be used to identify the type of
             the data object within the container.
            If this field is present, it should be set with "mimetype=" followed by
             the mime type of the data object held in the signed data object.
            */

             //pick  according to
             //An optional "mimetype" inserted containing the mime type defined in
             //clause 5.2.1. If the file extension does not imply use of ASiC then
             //the "mimetype" SHALL be present.
            /*if (byExtension!=null && byMimetype==null){
                decidedPackageType = byExtension;
            }
            else if (byExtension==null && byMimetype!=null){
                decidedPackageType = byMimetype;
            }
            else if (byExtension==null && byMimetype==null){
                throw new SignatureException("Unknown extension and missing mimetype!");
            }
            else if (byExtension==byMimetype){
                decidedPackageType = byExtension;
            }
            else if (byExtension!=byMimetype){
                throw new SignatureException("Extension("+byExtension+") and mimetype("+byMimetype+") does not match");
            }

            if (contents.getContainers().Count < 1)
                throw new SignatureException("No signature container found in package");
            SignatureContainerEntry entry = contents.getContainers()[0];
            String docName = entry.getASiCDocumentName().ToLower();
            if (docName.EndsWith("p7s"))
                format = SignatureFormat.CAdES;
            else if (docName.EndsWith("xml"))
                format = SignatureFormat.XAdES;

            AbstractSignaturePackage asp = selectPackageImpl(context, decidedPackageType, format);
            asp.setContents(contents);
            return asp;
        }*/
    }
}