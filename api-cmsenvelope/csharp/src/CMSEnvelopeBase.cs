using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.asn.cms;
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    public class CMSEnvelopeBase
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected IEnvelopeData mEnvelopeData = null;
        protected EContentInfo mContentInfo = new EContentInfo(new ContentInfo());

        protected CMSEnvelopeBase()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.CMSZARF);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        protected long _getVersion()
        {
            bool originatorInfoPresent = false;
            bool certsWithTypeOtherPresent = false;
            bool crlWithTypeOtherPresent = false;
            bool certsWithTypeAttributePresent = false;
            bool pwriPresent = false;
            bool oriPresent = false;
            bool unProtectedAttributesPresent = false;
            bool allRecipientInfoVersionIsZero = true;

            long version = 0;
            //check recipient infos versions
            for (int i = 0; i < mEnvelopeData.getRecipientInfoCount(); i++)
            {
                RecipientInfo ri = mEnvelopeData.getRecipientInfo(i);
                if (ri is KeyTransRecipient)
                {
                    version = ((KeyTransRecipient)ri).getCMSVersion().mValue;
                }
                else if (ri is KeyAgreeRecipient)
                {
                    version = ((KeyAgreeRecipient)ri).getCMSVersion().mValue;
                }
                else if (ri is KEKRecipient)
                {
                    version = ((KEKRecipient)ri).getCMSVersion().mValue;
                }
                else if (ri is PasswordRecipient)
                {
                    version = ((PasswordRecipient)ri).getCMSVersion().mValue;
                    pwriPresent = true;
                }
                else
                {
                    oriPresent = true;
                }

                if (version != 0)
                {
                    allRecipientInfoVersionIsZero = false;
                    break;
                }
            }

            //check originator info parameters
            if (mEnvelopeData.getOriginatorInfo() != null)
            {
                originatorInfoPresent = true;
                CertificateChoices[] certChoice = mEnvelopeData.getOriginatorInfo().certs.elements;
                for (int i = 0; i < certChoice.Length; i++)
                {
                    if (certChoice[i].ChoiceID != CertificateChoices._CERTIFICATE)
                        certsWithTypeOtherPresent = true;
                    if (certChoice[i].ChoiceID == CertificateChoices._V1ATTRCERT || certChoice[i].ChoiceID == CertificateChoices._V2ATTRCERT)
                        certsWithTypeAttributePresent = true;
                }
                RevocationInfoChoice[] crlChoice = mEnvelopeData.getOriginatorInfo().crls.elements;
                for (int i = 0; i < crlChoice.Length; i++)
                {
                    if (crlChoice[i].ChoiceID != RevocationInfoChoice._CRL)
                        crlWithTypeOtherPresent = true;
                }
            }

            //check unprotectedAttributes
            if (mEnvelopeData.getUnprotectedAttributes() != null)
            {
                unProtectedAttributesPresent = true;
            }

            if (originatorInfoPresent && (certsWithTypeOtherPresent || crlWithTypeOtherPresent))
            {
                version = 4;
            }
            else if ((originatorInfoPresent && certsWithTypeAttributePresent) || pwriPresent || oriPresent)
            {
                version = 3;
            }
            else if ((originatorInfoPresent == false) || (unProtectedAttributesPresent == false) || allRecipientInfoVersionIsZero)
            {
                version = 0;
            }
            else
            {
                version = 2;
            }
            return version;
        }
        protected void checkLicense(ECertificate aCer)
        {
            try
            {
                bool isTest = LV.getInstance().isTestLicense(LV.Products.CMSZARF);
                if (isTest && !aCer.getSubject().getCommonNameAttribute().ToLower().Contains("test"))
                {                   
                   logger.Fatal("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
                   throw new SystemException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
                }

                String issuerName = LV.getInstance().getCertificateIssuer(LV.Products.CMSZARF);
                if (!String.IsNullOrEmpty(issuerName) && !aCer.getIssuer().getCommonNameAttribute().Contains(issuerName))
                {
                    logger.Fatal("Certificate issuer and the issuer name field in license must match");
                    throw new ESYARuntimeException("Certificate issuer and the issuer name field in license must match");
                }
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

    }
}
