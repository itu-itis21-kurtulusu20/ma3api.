using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.pfxsigner
{
    public class EPfxSignerManager : ISignerManager
    {

        Dictionary<int,EPfxSigner> pfxSignerMap = new Dictionary<int, EPfxSigner>(); 
        private static EPfxSignerManager instance = null;

        public static string getPfxFileDirPath()
        {
            return "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\";
        }

        private string pfxFileDirPath;
        public static EPfxSignerManager getInstance(string pfxFileDirPath)
        {
            if (instance == null)
            {
                if (pfxFileDirPath == null)
                    pfxFileDirPath = getPfxFileDirPath();
                instance = new EPfxSignerManager(pfxFileDirPath);
            }
            return instance;
        }

        private EPfxSignerManager(string pfxFileDirPath)
        {
            this.pfxFileDirPath = pfxFileDirPath;
        }

        private EPfxSigner constructPfxSigner(int slot)
        {
            EPfxSigner retPfxSigner = null;
            switch (slot)
            {
                case 0:
                    retPfxSigner = new EPfxSigner(UnitTestParameters.SIGNATURE_ALGORITHM, pfxFileDirPath + UnitTestParameters.PFX_FILE, UnitTestParameters.PFX_PASSWORD);
                    break;
                case 1:
                    retPfxSigner = new EPfxSigner(UnitTestParameters.SIGNATURE_ALGORITHM, pfxFileDirPath + UnitTestParameters.PFX_2_FILE, UnitTestParameters.PFX_2_PASSWORD);
                    break;
                case 2:
                    retPfxSigner = new EPfxSigner(UnitTestParameters.SIGNATURE_ALGORITHM, pfxFileDirPath + UnitTestParameters.PFX_3_FILE, UnitTestParameters.PFX_3_PASSWORD);
                    break;
                case 3:
                    retPfxSigner = new EPfxSigner(UnitTestParameters.SIGNATURE_ALGORITHM, pfxFileDirPath + UnitTestParameters.PFX_REVOKED_FILE, UnitTestParameters.PFX_REVOKED_PASSWORD);
                    break;
            }
            return retPfxSigner;
        }

        public BaseSigner getSigner(int slot)
        {
            if (!pfxSignerMap.ContainsKey(slot))
            {
                EPfxSigner ePfxSigner = constructPfxSigner(slot);
                pfxSignerMap.Add(slot,ePfxSigner);
            }
            EPfxSigner pfxSigner = pfxSignerMap[slot];
            return pfxSigner;
        }


        public BaseSigner getSigner(int signSlotNo, int certSlotNo)
        {
            ECertificate signatureCertificate = getSigningCertificate(certSlotNo);
            EPfxSigner signPfxSigner = (EPfxSigner) getSigner(signSlotNo);
            signPfxSigner.setSignRequestCertificate(signatureCertificate);
            return signPfxSigner;
        }

        public ECertificate getSigningCertificate(int slot)
        {
            EPfxSigner ePfxSigner = (EPfxSigner) getSigner(slot);
            return ePfxSigner.getSigningCertificate();
        }

        public void logout(int slot)
        {
            
        }
    }
}
