using System;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes
{
    public class KeyTransRecipient : RecipientInfo
    {
        private readonly KeyTransRecipientInfo ri = new KeyTransRecipientInfo();

        private IPublicKey mPublicKey;    
       
        public KeyTransRecipient()
            : base()
        {
            SetElement(_KTRI, ri);
        }

        public KeyTransRecipient(KeyTransRecipientInfo aRI)
            : base()
        {
            ri = aRI;
            SetElement(_KTRI, ri);

        }

        public KeyTransRecipient(Certificate aRecipientCertificate, EnvelopeConfig config)
            : base()
        {
            SetElement(_KTRI, ri);
            setRecipientIdentifier(UtilCMS.issuerAndSerialNumberOlustur(aRecipientCertificate));
            setKeyEncryptionAlgorithm(config.getRsaKeyTransAlg());
            setPublicKey(KeyUtil.decodePublicKey(new ESubjectPublicKeyInfo(aRecipientCertificate.tbsCertificate.subjectPublicKeyInfo)));
        }

        public void calculateAndSetEncyptedKey(byte[] symmetricKey)
        {
            Pair<CipherAlg, IAlgorithmParams> cipher = CipherAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(ri.keyEncryptionAlgorithm));
            byte[] encryptedKey = CipherUtil.encrypt(cipher.first(), cipher.second(), symmetricKey, mPublicKey);
          
            encryptedKey = trimTheFirstLeadingZero(encryptedKey);
            setEncryptedKey(encryptedKey);
        }

        public void setEncryptedKey(byte[] aEncryptedKey)
        {
            ri.encryptedKey = new Asn1OctetString(aEncryptedKey);
        }

        public byte[] getEncryptedKey()
        {
            return ri.encryptedKey.mValue;
        }

        public void setKeyEncryptionAlgorithm(CipherAlg keyTransAlg) {
		   
            AlgorithmIdentifier algID = null;

		    if(keyTransAlg.getOID().SequenceEqual(_algorithmsValues.rsaEncryption))
		    {
		    	algID = new AlgorithmIdentifier(keyTransAlg.getOID());
		    }
		    else if(keyTransAlg.getOID().SequenceEqual(_algorithmsValues.id_RSAES_OAEP))
		    {
			    byte[] parameter = ((OAEPPadding)keyTransAlg.getPadding()).toRSAES_OAEP_params();
			    algID = new EAlgorithmIdentifier(keyTransAlg.getOID(), parameter).getObject();
		    }
		    else
		    {
			   throw new Exception("Unknown KeyTrans Algorithm !!!");
		    }

		    ri.keyEncryptionAlgorithm = algID;
	    }

        public byte[] trimTheFirstLeadingZero(byte[] encryptedKey)
        {

            if ((encryptedKey.Length % 8) == 1 && encryptedKey[0] == 0)
            {
                byte[] keyTrimmed = new byte[encryptedKey.Length - 1];
                Array.Copy(encryptedKey, 1, keyTrimmed, 0, encryptedKey.Length - 1);
                return keyTrimmed;
            }
            else
                return encryptedKey;
        }

        public EAlgorithmIdentifier getEncryptionAlgorithmIdentifier()
        {
            return new EAlgorithmIdentifier(getRecipientInfo().keyEncryptionAlgorithm);
        }


        public void setPublicKey(IPublicKey aPublicKey)
        {
            mPublicKey = aPublicKey;
        }  

        public void setRecipientIdentifier(IssuerAndSerialNumber aIssuerAndSerialNumber)
        {
            ri.rid = new RecipientIdentifier();
            ri.rid.Set_issuerAndSerialNumber(aIssuerAndSerialNumber);
            //If the RecipientIdentifier is the CHOICE issuerAndSerialNumber, then the version MUST be 0.
            ri.version = new CMSVersion(0);
        }

        public void setRecipientIdentifier(SubjectKeyIdentifier aSubjectKeyIdentifier)
        {
            ri.rid = new RecipientIdentifier();
            ri.rid.Set_subjectKeyIdentifier(aSubjectKeyIdentifier);
            //If the RecipientIdentifier is subjectKeyIdentifier, then the version MUST be 2.
            ri.version = new CMSVersion(2);
        }

        public EIssuerAndSerialNumber getIssuerAndSerialNumber()
        {
            int choice = ri.rid.ChoiceID;
            if (choice == RecipientIdentifier._ISSUERANDSERIALNUMBER)
            {
                return new EIssuerAndSerialNumber((IssuerAndSerialNumber)ri.rid.GetElement());
            }
            return null;
        }

        public ESubjectKeyIdentifier getSubjectKeyIdentifier()
        {
            int choice = ri.rid.ChoiceID;
            if (choice == RecipientIdentifier._SUBJECTKEYIDENTIFIER)
            {
                return new ESubjectKeyIdentifier((SubjectKeyIdentifier)ri.rid.GetElement());
            }
            return null;
        }

        public KeyTransRecipientInfo getRecipientInfo()
        {
            return ri;
        }

        public IPublicKey getPublicKey()
        {
            return mPublicKey;
        }


        public CMSVersion getCMSVersion()
        {
            return ri.version;
        }
    }
}
