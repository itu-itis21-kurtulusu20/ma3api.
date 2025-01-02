package tr.gov.tubitak.uekae.esya.api.certificate.validation.check;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;

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
 * </ul>
 *
 * Checkers may require some parameters to use during check operations. These
 * parameters are stored in ParametreListesi structure and initialized by
 * ValidationPolicy 
 *
 * <p>
 * <p>Ve bu işlemler için 3 abstract class tanımlanmıştır:<p>
 * <p>
 * <p>Sil kontrolü 2 temel adımdan oluşmaktadır:<p>
 * <ul>
 * <li>tek sil kontrolü</li>
 * <li>sili imzalayan sm sertifikası ile ilgili kontrol</li>
 * </ul>
 * <p>
 * <p>Ve bu işlemler için 2 abstract class tanımlanmıştır:<p>
 * <p>
 * <p>OCSP cevabı kontrolü tek adım olarak tanımlanmış.
 * @author IH
 */
public abstract class Checker implements Serializable
{
    private static Logger logger = LoggerFactory.getLogger(Checker.class);

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

    private boolean mCritical = true;

    public Checker()
    {
    	try
    	{
    		LV.getInstance().checkLD(Urunler.SERTIFIKADOGRULAMA);
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
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
    public void setCritical(boolean aCritical)
    {
        mCritical = aCritical;
    }

    /**
     * KritikKontrol alanını döner
     */
    public boolean isCritical()
    {
        return mCritical;
    }

    /**
     * Kontrol içeriğini yazı olarak
     */
    public String getCheckText()
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

