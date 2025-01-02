package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import java.util.List;

import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.CompleteCertificateRefs;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * The complete-certificate-references attribute is an unsigned attribute. It references the full set of CA
 * certificates that have been used to validate an ES with Complete validation data up to (but not including) the signer's
 * certificate.
 * (etsi 101733v010801 6.2.1)
 * @author aslihan.kubilay
 *
 */

public class CompleteCertRefAttr extends AttributeValue
{
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_certificateRefs;
	private DigestAlg mDigestAlg = DigestAlg.SHA256; //If not set by user,default sha256 is used
	
     public CompleteCertRefAttr()
     {
          super();
     }

     @SuppressWarnings("unchecked")
	 public void setValue() throws CMSSignatureException
     {
    	 //P_CERTIFICATE_REVOCATION_LIST parameter is set internally and includes certificates and the corresponding revocation information
    	 List<CertRevocationInfo> list = (List<CertRevocationInfo>) mAttParams.get(AllEParameters.P_CERTIFICATE_REVOCATION_LIST);
    	 
    	 Object digestAlgO = mAttParams.get(AllEParameters.P_REFERENCE_DIGEST_ALG);
         if(digestAlgO !=null)
         {
	       	  try
	       	  {
	       		  mDigestAlg = (DigestAlg) digestAlgO;
	       	  }
	       	  catch(ClassCastException aEx)
	       	  {
	       		  throw new CMSSignatureException("P_REFERENCE_DIGEST_ALG parameter is not of type DigestAlg",aEx);
	       	  }
         }
         
 		 //Creates the complete-certificate-references attribute
         CompleteCertificateRefs refs = AttributeUtil.createCertificateReferences(list, mDigestAlg);

        _setValue(refs);
    	 
     }
     
	 /**
	 * Checks whether attribute is signed or not.
	 * @return false 
	 */ 
	public boolean isSigned()
	{
		return false;
	}

	/**
	 * Returns AttributeOID of CompleteCertRefAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
}