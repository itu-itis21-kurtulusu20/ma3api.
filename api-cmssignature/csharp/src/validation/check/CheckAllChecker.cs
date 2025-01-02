using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;


namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{

    /**
     * Birden fazla checker verildigi zaman, hepsini ayri ayri kontrol eder.
     * Arsiv imzalarinda cadesc ve referans zaman damgalarinin ikiside bulunabilir,bulunmayabilir
     * ya da sadece biri bulunabilir. 
     * @author aslihanu
     *
     */
    public class CheckAllChecker : BaseChecker
    {
        readonly List<Checker> mCheckerList = null;
        readonly bool mIsOneCheckerMust = false;

        public CheckAllChecker(List<Checker> aCheckerList, bool aIsOneCheckerMust)
        {
            mCheckerList = aCheckerList;
            mIsOneCheckerMust = aIsOneCheckerMust;
        }

        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            //aCheckerResult.setCheckerName("Check All Checker");
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.CHECK_ALL_CHECKER), typeof(CheckAllChecker));

            bool oneFound = false;
            bool totalResult = true;
            foreach (Checker checker in mCheckerList)
            {
                CheckerResult cr = new CheckerResult();
                bool sonuc = checker.check(aSigner, cr);
                if (cr.getResultStatus() != Types.CheckerResult_Status.NOTFOUND)
                {
                    oneFound = true;
                    aCheckerResult.addCheckerResult(cr);
                    totalResult = totalResult && sonuc;
                }
            }

            if (mIsOneCheckerMust && !oneFound)
            {
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_MUST_ATTRIBUTE_IN_SIGNED_DATA)));
                return false;

            }
            else if (!mIsOneCheckerMust && !oneFound)
            {
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_OPTIONAL_ATTRIBUTE_IN_SIGNED_DATA)));
                return true;
            }
            else
            {
                if (totalResult)
                {
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ALL_CHECKERS_SUCCESSFULL)));
                }
                else
                {
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ALL_CHECKERS_UNSUCCESSFULL)));
                }

                return totalResult;
            }

        }

        public override void setParameters(Dictionary<String, Object> aParameters)
        {
            foreach (Checker checker in mCheckerList)
            {
                checker.setParameters(aParameters);
            }
        }
    }
}
