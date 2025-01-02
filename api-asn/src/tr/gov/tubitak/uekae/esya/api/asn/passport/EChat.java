package tr.gov.tubitak.uekae.esya.api.asn.passport;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.passport.Chat;


/**
 * Created by ahmet.asa on 31.07.2017.
 */
public class EChat extends BaseASNWrapper<Chat> {

    public EChat() {
        super(new Chat());
    }

    public EChat(int[] algId, String value) throws ESYAException {
        super(new Chat());
        byte[] roleValue = StringUtil.hexToByte(value);
        setAlgId(algId);
        setValue(roleValue);
    }

    public void setAlgId(int[] algId) throws ESYAException {
        getObject().oid = new Asn1ObjectIdentifier(algId);
    }

    public void setValue(byte[] value) throws ESYAException {
        getObject().value = new Asn1OctetString(value);
    }

}
