using System;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
/**
 * Member olarak asn1 wrapper'i olan EPrivateKeyInfo içeren ve IPrivateKey'i implement eder
 * 
 * */

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes
{
    public class PrivateKey : IPrivateKey
    {
        private readonly EPrivateKeyInfo _mPrivateKeyInfo;
        public PrivateKey(byte[] aObjectBytes)
        {
            _mPrivateKeyInfo = new EPrivateKeyInfo(aObjectBytes);
        }
        public PrivateKey(EPrivateKeyInfo aPrivateKeyInfo)
        {
            _mPrivateKeyInfo = aPrivateKeyInfo;
        }
        public String getAlgorithm()
        {           
            return AsymmetricAlg.fromOID(_mPrivateKeyInfo.getAlgorithm().getAlgorithm().mValue).getName();
        }

        public String getFormat()
        {
            return "PKCS#8";
        }
        public byte[] getEncoded()
        {
            return _mPrivateKeyInfo.getBytes();
        }
        
        //public static PrivateKey getPrivateKeyFromPFX(byte[] aPfxContent, String aPassword)
        //{
        //    X509Certificate2 pfx = new X509Certificate2(aPfxContent, aPassword, X509KeyStorageFlags.Exportable);
        //    AsymmetricCipherKeyPair asymmetricKeyPair = DotNetUtilities.GetKeyPair(pfx.PrivateKey);
        //    BouncyPrivateKeyInfo bouncyPrivateKeyInfo = PrivateKeyInfoFactory.CreatePrivateKeyInfo(asymmetricKeyPair.Private);
        //    EPrivateKeyInfo privateKeyInfo = new EPrivateKeyInfo(bouncyPrivateKeyInfo.GetDerEncoded());            
        //    //PrivateKey privateKey = new PrivateKey(BouncyProviderUtil.ToAsn1(PrivateKeyInfoFactory.CreatePrivateKeyInfo(asymmetricKeyPair.Private)));
        //    PrivateKey privateKey = new PrivateKey(privateKeyInfo);
        //    return privateKey;
        //}
    }
}
