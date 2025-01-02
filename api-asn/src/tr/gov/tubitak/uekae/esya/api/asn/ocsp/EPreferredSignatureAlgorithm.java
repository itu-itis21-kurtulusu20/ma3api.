package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.PreferredSignatureAlgorithm;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 12/23/11 - 10:18 AM <p>
 * <b>Description</b>: <br>
 */
public class EPreferredSignatureAlgorithm extends BaseASNWrapper<PreferredSignatureAlgorithm> {
    public EPreferredSignatureAlgorithm(PreferredSignatureAlgorithm aObject) {
        super(aObject);
    }

    public EPreferredSignatureAlgorithm(byte[] aBytes) throws ESYAException {
        super(aBytes, new PreferredSignatureAlgorithm());
    }

    public EPreferredSignatureAlgorithm(EAlgorithmIdentifier sigIdentifier)  {
        super(new PreferredSignatureAlgorithm(sigIdentifier.getObject()));
    }

    public EPreferredSignatureAlgorithm(EAlgorithmIdentifier sigIdentifier, EAlgorithmIdentifier certIdentifier)  {
        super(new PreferredSignatureAlgorithm(
                sigIdentifier.getObject(),
                certIdentifier.getObject()));
    }

    public EAlgorithmIdentifier getSigIdentifier(){
        return new EAlgorithmIdentifier(mObject.sigIdentifier);
    }

    public EAlgorithmIdentifier getCertIdentifier(){
        if(mObject.certIdentifier == null)
            return null;
        return new EAlgorithmIdentifier(mObject.certIdentifier);
    }
}
