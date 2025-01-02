package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.ESSCertIDv2;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

public class EESSCertIDv2 extends BaseASNWrapper<ESSCertIDv2>  
{
	public EESSCertIDv2(ESSCertIDv2 aSignerInfo)
	{
		super(aSignerInfo);
	}
	
	public EESSCertIDv2(byte[] aBytes) throws ESYAException
	{
		super(aBytes,new ESSCertIDv2());
	}
	
	public EAlgorithmIdentifier getHashAlgorithm()
	{
		if(mObject.hashAlgorithm != null)
			return new EAlgorithmIdentifier(mObject.hashAlgorithm);
		else
			return new EAlgorithmIdentifier(new AlgorithmIdentifier(_algorithmsValues.id_sha256));
	}
	
}
