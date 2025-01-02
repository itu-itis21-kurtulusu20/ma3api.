package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EMessageDigestAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;

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

public class MessageDigestAttr extends AttributeValue
{
	private static final DigestAlg OZET_ALG = DigestAlg.SHA256;
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_messageDigest;

     public MessageDigestAttr ()
     {
          super();
     }
     
     
     public void setValue () throws CMSSignatureException
     {
    	 Object content = mAttParams.get(AllEParameters.P_CONTENT);
    	 
    	 //If it is detached signature,content must be given by user as parameter
    	 if(content == null)
         {
    		   content = mAttParams.get(AllEParameters.P_EXTERNAL_CONTENT);
    		   if(content==null)
    			   throw new CMSSignatureException("For messagedigest attribute, content could not be found in signeddata or in parameters");
         }
    	 
    	 Object digestAlgO = mAttParams.get(AllEParameters.P_DIGEST_ALGORITHM);
         if (digestAlgO == null)
         {
              digestAlgO = OZET_ALG;
         }
          
         ISignable contentI = null;
          try
          {
        	  contentI = (ISignable) content;
          } 
          catch (ClassCastException ex)
          {
               throw new CMSSignatureException("P_EXTERNAL_CONTENT parameter is not of type ISignable",ex);
          }
          
          DigestAlg digestAlg = null;
          try
          {
        	  digestAlg = (DigestAlg) digestAlgO;
          } catch (ClassCastException ex)
          {
               throw new CMSSignatureException("P_DIGEST_ALGORITHM parameter is not of type DigestAlg",ex);
          }

          setValue(digestAlg, contentI);


     }

     public void setValue(DigestAlg aDigestAlg, ISignable aContent) throws CMSSignatureException
     {
         try
         {
             _setValue(new Asn1OctetString(aContent.getMessageDigest(aDigestAlg)));
         }
         catch(Exception aEx)
         {
             throw new CMSSignatureException("Error in hash calculation in messagedigest attribute",aEx);
         }
     }


     /**
 	 * Checks whether attribute is signed or not.
 	 * @return true 
 	 */ 
	public boolean isSigned() 
	{
		return true;
	}

	/**
	 * Returns AttributeOID of MessageDigestAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}

    public static EMessageDigestAttr toMessageDigest(EAttribute aAttribute) throws ESYAException {
        return new EMessageDigestAttr(aAttribute.getValue(0));
    }
}