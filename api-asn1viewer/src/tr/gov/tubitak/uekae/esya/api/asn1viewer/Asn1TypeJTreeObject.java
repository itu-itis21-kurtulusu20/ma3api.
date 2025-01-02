package tr.gov.tubitak.uekae.esya.api.asn1viewer;

/**
 * Created by orcun.ertugrul on 06-Oct-17.
 */
class Asn1TypeJTreeObject
{
    protected  Class mAsn1Type;
    protected String mName;

    public Asn1TypeJTreeObject(Class asn1Type, String name)
    {
        mAsn1Type = asn1Type;
        mName = name;
    }

    public Class getAsn1Type()
    {
        return mAsn1Type;
    }

    public String getName()
    {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }
}
