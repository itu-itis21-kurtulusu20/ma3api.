package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.ocsp.CertID;

import java.math.BigInteger;

/**
 * @author ayetgin
 */
public class ECertID extends BaseASNWrapper<CertID>
{
    public ECertID(CertID aObject)
    {
        super(aObject);
    }

    public EAlgorithmIdentifier getHashAlgorithm(){
        return new EAlgorithmIdentifier(mObject.hashAlgorithm);
    }

    public byte[] getIssuerNameHash(){
        return mObject.issuerNameHash.value;
    }

    public byte[] getIssuerKeyHash(){
        return mObject.issuerKeyHash.value;
    }

    public BigInteger getSerialNumber(){
        return mObject.serialNumber.value;
    }

    public String getSerialNumberInHex(){
        return StringUtil.toString(mObject.serialNumber.value.toByteArray());
    }

    public void setHashAlgorithm(EAlgorithmIdentifier aAlgorithmIdentifier){
        mObject.hashAlgorithm = aAlgorithmIdentifier.getObject();
    }

    public void setIssuerNameHash(byte[] aValue){
        mObject.issuerNameHash = new Asn1OctetString(aValue);
    }

    public void setIssuerKeyHash(byte[] aValue){
        mObject.issuerKeyHash = new Asn1OctetString(aValue);
    }

    public void setSerialNumber(BigInteger aSerialNumber){
        mObject.serialNumber = new Asn1BigInteger(aSerialNumber);
    }

    public String toString(){
        return getSerialNumberInHex();
    }

}
