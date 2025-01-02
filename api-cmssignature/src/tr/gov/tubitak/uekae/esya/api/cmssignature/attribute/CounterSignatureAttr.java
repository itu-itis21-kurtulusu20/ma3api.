package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerInfo;

import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;


public class CounterSignatureAttr
extends AttributeValue
{
	/**
	 * only attributes that are allowed in a
	 * counterSignature attribute are
	 * counterSignature, messageDigest,
      signingTime, and signingCertificate.
	 */
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_countersignature;
	private static final int DEFAULT_SIGNERINFO_VERSION = 1;



	public CounterSignatureAttr ()
	{
		super();
	}


	public CounterSignatureAttr (SignerInfo aCounter) throws CMSSignatureException
	{
		super();
		_setValue(aCounter);
	}


	public void setValue () throws CMSSignatureException
	{
		Object sertifika = mAttParams.get(AllEParameters.P_COUNTER_SIGNATURE_CERTIFICATE);
		if(sertifika==null)
			throw new NullParameterException("P_COUNTER_SIGNATURE_CERTIFICATE parametre degeri null");

		Object imzaci = mAttParams.get(AllEParameters.P_COUNTER_SIGNATURE_SIGNER_INTERFACE);
		if(imzaci==null)
			throw new NullParameterException("P_COUNTER_SIGNATURE_SIGNER_INTERFACE parametre degeri null");

		Object signature = mAttParams.get(AllEParameters.P_SIGNATURE);
		if(signature == null)
			throw new NullParameterException("P_SIGNATURE parametre degeri null");

		ECertificate cer = null;
		try
		{
			cer = (ECertificate) sertifika;
		}
		catch(ClassCastException aEx)
		{
			throw new CMSSignatureException("P_COUNTER_SIGNATURE_CERTIFICATE parametresi ECertificate tipinde degil",aEx);
		}

		BaseSigner imzaciI = null;
		try
		{
			imzaciI = (BaseSigner) imzaci;
		}
		catch(ClassCastException aEx)
		{
			throw new CMSSignatureException("P_COUNTER_SIGNATURE_SIGNER_INTERFACE parametre degeri AY_BasitImzaci tipinde degil",aEx);
		}

		byte[] signatureBA = null;
		try
		{
			signatureBA = (byte[]) signature;
		}
		catch(ClassCastException aEx)
		{
			throw new CMSSignatureException("P_SIGNATURE parametre degeri byte[] tipinde degil",aEx);
		}

		ESignerInfo signerInfo = null;
		try
		{
			signerInfo = _signerInfoOlustur(cer,imzaciI,signatureBA);
		}
		catch(Exception aEx)
		{
			throw new CMSSignatureException("Verilen parametrelerle CounterSignature icin imzaci olusturulamadi.",aEx);
		}

		_setValue(signerInfo.getObject());
	}	

	private ESignerInfo _signerInfoOlustur(ECertificate aCer,BaseSigner aImzaci,byte[] aContent)
			throws CMSSignatureException,ESYAException
			{
		
		SignatureAlg signatureAlg = SignatureAlg.fromName(aImzaci.getSignatureAlgorithmStr());
		DigestAlg digestAlg = signatureAlg.getDigestAlg(); 

		ESignerInfo si = new ESignerInfo(new SignerInfo());

		si.setVersion(DEFAULT_SIGNERINFO_VERSION);

		ESignerIdentifier sid = new ESignerIdentifier(new SignerIdentifier());
		sid.setIssuerAndSerialNumber(new EIssuerAndSerialNumber(aCer));
		si.setSignerIdentifier(sid);

		si.setDigestAlgorithm(digestAlg.toAlgorithmIdentifier());

		AlgorithmParameterSpec spec = aImzaci.getAlgorithmParameterSpec();
        EAlgorithmIdentifier signatureAlgIdentifier = signatureAlg.toAlgorithmIdentifierFromSpec(spec);
        si.setSignatureAlgorithm(signatureAlgIdentifier);




		//byte[] imzalanacak = DigestUtil.digest(digestAlg, aContent);

		byte[] imza;
		try {
			imza = aImzaci.sign(aContent);
		} catch (ESYAException e) {
			throw new CryptoException("Can not sign data", e);
		}

		si.setSignature(imza);

		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put(AllEParameters.P_CONTENT, new SignableByteArray(aContent));
		params.put(AllEParameters.P_DIGEST_ALGORITHM, digestAlg);
		params.put(AllEParameters.P_SIGNING_CERTIFICATE, aCer);


		MessageDigestAttr md = new MessageDigestAttr();
		md.setParameters(params);
		md.setValue();

		IAttribute sc = null;
		if(digestAlg.equals(DigestAlg.SHA1))
		{
			sc = new SigningCertificateAttr();
		}
		else
		{
			sc = new SigningCertificateV2Attr();
		}
		sc.setParameters(params);
		sc.setValue();




		si.addSignedAttribute(md.getAttribute());
		si.addSignedAttribute(sc.getAttribute());

		return si;
			}
    /**
	 * Checks whether attribute is signed or not.
	 * @return false 
	 */ 
	public boolean isSigned() 
	{
		return false;
	}

	/**
	 * Returns AttributeOID of CounterSignatureAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}

}

