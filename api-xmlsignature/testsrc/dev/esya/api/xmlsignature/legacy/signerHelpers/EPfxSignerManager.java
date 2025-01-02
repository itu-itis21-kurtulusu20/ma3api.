package dev.esya.api.xmlsignature.legacy.signerHelpers;

import dev.esya.api.xmlsignature.legacy.UnitTestParameters;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.signature.util.PfxSigner;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 13:31
 * To change this template use File | Settings | File Templates.
 */
public class EPfxSignerManager implements ISignerManager
{
    Map<Integer, PfxSigner> pfxSignerMap = new HashMap<Integer, PfxSigner>();
    private static EPfxSignerManager instance = null;
    private static String pfxFileDirPath;

    public static String getPfxFileDirPath()
    {
        URL root = EPfxSignerManager.class.getResource("../../../../../../../../../../");
        String classPath = root.getPath();
        File binDir = new File(classPath);
        String rootDir = binDir.getParentFile().getParent();
        String pfxDirPath = rootDir+"/docs/certstore/";
        return pfxDirPath;
    }

    private EPfxSignerManager(String aPfxFileDirPath)
    {
        pfxFileDirPath = aPfxFileDirPath;
    }

    public static EPfxSignerManager getInstance(String aPfxFileDirPath)
    {
        if(instance == null)
        {
            if(aPfxFileDirPath == null)
            {
                pfxFileDirPath = getPfxFileDirPath();
            }
            instance = new EPfxSignerManager(pfxFileDirPath);
        }
        return instance;
    }

    private PfxSigner constructPfxSigner(int slot)
    {
        PfxSigner retPfxSigner = null;
        switch (slot)
        {
            case 0: retPfxSigner = new PfxSigner(UnitTestParameters.SIGNATURE_ALG, pfxFileDirPath + UnitTestParameters.PFX_FILE, UnitTestParameters.PFX_PASSWORD.toCharArray()); break;
            case 1: retPfxSigner = new PfxSigner(UnitTestParameters.SIGNATURE_ALG, pfxFileDirPath + UnitTestParameters.PFX_2_FILE, UnitTestParameters.PFX_2_PASSWORD.toCharArray()); break;
            case 2: retPfxSigner = new PfxSigner(UnitTestParameters.SIGNATURE_ALG, pfxFileDirPath + UnitTestParameters.PFX_3_FILE, UnitTestParameters.PFX_3_PASSWORD.toCharArray()); break;
            case 3: retPfxSigner = new PfxSigner(UnitTestParameters.SIGNATURE_ALG, pfxFileDirPath + UnitTestParameters.PFX_REVOKED_FILE, UnitTestParameters.PFX_REVOKED_PASSWORD.toCharArray()); break;
            default: retPfxSigner = null; break;
        }
        return retPfxSigner;
    }

    public BaseSigner getSigner(int slot)
    {
        if (!pfxSignerMap.containsKey(slot))
        {
            PfxSigner ePfxSigner = constructPfxSigner(slot);
            pfxSignerMap.put(slot,ePfxSigner);
        }
        PfxSigner pfxSigner = pfxSignerMap.get(slot);
        return pfxSigner;
    }

    public BaseSigner getSigner(int signSlotNo, int certSlotNo)
    {
        //ECertificate signatureCertificate = getSigningCertificate(certSlotNo);
        PfxSigner signPfxSigner = (PfxSigner) getSigner(signSlotNo);
        //signPfxSigner.setSignRequestCertificate(signatureCertificate);
        return signPfxSigner;
    }

    public ECertificate getSigningCertificate(int slot)
    {
        PfxSigner ePfxSigner = (PfxSigner) getSigner(slot);
        return ePfxSigner.getSignersCertificate();
    }

    public void logout(int slot)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
