namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl
{
    /**
     * <p>Sil kontrol olasi sonuclarini tanimlamaktadir.</p>
     * @author IH
     *
     */
    public enum CRLStatus
    {        
            NOT_CHECKED,
            VALID,
            ISSUING_CERTIFICATE_INVALID,
            CRL_INVALID        
    }
}
