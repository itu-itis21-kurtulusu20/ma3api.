package tr.gov.tubitak.uekae.esya.asn.util;

import tr.gov.tubitak.uekae.esya.asn.cms.IssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

public class UtilCMS
{
	public static IssuerAndSerialNumber issuerAndSerialNumberOlustur(Certificate aSertifika)
	{
		IssuerAndSerialNumber ias = new IssuerAndSerialNumber();
		ias.issuer = aSertifika.tbsCertificate.issuer;
		ias.serialNumber = aSertifika.tbsCertificate.serialNumber;
		return ias;
	}
}
