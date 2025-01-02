using System.IO;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    /**
     * Encapsulates data to be signed.
     */
    public interface ISignable
    {
        /**
     * Returns content data to add content to the signature. Not used at external sign.
     * @return data to be signed
     */
        byte[] getContentData();

        /**
    * Returns digest of content to sign. Cache digest if function takes time, it is called several times for one signature. 
    * @param aDigestAlg algorithm for digest operation
    * @return digest value of the data according t digest alg
    */
        byte[] getMessageDigest(DigestAlg aDigestAlg);

        /**
	 * Returns content as input stream. Used while creating ESA type signatures.
	 * @return
	 * @throws IOException
	 */
        Stream getAsInputStream();
    }
}