package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECompleteCertificateReferences;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ValueFinderFromElsewhere;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateValues;

import java.util.List;

/**
 * The certificate-values attribute is an unsigned attribute. Only a single instance of this attribute shall occur with
 * an electronic signature. It holds the values of certificates referenced in the complete-certificate-references attribute.
 * (etsi 101733v010801 6.3.3)
 * @author aslihan.kubilay
 *
 */

public class CertValuesAttr extends AttributeValue {

	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_certValues;

	public CertValuesAttr()
	{
		super();
	}
	
	
	@SuppressWarnings("unchecked")
	public void setValue() throws CMSSignatureException 
	{
		CertificateValues values = null;
		
		//This list includes certificates and the revocation information for each certificate.
		//This parameter is an internally set
		Object list = mAttParams.get(AllEParameters.P_CERTIFICATE_REVOCATION_LIST);

		if (list != null)
        {
        	List<CertRevocationInfo> certRevList = null;
	
			try 
			{
				certRevList = (List<CertRevocationInfo>) list;
			} 
			catch (ClassCastException aEx) 
			{
				throw new CMSSignatureException("P_CERTIFICATE_REVOCATION_LIST parameter is not of type List<CertRevocationInfo>",aEx);
			}

			//create the certificate-values attribute
			values = AttributeUtil.createCertificateValues(certRevList);
			
        }
		else
		{
			//If the P_CERTIFICATE_REVOCATION_LIST parameter is not set, the complete-certificate-references 
			//attribute in signerinfo is used. The references are searched in certificate store 
			ESignerInfo si = (ESignerInfo) mAttParams.get(AllEParameters.P_SIGNER_INFO);
			List<EAttribute> attrs = si.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs);
			if(attrs.isEmpty())
				throw new CMSSignatureException("CompleteCertificateRefs attribute doesnot exist in signerinfo");
			
			ECompleteCertificateReferences certRefs = null;
			try
			{
				certRefs = new ECompleteCertificateReferences(attrs.get(0).getValue(0));
			}
			catch(Exception aEx)
			{
				throw new CMSSignatureException("Error while decoding attribute as CompleteCertificateReferences",aEx);
			}

            ValidationInfoResolver vir = (ValidationInfoResolver)mAttParams.get(EParameters.P_VALIDATION_INFO_RESOLVER);
			ValueFinderFromElsewhere vfCS = new ValueFinderFromElsewhere(vir);

    		values = vfCS.findCertValues(certRefs).getObject();
    		
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
	 * Returns AttributeOID of CertValuesAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}

}
