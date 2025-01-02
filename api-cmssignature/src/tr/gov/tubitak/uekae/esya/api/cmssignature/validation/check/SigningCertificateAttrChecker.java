package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import java.io.ByteArrayInputStream;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.ESSCertID;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerSerial;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificate;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;

/**
 * Checks if the information in signing certificate reference attribute is true
 * for the certificate used in signature validation
 * @author aslihan.kubilay
 *
 */
public class SigningCertificateAttrChecker extends BaseChecker
{
	protected boolean _check(Signer aSigner,CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_CERTIFICATE_ATTRIBUTE_CHECKER), SigningCertificateAttrChecker.class);
		ECertificate signerCert = aSigner.getSignerCertificate();
		
		List<EAttribute> scAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_aa_signingCertificate);
		
		if(scAttrs.isEmpty())
		{
			Checker checker = new SigningCertificateV2AttrChecker();
			checker.setParameters(getParameters());
			return checker.check(aSigner, aCheckerResult);
		}
		
		EAttribute	scAttr = scAttrs.get(0);
		
		SigningCertificate sc = null;
		try
		{
			sc = new SigningCertificate();
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(scAttr.getValue(0));
			sc.decode(decBuf);
		}
		catch(Exception tEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_CERTIFICATE_DECODE_ERROR),tEx));
			return false;
		}
			
		//The first certificate identified in the sequence of certificate identifiers MUST be the certificate used to verify the signature. (rfc2634)
		ESSCertID certID = sc.certs.elements[0];
		
		//If present, the issuerAndSerialNumber in SignerIdentifier field of the SignerInfo shall match the
		//issuerSerial field present in ESSCertID. (ETSI TS 101 733 V1.7.4)
		if(aSigner.getSignerInfo().getObject().sid.getElement() instanceof IssuerAndSerialNumber)
		{
            IssuerAndSerialNumber issuerserial = (IssuerAndSerialNumber) aSigner.getSignerInfo().getObject().sid.getElement();
            IssuerSerial issuerserial2 = certID.issuerSerial;

            if (issuerserial!=null && issuerserial2!=null){
                boolean sonuc = _checkIssuerAndSerial(issuerserial, issuerserial2);
                if(!sonuc)
                {
                    aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ISSUER_SERIAL_DOESNOT_MATCH_SIGNER_IDENTIFIER)));
                    return false;
                }
            }
		}
		
		//The certHash from ESSCertID shall match the SHA-1 hash of the certificate.(ETSI TS 101 733 V1.7.4)
		try
		{
			if(!_checkDigest(signerCert, certID))
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CERT_HASH_DOESNOT_MATCH)));
				return false;
			}
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_CERTIFICATE_ATTRIBUTE_HASH_CALCULATION_ERROR),aEx));
			return false;
		}
		
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_CERTIFICATE_ATTRIBUTE_CHECK_SUCCESSFUL)));
		aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
		return true;
	}
	
	private boolean _checkIssuerAndSerial(IssuerAndSerialNumber aSertifika, IssuerSerial aAttribute)
    {
         Name attr_issuer = (Name) aAttribute.issuer.elements[0].getElement();
         Name cert_issuer = aSertifika.issuer;
         if(!UtilEsitlikler.esitMi(attr_issuer, cert_issuer))
         {
        	 return false;
         }
         int attr_serial = aAttribute.serialNumber.value.intValue();
         int cert_serial = aSertifika.serialNumber.value.intValue();
         if(attr_serial != cert_serial)
         {
              return false;
         }
         return true;
    }

	
	private boolean _checkDigest(ECertificate aSertifika,ESSCertID aCertId) throws ESYAException
	{
		try {
			byte[] attrHash = aCertId.certHash.value;
		return _checkDigest(attrHash, new ByteArrayInputStream(aSertifika.getEncoded()), DigestAlg.SHA1);
		}  catch (Exception e){
			throw new ESYAException(e);
		}

	}
}
