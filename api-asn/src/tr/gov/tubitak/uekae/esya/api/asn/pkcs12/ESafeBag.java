package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.PKCS12Attribute;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.PKCS12Attribute_attrValues;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.SafeBag;
import tr.gov.tubitak.uekae.esya.asn.pkcs12._SetOfPKCS12Attribute;
import tr.gov.tubitak.uekae.esya.asn.pkcs9._pkcs9Values;
import tr.gov.tubitak.uekae.esya.asn.util.UtilOpenType;

import java.io.IOException;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:12 AM
 */
public class ESafeBag extends BaseASNWrapper<SafeBag>{
    public ESafeBag(byte[] aBytes) throws ESYAException {
        super(aBytes, new SafeBag());
    }

    public ESafeBag( Asn1ObjectIdentifier bagId, Asn1OpenType bagValue, EPKCS12Attribute[] attributes) {
        super(new SafeBag(bagId,bagValue));
        PKCS12Attribute[] elements = new PKCS12Attribute[attributes.length];
        for (int i = 0; i < attributes.length; i++)
            elements[i]=attributes[i].getObject();
        mObject.bagAttributes = new _SetOfPKCS12Attribute(elements);
    }

    public ESafeBag( Asn1ObjectIdentifier bagId, Asn1OpenType bagValue, int keyID) throws IOException, Asn1Exception {
        super(new SafeBag(bagId, bagValue));

        PKCS12Attribute_attrValues v = new PKCS12Attribute_attrValues(
                new Asn1OpenType[]{
                        UtilOpenType.toOpenType(new Asn1OctetString(new byte[]{(byte) (keyID + 1), 0, 0, 0}))
                });
    	PKCS12Attribute at = new PKCS12Attribute(_pkcs9Values.pkcs_9_at_localKeyId, v);
    	mObject.bagAttributes = new _SetOfPKCS12Attribute(new PKCS12Attribute[]{at});
    }

    public ESafeBag( Asn1ObjectIdentifier bagId, ECertBag certBag, int keyID) throws IOException, Asn1Exception {
        this(bagId, UtilOpenType.toOpenType(certBag),keyID);
    }

    public ESafeBag( Asn1ObjectIdentifier bagId, EPKCS8ShroudedKeyBag shroudedKeyBag, int keyID) throws IOException, Asn1Exception {
        this(bagId, UtilOpenType.toOpenType(shroudedKeyBag), keyID);
    }
}
