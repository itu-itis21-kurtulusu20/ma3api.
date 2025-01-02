package tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyClassInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Specifies which saver classes are used during validation </p>
 * @author IH
 */
public class SavePolicy
{

    private List<PolicyClassInfo> mCertificateSavers = new ArrayList<PolicyClassInfo>();
    private List<PolicyClassInfo> mCRLSavers = new ArrayList<PolicyClassInfo>();
    private List<PolicyClassInfo> mOCSPResponseSavers = new ArrayList<PolicyClassInfo>();

    public SavePolicy()
    {

    }

    public List<PolicyClassInfo> getOCSPResponseSavers()
    {
        return mOCSPResponseSavers;
    }

    public void setOCSPResponseSavers(List<PolicyClassInfo> aOCSPResponseSavers)
    {
        mOCSPResponseSavers = aOCSPResponseSavers;
    }

    public List<PolicyClassInfo> getCRLSavers()
    {
        return mCRLSavers;
    }

    public void setCRLSavers(List<PolicyClassInfo> aCRLSavers)
    {
        mCRLSavers = aCRLSavers;
    }

    public List<PolicyClassInfo> getCertificateSavers()
    {
        return mCertificateSavers;
    }

    public void setCertificateSavers(List<PolicyClassInfo> aCertificateSavers)
    {
        mCertificateSavers = aCertificateSavers;
    }
}
