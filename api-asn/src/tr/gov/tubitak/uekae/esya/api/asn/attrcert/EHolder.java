package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralNames;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.Holder;
import tr.gov.tubitak.uekae.esya.asn.attrcert.IssuerSerial;

import java.math.BigInteger;

/**
 * User: zeldal.ozdemir
 * Date: 3/15/11
 * Time: 4:39 PM
 */
public class EHolder extends BaseASNWrapper<Holder> {
    public EHolder(Holder aObject) {
        super(aObject);
    }

    public EHolder() {
        super(new Holder());
    }

    public EHolder(byte[] aBytes) throws ESYAException {
        super(aBytes, new Holder());
    }

    public void setIssuerSerial(EGeneralNames issuer, BigInteger serial) {
        mObject.baseCertificateID = new IssuerSerial(issuer.getObject(), new Asn1BigInteger(serial));
    }


    public EGeneralNames getHolderIssuerName() {
        if (mObject.baseCertificateID == null)
            return null;
        else
            return new EGeneralNames(mObject.baseCertificateID.issuer);
    }

    public BigInteger getHolderIssuerSerial() {
        if (mObject.baseCertificateID == null)
            return null;
        else
            return mObject.baseCertificateID.serial.value;
    }

    public void setEntityName(EGeneralNames entityName) {
        mObject.entityName = entityName.getObject();
    }

    public EGeneralNames getEntityName() {
        if (mObject.entityName == null)
            return null;
        else
            return new EGeneralNames(mObject.entityName);
    }

}
