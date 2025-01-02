using System;
using System.Collections.Generic;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks if the information in signing certificate reference attribute is true
 * for the certificate used in signature validation
 * @author aslihan.kubilay
 *
 */
    public class SigningCertificateAttrChecker : BaseChecker
    {
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
	    {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.SIGNING_CERTIFICATE_ATTRIBUTE_CHECKER), typeof(SigningCertificateAttrChecker));
        ECertificate signerCert = (ECertificate)getParameters()[AllEParameters.P_SIGNING_CERTIFICATE];

        List<EAttribute> scAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_aa_signingCertificate);
		
		if(scAttrs.Count == 0)
		{
			Checker checker = new SigningCertificateV2AttrChecker();
			checker.setParameters(getParameters());
            return checker.check(aSigner, aCheckerResult);
		}
		
		EAttribute	scAttr = scAttrs[0];
		
		SigningCertificate sc = null;
		try
		{
			sc = new SigningCertificate();
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(scAttr.getValue(0));
			sc.Decode(decBuf);
		}
		catch(Exception tEx)
		{
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNING_CERTIFICATE_DECODE_ERROR), tEx));
			return false;
		}
			
		//The first certificate identified in the sequence of certificate identifiers MUST be the certificate used to verify the signature. (rfc2634)
		ESSCertID certID = sc.certs.elements[0];
		
		//If present, the issuerAndSerialNumber in SignerIdentifier field of the SignerInfo shall match the
		//issuerSerial field present in ESSCertID. (ETSI TS 101 733 V1.7.4)
        if (aSigner.getSignerInfo().getObject().sid.GetElement() is IssuerAndSerialNumber)
		{
            IssuerAndSerialNumber issuerserial = (IssuerAndSerialNumber)aSigner.getSignerInfo().getObject().sid.GetElement();
            IssuerSerial issuerserial2 = certID.issuerSerial;

            if (issuerserial != null && issuerserial2 != null)
            {
                bool sonuc = _checkIssuerAndSerial(issuerserial, issuerserial2);
                if (!sonuc)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ISSUER_SERIAL_DOESNOT_MATCH_SIGNER_IDENTIFIER)));
                    return false;
                }
            }

		}
		
		//The certHash from ESSCertID shall match the SHA-1 hash of the certificate.(ETSI TS 101 733 V1.7.4)
		try
		{
			if(!_checkDigest(signerCert, certID))
			{
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CERT_HASH_DOESNOT_MATCH)));
				return false;
			}
		}
		catch(Exception aEx)
		{
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNING_CERTIFICATE_ATTRIBUTE_HASH_CALCULATION_ERROR), aEx));
			return false;
		}

        aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNING_CERTIFICATE_ATTRIBUTE_CHECK_SUCCESSFUL)));
		aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
		return true;
	}
	
	private bool _checkIssuerAndSerial(IssuerAndSerialNumber aSertifika, IssuerSerial aAttribute)
    {
         Name attr_issuer = (Name) aAttribute.issuer.elements[0].GetElement();
         Name cert_issuer = aSertifika.issuer;
         if(!UtilEsitlikler.esitMi(attr_issuer, cert_issuer))
         {
        	 return false;
         }        
         if (!aAttribute.serialNumber.mValue.Equals(aSertifika.serialNumber.mValue))
         {
             return false;
         }
         return true;
         //int attr_serial = aAttribute.serialNumber.mValue.intValue();
         //int cert_serial = aSertifika.serialNumber.mValue.intValue();
         //if(attr_serial != cert_serial)
         //{
         //     return false;
         //}
         //return true;
    }

	
	private bool _checkDigest(ECertificate aSertifika,ESSCertID aCertId)
	{
		byte[] attrHash = aCertId.certHash.mValue;
        //System.IO.File.WriteAllBytes("C:\\CERT_NET.BIN", aSertifika.getBytes());
        using (MemoryStream memoryStream = new MemoryStream(aSertifika.getBytes()))
        {
            return _checkDigest(attrHash, memoryStream, DigestAlg.SHA1);
        }
	}
    }
}
