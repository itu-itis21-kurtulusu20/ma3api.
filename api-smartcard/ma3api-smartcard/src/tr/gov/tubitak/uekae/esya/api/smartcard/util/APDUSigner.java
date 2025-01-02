package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.APDUSmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import javax.smartcardio.CardException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;


public class APDUSigner implements BaseSigner
{
	APDUSmartCard msc;
	X509Certificate certificate;
	String signingAlg;
	AlgorithmParameterSpec params;
	
	public APDUSigner(APDUSmartCard aSC, X509Certificate aCertificate,String aSigningAlg)
	{
		msc = aSC;
		certificate = aCertificate;
		signingAlg = aSigningAlg;
	}
	
	public APDUSigner(APDUSmartCard aSC, X509Certificate aCertificate,String aSigningAlg, AlgorithmParameterSpec aParams)
	{
		this(aSC, aCertificate, aSigningAlg);
		params = aParams;
	}
	
	
	
	@Override
	public byte[] sign(byte[] aData) throws ESYAException 
	{
		try 
		{
			return msc.sign(aData, certificate, signingAlg, params);
		} 
		catch (CardException e) 
		{
			throw new ESYAException(e);
		}
		catch (SmartCardException e) 
		{
			throw new ESYAException(e);
		}
	}

	@Override
	public String getSignatureAlgorithmStr() 
	{
		return signingAlg;
	}

    @Override
    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return params;
    }

}
