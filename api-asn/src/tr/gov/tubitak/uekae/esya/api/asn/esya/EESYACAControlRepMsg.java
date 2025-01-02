package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACAControlRepMsg;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACAControlReqMsg;


public class EESYACAControlRepMsg extends BaseASNWrapper<ESYACAControlRepMsg> implements ExtensionType {

    public EESYACAControlRepMsg(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYACAControlRepMsg());
    }

    public EESYACAControlRepMsg(ESYACAControlRepMsg esyacaControlRepMsg) throws ESYAException {
        super(esyacaControlRepMsg);
    }


    public EExtension toExtension(boolean aCritic) throws ESYAException {
		return new EExtension(EESYAOID.oid_cmpCAControlRepMsg, aCritic, this);
	}
}
