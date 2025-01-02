package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerIdentifier;

import java.util.Arrays;

public class ESignerIdentifier extends BaseASNWrapper<SignerIdentifier>
{
	public ESignerIdentifier(SignerIdentifier aObject)
	{
		super(aObject);
	}
	public ESignerIdentifier(byte[] aBytes) throws ESYAException
	{
		super(aBytes,new SignerIdentifier());
	}
	public int getType()
	{
		return mObject.getChoiceID();
	}

	public void setIssuerAndSerialNumber(EIssuerAndSerialNumber aIssuerAndSerial)
	{
		mObject.setElement(SignerIdentifier._ISSUERANDSERIALNUMBER, aIssuerAndSerial.getObject());
	}

	public void setSubjectKeyIdentifier(byte[] aSubjectKeyIdentifier)
	{
		mObject.setElement(SignerIdentifier._SUBJECTKEYIDENTIFIER, new Asn1OctetString(aSubjectKeyIdentifier));
	}

	public EIssuerAndSerialNumber getIssuerAndSerialNumber()
	{
		if(mObject.getChoiceID()==SignerIdentifier._ISSUERANDSERIALNUMBER)
			return new EIssuerAndSerialNumber((IssuerAndSerialNumber)mObject.getElement());

		return null;
	}

	public byte[] getSubjectKeyIdentifier()
	{
		if(mObject.getChoiceID()==SignerIdentifier._SUBJECTKEYIDENTIFIER)
			return ((Asn1OctetString) mObject.getElement()).value;

		return null;
	}

	public boolean isEqual(ECertificate aCertificate)
	{
		EIssuerAndSerialNumber issuerAndSerial = getIssuerAndSerialNumber();
		if(issuerAndSerial != null)
		{
			EIssuerAndSerialNumber certIssuerAndSerial = new EIssuerAndSerialNumber(aCertificate);
			if(certIssuerAndSerial.equals(issuerAndSerial))
				return true;
			else 
				return false;
		}

		byte [] subjectKeyId = getSubjectKeyIdentifier();
		if(subjectKeyId != null)
		{
			EExtensions extensions = aCertificate.getExtensions();
			if (extensions == null)
				return false;
			
			ESubjectKeyIdentifier certSki = extensions.getSubjectKeyIdentifier();
			if (certSki == null)
				return false;
			
			if(Arrays.equals(subjectKeyId, certSki.getValue()))
				return true;
			else
				return false;
		}

		return false;

	}

	public boolean equals(Object obj) 
	{
		if(obj instanceof ESignerIdentifier)
		{
			ESignerIdentifier objSignerID = (ESignerIdentifier) obj;
			if(getIssuerAndSerialNumber() != null && objSignerID.getIssuerAndSerialNumber() != null)
				return getIssuerAndSerialNumber().equals(objSignerID.getIssuerAndSerialNumber());
			
			if(getSubjectKeyIdentifier() != null && objSignerID.getSubjectKeyIdentifier() != null)
				return Arrays.equals(getSubjectKeyIdentifier() , objSignerID.getSubjectKeyIdentifier());
		}
		
		return super.equals(obj);
	}

	public int hashCode(){
		return super.hashCode();
	}


}
