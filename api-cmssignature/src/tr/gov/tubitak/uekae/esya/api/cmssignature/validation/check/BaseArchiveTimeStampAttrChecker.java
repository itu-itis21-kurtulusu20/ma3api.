package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;

/**
 * Created by sura.emanet on 11.07.2019.
 */
public abstract class BaseArchiveTimeStampAttrChecker extends BaseChecker{

    public abstract boolean checkOneTimeStampAttr(EAttribute attr, Signer aSigner, CheckerResult aCheckerResult);

    public static BaseArchiveTimeStampAttrChecker getTSCheckerAccordingToGivenOID(Asn1ObjectIdentifier tsOid){

        BaseArchiveTimeStampAttrChecker checker;

        if(tsOid.equals(AttributeOIDs.id_aa_ets_archiveTimestamp)){
            checker = new ArchiveTimeStampAttrChecker();
        }else if(tsOid.equals(AttributeOIDs.id_aa_ets_archiveTimestampV2)){
            checker = new ArchiveTimeStampV2AttrChecker();
        }else if(tsOid.equals(AttributeOIDs.id_aa_ets_archiveTimestampV3)){
            checker = new ArchiveTimeStampV3AttrChecker();
        }else{
            throw new IllegalArgumentException("Error in given oid");
        }

        return checker;
    }
}
