package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponderID;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 12/23/11 - 8:44 AM <p>
 * <b>Description</b>: <br>
 */
public class EResponderID extends BaseASNWrapper<ResponderID> {

    public final static byte _BYNAME = 1;
    public final static byte _BYKEY = 2;

    public EResponderID(ResponderID aObject) {
        super(aObject);
    }

    public EResponderID(byte[] aBytes) throws ESYAException {
        super(aBytes, new ResponderID());
    }

    public EResponderID(EName name) throws ESYAException {
        super(new ResponderID());
        mObject.set_byName(name.getObject());
    }

    public EResponderID(Asn1OctetString name) throws ESYAException {
        super(new ResponderID());
        mObject.set_byKey(name);
    }

    public int getType(){
        return mObject.getChoiceID();
    }

    public EName getResponderIdByName(){
        return new EName((Name)mObject.getElement());
    }

    public byte[] getResponderIdByKey(){
        return ((Asn1OctetString)mObject.getElement()).value;
    }




}
