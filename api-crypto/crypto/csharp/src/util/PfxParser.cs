using System;
using System.Collections.Generic;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.crypto.util
{
    // This class is used for RSA encryption 
    // Crypto.getPfxParser(); can be used for both RSA&EC encryption
    [Obsolete]
    public class PfxParser
    {
        private ECertificate _mCertificate;
        private PrivateKey _mPrivateKey;

        private readonly List<Pair<ECertificate, IPrivateKey>> _keyCertPairs = new List<Pair<ECertificate, IPrivateKey>>();

        public PfxParser(String aPFXFilePath, String aPassword)
        {
            _init(aPFXFilePath, aPassword);
        }

        protected void _init(String aPFXFilePath, String aPassword)
        {
            X509Certificate2 pfx = new X509Certificate2(aPFXFilePath, aPassword, X509KeyStorageFlags.Exportable);

            _mCertificate = new ECertificate(pfx.GetRawCertData());
            RSACryptoServiceProvider rsaPrivKey = pfx.PrivateKey as RSACryptoServiceProvider;

            _mPrivateKey = Crypto.getKeyFactory().convertCSharpPrivateKey(rsaPrivKey);
        }

        public ECertificate getCertificate()
        {
            return _mCertificate;
        }

        public PrivateKey getPrivateKey()
        {
            return _mPrivateKey;
        }

    }
}
