using log4net;
using System.Reflection;
using System;
using tr.gov.tubitak.uekae.esya.api.common.license;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check
{
    /**
     * The base class for checker classes.
     *
     * Checkers performs the control steps of validation. To make it configurable;
     * each control step is defined in its own checker. For example the condition
     * which is specified in RFC 5280 "The serial number is a positive integer." is
     * checked by PositiveSerialNumberChecker class.
     *
     * Checkers are divided into three main category: Certificate Checker CRL
     * Checkers OCSP Response Checkers
     *
     * Certificate checkers include:
     * <ul>
     * <li>{@link tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateSelfChecker}</li>
     * <li>{@link tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerChecker}</li>
     * <li>{@link tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationChecker}</li>
     * </ul>
     * CRL checkers include: CRLSelfChecker CRLIssuerChecker DeltaCRLChecker
     * <ul>
     * <li>{@link tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self.CRLSelfChecker}</li>
     * <li>{@link tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer.CRLIssuerChecker}</li>
     * </ul>
     * OCSP Response checkers include:
     * <ul>
     * <li>{@link tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseChecker}</li>
     * <ul>
     *
     * Checkers may require some parameters to use during check operations. These
     * parameters are stored in ParametreListesi structure and initialized by
     * ValidationPolicy 
     *
     * <p/>
     * <p>Ve bu islemler icin 3 abstract class tanimlanmistir:</p>
     * <p/>
     * <p>Sil kontrolu 2 temel adimdan olusmaktadir:</p>
     * <li>tek sil kontrolu</li>
     * <li>sili imzalayan sm sertifikasi ile ilgili kontrol</li>
     * <p/>
     * <p>Ve bu islemler icin 2 abstract class tanimlanmistir:</p>
     * <p/>
     * <p>OCSP cevabi kontrolu tek adim olarak tanimlanmis ve bu islem icin ,

     * <p/>
     * <p>Kontrolculer zinciri politika bilgisine gore
     * {@link tr.gov.tubitak.uekae.esya.sertifikaislemleri.kontrol.KontrolParam} classinda tanimlanmis olan her 
     * <b>temel</b> kontrolcu ekleme metodlari ile olusturulmaktadir.
     * ( {@link tr.gov.tubitak.uekae.esya.sertifikaislemleri.kontrol.KontrolParam#tekSertifikaKontrolcuEkle},
     * {@link tr.gov.tubitak.uekae.esya.sertifikaislemleri.kontrol.KontrolParam#silSMKontrolcuEkle} )</p>
     * @author IH
     */    
    public abstract class Checker
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /*
        protected  ValidationSystem	mParentSystem;

        //Zincir üzerindeki sonraki kontrolcü
        protected Kontrolcu mSonrakiKontrolcu;
        //Kontrolcu classa dışarıdan politika ile verilebilecek parametreler
        protected ParameterList mCheckParams;
        //Kontrolün kritik olup olmadığı bilgisi
        protected boolean mCritical = true;
        */

        protected ValidationSystem mParentSystem;
        protected ParameterList mCheckParams;

        private bool mCritical = true;

        public Checker()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.SERTIFIKADOGRULAMA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz." + ex.Message);
            }
           
        }

        public void setCheckParameters(ParameterList aKontrolcuParam)
        {
            mCheckParams = aKontrolcuParam;
        }

        /**
         * Parent Sistem alanını belirler
         */
        public void setParentSystem(ValidationSystem aValidationSystem)
        {
            mParentSystem = aValidationSystem;
        }

        /**
         * KritikKontrol alanını belirler
         */
        public void setCritical(bool aCritical)
        {
            mCritical = aCritical;
        }

        /**
         * KritikKontrol alanını döner
         */
        public bool isCritical()
        {
            return mCritical;
        }

        /**
         * Kontrol içeriğini yazı olarak
         */
        public virtual String getCheckText()
        {
            return "Tanımsız Kontrol";
        }

        /**
         * Kontrol sonucunu yazı olarak döner.
         *
        protected abstract String getResultText(CheckStatus aCheckStatus);
        {
            return "Tanımsız Kontrol Sonucu";
        }          */
    }
}
