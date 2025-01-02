using System;
using System.Collections.Generic;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{
	//using Validator = tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.Validator;
	using SignatureType = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


    using Element = XmlElement;

	/// <summary>
	/// @author ahmety
	/// date: Dec 9, 2009
	/// </summary>
	public class SignatureProfileValidationConfig : BaseConfigElement
	{
		private readonly IList<Type> mValidators = new List<Type>(0);
		private readonly SignatureType ? mSignType=null;
		private readonly SignatureType ? mInheritValidatorsFrom=null;

        public SignatureProfileValidationConfig(SignatureType aType, SignatureType? aInheritValidatorsFrom, IList<Type> aValidators)
        {
            mValidators = aValidators;
            mSignType = aType;
            mInheritValidatorsFrom = aInheritValidatorsFrom;
        }

		public SignatureProfileValidationConfig(Element aElement) : base(aElement)
		{

			if (aElement != null)
			{
                string typeStr = aElement.GetAttribute(ConfigConstants.ATTR_TYPE);

				try
				{
                    mSignType = (SignatureType)Enum.Parse(typeof(SignatureType), typeStr);
				}
				catch (System.ArgumentException exc)
				{
					throw new ConfigurationException(exc,"config.invalidProfile", typeStr);
				}


				string inheritStr = aElement.GetAttribute(ConfigConstants.ATTR_INHERIT_VALIDATORS);
				if ((inheritStr != null) && (inheritStr.Length > 0))
				{
					try
					{
                        mInheritValidatorsFrom = (SignatureType)Enum.Parse(typeof(SignatureType), inheritStr);
					}
					catch (System.ArgumentException exc)
					{
						throw new ConfigurationException(exc,"config.invalidProfile", inheritStr);
					}
				}

				Element[] validatorElms = XmlCommonUtil.selectNodes(aElement.FirstChild, Constants.NS_MA3, ConfigConstants.TAG_VALIDATOR);
				if (validatorElms != null)
				{
					foreach (Element validatorElm in validatorElms)
					{
						string validatorclass = validatorElm.GetAttribute(ConfigConstants.ATTR_CLASS);
						if ((validatorclass != null) && (validatorclass.Length > 0))
						{
							try
							{
								Type clazz = Type.GetType(validatorclass);
							    if (!typeof(Validator).IsAssignableFrom(clazz))
								{
									throw new ConfigurationException("config.invalidValidator", validatorclass, mSignType);
								}
								mValidators.Add(clazz);
							}
							catch (Exception exc)
							{
								throw new ConfigurationException(exc,"config.validatorClassNotFound", validatorclass);
							}
						}
					}
				}
			}
		}


		public virtual SignatureType ? SignType
		{
			get
			{
				return mSignType;
			}
		}

		public virtual SignatureType ? InheritValidatorsFrom
		{
			get
			{
				return mInheritValidatorsFrom;
			}
		}

		public virtual IList<Type> Validators
		{
			get
			{
				return mValidators;
			}
		}

		public virtual IList<Validator> createValidators()
		{
			IList<Validator> list = new List<Validator>(mValidators.Count);
			for (int i = 0; i < mValidators.Count; i++)
			{
				Type aClass = mValidators[i];
				try
				{
					list.Add((Validator)Activator.CreateInstance(aClass));
				}
				catch (Exception exc)
				{
					throw new XMLSignatureException(exc,"core.cantInstantiateValidatorClass");
				}
			}
			return list;
		}
	}

}