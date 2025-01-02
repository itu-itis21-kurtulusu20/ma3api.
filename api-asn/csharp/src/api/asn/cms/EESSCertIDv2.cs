using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;


namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EESSCertIDv2 : BaseASNWrapper<ESSCertIDv2> 
    {
     public EESSCertIDv2(ESSCertIDv2 aSignerInfo)
          : base(aSignerInfo)
	{
	}
	
	public EESSCertIDv2(byte[] aBytes)
        : base(aBytes,new ESSCertIDv2())
	{		
	}
	
	public EAlgorithmIdentifier getHashAlgorithm()
	{
		if(mObject.hashAlgorithm != null)
			return new EAlgorithmIdentifier(mObject.hashAlgorithm);
		else
			return new EAlgorithmIdentifier(new AlgorithmIdentifier(_algorithmsValues.id_sha256));
	}
	








    }
}
