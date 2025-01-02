using System.Collections;
using System.Globalization;
using Org.BouncyCastle.Asn1;
using Org.BouncyCastle.Asn1.EdEC;
using Org.BouncyCastle.Asn1.X9;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Agreement;
using Org.BouncyCastle.Crypto.Agreement.Kdf;
using Org.BouncyCastle.Crypto.Digests;
using Org.BouncyCastle.Utilities;

namespace Org.BouncyCastle.Security
{
	/// <remarks>
	///  Utility class for creating IBasicAgreement objects from their names/Oids
	/// </remarks>
	public sealed class AgreementUtilities
	{
		private AgreementUtilities()
		{
		}

		private static readonly IDictionary algorithms = Platform.CreateHashtable();
        //private static readonly IDictionary oids = Platform.CreateHashtable();

        static AgreementUtilities()
		{
		    algorithms[X9ObjectIdentifiers.DHSinglePassStdDHSha1KdfScheme.Id] = "ECDHWITHSHA1KDF";
		    algorithms[X9ObjectIdentifiers.DHSinglePassStdDHSha224KdfScheme.Id] = "ECDHWITHSHA224KDF";
		    algorithms[X9ObjectIdentifiers.DHSinglePassStdDHSha256KdfScheme.Id] = "ECDHWITHSHA256KDF";
		    algorithms[X9ObjectIdentifiers.DHSinglePassStdDHSha384KdfScheme.Id] = "ECDHWITHSHA384KDF";
		    algorithms[X9ObjectIdentifiers.DHSinglePassStdDHSha512KdfScheme.Id] = "ECDHWITHSHA512KDF";

		    algorithms[X9ObjectIdentifiers.DHSinglePassCofactorDHSha1KdfScheme.Id] = "ECCDHWITHSHA1KDF";
		    algorithms[X9ObjectIdentifiers.DHSinglePassCofactorDHSha224KdfScheme.Id] = "ECCDHWITHSHA224KDF";
		    algorithms[X9ObjectIdentifiers.DHSinglePassCofactorDHSha256KdfScheme.Id] = "ECCDHWITHSHA256KDF";
		    algorithms[X9ObjectIdentifiers.DHSinglePassCofactorDHSha384KdfScheme.Id] = "ECCDHWITHSHA384KDF";
		    algorithms[X9ObjectIdentifiers.DHSinglePassCofactorDHSha512KdfScheme.Id] = "ECCDHWITHSHA512KDF";

		    algorithms[X9ObjectIdentifiers.MqvSinglePassSha1KdfScheme.Id] = "ECMQVWITHSHA1KDF";

            algorithms[EdECObjectIdentifiers.id_X25519.Id] = "X25519";
            algorithms[EdECObjectIdentifiers.id_X448.Id] = "X448";
        }

        public static IBasicAgreement GetBasicAgreement(
			DerObjectIdentifier oid)
		{
			return GetBasicAgreement(oid.Id);
		}

		public static IBasicAgreement GetBasicAgreement(
			string algorithm)
		{
		    string upper = algorithm.ToUpper(CultureInfo.InvariantCulture);
		    string mechanism = (string)algorithms[upper];

		    if (mechanism == null)
		    {
		        mechanism = upper;
		    }

            if (mechanism == "DH" || mechanism == "DIFFIEHELLMAN")
				return new DHBasicAgreement();

			if (mechanism == "ECDH")
				return new ECDHBasicAgreement();

            if (mechanism == "ECDHC" || mechanism == "ECCDH")
                    return new ECDHCBasicAgreement();

			if (mechanism == "ECMQV")
				return new ECMqvBasicAgreement();

			throw new SecurityUtilityException("Basic Agreement " + algorithm + " not recognised.");
		}

		public static IBasicAgreement GetBasicAgreementWithKdf(
			DerObjectIdentifier oid,
			string				wrapAlgorithm)
		{
			return GetBasicAgreementWithKdf(oid.Id, wrapAlgorithm);
		}

		public static IBasicAgreement GetBasicAgreementWithKdf(
			string agreeAlgorithm,
			string wrapAlgorithm)
		{
		    string upper = agreeAlgorithm.ToUpper(CultureInfo.InvariantCulture);
            string mechanism = (string)algorithms[upper];

		    if (mechanism == null)
		    {
		        mechanism = upper;
		    }

		    // 'DHWITHSHA1KDF' retained for backward compatibility
		    if (mechanism == "DHWITHSHA1KDF" || mechanism == "ECDHWITHSHA1KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha1Digest()));
		    else if (mechanism == "ECDHWITHSHA224KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha224Digest()));
		    else if (mechanism == "ECDHWITHSHA256KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha256Digest()));
		    else if (mechanism == "ECDHWITHSHA384KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha384Digest()));
		    else if (mechanism == "ECDHWITHSHA512KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha512Digest()));

		    else if (mechanism == "ECCDHWITHSHA1KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha1Digest()));
		    else if (mechanism == "ECCDHWITHSHA224KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha224Digest()));
		    else if (mechanism == "ECCDHWITHSHA256KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha256Digest()));
		    else if (mechanism == "ECCDHWITHSHA384KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha384Digest()));
		    else if (mechanism == "ECCDHWITHSHA512KDF")
		        return new ECDHWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha512Digest()));

		    else if (mechanism == "ECMQVWITHSHA1KDF")
		        return new ECMqvWithKdfBasicAgreement(wrapAlgorithm, new ECDHKekGenerator(new Sha1Digest()));
      
			throw new SecurityUtilityException("Basic Agreement (with KDF) " + agreeAlgorithm + " not recognised.");
   }

        public static IRawAgreement GetRawAgreement(
            DerObjectIdentifier oid)
        {
            return GetRawAgreement(oid.Id);
        }

        public static IRawAgreement GetRawAgreement(
            string algorithm)
        {
            string mechanism = GetMechanism(algorithm);

            if (mechanism == "X25519")
                return new X25519Agreement();

            if (mechanism == "X448")
                return new X448Agreement();

            throw new SecurityUtilityException("Raw Agreement " + algorithm + " not recognised.");
        }

		public static string GetAlgorithmName(
			DerObjectIdentifier oid)
		{
			return (string)algorithms[oid.Id];
		}

        private static string GetMechanism(string algorithm)
        {
            string upper = Platform.ToUpperInvariant(algorithm);
            string mechanism = (string)algorithms[upper];
            return mechanism == null ? upper : mechanism;
        }
	}
}
