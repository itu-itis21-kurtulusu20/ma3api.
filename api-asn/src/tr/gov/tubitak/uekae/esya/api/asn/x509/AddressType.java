package tr.gov.tubitak.uekae.esya.api.asn.x509;

public enum AddressType
{
    LDAP("ldap"),
    HTTP("http"),
    DN ("dn");

    private String mValue;

    private AddressType(String aValue)
    {
        mValue = aValue;
    }

    public String asString()
    {
        return mValue;
    }
}
