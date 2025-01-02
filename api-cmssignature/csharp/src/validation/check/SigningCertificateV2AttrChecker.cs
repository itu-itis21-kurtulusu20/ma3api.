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
    public class SigningCertificateV2AttrChecker : BaseChecker
    {
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.SIGNING_CERTIFICATE_V2_ATTRIBUTE_CHECKER), typeof(SigningCertificateV2AttrChecker));
            ECertificate signerCert = aSigner.getSignerCertificate();

            List<EAttribute> scAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_aa_signingCertificateV2);

            if (scAttrs.Count == 0)
            {
                aCheckerResult.addMessage(new ValidationMessage(AttributeOIDs.id_aa_signingCertificateV2 + "OID'li SigningCertificateV2 Attribute'u bulunamadi"));
                return false;
            }

            EAttribute scAttr = scAttrs[0];

            SigningCertificateV2 sc = null;
            try
            {
                sc = new SigningCertificateV2();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(scAttr.getValue(0));
                sc.Decode(decBuf);
            }
            catch (Exception tEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNING_CERTIFICATE_DECODE_ERROR), tEx));
                return false;
            }

            //The first certificate identified in the sequence of certificate identifiers MUST be the certificate used to verify the signature. (rfc2634)
            EESSCertIDv2 certID = new EESSCertIDv2(sc.certs.elements[0]);

            //Zaman damgası issuerserial bulundurmak zorunda değil
            bool isTS = false;
            if (getSignedData().getEncapsulatedContentInfo().getContentType().Equals(AttributeOIDs.id_ct_TSTInfo))
            {
                isTS = true;
            }
            bool sidIsIssuerAndSerial = aSigner.getSignerInfo().getObject().sid.GetElement() is IssuerAndSerialNumber;
		    bool issuerSerialNull=false;
		    if(certID.getObject().issuerSerial==null){
			    issuerSerialNull=true;
		    }
            if (issuerSerialNull)
            {
                if ((!isTS) || (isTS && (!sidIsIssuerAndSerial)))
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ISSUER_SERIAL_DOESNOT_EXISTS)));
                    return false;
                }
            }
            //If present, the issuerAndSerialNumber in SignerIdentifier field of the SignerInfo shall match the
            //issuerSerial field present in ESSCertID. (ETSI TS 101 733 V1.7.4)
            if (sidIsIssuerAndSerial && (!issuerSerialNull))
            {
                IssuerAndSerialNumber issuerserial = (IssuerAndSerialNumber)aSigner.getSignerInfo().getObject().sid.GetElement();
                bool sonuc = _checkIssuerAndSerial(issuerserial, certID.getObject().issuerSerial);
                if (!sonuc)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ISSUER_SERIAL_DOESNOT_MATCH_SIGNER_IDENTIFIER)));
                    return false;
                }
            }

            //the certHash from ESSCertIDv2 shall match the hash of the certificate computed
            //using the hash function specified in the hashAlgorithm field.(ETSI TS 101 733 V1.7.4)
            try
            {
                if (!_checkDigest(signerCert, certID))
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CERT_HASH_DOESNOT_MATCH)));
                    return false;
                }
            }
            catch (Exception aEx)
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
            Name attr_issuer = (Name)aAttribute.issuer.elements[0].GetElement();
            Name cert_issuer = aSertifika.issuer;
            if (!UtilEsitlikler.esitMi(attr_issuer, cert_issuer))
            {
                return false;
            }
            //int attr_serial = aAttribute.serialNumber.mValue.intValue();
            //int cert_serial = aSertifika.serialNumber.mValue.intValue();
            //if(attr_serial != cert_serial)
            //{
            //     return false;
            //}
            //return true;

            if (!aAttribute.serialNumber.mValue.Equals(aSertifika.serialNumber.mValue))
            {
                return false;
            }
            return true;
        }


        private bool _checkDigest(ECertificate aSertifika, EESSCertIDv2 aCertId)
        {
            byte[] attrHash = aCertId.getObject().certHash.mValue;
            using(MemoryStream memoryStream = new MemoryStream(aSertifika.getBytes()))
            {
                return _checkDigest(attrHash, memoryStream, DigestAlg.fromOID(aCertId.getHashAlgorithm().getAlgorithm().mValue));
            }
        }
    }
}
