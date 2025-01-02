package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateValues;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

import java.util.ArrayList;
import java.util.List;

public class ECertificateValues extends BaseASNWrapper<CertificateValues>
{
	public ECertificateValues(CertificateValues aObject)
	{
		super(aObject);
	}
	
	public ECertificateValues(byte[] aBytes)
	throws ESYAException
	{
		super(aBytes,new CertificateValues());
	}

    public ECertificateValues(List<ECertificate> aCertificates)
    {
        super(new CertificateValues());
        if (aCertificates!=null){
            mObject.elements = unwrapArray(aCertificates.toArray(new ECertificate[aCertificates.size()]));
        }
    }

    public int getCertificateCount()
	{
		if(mObject.elements ==null)
			return 0;
		
		return mObject.elements.length;
	}
	
	public ECertificate getCertificate(int aIndex)
	{
		if(mObject.elements == null)
			return null;
		
		return new ECertificate(mObject.elements[aIndex]);
	}
	
	public List<ECertificate> getCertificates()
	{
		if(mObject.elements ==null)
			return null;
		
		List<ECertificate> certs = new ArrayList<ECertificate>();
		for(Certificate cert:mObject.elements)
		{
			certs.add(new ECertificate(cert));
		}
		
		return certs;
	}
}
