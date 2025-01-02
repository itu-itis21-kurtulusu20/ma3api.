package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.PreferredSignatureAlgorithms;
import tr.gov.tubitak.uekae.esya.asn.ocsp._ocspValues;

import java.util.Arrays;
import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 12/23/11 - 10:12 AM <p>
 * <b>Description</b>: <br>
 */
public class EPreferredSignatureAlgorithms extends BaseASNWrapper<PreferredSignatureAlgorithms> implements ExtensionType{

    public EPreferredSignatureAlgorithms(PreferredSignatureAlgorithms aObject) {
        super(aObject);
    }

    public EPreferredSignatureAlgorithms(byte[] aBytes) throws ESYAException {
        super(aBytes, new PreferredSignatureAlgorithms());
    }

    public EPreferredSignatureAlgorithms(List<EPreferredSignatureAlgorithm> prefAlgs)  {
        super(new PreferredSignatureAlgorithms(
                unwrapArray(prefAlgs.toArray(new EPreferredSignatureAlgorithm[prefAlgs.size()]))
        ));
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_pref_sig_algs),aCritic,this );
    }

    public List<EPreferredSignatureAlgorithm> getPreferredSignatureAlgorithms(){
       return Arrays.asList(wrapArray(mObject.elements, EPreferredSignatureAlgorithm.class));
    }
}
