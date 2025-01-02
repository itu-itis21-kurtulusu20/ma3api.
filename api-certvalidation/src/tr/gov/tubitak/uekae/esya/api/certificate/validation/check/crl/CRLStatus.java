package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;

/**
 * <p>Sil kontrol muhtemel sonuçlarını tanımlamaktadır.</p>
 * @author IH
 *
 */
public enum CRLStatus implements Cloneable
{
    NOT_CHECKED,
    VALID,
    ISSUING_CERTIFICATE_INVALID,
    CRL_INVALID
}
