package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevokeCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for the Checkers that checks the certificate revocation status
 * 
 * @author IH
 */
public abstract class RevocationChecker extends Checker implements Cloneable{

    protected static final String DEVAM_EDILSIN = "devam";

    protected List<Finder> mFinders = new ArrayList<Finder>();

    /**
     * Sertifikanin iptal kontrollerini yapar
     */
    public RevokeCheckStatus check(ECertificate aIssuerCertificate,
                                   CertificateStatusInfo aCertStatusInfo)
            throws ESYAException
    {
        return _check(aIssuerCertificate, aCertStatusInfo);
    }

    /**
     * DevamEdilsin alanini doner
     */
    public boolean isContinue()
    {
        return mCheckParams.getParameterBoolean(DEVAM_EDILSIN);
    }

    public List<? extends Finder> getFinders(){
        return mFinders;
    }

    public void setFinders(List<Finder> aFinders ){
        mFinders = aFinders;
    }
    
    public <T extends Finder> void addFinder(T aFinder){
        mFinders.add(aFinder);
    }

    protected abstract RevokeCheckStatus _check(ECertificate aCertificate, CertificateStatusInfo aCertStatusInfo)
            throws ESYAException;

    //@Override
    public Object clone() throws CloneNotSupportedException {

        RevocationChecker rc = (RevocationChecker)super.clone();
        rc.setFinders(new ArrayList<Finder>(mFinders));
        return rc;
    }
}
