using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.esya;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYASDOHash : BaseASNWrapper<ESYASDOHash>,ExtensionType
    {
        public EESYASDOHash(ESYASDOHash aObject):base(aObject) {}

        public EESYASDOHash(byte[] aBytes) : base(aBytes, new ESYASDOHash()){}

        public EESYASDOHash( EAlgorithmIdentifier hashAlgorithm, byte[] hashValue) 
            :base(new ESYASDOHash(hashAlgorithm.getObject(), hashValue)){}

        public EAlgorithmIdentifier getHashAlgorithm(){
            return new EAlgorithmIdentifier(mObject.hashAlgorithm);
        }

        public byte[] getHashValue(){
            return mObject.hashValue.mValue;
        }

        public EExtension toExtension(bool aCritic){
            return new EExtension(EESYAOID.oid_ESYA_SDO, aCritic, getBytes());
        }
    }
}
