using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;


namespace tr.gov.tubitak.uekae.esya.api.asn.algorithms
{
    public class ERSASSA_PSS_params :BaseASNWrapper<RSASSA_PSS_params>
    {

        public ERSASSA_PSS_params(RSASSA_PSS_params aObject)
            :base(aObject)
        {
        }

        public ERSASSA_PSS_params(EAlgorithmIdentifier digestAlg, int [] mgfOid, EAlgorithmIdentifier MGFDigestAlg, int saltLength, int trailerField)
            : base(new RSASSA_PSS_params())
        {           
           try
		   {
                mObject.hashAlgorithm = digestAlg.getObject();
                mObject.maskGenAlgorithm = new EAlgorithmIdentifier(mgfOid, MGFDigestAlg.getEncoded()).getObject();
                mObject.saltLength = new Asn1Integer(saltLength);
                mObject.trailerField = new Asn1Integer(trailerField);
            }
		   catch(Exception aEx)
		   {
			  throw new ESYAException("Error in conversion to asn object", aEx);
		   }
        }
    }
}
