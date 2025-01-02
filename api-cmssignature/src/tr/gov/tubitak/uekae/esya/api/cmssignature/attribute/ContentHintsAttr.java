package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentHints;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * The content-hints attribute provides information on the innermost signed content 
 * of a multi-layer message where one content is encapsulated in another.
 * (etsi 101733v010801 5.10.3)
 * @author aslihanu
 *
 */

public class ContentHintsAttr
extends AttributeValue
{
     public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_contentHint;

     protected EContentHints mContentHints;

     /**
 	 * Create ContentHintsAttr with content hints
 	  * @param aContentHints EContentHints
 	  * @throws NullParameterException
 	  */
     public ContentHintsAttr (EContentHints aContentHints) throws NullParameterException  
     {
          super();
          mContentHints = aContentHints;
          if(mContentHints == null)
          {
        	  throw new NullParameterException("Content hints must be set");
          }
     }
     /**
    	 * Set content hints
    	 */
     public void setValue () throws CMSSignatureException
     {
          _setValue(mContentHints.getObject());
     }

     /**
 	 * Checks whether attribute is signed or not.
 	 * @return True 
 	 */   
	public boolean isSigned() 
	{
		return true;
	}

	/**
	 * Returns AttributeOID of Content Hints attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	/**
	 * Returns  content hints
	 * @param aAttribute EAttribute
	 * @return
	 * @throws ESYAException
	 */
	public static EContentHints toContentHints(EAttribute aAttribute) throws ESYAException
	{
		return new EContentHints(aAttribute.getValue(0));
	}
}