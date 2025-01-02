package tr.gov.tubitak.uekae.esya.api.infra.crl.crlincache;

import com.objsys.asn1j.runtime.Asn1Type;
import com.objsys.asn1j.runtime.Asn1BerInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: zeldalo
 * Date: 12.May.2009
 * Time: 11:26:51
 * To change this template use File | Settings | File Templates.
 */
interface ISignChecker {
    public void collect(Asn1Type aAsn);
    public void collect(byte[] data);
    public boolean verify();
}
