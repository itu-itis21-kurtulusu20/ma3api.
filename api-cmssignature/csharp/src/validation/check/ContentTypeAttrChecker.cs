using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;


namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks if the content-type attribute has its value (i.e. ContentType) equal to the eContentType of the EncapsulatedContentInfo 
 * value being signed
 * @author aslihan.kubilay
 *
 */
    public class ContentTypeAttrChecker : BaseChecker
    {
        //As stated in RFC 3852 [4], the content-type attribute must have its value (i.e. ContentType) equal to the
        //eContentType of the EncapsulatedContentInfo value being signed. (ETSI TS 101 733 V1.7.4 5.7.1)
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.CONTENT_TYPE_ATTRIBUTE_CHECKER), typeof(ContentTypeAttrChecker));
            ESignedData signedData = (ESignedData)getParameters()[AllEParameters.P_SIGNED_DATA];

            List<EAttribute> ctAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_contentType);

            if (ctAttrs.Count == 0)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_CONTENT_TYPE_ATTRIBUTE_IN_SIGNED_DATA)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                return false;
            }

            EAttribute ctAttr = ctAttrs[0];

            Asn1ObjectIdentifier oid = new Asn1ObjectIdentifier();
            try
            {
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(ctAttr.getValue(0));
                oid.Decode(decBuf);
            }
            catch (Exception tEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CONTENT_TYPE_ATTRIBUTE_DECODE_ERROR), tEx));
                return false;
            }

            if (!signedData.getEncapsulatedContentInfo().getContentType().Equals(oid))
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CONTENT_TYPE_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
                return false;
            }
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CONTENT_TYPE_ATTRIBUTE_CHECKER_SUCCESSFUL)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            return true;
        }

    }
}
