/**
 * This particular xml document suppose to
 *    output same exact bytes that comes in if document did not change
 *    In signatures' world, this is how things goes...
 * @author yavuz.kahveci
 */
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    public interface ASiCDocument
    {
        void read(Stream inputStream);

        /**
         * Output document content to stream. If document has no changes
         * this method supposed to output same bytes that were read.
         *
         * @param outputStream
         * @throws SignatureException
         */
        void write(Stream outputStream);

        string getASiCDocumentName();
        void setASiCDocumentName(string name);

    }
}