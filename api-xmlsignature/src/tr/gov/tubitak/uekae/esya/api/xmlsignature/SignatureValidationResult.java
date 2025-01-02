package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.provider.XMLProviderUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class SignatureValidationResult extends ValidationResult
    implements tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult
{
    private CertificateStatusInfo mStatusInfo;
    private String mcheckMessage;

    public SignatureValidationResult()
    {
        super(XMLSignature.class);
        setCheckMessage(I18n.translate("validation.check.xmlSignature"));
    }

    /*
    public SignatureValidationResult(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType aResultType, String aMessage)
    {
        super(aResultType, aMessage);
    } */

    public SignatureValidationResult(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType aResultType,
                                     String aMessage, String aInfo)
    {
        super(aResultType, I18n.translate("validation.check.xmlSignature"), aMessage, aInfo, XMLSignature.class);
    }

    public void setStatus(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType aType, String aMessage)
    {
        super.setStatus(aType, getCheckMessage(), aMessage, null);
    }

    public void setStatus(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType aType, String aMessage, String aInfo)
    {
        super.setStatus(aType, getCheckMessage(), aMessage, aInfo);
    }

    public void setCertificateStatusInfo(CertificateStatusInfo aMStatusInfo)
    {
        mStatusInfo = aMStatusInfo;
    }

    public CertificateStatusInfo getCertificateStatusInfo()
    {
        return mStatusInfo;
    }

    public List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult> getCounterSignatureValidationResults()
    {
        List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult> results = new ArrayList<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult>();
        for (ValidationResult vr : items){
            if (vr instanceof SignatureValidationResult){
                results.add((tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult)vr);
            }
        }
        return results;
    }
}
