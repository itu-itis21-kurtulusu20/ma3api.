using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.X509;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11
{
    public interface IBaseSmartCard
    {
        void openSession(long aSlotID);
        List<byte[]> getSignatureCertificates();
        List<byte[]> getEncryptionCertificates();        
        void login(String aCardPIN);
        void logout();
        void closeSession();
        byte[] getSerial();
        byte[] getSerial(long aSlotID);
        BaseSigner getSigner(ECertificate aCert, String aSigningAlg);
        BaseSigner getSigner(ECertificate aCert, String aSigningAlg, IAlgorithmParameterSpec aParams);
    }
}
