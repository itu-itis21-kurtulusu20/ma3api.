package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.Registry;
import gnu.crypto.key.IKeyPairGenerator;
import gnu.crypto.key.KeyPairGeneratorFactory;
import gnu.crypto.key.ecdsa.ECDSAKeyPairGenerator;
import gnu.crypto.key.ecdsa.ECDSAKeyPairX509Codec;
import gnu.crypto.key.rsa.RSAKeyPairGenerator;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyPairGenerator;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.UnknownElement;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

import java.security.KeyPair;
import java.security.spec.ECParameterSpec;
import java.util.HashMap;
import java.util.Map;
/**
 * @author ayetgin
 */
public class GNUKeyPairGenerator implements KeyPairGenerator
{
    private AsymmetricAlg mAsymmetricAlg;

    public GNUKeyPairGenerator(AsymmetricAlg aAsymmetricAlg)
    {
        mAsymmetricAlg = aAsymmetricAlg;
    }

  
    
    public KeyPair generateKeyPair(AlgorithmParams aParams) throws CryptoException
    {
    	if(aParams == null)
    		throw new CryptoException("AlgorithParams can not be null");
    	
    	if(aParams instanceof ParamsWithLength)
    	{
    		int length = ((ParamsWithLength)aParams).getLength();
    		return _generateKeyPair(length);
    	}
    	else if (aParams instanceof ParamsWithECParameterSpec)
    	{
    		ECParameterSpec domainParams = ((ParamsWithECParameterSpec)aParams).getECDomainParams();
    		ECDomainParameter gnuDomainParameter = ECDomainParameter.getInstance(domainParams);
    		return _generateKeyPair(gnuDomainParameter);
    	}
    	else if(aParams instanceof ParamsWithAlgorithmIdentifier)
    	{
    		EAlgorithmIdentifier algIden = ((ParamsWithAlgorithmIdentifier)aParams).getAlgorithmIdentifier();
    		ECDSAKeyPairX509Codec codec = new ECDSAKeyPairX509Codec();
    		ECDomainParameter domainParams;
			try 
			{
				domainParams = codec.toDomainParameters(algIden.getObject());
			} 
			catch (Exception ex)
			{
				throw new CryptoException("Algorithm Identifier problem", ex);
			}
    		return _generateKeyPair(domainParams);
    		
    	}
    	
    	throw new CryptoException("This type AlgorithParams does not support");
    }

    private KeyPair _generateKeyPair(ECDomainParameter domainParams) {
    	Map<String, Object> options = new HashMap<String, Object>();
    	
    	IKeyPairGenerator kpg = KeyPairGeneratorFactory.getInstance(Registry.ECDSA_KPG);
    	options.put(ECDSAKeyPairGenerator.DOMAIN_PARAMETERS, domainParams);
    	kpg.setup(options);
    	
    	return kpg.generate();
	}



	private KeyPair _generateKeyPair(int aLength) throws CryptoException
    {
        Map<String, Object> options = new HashMap<String, Object>();
        KeyPair kp;
        IKeyPairGenerator kpg;

        switch (mAsymmetricAlg){

            case RSA        :
                kpg = KeyPairGeneratorFactory.getInstance(Registry.RSA_KPG);
                options.put(RSAKeyPairGenerator.MODULUS_LENGTH, aLength);
                options.put(RSAKeyPairGenerator.SOURCE_OF_RANDOMNESS, null);
                break;

            case DSA         :
               kpg = KeyPairGeneratorFactory.getInstance(Registry.DSA_KPG);
               options.put(RSAKeyPairGenerator.MODULUS_LENGTH, aLength);
               options.put(RSAKeyPairGenerator.SOURCE_OF_RANDOMNESS, null);
               break;

           case ECDSA       :
               kpg = KeyPairGeneratorFactory.getInstance(Registry.ECDSA_KPG);
               int[] paramOid;
               switch (aLength)
               {
               case 163: paramOid = _algorithmsValues.c2pnb163v3;   break;
               case 176: paramOid = _algorithmsValues.c2pnb176w1;   break;
               case 191: paramOid = _algorithmsValues.c2tnb191v3;   break;
               case 208: paramOid = _algorithmsValues.c2pnb208w1;   break;
//               case 239: paramOid = _algorithmsValues.c2tnb239v3;break;
               case 272: paramOid = _algorithmsValues.c2pnb272w1;   break;
               case 304: paramOid = _algorithmsValues.c2pnb304w1;   break;
               case 359: paramOid = _algorithmsValues.c2tnb359v1;   break;
               case 368: paramOid = _algorithmsValues.c2pnb368w1;   break;
               case 384: paramOid = _algorithmsValues.secp384r1;    break;
               case 431: paramOid = _algorithmsValues.c2tnb431r1;   break;
               case 192: paramOid = _algorithmsValues.prime192v3;   break;
               case 239: paramOid = _algorithmsValues.prime239v3;   break;
               case 256: paramOid = _algorithmsValues.prime256v1;   break;
               case 521: paramOid = _algorithmsValues.secp521r1;break;
               case 571: paramOid = _algorithmsValues.sect571r1;break;
               default:
                    throw new UnknownElement(GenelDil.mesaj(GenelDil.ECDSAANAHTARBOYU_0_BILINMIYOR, new String[]{""+aLength})); //Imzalama algoritmasi %s bilinmiyor!
               }
               options.put(ECDSAKeyPairGenerator.DOMAIN_PARAMETERS, ECDomainParameter.getInstance(paramOid));
               break;

            default         :
                throw new UnknownElement(GenelDil.mesaj(GenelDil.IMZALG_0_BILINMIYOR, new String[]{mAsymmetricAlg.name()})); //Imzalama algoritmasi %s bilinmiyor!

        }

        if (kpg == null)
        {
             throw new UnknownElement(GenelDil.mesaj(GenelDil.IMZALG_0_BILINMIYOR, new String[]{mAsymmetricAlg.name()})); //Imzalama algoritmasi %s bilinmiyor!
        }

        try
        {
             //anahtar ciftini uret
             kpg.setup(options);
             kp = kpg.generate();

        } catch (Exception e)
        {
             throw new CryptoException(GenelDil.mesaj(GenelDil.ANAH_URETILEMEDI), e); //Anahtar uretiminde hata!
        }

        return kp;

    }
}
