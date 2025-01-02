using System;
using System.Collections.Generic;
using System.Globalization;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignerInfo : BaseASNWrapper<SignerInfo>
    {
        public ESignerInfo(SignerInfo aSignerInfo)
            : base(aSignerInfo)
        {
        }

        public ESignerInfo(byte[] aBytes)
            : base(aBytes, new SignerInfo())
        {
        }

        public int getVersion()
        {
            return (int)mObject.version.mValue;
        }

        public void setVersion(int aVersion)
        {
            mObject.version = new CMSVersion(aVersion);
        }

        public ESignerIdentifier getSignerIdentifier()
        {
            return new ESignerIdentifier(mObject.sid);
        }

        public void setSignerIdentifier(ESignerIdentifier aSignerIdentifier)
        {
            mObject.sid = aSignerIdentifier.getObject();
        }

        public ECertificate getSignerCertificate(List<ECertificate> certs)
        {
            ESignerIdentifier signerIden = getSignerIdentifier();
            foreach (ECertificate certificate in certs)
            {
                if (signerIden.isEqual(certificate))
                    return certificate;
            }
            return null;
        }

        public EAlgorithmIdentifier getDigestAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.digestAlgorithm);
        }



        public void setDigestAlgorithm(EAlgorithmIdentifier aDigestAlgorithm)
        {
            mObject.digestAlgorithm = aDigestAlgorithm.getObject();
        }

        public ESignedAttributes getSignedAttributes()
        {
            return new ESignedAttributes(mObject.signedAttrs);
        }

        public void addSignedAttribute(EAttribute aAttribute)
        {
            if (mObject.signedAttrs == null)
            {
                mObject.signedAttrs = new SignedAttributes();
                mObject.signedAttrs.elements = new Attribute[0];
            }

            mObject.signedAttrs.elements = extendArray(mObject.signedAttrs.elements, aAttribute.getObject());
        }

        public void setSignedAttributes(ESignedAttributes aSignedAttributes)
        {
            mObject.signedAttrs = aSignedAttributes.getObject();
        }

        public EAlgorithmIdentifier getSignatureAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
        }

        public void setSignatureAlgorithm(EAlgorithmIdentifier aSignatureAlgorithm)
        {
            mObject.signatureAlgorithm = aSignatureAlgorithm.getObject();
        }

        public byte[] getSignature()
        {
            return mObject.signature.mValue;
        }

        public void setSignature(byte[] aSignature)
        {
            mObject.signature = new Asn1OctetString(aSignature);
        }

        public EUnsignedAttributes getUnsignedAttributes()
        {
            return (mObject.unsignedAttrs == null) ? null : new EUnsignedAttributes(mObject.unsignedAttrs);
        }

        public void addUnsignedAttribute(EAttribute aAttribute)
        {
            if (mObject.unsignedAttrs == null)
            {
                mObject.unsignedAttrs = new UnsignedAttributes();
                mObject.unsignedAttrs.elements = new Attribute[0];
            }

            mObject.unsignedAttrs.elements = extendArray(mObject.unsignedAttrs.elements, aAttribute.getObject());
        }

        public void setUnsignedAttributes(EUnsignedAttributes aAttributes)
        {
            mObject.unsignedAttrs = aAttributes.getObject();
        }

        public List<EAttribute> getSignedAttribute(Asn1ObjectIdentifier aOID)
        {
            List<EAttribute> list = new List<EAttribute>();
            if (mObject.signedAttrs != null && mObject.signedAttrs.elements != null)
            {
                foreach (Attribute attr in mObject.signedAttrs.elements)
                {
                    if (attr.type.Equals(aOID))
                        list.Add(new EAttribute(attr));
                }
            }

            return list;

        }


        public List<EAttribute> getUnsignedAttribute(Asn1ObjectIdentifier aOID)
        {
            List<EAttribute> list = new List<EAttribute>();
            if (mObject.unsignedAttrs != null && mObject.unsignedAttrs.elements != null)
            {
                foreach (Attribute attr in mObject.unsignedAttrs.elements)
                {
                    if (attr.type.Equals(aOID))
                        list.Add(new EAttribute(attr));
                }
            }

            return list;

        }

        public bool removeUnSignedAttribute(EAttribute aAttribute)
        {
            if (mObject.unsignedAttrs != null && mObject.unsignedAttrs.elements != null)
            {
                try
                {
                    mObject.unsignedAttrs.elements = removeFromArray(mObject.unsignedAttrs.elements, aAttribute.getObject());
                    if (mObject.unsignedAttrs.elements.Length == 0)
                        mObject.unsignedAttrs = null;
                    return true;
                }
                catch (Exception ex)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        public ERevocationValues getRevocationValues()
        {
            List<EAttribute> unsignedAttribute = getUnsignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_revocationValues));
            if (unsignedAttribute.Count <= 0)
                return null;
            return new ERevocationValues(unsignedAttribute[0].getValue(0));
        }
        public ECertificateValues getCertificateValues()
        {
            List<EAttribute> unsignedAttribute = getUnsignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_certValues));
            if (unsignedAttribute.Count <= 0)
                return null;
            return new ECertificateValues(unsignedAttribute[0].getValue(0));
        }
        public ECompleteCertificateReferences getCompleteCertificateReferences()
        {
            List<EAttribute> unsignedAttribute = getUnsignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_certificateRefs));
            if (unsignedAttribute.Count <= 0)
                return null;
            return new ECompleteCertificateReferences(unsignedAttribute[0].getValue(0));
        }

        public ECompleteRevocationReferences getCompleteRevocationReferences()
        {
            List<EAttribute> unsignedAttribute = getUnsignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_revocationRefs));
            if (unsignedAttribute.Count <= 0)
                return null;
            return new ECompleteRevocationReferences(unsignedAttribute[0].getValue(0));
        }


        public TurkishESigProfile getProfile()
        {
            ESignaturePolicy signaturePolicy = getPolicyAttr();
            if (signaturePolicy == null)
                return null;
            ESignaturePolicyId signaturePolicyId = signaturePolicy.getSignaturePolicyId();
            return TurkishESigProfile.getSignatureProfile(signaturePolicyId.getPolicyObjectIdentifier().mValue);
        }

        public ESignaturePolicy getPolicyAttr()
        {
            List<EAttribute> unsignedAttribute = getSignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_sigPolicyId));
            if (unsignedAttribute.Count <= 0)
                return null;
            return new ESignaturePolicy(unsignedAttribute[0].getValue(0));
        }
        public DateTime? getSigningTime()
        {
            List<EAttribute> stAttrs = getSignedAttribute(new Asn1ObjectIdentifier(_cmsValues.id_signingTime));
            if (stAttrs.Count != 0)
            {
                EAttribute stAttr = stAttrs[0];
                Time time = new Time();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(stAttr.getValue(0));
                try
                {
                    time.Decode(decBuf);
                }
                catch (Exception aEx)
                {
                    throw new ESYARuntimeException(aEx);
                }

                return UtilTime.timeToDate(time);
            }
            return null;
        }

    }
}
