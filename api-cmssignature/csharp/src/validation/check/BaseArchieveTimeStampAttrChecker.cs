using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.src.validation.check
{
   
    public abstract class BaseArchieveTimeStampAttrChecker : BaseChecker
    {
       public abstract bool checkOneTimeStampAttr(EAttribute attr, Signer aSigner, CheckerResult aCheckerResult);

       public static BaseArchieveTimeStampAttrChecker getTSCheckerAccordingToGivenOID(Asn1ObjectIdentifier tsOid)
       {
           BaseArchieveTimeStampAttrChecker checker;

           if (tsOid.Equals(AttributeOIDs.id_aa_ets_archiveTimestamp))
           {
               checker = new ArchiveTimeStampAttrChecker();
           }
           else if (tsOid.Equals(AttributeOIDs.id_aa_ets_archiveTimestampV2))
           {
               checker = new ArchiveTimeStampV2AttrChecker();
           }
           else if (tsOid.Equals(AttributeOIDs.id_aa_ets_archiveTimestampV3))
           {
               checker = new ArchiveTimeStampV3AttrChecker();
           }
           else
           {
               throw new ArgumentException("Error in given oid");
           }

           return checker;
       }
    }
}
