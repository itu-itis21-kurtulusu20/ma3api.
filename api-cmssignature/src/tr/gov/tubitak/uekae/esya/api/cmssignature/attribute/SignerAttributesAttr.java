package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.attrcert.EAttributeCertificate;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EClaimedAttributes;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerAttribute;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerAttribute_element;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * The signer-attributes attribute specifies additional attributes of the signer (e.g. role).
 *  It may be either:
		• claimed attributes of the signer; or
		• certified attributes of the signer.
		
 * (etsi 101733v010801 5.11.3)		
 * @author aslihan.kubilay
 *
 */

public class SignerAttributesAttr
extends AttributeValue
{
	 public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_signerAttr;

	 protected EClaimedAttributes mClaimedAttributes;
	 protected EAttributeCertificate mAttributeCertificate;
	 
	 public SignerAttributesAttr (EClaimedAttributes aClaimedAttributes) 
     {
          super();
          mClaimedAttributes = aClaimedAttributes;
     }
	 
	 public SignerAttributesAttr (EAttributeCertificate aAttributeCertificate)
	 {
		 super();
		 mAttributeCertificate = aAttributeCertificate;
	 }
	 
	 private SignerAttribute _signerAttrsOlustur(EClaimedAttributes aClaimedAttributes)
     {
    	 SignerAttribute_element[] saElement = new SignerAttribute_element[1];
    	 saElement[0] = new SignerAttribute_element();
    	 saElement[0].set_claimedAttributes(aClaimedAttributes.getObject());
    	 SignerAttribute signerAttribute = new SignerAttribute(saElement);
    	 return signerAttribute;
     }
   
       
//     private SignerAttribute _signerAttrsOlustur(EAttribute[] aAttributes)
//     {
//    	 ClaimedAttributes claimeds = new ClaimedAttributes(aAttributes.length);
//    	 for(int i=0;i<aAttributes.length;i++)
//    	 {
//    		 claimeds.elements[i]=aAttributes[i].getObject();
//    	 }
//    	 
//    	 
//    	 SignerAttribute_element[] saElement = new SignerAttribute_element[1];
//    	 saElement[0] = new SignerAttribute_element();
//    	 saElement[0].set_claimedAttributes(claimeds);
//    	 
//    	 SignerAttribute signerAttribute = new SignerAttribute(saElement);
//    	 return signerAttribute;
//    	 
//     }
     
     private SignerAttribute _signerAttrsOlustur(AttributeCertificate aAttributeCert)
     {
    	 SignerAttribute_element[] saElement = new SignerAttribute_element[1];
    	 saElement[0] = new SignerAttribute_element();
    	 saElement[0].set_certifiedAttributes(aAttributeCert);
    	 SignerAttribute signerAttribute = new SignerAttribute(saElement);
    	 return signerAttribute;
    	 
     }

     public void setValue ()
     throws CMSSignatureException
     {
        if(mClaimedAttributes==null && mAttributeCertificate==null)
        {
        	throw new CMSSignatureException("Claimed attribute or attribute certificate must be set");
        }
        
        //If claimed signer attributes will be added
        if(mClaimedAttributes!=null)
        {
        	_setValue(_signerAttrsOlustur(mClaimedAttributes));
        }
        else
        {
           	_setValue(_signerAttrsOlustur(mAttributeCertificate.getObject()));
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
	 * Returns AttributeOID of SignerAttributesAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	
	public static  ESignerAttribute  toESignerAttribute(EAttribute aAttribute) throws ESYAException
	{
		return new ESignerAttribute(aAttribute.getValue(0));
	}
	
	
}