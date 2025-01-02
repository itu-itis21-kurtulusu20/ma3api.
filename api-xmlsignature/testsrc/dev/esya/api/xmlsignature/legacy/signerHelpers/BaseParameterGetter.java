package dev.esya.api.xmlsignature.legacy.signerHelpers;

import dev.esya.api.xmlsignature.legacy.helper.XmlSignatureTestHelper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.OfflineResolver;
import dev.esya.api.xmlsignature.legacy.UnitTestParameters;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 22.11.2012
 * Time: 08:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseParameterGetter {

    private static OfflineResolver policyResolver = new OfflineResolver();
    private Context context;

    public BaseParameterGetter()
    {
        init();
    }

    public void init()
    {
        //loadLicense();
        setPolicy();
    }

    public abstract void loadLicense();

    private void setPolicy()
    {
        String policy_file_path_2 = getRootDir() + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME2;
        String policy_file_path_3 = getRootDir() + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME3;
        String policy_file_path_4 = getRootDir() + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME4;
        policyResolver.register("2.16.792.1.61.0.1.5070.3.1.1", policy_file_path_2, "text/xml");
        policyResolver.register("2.16.792.1.61.0.1.5070.3.2.1", policy_file_path_3, "text/xml");
        policyResolver.register("2.16.792.1.61.0.1.5070.3.3.1", policy_file_path_4, "text/xml");
    }

    public ISignerManager getSignerManager()    //pfx or smart card
    {
        return SignerManagerFactory.getSignerMAnager();
    }

    public Context getContext()
    {
        try
        {
            if(context == null)
            {
                context = new Context(getBaseDir());
            }
            return context;
        }
        catch(XMLSignatureException e)
        {
            return null;
        }
    }

    public BaseSigner _getSigner(int signSlotNo, int certSlotNo)
    {
        return getSignerManager().getSigner(signSlotNo,certSlotNo);
    }

    public ECertificate _getCertificateForSlot(int slotNo)
    {
        return getSignerManager().getSigningCertificate(slotNo);
    }

    private void _signWithSigner(XMLSignature signature, int signSlotNo, int certSlotNo) throws XMLSignatureException
    {
        BaseSigner signer = _getSigner(signSlotNo, certSlotNo);
        signature.sign(signer);
        getSignerManager().logout(signSlotNo);
    }

    public void signWithBaseSigner(XMLSignature signature) throws XMLSignatureException    //yavuz.kahveci
    {
        _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO0, UnitTestParameters.SMARTCARD_SLOTNO0);
    }

    public void signWithBaseSigner2(XMLSignature signature) throws XMLSignatureException    //yavuz.kahveci
    {
        _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO1, UnitTestParameters.SMARTCARD_SLOTNO1);
    }

    public void signWithBaseSigner3(XMLSignature signature) throws XMLSignatureException    //yavuz.kahveci
    {
        _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO2, UnitTestParameters.SMARTCARD_SLOTNO2);
    }

    public void signWithBaseSignerRevoked(XMLSignature signature) throws XMLSignatureException    //yavuz.kahveci
    {
        _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO_REVOKED, UnitTestParameters.SMARTCARD_SLOTNO_REVOKED);
    }

    public void signWithifferentBaseSigner(XMLSignature signature) throws XMLSignatureException    //yavuz.kahveci
    {
        _signWithSigner(signature, UnitTestParameters.SMARTCARD_SLOTNO1, UnitTestParameters.SMARTCARD_SLOTNO0);
    }

    public ECertificate getCertificate()
    {
        return _getCertificateForSlot(UnitTestParameters.SMARTCARD_SLOTNO0);
    }

    public ECertificate getCertificate2()
    {
        return _getCertificateForSlot(UnitTestParameters.SMARTCARD_SLOTNO1);
    }

    public ECertificate getCertificate3()
    {
        return _getCertificateForSlot(UnitTestParameters.SMARTCARD_SLOTNO2);
    }

    public ECertificate getCertificateRevoked()
    {
        return _getCertificateForSlot(UnitTestParameters.SMARTCARD_SLOTNO_REVOKED);
    }

    public IResolver getPolicyResolver()
    {
        return policyResolver;
    }

    public String getRootDir()
    {
        return XmlSignatureTestHelper.getInstance().getRootDir();
    }

    public String getBaseDir()
    {
        return XmlSignatureTestHelper.getInstance().getBaseDir();
    }

}
