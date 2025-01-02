using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * The certificate-values attribute is an unsigned attribute. Only a single instance of this attribute shall occur with
 * an electronic signature. It holds the values of certificates referenced in the complete-certificate-references attribute.
 * (etsi 101733v010801 6.3.3)
 * @author aslihan.kubilay
 *
 */
    public class CertValuesAttr:AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_certValues;

	public CertValuesAttr():base()
	{		
	}
		
	//@SuppressWarnings("unchecked")
	public override void setValue()
	{
		CertificateValues values = null;
		
		//This list includes certificates and the revocation information for each certificate.
		//This parameter is an internally set
        Object list = null;
        mAttParams.TryGetValue(AllEParameters.P_CERTIFICATE_REVOCATION_LIST, out list);
		
		if (list != null)
        {
            List<CertRevocationInfoFinder.CertRevocationInfo> certRevList = null;
	
			try 
			{
                certRevList = (List<CertRevocationInfoFinder.CertRevocationInfo>)list;
			} 
			catch (InvalidCastException aEx) 
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
            ESignerInfo si = (ESignerInfo)mAttParams[AllEParameters.P_SIGNER_INFO];
			List<EAttribute> attrs = si.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs);
			if(attrs.Count == 0)
				throw new CMSSignatureException("CompleteCertificateRefs attribute doesnot exist in signerinfo");
			
			ECompleteCertificateReferences certRefs = null;
			try
			{
				certRefs = new ECompleteCertificateReferences(attrs[0].getValue(0));
			}
			catch(Exception aEx)
			{
				throw new CMSSignatureException("Error while decoding attribute as CompleteCertificateReferences",aEx);
			}


            ValidationInfoResolver vir = (ValidationInfoResolver)mAttParams[EParameters.P_VALIDATION_INFO_RESOLVER];
            ValueFinderFromElsewhere vfCS = new ValueFinderFromElsewhere(vir);
            values = vfCS.findCertValues(certRefs).getObject();
		}

		_setValue(values);
	}

    /**
* Checks whether attribute is signed or not.
* @return false 
*/
	public override bool isSigned() 
	{
		return false;
	}
    /**
     * Returns AttributeOID of CertValuesAttr attribute
     * @return
     */
	public override Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
    }
}
