package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.Validator;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.*;


/**
 * @author ahmety
 * date: Dec 9, 2009
 */
public class SignatureProfileValidationConfig extends BaseConfigElement
{
    private List<Class<? extends Validator>> mValidators = new ArrayList<Class<? extends Validator>>(0);
    private SignatureType mType;
    private SignatureType mInheritValidatorsFrom;


    public SignatureProfileValidationConfig(SignatureType aType, SignatureType aInheritValidatorsFrom, List<Class<? extends Validator>> aValidators)
    {
        mValidators = aValidators;
        mType = aType;
        mInheritValidatorsFrom = aInheritValidatorsFrom;
    }

    @SuppressWarnings("unchecked")
    public SignatureProfileValidationConfig(Element aElement) throws tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException
    {
        super(aElement);

        if (aElement!=null){
            String typeStr = aElement.getAttribute(ATTR_TYPE);

            try {
                mType = SignatureType.valueOf(typeStr);
            } catch (IllegalArgumentException e){
                throw new ConfigurationException("config.invalidProfile", typeStr, e);
            }


            String inheritStr = aElement.getAttribute(ATTR_INHERIT_VALIDATORS);
            if ((inheritStr!=null) && (inheritStr.length()>0)){
                try {
                    mInheritValidatorsFrom = SignatureType.valueOf(inheritStr);
                } catch (IllegalArgumentException e){
                    throw new ConfigurationException("config.invalidProfile", inheritStr, e);
                }
            }

            Element[] validatorElms = XmlUtil.selectNodes(aElement.getFirstChild(), Constants.NS_MA3, TAG_VALIDATOR);
            if (validatorElms!=null)
            {
                for (Element validatorElm : validatorElms)
                {
                    String validatorclass = validatorElm.getAttribute(ATTR_CLASS);
                    if ((validatorclass!=null) && (validatorclass.length()>0))
                    {
                        try {
                            Class clazz = Class.forName(validatorclass);
                            if (!Validator.class.isAssignableFrom(clazz)){
                                throw new ConfigurationException("config.invalidValidator", validatorclass, mType);
                            }
                            mValidators.add(clazz);
                        } catch (ClassNotFoundException e){
                            throw new ConfigurationException("config.validatorClassNotFound", validatorclass, e);
                        }
                    }
                }
            }
        }
    }


    public SignatureType getType()
    {
        return mType;
    }

    public SignatureType getInheritValidatorsFrom()
    {
        return mInheritValidatorsFrom;
    }

    public List<Class<? extends Validator>> getValidators()
    {
        return mValidators;
    }

    public List<Validator> createValidators() throws XMLSignatureException
    {
        List<Validator> list = new ArrayList<Validator>(mValidators.size());
        for (int i = 0; i < mValidators.size(); i++) {
            Class<? extends Validator> aClass = mValidators.get(i);
            try {
                list.add(aClass.newInstance());
            } catch (Exception e){
                throw new XMLSignatureException("core.cantInstantiateValidatorClass", e);
            }
        }
        return list;
    }
}
