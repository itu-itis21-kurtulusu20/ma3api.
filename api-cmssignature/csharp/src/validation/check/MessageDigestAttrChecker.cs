using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public class MessageDigestAttrChecker : BaseChecker
    {
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.MESSAGE_DIGEST_ATTRIBUTE_CHECKER), typeof(MessageDigestAttrChecker));
            ESignedData signedData = (ESignedData)getParameters()[AllEParameters.P_SIGNED_DATA];
            Object parentSignerInfo = null;
            getParameters().TryGetValue(AllEParameters.P_PARENT_SIGNER_INFO, out parentSignerInfo);

            List<EAttribute> mdAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_messageDigest);
            if (mdAttrs.Count == 0)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_MESSAGE_DIGEST_ATTRIBUTE_FOUND)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                return false;
            }

            EAttribute mdAttr = mdAttrs[0];

            Asn1OctetString octetS = new Asn1OctetString();
            try
            {
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(mdAttr.getValue(0));
                octetS.Decode(decBuf);
            }
            catch (Exception tEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.MESSAGE_DIGEST_ATTRIBUTE_DECODE_ERROR), tEx));
                return false;
            }

            byte[] contentDigest;
            DigestAlg digestAlg = DigestAlg.fromOID(aSigner.getSignerInfo().getDigestAlgorithm().getAlgorithm().mValue);
            if (digestAlg == null)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNER_DIGEST_ALGORITHM_UNKNOWN)));
                return false;
            }

            try
            {
                if (parentSignerInfo != null && parentSignerInfo is ESignerInfo)
                {
                    contentDigest = DigestUtil.digest(digestAlg, ((ESignerInfo)parentSignerInfo).getSignature());
                }
                else if (signedData.getEncapsulatedContentInfo().getContent() != null)
                {
                    contentDigest = DigestUtil.digest(digestAlg, signedData.getEncapsulatedContentInfo().getContent());
                }
                else
                {

                    ISignable contentI = null;
                    Object contentO;

                    if (getParameters().ContainsKey(AllEParameters.P_EXTERNAL_CONTENT) && getParameters()[AllEParameters.P_EXTERNAL_CONTENT] != null)
                    {
                        contentO = getParameters()[AllEParameters.P_EXTERNAL_CONTENT];
                    }
                    else if (getParameters().ContainsKey(AllEParameters.P_CONTENT) && getParameters()[AllEParameters.P_CONTENT] != null)
                    {
                        contentO = getParameters()[AllEParameters.P_CONTENT];
                    }
                    else
                    {
                        aCheckerResult.addMessage(new ValidationMessage("Imzalanan veri parametresi null"));
                        return false;
                    }

                    try
                    {
                        contentI = (ISignable)contentO;
                        contentDigest = contentI.getMessageDigest(digestAlg);
                    }
                    catch (InvalidCastException ex)
                    {
                        aCheckerResult.addMessage(new ValidationMessage("The external content object is not in the type of ISignable.", ex));
                        return false;
                    }
                    catch (IOException e)
                    {
                        aCheckerResult.addMessage(new ValidationMessage("External can not be read", e));
                        return false;
                    }
                }
            }
            catch (CryptoException ex)
            {
                aCheckerResult.addMessage(new ValidationMessage("Ozet kontrolu yapilirken hata olustu.", ex));
                return false;
            }


            bool isMatching = false;
            try
            {
                //isMatching = _checkDigest(octetS.mValue, content, digestAlg);
                //isMatching = Arrays.equals(octetS.mValue, contentDigest);
                isMatching = octetS.mValue.SequenceEqual(contentDigest);
            }
            catch (Exception tEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.MESSAGE_DIGEST_CHECKER_ERROR), tEx));
                return false;
            }
            //}

            if (!isMatching)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.MESSAGE_DIGEST_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
                return false;
            }

            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.MESSAGE_DIGEST_ATTRIBUTE_CHECKER_SUCCESSFUL)));
            return true;
        }
    }
}
