package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1IA5String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.AccessDescription;
import tr.gov.tubitak.uekae.esya.asn.x509.AuthorityInfoAccessSyntax;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ahmety
 *         date: Feb 5, 2010
 */
public class EAuthorityInfoAccessSyntax
        extends BaseASNWrapper<AuthorityInfoAccessSyntax>
        implements ExtensionType
{

    public EAuthorityInfoAccessSyntax(AuthorityInfoAccessSyntax aObject)
    {
        super(aObject);
    }

    public EAuthorityInfoAccessSyntax(byte[] aBytes) throws ESYAException {
        super(aBytes, new AuthorityInfoAccessSyntax());
    }

    public EAuthorityInfoAccessSyntax(int[][] metodlar, String[] yerler) throws ESYAException
    {
        super(null);

        if (metodlar.length != yerler.length)
            throw new ESYAException("Invalid AIA arguments");

        int s = metodlar.length;

        AccessDescription[] elem = new AccessDescription[s];
        GeneralName temp;

        for (int i = 0; i < s; i++) {
            temp = new GeneralName();
            temp.set_uniformResourceIdentifier(new Asn1IA5String(yerler[i]));
            elem[i] = new AccessDescription(metodlar[i], temp);
        }

        mObject = new AuthorityInfoAccessSyntax(elem);
    }

    public int getAccessDescriptionCount()
    {
        return mObject.getLength();
    }

    public EAccessDescription getAccessDescription(int aIndex)
    {
        return new EAccessDescription(mObject.elements[aIndex]);
    }

    public List<String> getCAIssuerAddresses()
    {
        return getAddressesByType(null);
    }

    public String getCAIssuerAddressesStr()
    {
        List<String> aialar = getCAIssuerAddresses();
        if (aialar == null || aialar.isEmpty()) {
            return "";
        }
        String aia = "";
        int count = 1;
        for (int i = 1; i < aialar.size(); i += 2) {
            aia = aia + " [" + (count++) + "]\n" + aialar.get(i) + "\n";
        }
        return aia;
    }

    public List<String> getLdapAddresses()
    {
        return getAddressesByType(AddressType.LDAP);
    }

    public List<String> getHttpAddresses()
    {
        return getAddressesByType(AddressType.HTTP);
    }


    private List<String> getAddressesByType(AddressType aAddressType)
    {
        if (mObject.elements == null) {
            return null;
        }
        List<String> adresler = new ArrayList<String>();
        for (AccessDescription ad : mObject.elements) {
            if (ad.accessMethod.equals(Constants.EXP_ID_AD_CAISSUERS)) {
                String adres = UtilName.generalName2String(ad.accessLocation);
                if (aAddressType == null || adres.startsWith(aAddressType.asString())) {
                    adresler.add(adres);
                }
            }
        }
        return adresler;
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_pe_authorityInfoAccess, aCritic, this);
    }

    @Override
    public String toString()
    {
        // todo localization
        StringBuilder buffer = new StringBuilder();
        int count = getAccessDescriptionCount();
        buffer.append("Authority Access Info {\n");
        for (int i=0; i<count; i++){
            buffer.append("[").append(i).append("] ");
            EAccessDescription accessDescription = getAccessDescription(i);
            buffer.append(accessDescription.toString());
        }
        buffer.append("}\n");
        return buffer.toString();
    }
/*
    public static void main(String[] args) throws Exception
    {
        ECertificate cert = ECertificate.readFromFile("C:\\ahmet\\desktop\\23_03_2010\\xml-sign\\etsi\\XAdES_plugtest_20090303_155731\\XAdES-A.SCOK\\AI\\1130070929206403879.crt");
        System.out.println(cert.getExtensions().getAuthorityInfoAccessSyntax().toString());
    }  */
}
