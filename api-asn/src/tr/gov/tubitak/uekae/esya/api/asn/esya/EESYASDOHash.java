package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASDOHash;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 8/9/11 - 4:32 PM <p>
 <b>Description</b>: <br>
 */
public class EESYASDOHash extends BaseASNWrapper<ESYASDOHash> implements ExtensionType{
    public EESYASDOHash(ESYASDOHash aObject) {
        super(aObject);
    }

    public EESYASDOHash(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYASDOHash());
    }

    public EESYASDOHash( EAlgorithmIdentifier hashAlgorithm, byte[] hashValue) {
        super(new ESYASDOHash(hashAlgorithm.getObject(), hashValue));
    }

    public EAlgorithmIdentifier getHashAlgorithm(){
        return new EAlgorithmIdentifier(mObject.hashAlgorithm);
    }

    public byte[] getHashValue(){
        return mObject.hashValue.value;
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EESYAOID.oid_ESYA_SDO, aCritic, this);
    }
}
