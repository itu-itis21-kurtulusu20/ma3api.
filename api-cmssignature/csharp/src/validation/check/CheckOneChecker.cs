using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Birden fazla checker verildigi zaman yapida hangisi varsa,onu kontrol eder.
 * SigningCertificate ve SigningCertificateV2 checker larindan hangisi ile ilgili ozellik varsa,o
 * checker i cagirir
 * @author aslihanu
 *
 */
    public class CheckOneChecker : BaseChecker
    {
        private readonly List<Checker> mCheckerList = null;

        public CheckOneChecker(List<Checker> aCheckerList)
        {
            mCheckerList = aCheckerList;
        }


        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {

            foreach (Checker checker in mCheckerList)
            {
                bool sonuc = checker.check(aSigner, aCheckerResult);
                Types.CheckerResult_Status status = aCheckerResult.getResultStatus();

                if (status == Types.CheckerResult_Status.NOTFOUND)
                    continue;
                else
                    return sonuc;
            }

            aCheckerResult.setCheckerName(Msg.getMsg(Msg.CHECK_ONE_CHECKER), typeof(CheckOneChecker));
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_CHECKER_SUCCESSFULL)));
            return false;
        }
    }
}
