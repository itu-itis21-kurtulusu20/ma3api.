package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1ValueParseException;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ExtensionType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYATekIstekHatadaTumIstekGeriAl;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 20.06.2013
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class EESYATekIstekHatadaTumIstekGeriAl extends BaseASNWrapper<ESYATekIstekHatadaTumIstekGeriAl> implements ExtensionType {
    public EESYATekIstekHatadaTumIstekGeriAl(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYATekIstekHatadaTumIstekGeriAl());
    }

    public EESYATekIstekHatadaTumIstekGeriAl(boolean boolIstekGeriAl) throws Asn1ValueParseException {
        super(new ESYATekIstekHatadaTumIstekGeriAl(boolIstekGeriAl));
    }

    public EESYATekIstekHatadaTumIstekGeriAl(ESYATekIstekHatadaTumIstekGeriAl aObject) {
        super(aObject);
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EESYAOID.oid_cmpTekIstekHatadaTumIstekGeriAl, aCritic, this);
    }
}
