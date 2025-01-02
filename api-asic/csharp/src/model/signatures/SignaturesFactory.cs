using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;
using NotSupportedException = tr.gov.tubitak.uekae.esya.api.signature.NotSupportedException;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.signatures
{
    /**
     * @author yavuz.kahveci
     */
    public static class SignaturesFactory
    {
        private static Context defaultContext = new Context();
        
        // todo bu alttaki iki statement kapaliydi, ben actim, 9/9/13, suleyman
        // todo sonra da kapattim cunku garip bir hata veriyordu 12/9/13

        /*private static List<BaseSignatures> knownSignatures = new List<BaseSignatures>()
                                                                  {{new ASiCSignatures(defaultContext, null)},
                                                                   {new OCFSignatures(defaultContext, null)},
                                                                   {new ODFSignatures(defaultContext, null)},
                                                                  };

        static SignaturesFactory()
        {
            Context defaultContext = new Context();
            knownSignatures.Add(new ASiCSignatures(defaultContext, null));
            knownSignatures.Add(new OCFSignatures(defaultContext, null));
            knownSignatures.Add(new ODFSignatures(defaultContext, null));
        }*/


        public static BaseSignatures createSignatureContainer(PackageType type, Context context)
        {
            // set resolver for new types
            var contents = new PackageContents();
            switch (type) {
                case PackageType.ASiC_E:
                    return new ASiCSignatures(context, contents);
                case PackageType.OCF:
                    return new OCFSignatures(context, contents);
                case PackageType.ODF:
                    return new ODFSignatures(context, contents);
                case PackageType.UCF:
                    return null; // todo
            }
            throw new NotSupportedException("Signatures for type " + type);
        }

        public static SignatureContainerEntryImpl readSignatureContainer(Context context, PackageContents contents, Stream stream, string fileName)
        {
            try
            {
                SignatureContainer sc = null;
                if (fileName.ToLower().EndsWith("p7s"))
                {
                    sc = SignatureFactory.readContainer(SignatureFormat.CAdES, stream, context);
                }
                else
                {
                    ASiCSignatures aSignatures = new ASiCSignatures(context, contents);
                    aSignatures.setOwner(contents);
                    aSignatures.read(stream);
                    aSignatures.setASiCDocumentName(fileName);
                    sc = aSignatures;
                }
                var sce = new SignatureContainerEntryImpl(fileName, sc);
                return sce;
            }
            catch (Exception x)
            {
                Console.WriteLine(x.StackTrace);
                throw new SignatureRuntimeException(x);
            }
        }
    }
}