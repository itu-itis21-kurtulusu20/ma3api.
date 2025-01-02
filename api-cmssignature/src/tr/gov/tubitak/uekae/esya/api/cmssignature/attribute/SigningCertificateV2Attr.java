package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESigningCertificateV2;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.ESSCertIDv2;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerSerial;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificateV2;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfESSCertIDv2;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfPolicyInformation;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyInformation;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;


public class SigningCertificateV2Attr extends AttributeValue {

	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_signingCertificateV2;
	private DigestAlg mDigestAlg = DigestAlg.SHA256;


	public SigningCertificateV2Attr()
    {
         super();
    }


	public void setValue() throws CMSSignatureException
    {
         //tek sertifika olduğunu varsayıyorum

         Object sertifika = mAttParams.get(AllEParameters.P_SIGNING_CERTIFICATE);

         if (sertifika == null)
         {
        	 Object objSC = mAttParams.get(AllEParameters.P_MOBILE_SIGNER_SIGNING_CERT_ATTRv2);
        	 if(objSC == null)
        		 throw new NullParameterException("SIGNING_CERTIFICATE parametre değeri null");
        	 else{
        		 SigningCertificateV2 sc = ((ESigningCertificateV2) objSC).getObject();
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
              throw new CMSSignatureException("SIGNING_CERTIFICATE parametresi ECertificate tipinde değil",
                                     ex);
         }

         Object digestAlgO = mAttParams.get(AllEParameters.P_REFERENCE_DIGEST_ALG);
         if(digestAlgO != null)
         {
        	 try
        	 {
        		 mDigestAlg = (DigestAlg) digestAlgO;
        	 }
        	 catch(ClassCastException ex)
        	 {
        		 throw new CMSSignatureException("P_REFERENCE_DIGEST_ALG parametresi DigestAlg tipinde değil",ex);
        	 }
         }

         _setValue(_signCertOlustur(new ECertificate[]{cert},new DigestAlg[]{mDigestAlg},null));
    }

	private SigningCertificateV2 _signCertOlustur (ECertificate[] aCertificates,DigestAlg[] aDigestAlgs, PolicyInformation[] aPolicies) throws
	CMSSignatureException
    {
		 _SeqOfESSCertIDv2 certs = _certSeqOlustur(aCertificates,aDigestAlgs);
		 _SeqOfPolicyInformation policies = null;

		 if(aPolicies != null && aPolicies.length != 0)
		 {
			 policies = new _SeqOfPolicyInformation(aPolicies);
		 }

	     SigningCertificateV2 sc = new SigningCertificateV2(certs,policies);
	     return sc;
    }

	private _SeqOfESSCertIDv2 _certSeqOlustur (ECertificate[] aCertificates,DigestAlg[] aDigestAlgs) throws CMSSignatureException
    {
		ESSCertIDv2[] ids = new ESSCertIDv2[aCertificates.length];
        for (int i = 0; i < aCertificates.length; i++)
         {
        	  ECertificate certificate = aCertificates[i];

              Asn1OctetString certHash = new Asn1OctetString(AttributeUtil.createOtherHashValue(certificate.getEncoded(), mDigestAlg));

              AlgorithmIdentifier aid = new AlgorithmIdentifier(aDigestAlgs[i].getOID());
              if(aDigestAlgs[i] == DigestAlg.SHA256){
            	  aid = null;
              }
              IssuerSerial is = AttributeUtil.createIssuerSerial(certificate);

              ids[i] = new ESSCertIDv2(aid,certHash, is);


         }
         _SeqOfESSCertIDv2 certs = new _SeqOfESSCertIDv2(ids);
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
	 * Returns AttributeOID of SigningCertificateV2Attr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID()
	{
		return OID;
	}



}
