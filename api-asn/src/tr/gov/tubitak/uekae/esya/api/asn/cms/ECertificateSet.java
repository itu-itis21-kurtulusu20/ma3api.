package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateChoices;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateSet;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

public class ECertificateSet extends BaseASNWrapper<CertificateSet>
{
	public ECertificateSet(CertificateSet aObject)
	{
		super(aObject);
	}
	
	public int getCertificateChoicesCount()
	{
		if(mObject.elements==null)
			return 0;
		return mObject.elements.length;
	}
	
	
	public ECertificateChoices getCertificateChoices(int aIndex)
	{
		if(mObject.elements==null)
			return null;
		
		return new ECertificateChoices(mObject.elements[aIndex]);
	}
	
	public void addCertificateChoices(ECertificateChoices aChoices)
	{
		mObject.elements = extendArray(mObject.elements, aChoices.getObject());
	}
	
	public ECertificate[] getCertificates()
	{
		if(mObject.elements==null)
			return null;
		
		ECertificate[] certs = null;
		for(int i =0;i<mObject.elements.length;i++)
		{
			CertificateChoices cc = mObject.elements[i];
			if(cc.getChoiceID()==CertificateChoices._CERTIFICATE)
				certs = extendArray(certs, new ECertificate((Certificate) cc.getElement()));
		}
		
		return certs;
	}
}
