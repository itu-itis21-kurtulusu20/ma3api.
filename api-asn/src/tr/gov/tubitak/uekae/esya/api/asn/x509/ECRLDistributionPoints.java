package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1IA5String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ahmety
 * date: Feb 5, 2010
 */
public class ECRLDistributionPoints
        extends BaseASNWrapper<CRLDistributionPoints>
        implements ExtensionType
{
    private ECertificate mCertificate;

    @Deprecated
    public ECRLDistributionPoints(CRLDistributionPoints aObject, ECertificate aCertificate)
    {
        super(aObject);
        mCertificate = aCertificate;
    }

    public ECRLDistributionPoints(String[] cdps) throws ESYAException
    {
        super(null);
        DistributionPoint[] elem = new DistributionPoint[cdps.length];
        DistributionPointName temp;
        for (int i = 0; i < cdps.length; i++)
        {
            GeneralName[] genName = new GeneralName[1];
            genName[0] = new GeneralName();
            genName[0].set_uniformResourceIdentifier(new Asn1IA5String(cdps[i]));

            GeneralNames genNames = new GeneralNames(genName);

            temp = new DistributionPointName();
            temp.set_fullName(genNames);

            elem[i] = new DistributionPoint();
            elem[i].distributionPoint = temp;
        }

        mObject =  new CRLDistributionPoints(elem);
    }

    public ECRLDistributionPoints(byte[] bytes) throws ESYAException {
        super(bytes, new CRLDistributionPoints());
    }

    public int getCRLDistributionPointCount(){
        return mObject.getLength();
    }

    public ECRLDistributionPoint getCRLDistributionPoint(int aIndex){
        return new ECRLDistributionPoint(mObject.elements[aIndex], mCertificate);
    }

    public ECRLDistributionPoint getCRLDistributionPointWithoutResolve(int aIndex) {
        return new ECRLDistributionPoint(mObject.elements[aIndex]);
    }

    public List<String> getHttpAddresses(){
       return getAddressesByType(AddressType.HTTP);
    }

    public List<String> getLdapAddresses(){
       return getAddressesByType(AddressType.LDAP);
    }

    public List<String> getDnAddresses(){
       return getAddressesByType(AddressType.DN);
    }

    private List<String> getAddressesByType(AddressType aAddressType){
        List<String> addresses = new ArrayList<String>();
        for (int i=0; i<getCRLDistributionPointCount(); i++){
            ECRLDistributionPoint cdp = getCRLDistributionPoint(i);
            if (cdp.getAddressType().equals(aAddressType)){
                addresses.add(cdp.getAddress());
            }
        }
        return addresses;
    }

    public List<EName> getHttpIssuers(){
       return getIssuersByType(AddressType.HTTP);
    }

    public List<EName> getLdapIssuers(){
       return getIssuersByType(AddressType.LDAP);
    }

    public List<EName> getDnIssuers(){
       return getIssuersByType(AddressType.DN);
    }

    private List<EName> getIssuersByType(AddressType aAddressType){
        List<EName> issuers = new ArrayList<EName>();
        for (int i=0; i<getCRLDistributionPointCount(); i++){
            ECRLDistributionPoint cdp = getCRLDistributionPoint(i);
            if (cdp.getAddressType().equals(aAddressType)){
                issuers.add(cdp.getCRLIssuer());
            }
        }
        return issuers;
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_cRLDistributionPoints, aCritic, this);
    }

    public String toString(){
		if((mObject.elements == null) || (mObject.elements.length==0))
		{
			return "";
		}
		String str = "";
        int count = 1;
        for (int i = 0; i < getCRLDistributionPointCount(); i++)
        {
            ECRLDistributionPoint cdp = getCRLDistributionPoint(i);
             str += " [" + (count++) + "]\n" + cdp.getAddress() + "\n";
        }
        return str;
    }


}
