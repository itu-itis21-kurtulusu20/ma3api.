package tr.gov.tubitak.uekae.esya.api.common;

import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;

/**
 * @author ayetgin
 */

public class OID
{

    private int[] mValue;
    private String mStringValue;

    public OID(int[] aValue)
    {
        mValue = aValue;
        mStringValue = OIDUtil.concat(mValue);
    }

    public int[] getValue()
    {
        return mValue;
    }

    public static OID parse(String str){
        return new OID(OIDUtil.parse(str));
    }

    public static OID fromURN(String str){
        return new OID(OIDUtil.fromURN(str));
    }

    @Override
    public String toString()
    {
        return mStringValue;
    }

    @Override
    public int hashCode()
    {
        return mStringValue.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj != null &&  obj instanceof OID){
            return mStringValue.equals(obj.toString());
        }
        return super.equals(obj);
    }
}
