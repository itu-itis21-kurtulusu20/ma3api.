package tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes;

import tr.gov.tubitak.uekae.esya.asn.cms.CMSVersion;
import tr.gov.tubitak.uekae.esya.asn.cms.KEKRecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

@SuppressWarnings("serial")
public class KEKRecipient extends RecipientInfo
{
	private KEKRecipientInfo ri = new KEKRecipientInfo();
	
	public KEKRecipient()
	{
		super();
		_ilkIsler();
	}
	
	public KEKRecipient(Certificate aSertifika)
	{
		super();
		_ilkIsler();
		setKeyEncryptionAlgorithm(aSertifika.tbsCertificate.subjectPublicKeyInfo.algorithm);
	}
	
	public void setKeyEncryptionAlgorithm(AlgorithmIdentifier aAlgorithmIdentifier)
	{
		ri.keyEncryptionAlgorithm = aAlgorithmIdentifier;
	}
	
	public CMSVersion getCMSVersion()
	{
		return ri.version;
	}
	
	private void _ilkIsler()
	{
		setElement(_KEKRI, ri);
		ri.version = new CMSVersion(4);
	}
}
