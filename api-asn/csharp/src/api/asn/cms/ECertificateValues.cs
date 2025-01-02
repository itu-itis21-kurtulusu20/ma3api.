using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ECertificateValues : BaseASNWrapper<CertificateValues>
    {
        public ECertificateValues(CertificateValues aObject):base(aObject)
	{
		
	}
	
	public ECertificateValues(byte[] aBytes):base(aBytes,new CertificateValues())
	{		
	}
    public ECertificateValues(List<ECertificate> aCertificates) :  base(new CertificateValues())
    {
        if (aCertificates != null)
        {
            mObject.elements = unwrapArray<Certificate, ECertificate>(aCertificates.ToArray());
        }
    }
	public int getCertificateCount()
	{
		if(mObject.elements ==null)
			return 0;
		
		return mObject.elements.Length;
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
		
		List<ECertificate> certs = new List<ECertificate>();
		foreach(Certificate cert in mObject.elements)
		{
			certs.Add(new ECertificate(cert));
		}
		
		return certs;
	}
    }
}
