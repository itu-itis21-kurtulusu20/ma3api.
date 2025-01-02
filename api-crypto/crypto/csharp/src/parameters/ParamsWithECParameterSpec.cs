//using Org.BouncyCastle.Crypto.Parameters;
using System;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    //todo ortak kripto modulu bouncy'e bagimli olmasin diye ECDomainParameters'i cikardim 26.01.2011
    public class ParamsWithECParameterSpec:IAlgorithmParams
    {
        private readonly /*ECDomainParameters*/ECParameters _mDomainParams;

        public ParamsWithECParameterSpec(/*ECDomainParameters*/ECParameters aDomainParams)
        {
            this._mDomainParams = aDomainParams;
        }

        public /*ECDomainParameters*/ECParameters getECDomainParams()
        {
            return _mDomainParams;
        }

        public byte[] getEncoded()
        {
            // todo move form smartcard ECParameters
            throw new NotImplementedException();
        }
    }
}
