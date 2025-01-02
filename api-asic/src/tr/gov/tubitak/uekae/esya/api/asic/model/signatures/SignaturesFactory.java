package tr.gov.tubitak.uekae.esya.api.asic.model.signatures;


import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.asic.model.SignatureContainerEntryImpl;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class SignaturesFactory
{
    private static List<BaseSignatures> knownSignatures = new ArrayList<BaseSignatures>();

    static {
        Context defaultContext = new Context();
        knownSignatures.add(new ASiCSignatures(defaultContext, null));
        knownSignatures.add(new OCFSignatures(defaultContext, null));
        knownSignatures.add(new ODFSignatures(defaultContext, null));
    }


    public static BaseSignatures createSignatureContainer(PackageType type, Context context)
    {
        // set resolver for new types
        PackageContents contents = new PackageContents();
        switch (type) {
            case ASiC_E:
                return new ASiCSignatures(context, contents);
            case OCF:
                return new OCFSignatures(context, contents);
            case ODF:
                return new ODFSignatures(context, contents);
            case UCF:
                return null; // todo
        }
        throw new NotSupportedException("Signatures for type " + type);
    }

    public static SignatureContainerEntryImpl readSignatureContainer(Context context, PackageContents contents, InputStream stream, String fileName)
            throws SignatureException
    {
        SignatureContainer sc = null;
        if (fileName.toLowerCase().endsWith("p7s")){
            sc = SignatureFactory.readContainer(SignatureFormat.CAdES, stream, context);
        }
        else {
            ASiCSignatures as = new ASiCSignatures(context, contents);
            as.setOwner(contents);
            as.read(stream);
            as.setASiCDocumentName(fileName);
            sc = as;
        }
        SignatureContainerEntryImpl sce = new SignatureContainerEntryImpl(fileName, sc);
        return sce;
    }
}
