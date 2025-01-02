using System;
using Com.Objsys.Asn1.Runtime;
using smartcard.src.tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.smartcard.src.pkcs11.scheme;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme
{
    public static class SignatureSchemeFactory
    {
        public static ISignatureScheme getSignatureScheme(bool aIsSigning, String aSigningAlg, IAlgorithmParameterSpec aParamSpec, long[] aMechanisms, KeyFinder aKeyFinder)
        {
            if (aSigningAlg.Contains("ISO9796"))
            {
                int keyLenght = aKeyFinder.getKeyLenght() >> 3;
                return new Iso9796_2_SC1(aSigningAlg, keyLenght);
            }
            else if(aSigningAlg.Contains("RSAPSS"))
		    {
                int modBits = aKeyFinder.getKeyLenght();

                RSAPSSParams params_ = null;
                if (aParamSpec == null)
                    params_ = RSAPSSParams.DEFAULT;
			    else if (aParamSpec is RSAPSSParams)
                    params_ = ((RSAPSSParams)aParamSpec);
			    else
				    throw new SmartCardException("Sadece RSAPSSParams tipi desteklenmektedir");


                RSAPSS_SS rsapss = new RSAPSS_SS(params_, modBits, aMechanisms);
                return rsapss;
            }
            else if (aSigningAlg.Contains("RSA"))
            {
                return new Rsa_SS(aSigningAlg, aMechanisms);
            }
            else if (aSigningAlg.Contains("ECDSA"))
            {
                return new ECDSA_SS(aSigningAlg, aMechanisms);
            }
            throw new SmartCardException("Algorithm is not supported.");
        }
    }
}