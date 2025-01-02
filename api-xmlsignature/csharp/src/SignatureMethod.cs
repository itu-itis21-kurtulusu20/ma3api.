using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	using Algorithm = tr.gov.tubitak.uekae.esya.api.crypto.alg.IAlgorithm;
	using DigestAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
	using MACAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
	using SignatureAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	/// <summary>
	/// Defines known signature algorithms.
	/// 
	/// @author ahmety
	/// date: Apr 24, 2009
	/// </summary>
	public class SignatureMethod
	{

	/*  "http://www.w3.org/2000/09/xmldsig#dsa-sha1"
	    "http://www.w3.org/2001/04/xmldsig-more#rsa-md5"
	    "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160"
	    "http://www.w3.org/2000/09/xmldsig#rsa-sha1"
	    "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"
	    "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384"
	    "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"
	    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1"
	    "http://www.w3.org/2001/04/xmldsig-more#hmac-md5"
	    "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160"
	    "http://www.w3.org/2000/09/xmldsig#hmac-sha1"
	    "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256"
	    "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384"
	    "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512"
	    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1"
	    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224"
	    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256"
	    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384"
	    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512"
	    */



		public static readonly  SignatureMethod DSA_SHA1  = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG + "dsa-sha1", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.DSA_SHA1, typeof(DSAXmlSignature.DSAWithSHA1));
	    public static readonly SignatureMethod  DSA_SHA256 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_11 + "dsa-sha256",tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.DSA_SHA1,typeof (DSAXmlSignature.DSAWithSHA1));
	    public static readonly SignatureMethod  RSA_MD5 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "rsa-md5",tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_MD5,typeof (RSAXmlSignature.RSAwithMD5));
		public static readonly  SignatureMethod RSA_SHA1    = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG + "rsa-sha1", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_SHA1, typeof(RSAXmlSignature.RSAwithSHA1));
		public static readonly  SignatureMethod RSA_SHA256  = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "rsa-sha256", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_SHA256, typeof(RSAXmlSignature.RSAwithSHA256));
		public static readonly  SignatureMethod RSA_SHA384= new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "rsa-sha384", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_SHA384, typeof(RSAXmlSignature.RSAwithSHA384));
		public static readonly  SignatureMethod RSA_SHA512= new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "rsa-sha512", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_SHA512, typeof(RSAXmlSignature.RSAwithSHA512));


	    public static readonly SignatureMethod RSA_PSS_SHA1 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE_2007 + "sha1-rsa-MGF1", new RSAPSSParams(DigestAlg.SHA1), tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_PSS, typeof(RSAXmlSignature.RSAwithPSS));
	    public static readonly SignatureMethod RSA_PSS_SHA256 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE_2007 + "sha256-rsa-MGF1", new RSAPSSParams(DigestAlg.SHA256), tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_PSS, typeof(RSAXmlSignature.RSAwithPSS));
	    public static readonly SignatureMethod RSA_PSS_SHA384 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE_2007 + "sha384-rsa-MGF1", new RSAPSSParams(DigestAlg.SHA384), tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_PSS, typeof(RSAXmlSignature.RSAwithPSS));
	    public static readonly SignatureMethod RSA_PSS_SHA512 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE_2007 + "sha512-rsa-MGF1", new RSAPSSParams(DigestAlg.SHA512), tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.RSA_PSS, typeof(RSAXmlSignature.RSAwithPSS));


        public static readonly  SignatureMethod ECDSA_SHA1= new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "ecdsa-sha1", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.ECDSA_SHA1, typeof(ECDSAXmlSignature.ECDSAwithSHA1));
		public static readonly  SignatureMethod ECDSA_SHA256= new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "ecdsa-sha256", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.ECDSA_SHA256,typeof(ECDSAXmlSignature.ECDSAwithSHA256));
		public static readonly  SignatureMethod ECDSA_SHA384= new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "ecdsa-sha384", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.ECDSA_SHA384, typeof(ECDSAXmlSignature.ECDSAwithSHA384));
		public static readonly  SignatureMethod ECDSA_SHA512= new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "ecdsa-sha512", tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg.ECDSA_SHA512, typeof(ECDSAXmlSignature.ECDSAwithSHA512));


        public static readonly SignatureMethod HMAC_MD5 =new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "hmac-md5",tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg.HMAC_MD5,typeof (HMACIntegrity.HMACwithMD5));
        public static readonly SignatureMethod HMAC_RIPEMD = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "hmac-ripemd160",tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg.HMAC_RIPEMD, typeof (HMACIntegrity.HMACwithRIPEMD));
        public static readonly SignatureMethod HMAC_SHA1 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG + "hmac-sha1",tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg.HMAC_SHA1,typeof (HMACIntegrity.HMACwithSHA1));
        public static readonly SignatureMethod HMAC_SHA256 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "hmac-sha256",tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg.HMAC_SHA256,typeof (HMACIntegrity.HMACwithSHA256));
        public static readonly SignatureMethod HMAC_SHA384 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "hmac-sha384",tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg.HMAC_SHA384,typeof (HMACIntegrity.HMACwithSHA384));
        public static readonly SignatureMethod HMAC_SHA512 = new SignatureMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "hmac-sha512",tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg.HMAC_SHA512,typeof (HMACIntegrity.HMACwithSHA512));

public static IEnumerable<SignatureMethod> Values
{
   get
   {
       yield return DSA_SHA1;
       yield return DSA_SHA256;
       yield return RSA_MD5;
       yield return RSA_SHA1;
       yield return RSA_SHA256;
       yield return RSA_SHA384;                
       yield return RSA_SHA512;
       yield return ECDSA_SHA1;
       yield return ECDSA_SHA256;
       yield return ECDSA_SHA384;
       yield return ECDSA_SHA512;
       yield return RSA_PSS_SHA1;
       yield return RSA_PSS_SHA256;
       yield return RSA_PSS_SHA384;
       yield return RSA_PSS_SHA512;
       yield return HMAC_MD5;
       yield return HMAC_RIPEMD;
       yield return HMAC_SHA1;
       yield return HMAC_SHA256;
       yield return HMAC_SHA384;
       yield return HMAC_SHA512;
   }
}

public Algorithm MAlgorithm
{
 get { return mAlgorithm; }
}

public Algorithm MSignatureAlg
{
   get { return mSignatureAlg; }
}


public DigestAlg MDigestAlg
{
 get { return mDigestAlg; }
}

public Type MSignatureClass
{
 get { return mSignatureClass; }
 set { mSignatureClass = value; }
}

public string Url
{
 get { return mUrl; }
}

    private readonly string mUrl;
    private readonly Algorithm mAlgorithm;
    private readonly Algorithm mSignatureAlg;
    private readonly DigestAlg mDigestAlg;
    private Type mSignatureClass;
    private readonly IAlgorithmParams mAlgorithmParams;

  SignatureMethod(String aUrl, tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg aSignatureAlg, Type aClass)
  {
      mUrl = aUrl;
      mAlgorithm = aSignatureAlg.getAsymmetricAlg();
      mSignatureAlg = aSignatureAlg;
      mDigestAlg = aSignatureAlg.getDigestAlg();
      MSignatureClass = aClass;
  }

  SignatureMethod(String aUrl, RSAPSSParams pssParams, SignatureAlg aSignatureAlg, Type aClass)
  {
	  mUrl = aUrl;
	  mSignatureAlg = aSignatureAlg;
	  mAlgorithm = aSignatureAlg.getAsymmetricAlg();
	  mAlgorithmParams = pssParams;
	  mDigestAlg = pssParams.getDigestAlg();
	  mSignatureClass = aClass;
  }

   SignatureMethod(String aUrl, tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg aMacAlg, Type aClass)
  {
      mUrl = aUrl;
      mAlgorithm = aMacAlg;
      mSignatureAlg = aMacAlg;
      mDigestAlg = aMacAlg.getDigestAlg();
      MSignatureClass = aClass;
   }




/// <summary>
/// return SignatureMethod from algorithm URL </summary>
/// <param name="aUrl"> of SignatureMethod </param>
/// <returns> matching SignatureMethod </returns>
/// <exception cref="UnknownAlgorithmException"> if no matching method found! </exception>

public static SignatureMethod resolve(String aUrl)
 {
     if (aUrl!=null)
     {
         foreach (SignatureMethod alg in Values)
         {
             if (alg.Url.Equals(aUrl))
                 return alg;
         }
     }
     throw new UnknownAlgorithmException(null, "unknown.algorithm",aUrl);
 }

public XmlSignatureAlgorithm getSignatureImpl()
{
   try
   {
       return (XmlSignatureAlgorithm)Activator.CreateInstance(MSignatureClass);
   }
   catch (Exception x)
   {
       // very low probability of no default arg constructor !..
       throw new XMLSignatureRuntimeException(x, "Can't construct implementor for " + Url);
   }
}
 public IAlgorithmParams getAlgorithmParams()
 {
	 return mAlgorithmParams;

 }

 public static SignatureMethod fromAlgorithmName(String name)
 {
     foreach (SignatureMethod method in Values)
     {
            if (name.Equals(method.getSignatureImpl().AlgorithmName))
                return method;
     }
     return null;
 }

 public static SignatureMethod fromAlgorithmAndParams(String name, IAlgorithmParameterSpec algorithmParams)
 {
     if (name.Equals("RSAPSS"))
     {
         foreach (SignatureMethod method in Values)
         {
             if (name.Equals(method.getSignatureImpl().AlgorithmName))
             {
                 RSAPSSParams methodParams = null;
                 if (method.getAlgorithmParams() is RSAPSSParams)
                 {
                     methodParams = (RSAPSSParams)method.getAlgorithmParams();
                     if (methodParams.Equals(algorithmParams))
                         return method;
                 }
             }
         }
     }
     return fromAlgorithmName(name);
 }
 }
}
