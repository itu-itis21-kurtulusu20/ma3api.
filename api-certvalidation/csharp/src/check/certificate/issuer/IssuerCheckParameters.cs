using Com.Objsys.Asn1.Runtime;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * Container class for issuer checker parameters. 
     * @author IH
     */
    public class IssuerCheckParameters
    {
        private long mCertificateOrder = 0;
        private Asn1OpenType mPreviousDSAParams;

        /**
         * Seritifika Zincirindeki derinlik özelliğini döner
         */
        public long getCertificateOrder()
        {
            return mCertificateOrder;
        }

        /**
         * Seritifika Zincirindeki derinlik özelliğini belirler
         */
        public void setCertificateOrder(long aCertificateOrder)
        {
            mCertificateOrder = aCertificateOrder;
        }

        /**
         * Seritifika Zincirindeki derinlik özelliğini bir arttırır
         */
        public void increaseCertificateOrder()
        {
            mCertificateOrder++;
        }

        /**
         * Seritifika Zincirindeki derinlik özelliğini bir azaltır
         */
        public void decreaseCertificateOrder()
        {
            mCertificateOrder--;
        }

        /**
         * Önceki DSA Paramatre özelliğini belirler
         */
        public void setPreviousDSAParams(Asn1OpenType aDSAParams)
        {
            mPreviousDSAParams = aDSAParams;
        }

        /**
         * Önceki DSA Paramatre özelliğini döner
         */
        public Asn1OpenType getPreviousDSAParams()
        {
            return mPreviousDSAParams;
        }
    }
}
