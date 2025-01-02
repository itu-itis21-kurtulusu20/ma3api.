package tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;

/**
 * A meta class between the XML class items in the validation policy file and
 * Validation System Classes(Checker, Finder, Matcher ). It stores the name and
 * the parameters of the Validation System classes. ClassLoader creates
 * corresponding validation system classes from PolicyClassInfo objects 
 *
 * @author IH
 */
public class PolicyClassInfo {

    private String mClassName;
    private ParameterList mParameters;

    public PolicyClassInfo()
    {
    }

    public PolicyClassInfo(String aClassName)
    {
        mClassName = aClassName;
    }

    public PolicyClassInfo(String aClassName, ParameterList aParameters)
    {
        mClassName = aClassName;
        mParameters = aParameters;
    }

    public String getClassName()
    {
        return mClassName;
    }

    public ParameterList getParameters()
    {
        return mParameters;
    }
}
