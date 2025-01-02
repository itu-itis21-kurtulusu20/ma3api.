using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.policy
{
    /**
     * A meta class between the XML class items in the validation policy file and
     * Validation System Classes(Checker, Finder, Matcher ). It stores the name and
     * the parameters of the Validation System classes. ClassLoader creates
     * corresponding validation system classes from PolicyClassInfo objects 
     *
     * @author IH
     */
    public class RevocationPolicyClassInfo : PolicyClassInfo
    {
        private List<PolicyClassInfo> mFinders;


        public RevocationPolicyClassInfo(String aClassName, ParameterList aParameters, List<PolicyClassInfo> aFinders)
            : base(aClassName, aParameters)
        {
            mFinders = aFinders;
        }

        public List<PolicyClassInfo> getFinders()
        {
            return mFinders;
        }

        public void setFinders(List<PolicyClassInfo> iFinders)
        {
            mFinders = iFinders;
        }

        public void addFinder(PolicyClassInfo iFinder)
        {
            mFinders.Add(iFinder);
        }
    }
}