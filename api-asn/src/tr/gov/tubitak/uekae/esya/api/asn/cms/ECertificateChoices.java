package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificateV1;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateChoices;
import tr.gov.tubitak.uekae.esya.asn.cms.ExtendedCertificate;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherCertificateFormat;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

public class ECertificateChoices extends BaseASNWrapper<CertificateChoices> 
{
	public ECertificateChoices(CertificateChoices aObject)
	{
		super(aObject);
	}
	
	public ECertificateChoices(byte[] aBytes) throws ESYAException
	{
		super(aBytes,new CertificateChoices());
	}
	
	public ECertificateChoices(ECertificate aCertificate)
	{
		super(new CertificateChoices());
		setCertificate(aCertificate);
	}
	
	public int getType()
	{
		return mObject.getChoiceID();
	}
	
	public void setCertificate(ECertificate aValue)
	{
		mObject.setElement(CertificateChoices._CERTIFICATE, aValue.getObject());
	}
	
	public void setExtendedCertificate(ExtendedCertificate aValue)
	{
		mObject.setElement(CertificateChoices._EXTENDEDCERTIFICATE, aValue);
	}
	
	public void setAttributeCertificateV1(AttributeCertificateV1 aValue)
	{
		mObject.setElement(CertificateChoices._V1ATTRCERT, aValue);
	}
	
	public void setAttributeCertificateV2(AttributeCertificate aValue)
	{
		mObject.setElement(CertificateChoices._V2ATTRCERT, aValue);
	}
	
	public void setOtherCertificateFormat(OtherCertificateFormat aValue)
	{
		mObject.setElement(CertificateChoices._OTHER, aValue);
	}
	
	public ECertificate getCertificate()
	{
		if(mObject.getChoiceID()==CertificateChoices._CERTIFICATE)
			return new ECertificate((Certificate) mObject.getElement());
		
		return null;
	}
	
	public ExtendedCertificate getExtendedCertificate()
	{
		if(mObject.getChoiceID()==CertificateChoices._EXTENDEDCERTIFICATE)
			return (ExtendedCertificate) mObject.getElement();
		
		return null;
	}
	
	public AttributeCertificateV1 getAttributeCertificateV1()
	{
		if(mObject.getChoiceID()==CertificateChoices._V1ATTRCERT)
			return (AttributeCertificateV1) mObject.getElement();
		
		return null;
	}
	
	public AttributeCertificate getAttributeCertificateV2()
	{
		if(mObject.getChoiceID()==CertificateChoices._V2ATTRCERT)
			return (AttributeCertificate) mObject.getElement();
		
		return null;
	}
	
	
	public OtherCertificateFormat getOtherCertificateFormat()
	{
		if(mObject.getChoiceID()==CertificateChoices._OTHER)
			return (OtherCertificateFormat) mObject.getElement();
		
		return null;
	}
	
}
