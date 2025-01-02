package tr.gov.tubitak.uekae.esya.api.certificate.validation.exception;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Created by orcun.ertugrul on 26-Jul-17.
 *
 */

/**
 * CertValidationException sınıfı aynı zamanda cms-signature projesi altında bulunmakta. cms-signature
 * projesi altındaki sınıfı cert-val projesine taşımak mümkün olmadığı için burada yeni bir sınıf yaratıldı.
 * */
public class CertValidationException extends ESYAException
{
    protected CertificateStatusInfo mCertificateStatusInfo;

    public CertValidationException(CertificateStatusInfo aCertificateStatusInfo)
    {
        super(aCertificateStatusInfo.getDetailedMessage());
        mCertificateStatusInfo = aCertificateStatusInfo;
    }

    public CertificateStatusInfo getCertificateStatusInfo()
    {
        return mCertificateStatusInfo;
    }

}
