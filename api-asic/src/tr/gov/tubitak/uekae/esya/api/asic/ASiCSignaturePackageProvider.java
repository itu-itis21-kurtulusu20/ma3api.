package tr.gov.tubitak.uekae.esya.api.asic;

import tr.gov.tubitak.uekae.esya.api.asic.core.impl.*;
import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.asic.model.SignatureContainerEntryImpl;
import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCReader;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignatureContainerEx;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackage;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackageProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
public class ASiCSignaturePackageProvider implements SignaturePackageProvider {

    public SignaturePackage createPackage(Context context, PackageType packageType, SignatureFormat format) {
        return selectPackageImpl(context, packageType, format);
    }

    public SignaturePackage readPackage(Context context, File file) throws SignatureException {
        try {
            ASiCReader reader = new ASiCReader();
            PackageContents contents = reader.read(file, context);

            AbstractSignaturePackage sp = selectPackageImpl(context, file, contents);

            for (SignatureContainerEntryImpl entry : contents.getContainers()){
                SignatureContainer sc = entry.getContainer();
                if (sc instanceof SignatureContainerEx){
                    ((SignatureContainerEx) sc).setSignaturePackage(sp);
                }
            }
            
            // todo verify contents ?

            return sp;
        }catch (Exception x){
            throw new SignatureException(x);
        }
    }

    public boolean supportsPackageType(PackageType packageType) {
        return true;
    }


    // internal
    private static AbstractSignaturePackage selectPackageImpl(Context context, PackageType packageType, SignatureFormat signatureFormat){
        switch (packageType){
            case ASiC_S:
                switch (signatureFormat){
                    case XAdES: return new ASiCSXAdESPackage(context);
                    case CAdES: return new ASiCSCAdESPackage(context);
                }
            case ASiC_E:
                switch (signatureFormat){
                    case XAdES: return new ASiCEXAdESPackage(context);
                    case CAdES: return new ASiCECAdESPackage(context);
                }
            default:
                throw new SignatureRuntimeException("Not supported signature package: "+packageType+"-"+signatureFormat);

        }
    }


    private static Map<String, PackageType> extensions = new HashMap<String, PackageType>();
    private static Map<String, PackageType> mimetypes  = new HashMap<String, PackageType>();

    static {
        extensions.put("asics", PackageType.ASiC_S);
        extensions.put("scs",   PackageType.ASiC_S);
        extensions.put("asice", PackageType.ASiC_E);
        extensions.put("sce",   PackageType.ASiC_E);

        mimetypes.put("application/vnd.etsi.asic-s+zip", PackageType.ASiC_S);
        mimetypes.put("application/vnd.etsi.asic-e+zip", PackageType.ASiC_E);
    }


    private AbstractSignaturePackage selectPackageImpl(Context context, File file, PackageContents contents)
            throws SignatureException
    {
        // todo determine type
        // detect mimetype
        PackageType byExtension = null;
        PackageType byMimetype = null;
        PackageType decidedPackageType = null;
        SignatureFormat format = null;

        String fileName = file.getName();
        String mimetype = contents.getMimetype();

        int ext = fileName.indexOf('.');

        if (ext>0){
            String extension = fileName.substring(ext+1);
            byExtension = extensions.get(extension.toLowerCase());
        }

        if (mimetype!=null){
            byMimetype = mimetypes.get(mimetype);
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
        if (byExtension!=null && byMimetype==null){
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

        if (contents.getContainers().size()<1)
            throw new SignatureException("No signature container found in package");
        SignatureContainerEntryImpl entry = contents.getContainers().get(0);
        String docName = entry.getASiCDocumentName().toLowerCase();
        if (docName.endsWith("p7s")) {
            format = SignatureFormat.CAdES;
        } else if (docName.endsWith("xml")) {
            format = SignatureFormat.XAdES;
        }

        AbstractSignaturePackage asp = selectPackageImpl(context, decidedPackageType, format);
        asp.setContents(contents);
        return asp;
    }

}