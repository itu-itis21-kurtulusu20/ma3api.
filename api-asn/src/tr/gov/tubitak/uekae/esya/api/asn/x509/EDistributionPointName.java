package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.x509.DistributionPointName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralNames;
import tr.gov.tubitak.uekae.esya.asn.x509.RelativeDistinguishedName;

/**
 * @author ahmety
 * date: Feb 17, 2010
 */
public class EDistributionPointName extends BaseASNWrapper<DistributionPointName>
{

    public EDistributionPointName(DistributionPointName aObject)
    {
        super(aObject);
    }

    public EDistributionPointName(EGeneralNames generalNames) {
        super(new DistributionPointName());
        mObject.set_fullName(generalNames.getObject());
    }

    public EDistributionPointName(ERelativeDistinguishedName distinguishedName) {
        super(new DistributionPointName());
        mObject.set_nameRelativeToCRLIssuer(distinguishedName.getObject());
    }

    public int getType(){
        return mObject.getChoiceID();
    }

    public EGeneralNames getFullName(){
        if (getType()==DistributionPointName._FULLNAME)
            return new EGeneralNames((GeneralNames)mObject.getElement());
        return null;
    }

    public RelativeDistinguishedName getNameRelativeToCRLIssuer(){
        if (getType()==DistributionPointName._NAMERELATIVETOCRLISSUER)
            return (RelativeDistinguishedName)mObject.getElement();
        return null;

    }
}
