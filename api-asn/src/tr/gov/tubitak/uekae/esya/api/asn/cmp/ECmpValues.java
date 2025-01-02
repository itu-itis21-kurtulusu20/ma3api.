package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.cmp._cmpValues;

/**
 * User: zeldal.ozdemir
 * Date: 2/3/11
 * Time: 3:16 PM
 */
public class ECmpValues extends BaseASNWrapper<Asn1ObjectIdentifier>{

    public static final ECmpValues oid_PasswordBasedMac  = new ECmpValues(_cmpValues.id_PasswordBasedMac);
    public static final ECmpValues oid_HMAC_SHA1         = new ECmpValues(_cmpValues.id_HMAC_SHA1);
    public static final ECmpValues oid_DHBasedMac        = new ECmpValues(_cmpValues.id_DHBasedMac);
    public static final ECmpValues oid_SuppLangTags      = new ECmpValues(_cmpValues.id_it_suppLangTags);

    public ECmpValues(int[] oid) {
        super(new Asn1ObjectIdentifier(oid));
    }

    public int[] getOID(){
        return mObject.value;
    }
}
