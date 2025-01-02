package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECompleteRevocationReferences;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ValueFinderFromElsewhere;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationValues;

import java.util.List;

/**
 * The revocation-values attribute is an unsigned attribute. Only a single instance of this attribute shall occur with
 * an electronic signature. It holds the values of CRLs and OCSP referenced in the
 * complete-revocation-references attribute.
 * (etsi 101733v010801 6.3.4)
 * 
 * @author aslihan.kubilay
 *
 */

public class RevocationValuesAttr extends AttributeValue 
{
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_revocationValues;
	
	
	public RevocationValuesAttr()
	{
		super();
	}
	
	@SuppressWarnings("unchecked")
	public void setValue() throws CMSSignatureException 
	{
		RevocationValues values = null;
		
		//P_CERTIFICATE_REVOCATION_LIST parameter refers to a list which contains
		//certificates and corresponding revocation information for each certificate
		//it is an internal parameter,is not set by user
		Object list = mAttParams.get(AllEParameters.P_CERTIFICATE_REVOCATION_LIST);

		if(list != null)
        {
        	List<CertRevocationInfo> certRevListMap = null;
        	try 
			{
				certRevListMap = (List<CertRevocationInfo>) list;
			} 
			catch (ClassCastException aEx) 
			{
				throw new CMSSignatureException("P_CERTIFICATE_REVOCATION_LIST parameter is not of type List<CertRevocationInfo>",aEx);
			}

			values = AttributeUtil.createRevocationValues(certRevListMap);
			
        }
		else
		{
			//If the P_CERTIFICATE_REVOCATION_LIST parameter is not set, the complete-revocation-references 
			//attribute in signerinfo is used. The references are searched in certificate store to find values  
			ESignerInfo si = (ESignerInfo) mAttParams.get(AllEParameters.P_SIGNER_INFO);
			List<EAttribute> attrs = si.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs);
			if(attrs.isEmpty())
				throw new CMSSignatureException("CompleteRevocationReferences attribute could not be found");
			
			ECompleteRevocationReferences revRefs = null;
			try
			{
				revRefs = new ECompleteRevocationReferences(attrs.get(0).getValue(0));
			}
			catch(Exception aEx)
			{
				throw new CMSSignatureException("Error while decoding attribute as CompleteRevocationReferences",aEx);
			}

            ValidationInfoResolver vir = (ValidationInfoResolver)mAttParams.get(EParameters.P_VALIDATION_INFO_RESOLVER);
            ValueFinderFromElsewhere vfCS = new ValueFinderFromElsewhere(vir);
    		values = vfCS.findRevocationValues(revRefs).getObject();
		}
		
		_setValue(values);
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
	 * Returns AttributeOID of RevocationValuesAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}

}
