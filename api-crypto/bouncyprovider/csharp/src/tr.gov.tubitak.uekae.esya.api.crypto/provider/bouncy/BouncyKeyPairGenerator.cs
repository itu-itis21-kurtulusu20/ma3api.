using System;
using Com.Objsys.Asn1.Runtime;
using Org.BouncyCastle.Asn1;
using Org.BouncyCastle.Asn1.Sec;
using Org.BouncyCastle.Asn1.X9;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Generators;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Math.EC;
using Org.BouncyCastle.Pkcs;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.X509;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using BouncyBigInteger = Org.BouncyCastle.Math.BigInteger;
using CryptoException = tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyKeyPairGenerator : IKeyPairGenerator
    {
        private readonly AsymmetricAlg mAsymmetricAlg;

        public BouncyKeyPairGenerator(AsymmetricAlg aAsymmetricAlg)
        {
            mAsymmetricAlg = aAsymmetricAlg;
        }

        public KeyPair generateKeyPair(IAlgorithmParams aParams)
        {
            if (aParams == null)
                throw new CryptoException("AlgorithParams can not be null");

            if (aParams is ParamsWithLength)
            {
                int length = ((ParamsWithLength)aParams).getLength();
                return _generateKeyPair(length);
            }
            else if (aParams is ParamsWithECParameterSpec)
            {
                //todo ortak kripto modulu bouncy'e bagimli olmasin diye ECDomainParameters'i cikardim 26.01.2011
                //ECDomainParameters domainParams = (ECDomainParameters)((ParamsWithECParameterSpec)aParams).getECDomainParams();
                ECDomainParameters domainParams = toBouncyECParameters(((ParamsWithECParameterSpec)aParams).getECDomainParams());
                return _generateKeyPair(domainParams);
            }
            else if (aParams is ParamsWithAlgorithmIdentifier)
            {
                AlgorithmIdentifier algIden = ((ParamsWithAlgorithmIdentifier)aParams).getAlgorithmIdentifier().getObject();
                return _generateKeyPair(toBouncyECParameters(algIden));
            }

            throw new CryptoException("This type AlgorithParams does not support");
        }

        private KeyPair _generateKeyPair(ECDomainParameters domainParams)
        {
            //IKeyPairGenerator kpg = KeyPairGeneratorFactory.getInstance(Registry.ECDSA_KPG);
            IAsymmetricCipherKeyPairGenerator keyPairGenerator = GeneratorUtilities.GetKeyPairGenerator(mAsymmetricAlg.getName());
            //options.put(ECDSAKeyPairGenerator.DOMAIN_PARAMETERS, domainParams);
            //kpg.setup(options);
            keyPairGenerator.Init(new ECKeyGenerationParameters(domainParams, new SecureRandom()));

            AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.GenerateKeyPair();
            PublicKey publicKey = new PublicKey(BouncyProviderUtil.ToAsn1(SubjectPublicKeyInfoFactory.CreateSubjectPublicKeyInfo(asymmetricCipherKeyPair.Public)));

            PrivateKey privateKey = new PrivateKey(BouncyProviderUtil.ToAsn1(PrivateKeyInfoFactory.CreatePrivateKeyInfo(asymmetricCipherKeyPair.Private)));
            KeyPair keyPair = new KeyPair(publicKey, privateKey);
            return keyPair;
            //return kpg.generate();
        }

        private KeyPair _generateKeyPair(int aLength)
        {
            KeyPair keyPair;
            IAsymmetricCipherKeyPairGenerator keyPairGenerator = GeneratorUtilities.GetKeyPairGenerator(BouncyProviderUtil.resolveAsymmetricAlg(mAsymmetricAlg));
            if (mAsymmetricAlg.Equals(AsymmetricAlg.RSA))
            {
                keyPairGenerator.Init(new KeyGenerationParameters(new SecureRandom(), aLength));
            }
            else if (mAsymmetricAlg.Equals(AsymmetricAlg.DSA))
            {
                DsaParametersGenerator pGen = new DsaParametersGenerator();
                pGen.Init(aLength, 80, new SecureRandom());
                SecureRandom keyRandom = new SecureRandom();
                DsaParameters parameters = pGen.GenerateParameters();
                DsaKeyGenerationParameters genParam = new DsaKeyGenerationParameters(keyRandom, parameters);
                keyPairGenerator.Init(genParam);
            }
            else if (mAsymmetricAlg.Equals(AsymmetricAlg.ECDSA))
            {
                ECKeyGenerationParameters genParam;
                DerObjectIdentifier publicKeyParamSet;
                switch (aLength)
                {
                    case 163: publicKeyParamSet = X9ObjectIdentifiers.C2Pnb163v3;/*_algorithmsValues.c2pnb163v3*/   break;
                    case 176: publicKeyParamSet = X9ObjectIdentifiers.C2Pnb176w1;/*_algorithmsValues.c2pnb176w1*/   break;
                    case 191: publicKeyParamSet = X9ObjectIdentifiers.C2Tnb191v3;/*_algorithmsValues.c2tnb191v3;*/   break;
                    case 208: publicKeyParamSet = X9ObjectIdentifiers.C2Pnb208w1;/*_algorithmsValues.c2pnb208w1*/   break;
                    //               case 239: paramOid = _algorithmsValues.c2tnb239v3;break;
                    case 272: publicKeyParamSet = X9ObjectIdentifiers.C2Pnb272w1;/*_algorithmsValues.c2pnb272w1;*/   break;
                    case 304: publicKeyParamSet = X9ObjectIdentifiers.C2Pnb304w1;/*_algorithmsValues.c2pnb304w1;*/   break;
                    case 359: publicKeyParamSet = X9ObjectIdentifiers.C2Tnb359v1;/*_algorithmsValues.c2tnb359v1;*/   break;
                    case 368: publicKeyParamSet = X9ObjectIdentifiers.C2Pnb368w1;/*_algorithmsValues.c2pnb368w1;*/   break;
                    case 384: publicKeyParamSet = SecObjectIdentifiers.SecP384r1;/*_algorithmsValues.secp384r1;*/    break;
                    case 431: publicKeyParamSet = X9ObjectIdentifiers.C2Tnb431r1;/*_algorithmsValues.c2tnb431r1;*/   break;
                    case 192: publicKeyParamSet = X9ObjectIdentifiers.Prime192v3;/*_algorithmsValues.prime192v3;*/   break;
                    case 239: publicKeyParamSet = X9ObjectIdentifiers.Prime239v3;/*_algorithmsValues.prime239v3*/   break;
                    case 256: publicKeyParamSet = X9ObjectIdentifiers.Prime256v1;/*_algorithmsValues.prime256v1;*/   break;
                    case 521: publicKeyParamSet = SecObjectIdentifiers.SecP521r1;/*_algorithmsValues.secp521r1;*/ break;
                    case 571: publicKeyParamSet = SecObjectIdentifiers.SecT571r1;/*_algorithmsValues.sect571r1;*/ break;

                    default:
                        throw new UnknownElement(Resource.message(Resource.ECDSAANAHTARBOYU_0_BILINMIYOR, new String[] { "" + aLength })); //Imzalama algoritmasi %s bilinmiyor!
                }
                genParam = new ECKeyGenerationParameters(publicKeyParamSet, new SecureRandom());
                keyPairGenerator.Init(genParam);

            }
            else
            {
                throw new UnknownElement(Resource.message(Resource.IMZALG_0_BILINMIYOR, new String[] { mAsymmetricAlg.getName() })); //Imzalama algoritmasi %s bilinmiyor!
            }

            AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.GenerateKeyPair();
            PublicKey publicKey = new PublicKey(BouncyProviderUtil.ToAsn1(SubjectPublicKeyInfoFactory.CreateSubjectPublicKeyInfo(asymmetricCipherKeyPair.Public)));

            PrivateKey privateKey = new PrivateKey(BouncyProviderUtil.ToAsn1(PrivateKeyInfoFactory.CreatePrivateKeyInfo(asymmetricCipherKeyPair.Private)));
            keyPair = new KeyPair(publicKey, privateKey);
            return keyPair;
        }


        public ECDomainParameters toBouncyECParameters(AlgorithmIdentifier aAlgID)
        {
            if (!UtilEsitlikler.esitMi(aAlgID.algorithm.mValue, _algorithmsValues.id_ecPublicKey))
            {
                throw new ArgumentException("Not an ECDSA Algorithm ID");
            }

            EcpkParameters params_ = new EcpkParameters();
            UtilOpenType.fromOpenType(aAlgID.parameters, params_);

            if (params_.ChoiceID == EcpkParameters._ECPARAMETERS)
            {
                ECParameters ecParams = (ECParameters)params_.GetElement();
                //
                //gnu.crypto.sig.ecdsa.ecmath.curve.Curve curve;
                return toBouncyECParameters(ecParams);
            }
            else if (params_.ChoiceID == EcpkParameters._NAMEDCURVE)
            {
                //IAsymmetricCipherKeyPairGenerator keyPairGenerator= GeneratorUtilities.GetKeyPairGenerator(BouncyProviderUtil.ToBouncy(((Asn1ObjectIdentifier)params_.GetElement())));
                //X9ECParameters ecps = X962NamedCurves.GetByOid(BouncyProviderUtil.ToBouncy(((Asn1ObjectIdentifier)params_.GetElement())));
                X9ECParameters ecps = BouncyProviderUtil.GetX9ECParameters((Asn1ObjectIdentifier)params_.GetElement());       
                if (ecps == null)
                    throw new ArgumentException("Curve OID not known: " + ((Asn1ObjectIdentifier)params_.GetElement()));
                ECDomainParameters p = new ECDomainParameters(ecps.Curve, ecps.G, ecps.N, ecps.H, ecps.GetSeed());
                //ECDomainParameters p = ecKeyGenerationParameters.DomainParameters;//ECDomainParameters.getInstance(((Asn1ObjectIdentifier)params_.GetElement()).mValue);            
                return p;
            }
            else
                throw new ArgumentException("Parameter not known");
        }

        public static ECDomainParameters toBouncyECParameters(ECParameters ecParams)
        {
            ECCurve curve;
            ECPoint g;

            if (UtilEsitlikler.esitMi(ecParams.fieldID.fieldType.mValue, _algorithmsValues.prime_field))
            {
                //The field is a Prime Field
                Asn1BigInteger p = new Asn1BigInteger();
                UtilOpenType.fromOpenType(ecParams.fieldID.parameters, p);
                //FieldFp field = FieldFp.getInstance(p.mValue);
                //FpFieldElement field = new FpFieldElement(

                //curve = new CurveFp(field,new BigInteger(1,ecParams.curve.a.mValue),new BigInteger(1,ecParams.curve.b.mValue));
                curve = new FpCurve(new BouncyBigInteger(p.mValue.GetData()), new BouncyBigInteger(1, ecParams.curve.a.mValue), new BouncyBigInteger(1, ecParams.curve.b.mValue));

                //g = new ECPointFp(curve,ecParams.base_.mValue);
                g = curve.DecodePoint(ecParams.base_.mValue);
            }
            else if (UtilEsitlikler.esitMi(ecParams.fieldID.fieldType.mValue, _algorithmsValues.characteristic_two_field))
            {
                //The Field is a F2m Field
                Characteristic_two charTwo = new Characteristic_two();
                UtilOpenType.fromOpenType(ecParams.fieldID.parameters, charTwo);
                //Determine the reduction polynomial
                BouncyBigInteger reductionP = new BouncyBigInteger("1");
                reductionP = reductionP.SetBit((int)charTwo.m.mValue);

                if (UtilEsitlikler.esitMi(charTwo.basis.mValue, _algorithmsValues.tpBasis))
                {
                    Asn1Integer trinomial = new Asn1Integer();
                    UtilOpenType.fromOpenType(charTwo.parameters, trinomial);

                    //trinomial bases
                    //reductionP = reductionP.SetBit((int)trinomial.mValue);
                    curve = new F2mCurve((int)charTwo.m.mValue, (int)trinomial.mValue, new BouncyBigInteger(1, ecParams.curve.a.mValue), new BouncyBigInteger(1, ecParams.curve.b.mValue));
                }
                else if (UtilEsitlikler.esitMi(charTwo.basis.mValue, _algorithmsValues.ppBasis))
                {
                    Pentanomial pentanomial = new Pentanomial();
                    UtilOpenType.fromOpenType(charTwo.parameters, pentanomial);

                    //pentanomial bases
                    //reductionP = reductionP.SetBit((int)pentanomial.k1.mValue);
                    //reductionP = reductionP.SetBit((int)pentanomial.k2.mValue);
                    //reductionP = reductionP.SetBit((int)pentanomial.k3.mValue);
                    curve = new F2mCurve((int)charTwo.m.mValue, (int)pentanomial.k1.mValue, (int)pentanomial.k2.mValue, (int)pentanomial.k3.mValue, new BouncyBigInteger(1, ecParams.curve.a.mValue), new BouncyBigInteger(1, ecParams.curve.b.mValue));
                }
                else
                    throw new ArgumentException("F2m not trinomial or pentanomial basis");

                //FieldF2mPolynomial field = FieldF2mPolynomial.getInstance((int)charTwo.m.mValue,reductionP);

                //curve = new CurveF2m(field,new BigInteger(ecParams.curve.a.mValue,1),new BigInteger(ecParams.curve.b.mValue,1));

                //g = new ECPointF2mPolynomial(curve,ecParams.base_.mValue);
                g = curve.DecodePoint(ecParams.base_.mValue);

            }
            else
                throw new ArgumentException("Field type not known");

            ECDomainParameters domainParams = null;
            if (ecParams.cofactor != null)
                domainParams = new ECDomainParameters(curve, g, new BouncyBigInteger(ecParams.order.mValue.GetData()), new BouncyBigInteger(ecParams.cofactor.mValue.GetData()));//ECDomainParameters.getInstance(curve,g,ecParams.order.mValue,ecParams.cofactor.mValue);
            else
                domainParams = new ECDomainParameters(curve, g, new BouncyBigInteger(ecParams.order.mValue.GetData()));//ECDomainParameters.getInstance(curve, g, ecParams.order.mValue); //TO-DO Cofactor hesaplanmali

            return domainParams;
        }

    }
}

