package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1OpenType;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherRevocationInfoFormat;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationInfoChoice;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;

public class ERevocationInfoChoice extends BaseASNWrapper<RevocationInfoChoice>
{
	public ERevocationInfoChoice(RevocationInfoChoice aRevocationInfoChoice)
	{
		super(aRevocationInfoChoice);
	}
	
	public ERevocationInfoChoice(ECRL aCRL)
	{
		super(new RevocationInfoChoice());
		setCRL(aCRL);
	}
	
	public ERevocationInfoChoice(EOCSPResponse aOCSP)
	{
		super(new RevocationInfoChoice());
		setOCSP(aOCSP);
	}
	
	public int getType()
	{
		return mObject.getChoiceID();
	}
	
	public void setCRL(ECRL aCRL)
	{
		mObject.setElement(RevocationInfoChoice._CRL, aCRL.getObject());
	}
	//TODO basicOCSPResponse?
	public void setOCSP(EOCSPResponse aOCSP)
	{
		OtherRevocationInfoFormat other=new OtherRevocationInfoFormat(_cmsValues.id_ri_ocsp_response, new Asn1OpenType(aOCSP.getEncoded()));
		mObject.setElement(RevocationInfoChoice._OTHER,other);
	}
	
	public void setOtherRevocationInfo(OtherRevocationInfoFormat aOther)
	{
		mObject.setElement(RevocationInfoChoice._OTHER, aOther);
	}
	
	public ECRL getCRL()
	{
		if(mObject.getChoiceID()==RevocationInfoChoice._CRL)
			return new ECRL((CertificateList) mObject.getElement());
		
		return null;
	}
	
	
	public OtherRevocationInfoFormat getOtherRevocationInfo()
	{
		if(mObject.getChoiceID()==RevocationInfoChoice._OTHER)
			return (OtherRevocationInfoFormat) mObject.getElement();
		
		return null;
	}
	
	
	
}
