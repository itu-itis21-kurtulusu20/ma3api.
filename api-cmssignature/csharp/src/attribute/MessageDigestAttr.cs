using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;


//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * The message-digest attribute type specifies the message digest of the 
 * encapContentInfo eContent OCTET STRING being signed in signed-data...
 * For signed-data, the message digest is computed using the signer’s message 
 * digest algorithm. (RFC 3852 11.2)
 * 
 * The message-digest attribute MUST be a signed attribute.(RFC 3852 11.2)
 * 
 * A message-digest attribute MUST have a single attribute value, even
 * though the syntax is defined as a SET OF AttributeValue. There MUST
 * NOT be zero or multiple instances of AttributeValue present.(RFC 3852 11.2)
 * 
 * The SignedAttributes in a signerInfo MUST include only one instance of the 
 * message-digest attribute.(RFC 3852 11.2)
 * 
 * @author aslihanu
 *
 */
    public class MessageDigestAttr : AttributeValue
    {
        private static readonly DigestAlg OZET_ALG = DigestAlg.SHA256;
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_messageDigest;

        public MessageDigestAttr()
            : base()
        {
        }


        public override void setValue()
        {
            Object content = null;
            mAttParams.TryGetValue(AllEParameters.P_CONTENT, out content);

            //If it is detached signature,content must be given by user as parameter
            if (content == null)
            {
                mAttParams.TryGetValue(AllEParameters.P_EXTERNAL_CONTENT, out content);
                if (content == null)
                    throw new CMSSignatureException("For messagedigest attribute, content could not be found in signeddata or in parameters");
            }

            Object digestAlgO = null;
            mAttParams.TryGetValue(AllEParameters.P_DIGEST_ALGORITHM, out digestAlgO);
            if (digestAlgO == null)
            {
                digestAlgO = OZET_ALG;
            }

            ISignable contentI = null;
            try
            {
                contentI = (ISignable)content;
            }
            catch (InvalidCastException ex)
            {
                throw new CMSSignatureException("P_EXTERNAL_CONTENT parameter is not of type ISignable", ex);
            }

            DigestAlg digestAlg = null;
            try
            {
                digestAlg = (DigestAlg)digestAlgO;
            }
            catch (InvalidCastException ex)
            {
                throw new CMSSignatureException("P_DIGEST_ALGORITHM parameter is not of type DigestAlg", ex);
            }

            try
            {
                _setValue(new Asn1OctetString(contentI.getMessageDigest(digestAlg)));
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error in hash calculation in messagedigest attribute", aEx);
            }
        }

        /**
        * Checks whether attribute is signed or not.
        * @return true 
        */ 
        public override bool isSigned()
        {
            return true;
        }

        /**
         * Returns AttributeOID of MessageDigestAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }

        public static EMessageDigestAttr toMessageDigest(EAttribute aAttribute)
        {
            return new EMessageDigestAttr(aAttribute.getValue(0));
        }
}
}
