package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1ValueParseException;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACAControlReqMsg;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAYeniSertifikaDurumu;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAYeniSertifikaDurumu_durum;


public class EESYACAControlReqMsg extends BaseASNWrapper<ESYACAControlReqMsg> implements ExtensionType {

    public EESYACAControlReqMsg(ESYACAControlReqMsg esyacaControlReqMsg) throws ESYAException {
        super(esyacaControlReqMsg);
    }

    public EESYACAControlReqMsg(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYACAControlReqMsg());
    }


    public EExtension toExtension(boolean aCritic) throws ESYAException {
		return new EExtension(EESYAOID.oid_cmpCAControlReqMsg, aCritic, this);
	}

    public EESYAParametreleri getParameters(){
        EESYAParametreleri retParameters=null;
        ESYACAControlReqMsg object = getObject();
        if((object!=null) && (object.params!=null)){
            retParameters=new EESYAParametreleri(object.params);
        }
        return retParameters;
    }
}
