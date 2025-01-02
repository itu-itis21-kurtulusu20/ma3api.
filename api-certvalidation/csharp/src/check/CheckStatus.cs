using System;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check
{
    /**
     * Her {@link tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker} kontrol sonucunun ayrıntısını belli bir
     * yapıda tutmak için tanımlanmış interface. Her {@link dogrulama.inspect.Checker} bunu implement
     * eden bir enum tanımlayarak {@link tr.gov.tubitak.uekae.esya.api.certificate.validation.StatusInfo}nin
     * {@link tr.gov.tubitak.uekae.esya.api.certificate.validation.StatusInfo#addDetail} metodu ile
     * kendisine ait olan ayrıntıyı set etmelidir.
     * Böylece doğrulama sonucunda alınan {@link tr.gov.tubitak.uekae.esya.api.certificate.validation.StatusInfo} içinden bu {@link tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker} ye
     * ait kontrol sonucuna ulaşılabilir.
     * @author IH
     */
    public interface CheckStatus
    {
        String getText();
        //int getValue();
    }
}
