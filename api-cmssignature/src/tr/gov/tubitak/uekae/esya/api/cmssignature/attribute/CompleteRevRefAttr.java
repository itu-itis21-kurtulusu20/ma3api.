package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import java.util.List;

import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.CompleteRevocationRefs;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;


/**
 * The complete-revocation-references attribute is an unsigned attribute. Only a single instance of this
 * attribute shall occur with an electronic signature. It references the full set of the CRL, ACRL, or OCSP responses that
 * have been used in the validation of the signer, and CA certificates used in ES with Complete validation data
 * 
 * CompleteRevocationRefs shall contain one CrlOcspRef for the signing-certificate, followed by one
 * for each OtherCertID in the CompleteCertificateRefs attribute. The second and subsequent CrlOcspRef
 * fields shall be in the same order as the OtherCertID to which they relate. At least one of CRLListID or
 * OcspListID or OtherRevRefs should be present for all but the "trusted" CA of the certificate path.
 * 
 * (etsi 101733v010801 6.2.2)
 * @author aslihan.kubilay
 *
 */

public class CompleteRevRefAttr
extends AttributeValue
{
	 public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_revocationRefs;
	 public static enum RevRefsType {OCSP,CRL,BOTH};
	 private DigestAlg mDigestAlg = DigestAlg.SHA256;
	 
	 
     public CompleteRevRefAttr ()
     {
          super();
          
     }

     @SuppressWarnings("unchecked")
	public void setValue() throws CMSSignatureException
     {
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
         
         //It creates the complete-revocation-references attribute. The first CrlOcspRef of the list refers to signing certificate. The 
         //second and subsequent CrlOcspRef elements refer to OtherCertID elements in order in complete-certificate-references. For trusted certificates,
         //CrlOcspRef elements with null CRLListID,OcspListID,OtherRevRefs are put in attribute
         CompleteRevocationRefs refs = AttributeUtil.createRevocationReferences(list, mDigestAlg);

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
	 * Returns AttributeOID of CompleteRevRefAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
}