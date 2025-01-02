using System;
using System.Collections.Generic;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignedData : BaseASNWrapper<SignedData>
    {
        private static readonly Asn1ObjectIdentifier IdCountersignature = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 6 });

        public ESignedData(SignedData aObject)
            : base(aObject)
        {
        }
        /**
         * Create ESignedData from byte array
          * @param aBytes byte[]
          * @throws ESYAException
          */
        public ESignedData(byte[] aBytes)
            : base(aBytes, new SignedData())
        {
        }
        /**
         * Returns version of signed data
         * @return int
         */
        public int getVersion()
        {
            return (int)mObject.version.mValue;
        }

        public void setVersion(int aVersion)
        {
            mObject.version = new CMSVersion(aVersion);
        }
        /**
         * Returns list of digest algorithms of signed data
         * @return 
         */
        public List<EAlgorithmIdentifier> getDigestAlgorithmList()
        {
            List<EAlgorithmIdentifier> digestAlgs = new List<EAlgorithmIdentifier>();

            if (mObject.digestAlgorithms == null || mObject.digestAlgorithms.elements == null) return digestAlgs;
            foreach (AlgorithmIdentifier algID in mObject.digestAlgorithms.elements)
            {
                digestAlgs.Add(new EAlgorithmIdentifier(algID));
            }

            return digestAlgs;

        }
        /**
         * Returns count of digest algorithms of signed data
         * @return 
         */
        public int getDigestAlgorithmIdentifierCount()
        {
            if (mObject.digestAlgorithms == null || mObject.digestAlgorithms.elements == null)
                return 0;

            return mObject.digestAlgorithms.elements.Length;
        }
        /**
         * Returns  digest algorithm of signed data at specific index
         *  @param aIndex index
         * @return 
         */
        public EAlgorithmIdentifier getDigestAlgorithmIdentifier(int aIndex)
        {
            if (mObject.digestAlgorithms == null || mObject.digestAlgorithms.elements == null)
                return null;

            return new EAlgorithmIdentifier(mObject.digestAlgorithms.elements[aIndex]);
        }
        /**
         * Add  digest algorithm to signed data
         *  @param aDigestAlgorithmIdentifier EAlgorithmIdentifier
         * @return 
         */
        public void addDigestAlgorithmIdentifier(EAlgorithmIdentifier aDigestAlgorithmIdentifier)
        {
            if (mObject.digestAlgorithms == null)
                mObject.digestAlgorithms = new DigestAlgorithmIdentifiers();

            mObject.digestAlgorithms.elements = extendArray(mObject.digestAlgorithms.elements, aDigestAlgorithmIdentifier.getObject());
        }

        /**
         * Returns Encapsulated Content Info of signed data
         * @return 
         */
        public EEncapsulatedContentInfo getEncapsulatedContentInfo()
        {
            return new EEncapsulatedContentInfo(mObject.encapContentInfo);
        }
        /**
         * Set Encapsulated Content Info of signed data
         * @param aEncapsulatedContentInfo EEncapsulatedContentInfo
         */
        public void setEncapsulatedContentInfo(EEncapsulatedContentInfo aEncapsulatedContentInfo)
        {
            mObject.encapContentInfo = aEncapsulatedContentInfo.getObject();
        }
        /**
         * Returns certificateset of signed data
         * @return 
         */
        public ECertificateSet getCertificateSet()
        {
            if (mObject.certificates == null)
                return null;
            return new ECertificateSet(mObject.certificates);
        }
        /**
         * Set certificateset of signed data
         * @param aCertificateSet ECertificateSet
         */
        public void setCertificateSet(ECertificateSet aCertificateSet)
        {
            mObject.certificates = aCertificateSet.getObject();
        }
        /**
         * Add certificate choices to signed data
         * @param aCertificateChoices ECertificateChoices
         */
        public void addCertificateChoices(ECertificateChoices aCertificateChoices)
        {
            if (mObject.certificates == null)
                mObject.certificates = new CertificateSet();

            mObject.certificates.elements = extendArray(mObject.certificates.elements, aCertificateChoices.getObject());
        }
        /**
         * Returns certificates of signed data
         * @return 
         */
        public List<ECertificate> getCertificates()
        {
            if (mObject.certificates == null || mObject.certificates.elements == null)
                return null;

            List<ECertificate> certs = new List<ECertificate>();
            foreach (CertificateChoices cc in mObject.certificates.elements)
            {
                if (cc.ChoiceID == CertificateChoices._CERTIFICATE)
                    certs.Add(new ECertificate((Certificate)cc.GetElement()));
            }
            return certs;
        }
        /**
         * Returns Revocation Info Choices of signed data
         * @return 
         */
        public ERevocationInfoChoices getRevocationInfoChoices()
        {
            if (mObject.crls == null)
                return null;
            return new ERevocationInfoChoices(mObject.crls);
        }
        /**
         * Set revocation info choices of signed data
         * @param aRevocationInfoChoices ERevocationInfoChoices
         */
        public void setRevocationInfoChoices(ERevocationInfoChoices aRevocationInfoChoices)
        {
            mObject.crls = aRevocationInfoChoices.getObject();
        }
        /**
         * Add revocation info choices to signed data
         * @param aRevocationInfoChoices ERevocationInfoChoice
         */
        public void addRevocationInfoChoice(ERevocationInfoChoice aRevocationInfoChoice)
        {
            if (mObject.crls == null)
                mObject.crls = new RevocationInfoChoices();

            mObject.crls.elements = extendArray(mObject.crls.elements, aRevocationInfoChoice.getObject());
        }
        /**
         * Returns CRLs of signed data
         * @return List<ECRL>
         */
        public List<ECRL> getCRLs()
        {
            if (mObject.crls == null || mObject.crls.elements == null)
                return null;

            List<ECRL> crls = new List<ECRL>();
            foreach (RevocationInfoChoice ric in mObject.crls.elements)
            {
                if (ric.ChoiceID == RevocationInfoChoice._CRL)
                    crls.Add(new ECRL((CertificateList)ric.GetElement()));
            }

            return crls;
        }
        /**
	 * Returns OCSP Responses of signed data
	 * @return List<EOCSPResponse>
	 */
        public List<EOCSPResponse> getOSCPResponses()
	{
		if(mObject.crls==null || mObject.crls.elements==null)
			return null;

        ERevocationInfoChoices revs = new ERevocationInfoChoices(mObject.crls);
        return revs.getOSCPResponses();
	}

        /**
         * Returns count of Signer Info in signed data
         * @return int
         */
        public int getSignerInfoCount()
        {
            if (mObject.signerInfos == null || mObject.signerInfos.elements == null)
                return 0;

            return mObject.signerInfos.elements.Length;
        }
        /**
         * Returns a Signer Info in signed data at specific index
         * @return
         */
        public ESignerInfo getSignerInfo(int aIndex)
        {
            if (mObject.signerInfos == null || mObject.signerInfos.elements == null)
                return null;

            return new ESignerInfo(mObject.signerInfos.elements[aIndex]);
        }
        /**
         * Add signer info  to signed data
         * @param aSignerInfo ESignerInfo
         */
        public void addSignerInfo(ESignerInfo aSignerInfo)
        {
            if (mObject.signerInfos == null)
            {
                mObject.signerInfos = new SignerInfos();
                mObject.signerInfos.elements = new SignerInfo[0];
            }

            mObject.signerInfos.elements = extendArray(mObject.signerInfos.elements, aSignerInfo.getObject());
        }
        /**
         * Remove signer info  from signed data
         * @param aSignerInfo ESignerInfo
         */
        public bool removeSignerInfo(ESignerInfo aSignerInfo)
        {
            if (mObject.signerInfos == null)
                return false;

            List<SignerInfo> newSignerInfos = new List<SignerInfo>(mObject.signerInfos.elements);
            bool removed = false;
            foreach (SignerInfo signerInfo in mObject.signerInfos.elements)
            {
                ESignerInfo existingSignerInfo = new ESignerInfo(signerInfo);
                if (!existingSignerInfo.Equals(aSignerInfo)) continue;
                newSignerInfos.Remove(signerInfo);
                removed = true;
                break;
            }

            if (removed == false)
            {
                byte[] reqSignerInfoBytes = aSignerInfo.getBytes();
                foreach (SignerInfo signerInfo in newSignerInfos)
                {
                    removed = RemoveInLoop(signerInfo, reqSignerInfoBytes);
                    if (removed)
                        return true;
                }
                return false;
            }
            mObject.signerInfos.elements = newSignerInfos.ToArray();
            return true;
        }

        private bool RemoveInLoop(SignerInfo signerInfo, byte[] aReqSignerInfo)
        {
            Attribute[] attrs = signerInfo.unsignedAttrs.elements;
            List<Attribute> newAttrs = new List<Attribute>(attrs);
            foreach (Attribute attribute in attrs)
            {
                if (!attribute.type.Equals(IdCountersignature)) continue;
                if (!attribute.values.elements[0].mValue.SequenceEqual(aReqSignerInfo)) continue;
                newAttrs.Remove(attribute);
                if (newAttrs.Count == 0)
                    signerInfo.unsignedAttrs = null;
                else
                    signerInfo.unsignedAttrs.elements = newAttrs.ToArray();
                return true;
            }

            foreach (Attribute attribute in attrs)
            {
                if (!attribute.type.Equals(IdCountersignature)) continue;
                try
                {
                    var si = new ESignerInfo(attribute.values.elements[0].mValue);
                    var removed = RemoveInLoop(si.getObject(), aReqSignerInfo);
                    if (!removed) continue;
                    attribute.values.elements[0].mValue = si.getBytes();
                    return true;
                }
                catch (ESYAException ex)
                {
                    logger.Info("SignerInfo unsignedAttrs silinirken hata oluştu.", ex);
                }
            }
            return false;
        }
        /**
         * Returns all Signer Info in signed data
         * @return
         */
        public List<ESignerInfo> getSignerInfos()
        {
            if (mObject.signerInfos == null || mObject.signerInfos.elements == null)
                return null;

            List<ESignerInfo> sis = new List<ESignerInfo>();
            foreach (SignerInfo si in mObject.signerInfos.elements)
            {
                sis.Add(new ESignerInfo(si));
            }

            return sis;
        }

    }
}
