using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public static class SignatureParser
    {    
        public static ESignatureType parse(ESignerInfo aSI)
        {
            //Counter signers have one less mandatory attribute(AttributeOIDs.id_contentType).
            //To cover counter signatures, it is accepted as counter signer.
            return parse(aSI, true);
        }

        public static ESignatureType parse(ESignerInfo aSI, bool aIsCounterSigner)
        {
            List<Asn1ObjectIdentifier> signedAttrOIDs = new List<Asn1ObjectIdentifier>();
            int saCount = aSI.getSignedAttributes().getAttributeCount();
            for (int i = 0; i < saCount; i++)
            {
                signedAttrOIDs.Add(aSI.getSignedAttributes().getAttribute(i).getType());
            }

            checkSigningCertAttribute(signedAttrOIDs);

            try
            {
                List<Asn1ObjectIdentifier> unsignedAttrOIDs = new List<Asn1ObjectIdentifier>();

                EUnsignedAttributes unsignedAttributes = aSI.getUnsignedAttributes();

                int usaCount = (unsignedAttributes == null) ? 0 : unsignedAttributes.getAttributeCount();//aSI.getUnsignedAttributes().getAttributeCount();
                for (int i = 0; i < usaCount; i++)
                {
                    unsignedAttrOIDs.Add(aSI.getUnsignedAttributes().getAttribute(i).getType());
                }

                ESignatureType matching = null;
                foreach (ESignatureType imzaTip in ESignatureType.values())
                {
                    if (_isTypeCorrect(imzaTip, aIsCounterSigner, signedAttrOIDs, unsignedAttrOIDs))
                        matching = imzaTip;
                }
                //todo bu kısım kontrol edilecek!
                if (matching == null)
                {
                    String signedErrMessage = "Signed Attrs" + Environment.NewLine;
                    foreach (Asn1ObjectIdentifier signedAttr in signedAttrOIDs)
                    {
                        signedErrMessage += signedAttr.ToString();
                        signedErrMessage += Environment.NewLine;
                    }

                    String unsignedErrMessage = "Unsigned Attrs";
                    foreach (Asn1ObjectIdentifier unsignedAttr in unsignedAttrOIDs)
                    {
                        unsignedErrMessage += unsignedAttr.ToString();
                        unsignedErrMessage += Environment.NewLine;
                    }

                    throw new CMSSignatureException("Signature type can not be detected" + Environment.NewLine + signedErrMessage + unsignedErrMessage);
                }

                return matching;
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error while finding signature type", aEx);
            }
        }

        //Amerika menşeli kütüphanelerde signingCertAttribute bulunmuyor. Daha anlaşılır bir hata vermek için bu fonksiyon eklendi.
        private static void checkSigningCertAttribute(List<Asn1ObjectIdentifier> signedAttrOIDs)
        {
            bool isSigningCertificateAttrExist = signedAttrOIDs.Contains(AttributeOIDs.id_aa_signingCertificate)
				|| signedAttrOIDs.Contains(AttributeOIDs.id_aa_signingCertificateV2);
		    if(isSigningCertificateAttrExist == false) {
			    throw new CMSSignatureException("The mandatory 'signingCertificate' or 'signingCertificateV2' attribute is missing.");
            }
        }

        private static bool _isTypeCorrect(ESignatureType aImzaTip, bool aIsCounterSigner, List<Asn1ObjectIdentifier> aSignedAttOIDs, List<Asn1ObjectIdentifier> aUnSignedAttOIDs)
        {
            Asn1ObjectIdentifier signCertOID = AttributeOIDs.id_aa_signingCertificate;
            Asn1ObjectIdentifier signCertV2OID = AttributeOIDs.id_aa_signingCertificateV2;

            Asn1ObjectIdentifier archiveTS = AttributeOIDs.id_aa_ets_archiveTimestamp;
            Asn1ObjectIdentifier archiveTSv2 = AttributeOIDs.id_aa_ets_archiveTimestampV2;
            Asn1ObjectIdentifier archiveTSv3 = AttributeOIDs.id_aa_ets_archiveTimestampV3;

            bool unsigned = true;
            bool signed = true;
            List<Asn1ObjectIdentifier> mandatorySignedAttrs = new List<Asn1ObjectIdentifier>(aImzaTip.getMandatorySignedAttributes(aIsCounterSigner));
            foreach (Asn1ObjectIdentifier oid in mandatorySignedAttrs)
            {
                if (!aSignedAttOIDs.Contains(oid))
                {
                    signed = false;
                    break;
                }
            }
            signed = signed && (aSignedAttOIDs.Contains(signCertOID) || aSignedAttOIDs.Contains(signCertV2OID));
            //bool signed = aSignedAttOIDs.containsAll(new List<Asn1ObjectIdentifier>(aImzaTip.getMandatorySignedAttributes(aIsCounterSigner))) && (aSignedAttOIDs.Contains(signCertOID) || aSignedAttOIDs.Contains(signCertV2OID));
            if (aUnSignedAttOIDs.Count == 0 && aImzaTip.getMandatoryUnSignedAttributes().Length != 0)
                return false;

            if (aImzaTip == ESignatureType.TYPE_ESA)
                unsigned = (aUnSignedAttOIDs.Contains(archiveTS)
                    || aUnSignedAttOIDs.Contains(archiveTSv2) || aUnSignedAttOIDs.Contains(archiveTSv3));

            if (aUnSignedAttOIDs.Count != 0 && aImzaTip != ESignatureType.TYPE_ESA)
            {
                List<Asn1ObjectIdentifier> mandatoryUnsignedAttrs = new List<Asn1ObjectIdentifier>(aImzaTip.getMandatoryUnSignedAttributes());
                foreach (Asn1ObjectIdentifier oid in mandatoryUnsignedAttrs)
                {
                    if (!aUnSignedAttOIDs.Contains(oid))
                    {
                        unsigned = false;
                        break;
                    }
                }
            }

            return signed && unsigned;
        }

    }
}
