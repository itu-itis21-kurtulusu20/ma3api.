package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1ValueParseException;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACardSerialNumber;


public class EESYACardSerialNumber extends BaseASNWrapper<ESYACardSerialNumber> implements ExtensionType {
	

    public EESYACardSerialNumber(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYACardSerialNumber());
    }

    public EESYACardSerialNumber(String value) throws Asn1ValueParseException {
        super(new ESYACardSerialNumber(value));
    }

    public EESYACardSerialNumber(ESYACardSerialNumber aObject) {
        super(aObject);
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
		return new EExtension(EESYAOID.oid_ESYA_CardSerialNumber, aCritic, this);
	}

}
