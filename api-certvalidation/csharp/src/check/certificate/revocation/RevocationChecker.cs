using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation
{
    /**
     * Base class for the Checkers that checks the certificate revocation status
     * 
     * @author IH
     */
    public abstract class RevocationChecker : Checker, ICloneable
    {
        //protected bool mContinue = true;
        protected static readonly String DEVAM_EDILSIN = "devam";

        protected List<Finder> mFinders = new List<Finder>();

        /**
         * Sertifikanın iptal kontrollerini yapar
         */
        public RevokeCheckStatus check(ECertificate aIssuerCertificate,
                                       CertificateStatusInfo aCertStatusInfo)
        {
            return _check(aIssuerCertificate, aCertStatusInfo);
        }

        /**
         * DevamEdilsin alanını doner
         */
        public bool isContinue()
        {
            //return mContinue;
            return mCheckParams.getParameterBoolean(DEVAM_EDILSIN);
        }

        public List<T> getFinders<T>() where T : Finder
        {
            return mFinders as List<T>;
        }

        public void setFinders(List<Finder> aFinders)
        {
            mFinders = aFinders;
        }

        public void addFinder<T>(T aFinder) where T : Finder
        {
            mFinders.Add(aFinder);
        }

        protected abstract RevokeCheckStatus _check(ECertificate aCertificate, CertificateStatusInfo aCertStatusInfo);

        public Object Clone()
        {
            RevocationChecker rc = (RevocationChecker) base.MemberwiseClone();

            rc.setFinders(new List<Finder>(mFinders));

            return rc;
        }
    }
}
