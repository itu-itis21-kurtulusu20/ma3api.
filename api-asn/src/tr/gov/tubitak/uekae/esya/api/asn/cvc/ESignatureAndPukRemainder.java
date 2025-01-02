package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1OctetString;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

public class ESignatureAndPukRemainder extends BaseASNWrapper<SignatureAndPukRemainder>
{

	public ESignatureAndPukRemainder(byte[] aBytes) throws ESYAException 
	{
		super(aBytes, new SignatureAndPukRemainder());
	}
	
	public ESignatureAndPukRemainder(byte[] aSignature, byte[] aPuKRemainder)
	{
		super(new SignatureAndPukRemainder());
		mObject.signature = new Asn1OctetString(aSignature);
		mObject.puKRemainder = new Asn1OctetString(aPuKRemainder);
	}
	
	
	public byte [] getSignature()
	{
		return mObject.signature.value;
	}
	
	public byte [] getPukRemainder()
	{
		return mObject.puKRemainder.value;
	}

}
