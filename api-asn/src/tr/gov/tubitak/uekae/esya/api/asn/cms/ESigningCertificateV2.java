package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificateV2;

public class ESigningCertificateV2 extends BaseASNWrapper<SigningCertificateV2>{

	public ESigningCertificateV2(SigningCertificateV2 aObject)
	{
		super(aObject);
	}

	public ESigningCertificateV2(byte[] aBytes) throws ESYAException
	{
		super(aBytes,new SigningCertificateV2());
	}

	public byte [] getFirstHash() {
		return mObject.certs.elements[0].certHash.value;
	}
}
