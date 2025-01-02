using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.asn.cms;


namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks signature of timestamp response
 * It uses checkers CryptoChecker,MessageDigestAttrChecker and SigningCertificateAttrChecker to check
  - 
  -
  -
 * @author aslihan.kubilay
 *
 */
    public class TimeStampSignatureChecker : BaseChecker
    {
        private readonly ESignedData mSignedData = null;

        public TimeStampSignatureChecker(ESignedData aSD)
        {
            mSignedData = aSD;
        }
        
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.TIMESTAMP_SIGNATURE_CHECKER), typeof(TimeStampSignatureChecker));

            CryptoChecker sigChecker = new CryptoChecker();
            MessageDigestAttrChecker mdAttrChecker = new MessageDigestAttrChecker();
            SigningCertificateAttrChecker scChecker = new SigningCertificateAttrChecker();


            List<Checker> checkers = new List<Checker>();
            checkers.Add(sigChecker);
            checkers.Add(mdAttrChecker);
            checkers.Add(scChecker);
		    
            ESignerInfo signerInfo = mSignedData.getSignerInfo(0);

            Signer signer = null;
            try
            {
                ESignatureType type = SignatureParser.parse(signerInfo, false);
                EContentInfo ci = new EContentInfo(new ContentInfo());
                ci.setContentType(new Asn1ObjectIdentifier(_cmsValues.id_signedData));
                ci.setContent(mSignedData.getEncoded());

                signer = ESignatureType.createSigner(type, new BaseSignedData(ci), signerInfo);
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_SIGNATURE_CHECKER_UNSUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                return false;
            }

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[AllEParameters.P_SIGNED_DATA] = mSignedData;
            ECertificate signerCertificate = signer.getSignerCertificate();
            if (signerCertificate != null)
                params_[AllEParameters.P_SIGNING_CERTIFICATE] = signerCertificate;
            else
                params_[AllEParameters.P_SIGNING_CERTIFICATE] = mSignedData.getCertificates()[0];



            bool allResult = true;
            foreach (Checker checker in checkers)
            {
                checker.setParameters(params_);
                CheckerResult cresult = new CheckerResult();
                bool result = checker.check(signer, cresult);
                allResult = allResult && result;
                aCheckerResult.addCheckerResult(cresult);
            }

            if (!allResult)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_SIGNATURE_CHECKER_UNSUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
            }
            else
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_SIGNATURE_CHECKER_SUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            }

            return allResult;
        }
    }
}
