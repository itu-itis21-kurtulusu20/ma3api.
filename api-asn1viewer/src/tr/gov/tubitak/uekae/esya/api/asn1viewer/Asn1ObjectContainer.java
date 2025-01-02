package tr.gov.tubitak.uekae.esya.api.asn1viewer;

import com.objsys.asn1j.runtime.Asn1Type;

/**
 * Created by orcun.ertugrul on 31-Jan-18.
 */
public class Asn1ObjectContainer
{
    public Asn1Type mObject;
    public String mType;

    public Asn1ObjectContainer(Asn1Type object)
    {
        mObject = object;
    }

    public void setType(String type)
    {
        mType = type;
    }



}
