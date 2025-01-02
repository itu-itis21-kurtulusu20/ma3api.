package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.asn.x509.AccessDescription;

/**
 * @author ayetgin
 */
public class EAccessDescription extends BaseASNWrapper<AccessDescription>
{
    public EAccessDescription(AccessDescription aObject)
    {
        super(aObject);
    }

    public Asn1ObjectIdentifier getAccessMethod(){
        return mObject.accessMethod;
    }

    public EGeneralName getAccessLocation(){
        return new EGeneralName(mObject.accessLocation);
    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        Asn1ObjectIdentifier method = getAccessMethod();
        String methodStr = "";
        if (Constants.EXP_ID_AD_CAISSUERS.equals(method)){
            methodStr = "CA Issuer ";
        }
        else if (Constants.EXP_ID_AD_OCSP.equals(method)){
            methodStr = "OCSP ";
        }
        buffer.append("Access Description {\n")
                .append("\tmethod : ").append(methodStr).append(method).append("\n")
                .append("\taddress : ").append(getAccessLocation()).append("\n")
                .append("}\n");
        return buffer.toString();
    }
}
