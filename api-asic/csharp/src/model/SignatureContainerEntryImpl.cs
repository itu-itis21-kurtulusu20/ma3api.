using System.IO;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    /**
     * Signature container and its location in the signature package
     * @author yavuz.kahveci
     */
    public class SignatureContainerEntryImpl : SignatureContainerEntry, ASiCDocument
    {
        string entryName;
        SignatureContainer container;

        public SignatureContainerEntryImpl()
        {
        }

        public SignatureContainerEntryImpl(string aEntryName, SignatureContainer aContainer)
        {
            entryName = aEntryName;
            container = aContainer;
        }

        public void read(Stream inputStream)
        {
            container.read(inputStream);
        }

        public void write(Stream outputStream)
        {
            container.write(outputStream);
        }

        public string getASiCDocumentName()
        {
            return entryName;
        }

        public void setASiCDocumentName(string name)
        {
            entryName = name;
        }

        public SignatureContainer getContainer()
        {
            return container;
        }

        public void setContainer(SignatureContainer aContainer)
        {
            container = aContainer;
        }
    }
}