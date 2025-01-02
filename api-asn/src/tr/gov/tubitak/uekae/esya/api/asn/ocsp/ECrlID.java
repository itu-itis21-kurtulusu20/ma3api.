package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.CrlID;
import tr.gov.tubitak.uekae.esya.asn.ocsp._ocspValues;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 12/27/11 - 4:44 PM <p>
 * <b>Description</b>: <br>
 */
public class ECrlID extends BaseASNWrapper<CrlID> implements ExtensionType {
    public ECrlID(CrlID aObject) {
        super(aObject);
    }

    public ECrlID(byte[] aBytes) throws ESYAException {
        super(aBytes, new CrlID());
    }

    public ECrlID() {
        super(new CrlID());
    }


    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_crl),aCritic,this);
    }
}
