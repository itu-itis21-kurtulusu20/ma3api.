package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.ESSCertID;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerSerial;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificate;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfESSCertID;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;

/**
 * The signing certificate attribute is designed to prevent the simple
 * substitution and re-issue attacks, and to allow for a restricted set
 * of authorization certificates to be used in verifying a signature.
 * The first certificate identified in the sequence of certificate
 * identifiers MUST be the certificate used to verify the signature.
 * RFC 2634 5.4
 * @author aslihanu
 *
 */

public class SigningCertificateAttr extends AttributeValue
{
     private static final DigestAlg DIGEST_ALG = DigestAlg.SHA1;
     public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_signingCertificate;

     public SigningCertificateAttr ()
     {
          super();
     }

     
     
     public void setValue() throws CMSSignatureException
     {
          //tek sertifika olduğunu varsayıyorum
    	  Object sertifika = mAttParams.get(AllEParameters.P_SIGNING_CERTIFICATE);
    	  if (sertifika == null)
    	  {
    		  Object objSC = mAttParams.get(AllEParameters.P_MOBILE_SIGNER_SIGNING_CERT_ATTR);
    		  if(objSC == null)
    			  throw new NullParameterException("SIGNING_CERTIFICATE parametre değeri null");
    		  else{
    			  SigningCertificate sc = (SigningCertificate) objSC;
    			  _setValue(sc);
    			  return;
    		  }
    	  }
          
    	  ECertificate cert = null;
          try
          {
               cert = (ECertificate) sertifika;
          } catch (ClassCastException ex)
          {
               throw new CMSSignatureException("SIGNING_CERTIFICATE parametresi Certificate tipinde değil",ex);
          }
          
          _setValue(_signCertOlustur(new ECertificate[]{cert}));
     }


     /**
      * The sequence of policy information terms identifies those certificate
      policies that the signer asserts apply to the certificate, and under
      which the certificate should be relied upon. This value suggests a
      policy value to be used in the relying party’s certification path
      validation.
      RFC 2634 page 47
      * @param aCertificates
      * @param aPolicyInfo
      * @throws KriptoException
      */
     

     // The sequence of policy information field is not used in the etsi
     private SigningCertificate _signCertOlustur (ECertificate[] aCertificates) throws
     CMSSignatureException
     {
          _SeqOfESSCertID certs = _certSeqOlustur(aCertificates);
          SigningCertificate sc = new SigningCertificate(certs);
          return sc;
     }


     
     private _SeqOfESSCertID _certSeqOlustur (ECertificate[] aCertificates) throws CMSSignatureException
     {
          ESSCertID[] ids = new ESSCertID[aCertificates.length];
          for (int i = 0; i < aCertificates.length; i++)
          {
               ECertificate certificate = aCertificates[i];
               //sertifikanin ozetini al
               //The ESS signing-certificate attribute, which is adopted in existing standards, may be used if the
               //SHA-1 hashing algorithm is used.(ETSI 5.7.3)
               Asn1OctetString certHash = new Asn1OctetString(AttributeUtil.createOtherHashValue(certificate.getEncoded(), DIGEST_ALG));

               //IssuerSerial ekle
               IssuerSerial is = AttributeUtil.createIssuerSerial(certificate);
               ids[i] = new ESSCertID(certHash, is);

          }
          _SeqOfESSCertID certs = new _SeqOfESSCertID(ids);
          return certs;

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
	 * Returns AttributeOID of SigningCertificateAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	
}