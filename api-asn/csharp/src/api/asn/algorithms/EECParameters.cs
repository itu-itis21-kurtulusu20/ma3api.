using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.asn.algorithms;

namespace tr.gov.tubitak.uekae.esya.src.api.asn.algorithms
{
    public class EECParameters: BaseASNWrapper<ECParameters>
    {      
        public EECParameters(byte[] aBytes) : base(aBytes, new ECParameters())
        {
        }       
    }  
}
