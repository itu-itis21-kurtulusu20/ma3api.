package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1ValueParseException;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACardSerialNumber;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAYeniSertifikaDurumu;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAYeniSertifikaDurumu_durum;


public class EESYAYeniSertifikaDurumu extends BaseASNWrapper<ESYAYeniSertifikaDurumu> implements ExtensionType {


    public EESYAYeniSertifikaDurumu(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYAYeniSertifikaDurumu());
    }

    public EESYAYeniSertifikaDurumu(boolean durumuAskida) throws Asn1ValueParseException {
        super(new ESYAYeniSertifikaDurumu(durumuAskida?ESYAYeniSertifikaDurumu_durum.askida():ESYAYeniSertifikaDurumu_durum.gecerli()));
    }

    public EESYAYeniSertifikaDurumu(ESYAYeniSertifikaDurumu aObject) {
        super(aObject);
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
		return new EExtension(EESYAOID.oid_cmpYeniSertifikaDurumu, aCritic, this);
	}

}
