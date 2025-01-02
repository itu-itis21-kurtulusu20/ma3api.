package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;

public enum RevokeCheckStatus implements CheckStatus
{
    CANT_CHECK,
    REVOKED,
    NOT_REVOKED;

    public String getText()
    {
        return null;  //Todo
    }
}
