package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1IA5String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

/**
 * @author ayetgin
 */
public class EFreshestCRL
        extends BaseASNWrapper<FreshestCRL>
        implements ExtensionType
{
    public EFreshestCRL(FreshestCRL aObject)
    {
        super(aObject);
    }

    public EFreshestCRL(String[] aCdps) throws ESYAException
    {
        super(new FreshestCRL());
        DistributionPoint[] distPoints = new DistributionPoint[1];
        GeneralName[] genNameArray = new GeneralName[aCdps.length];

        for (int i = 0; i < aCdps.length; i++) {
            genNameArray[i] = new GeneralName();
            genNameArray[i].set_uniformResourceIdentifier(new Asn1IA5String(aCdps[i]));
        }

        GeneralNames genNames = new GeneralNames(genNameArray);

        DistributionPointName dpn = new DistributionPointName();
        dpn.set_fullName(genNames);

        distPoints[0] = new DistributionPoint();
        distPoints[0].distributionPoint = dpn;

        mObject = new FreshestCRL(distPoints);
    }


    public EExtension toExtension(boolean aCritic) throws ESYAException
    {
        return new EExtension(EExtensions.oid_ce_freshestCRL, aCritic, this);
    }


}
