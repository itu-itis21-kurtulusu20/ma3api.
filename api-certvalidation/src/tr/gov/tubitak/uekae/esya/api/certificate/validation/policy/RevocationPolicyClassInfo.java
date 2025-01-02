package tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;

import java.util.List;

/**
 * A meta class between the XML class items in the validation policy file and
 * Validation System Classes(Checker, Finder, Matcher ). It stores the name and
 * the parameters of the Validation System classes. ClassLoader creates
 * corresponding validation system classes from PolicyClassInfo objects 
 *
 * @author IH
 */
public class RevocationPolicyClassInfo extends PolicyClassInfo
{
    private List<PolicyClassInfo> mFinders;

    public RevocationPolicyClassInfo(String aClassName, ParameterList aParameters, List<PolicyClassInfo> aFinders)
    {
        super(aClassName, aParameters);
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
        mFinders.add(iFinder);
    }

}
