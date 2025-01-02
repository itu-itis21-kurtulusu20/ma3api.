using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{
    using Element = XmlElement;

	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class Parameters : BaseConfigElement
	{
		private bool mWriteReferencedValidationDataToFileOnUpgrade;
		private bool mAddTimestampValidationData;

        public Parameters()
        {
            
        }

		public Parameters(Element aElement) : base(aElement)
		{
			mWriteReferencedValidationDataToFileOnUpgrade = getParamBoolean(ConfigConstants.TAG_WRITE_REFERENCED_PKI_DATA);
			mAddTimestampValidationData = getParamBoolean(ConfigConstants.TAG_ADD_TIMESTAMP_PKI_DATA);
		}

		public virtual bool WriteReferencedValidationDataToFileOnUpgrade
		{
			get
			{
				return mWriteReferencedValidationDataToFileOnUpgrade;
			}
            set { mWriteReferencedValidationDataToFileOnUpgrade = value; }
		}

		public virtual bool AddTimestampValidationData
		{
			get
			{
				return mAddTimestampValidationData;
			}
			set
			{
				mAddTimestampValidationData = value;
			}
		}


	}

}