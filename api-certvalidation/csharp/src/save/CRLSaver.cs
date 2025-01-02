using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.save
{
    /**
     * Sil kaydetme
     * @author IH
     */
    public abstract class CRLSaver:Saver
    {
        public CRLSaver()
            : base()
        {
            
        }

        public void addCRL(ECRL aSil)
        {
            _addCRL(aSil);
        }

        protected abstract void _addCRL(ECRL aSil);
    }
}
