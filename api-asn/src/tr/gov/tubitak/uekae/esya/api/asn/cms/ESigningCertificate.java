package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificate;

public class ESigningCertificate extends BaseASNWrapper<SigningCertificate>{
	
	public ESigningCertificate(SigningCertificate aObject)
	{
		super(aObject);
	}
	
	public ESigningCertificate(byte[] aBytes) throws ESYAException
	{
		super(aBytes,new SigningCertificate());
	}
}
