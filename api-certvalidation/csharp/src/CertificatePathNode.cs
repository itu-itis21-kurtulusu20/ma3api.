using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    public class CertificatePathNode
    {
        private CertificatePathNode mNext;
        private CertificatePathNode mPrevious;

        private ECertificate mSubject;
        private ECertificate mCurrentIssuer;

        private CertificateFinderIteration mIssuerIteration;

        public CertificatePathNode(ECertificate aSubject, ValidationSystem aValidationSystem)
            : this(aSubject, null, null, aValidationSystem)
        {            
        }

        public CertificatePathNode(ECertificate aSubject, CertificatePathNode aPrevious, CertificatePathNode aNext, ValidationSystem aValidationSystem)
        {
            mSubject = aSubject;
            mPrevious = aPrevious;
            mNext = aNext;
            mIssuerIteration = new CertificateFinderIteration(mSubject, aValidationSystem);
        }

        public CertificatePathNode previous()
        {
            return mPrevious;
        }

        public CertificatePathNode next()
        {
            return mNext;
        }

        public ECertificate getSubject()
        {
            return mSubject;
        }

        public CertificateFinderIteration getIssuerIteration()
        {
            return mIssuerIteration;
        }

        public void setPrevious(CertificatePathNode iPrevious)
        {
            mPrevious = iPrevious;
        }

        public void setNext(CertificatePathNode iNext)
        {
            mNext = iNext;
        }

        public void setSubject(ECertificate iSubject)
        {
            mSubject = iSubject;
        }

        public void setIssuerIteration(CertificateFinderIteration iCFI)
        {
            mIssuerIteration = iCFI;
        }

        public bool iterateIssuer(ValidationSystem aValidationSystem, List<ECertificate> aCurrentCertList)
        {
            while (mIssuerIteration.nextIteration(aValidationSystem))
            {
                mCurrentIssuer = mIssuerIteration.getCurrentItem();
                if (!aCurrentCertList.Contains(mCurrentIssuer))

                    return true;
            }
            return false;

        }

        public ECertificate getIssuer()
        {
            return mCurrentIssuer;
        }

        /*
         public String toString()
         {
             String str = "";

             str += "================================================================\n";
             str += "SUBJECT: " + mSubject.getSubject().stringValue();
             str += "\tCURRENT ADAY ISSUER\n\t-------------------\n";
             int i = 1;
             str += "\t\t"+(i++)+". "+getIssuer().getSubject().stringValue()+"\n";
             str += "================================================================\n";

             return str;
         }    */

    }
}
