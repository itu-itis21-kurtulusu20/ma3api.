namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using Logger = log4net.ILog;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using ValidationResultType = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Reference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
	using SignedInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
	using XMLObject = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.XMLObject;
	using DataObjectFormat = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DataObjectFormat;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
	using SignedDataObjectProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
	using SignedProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;


	/// <summary>
	/// <h3>G.2.2.8 Checking DataObjectFormat</h3>
	/// 
	/// <p>The verifier should check that the <code>ObjectReference</code> element
	/// actually references one <code>ds:Reference</code> element from the signature.
	/// 
	/// <p>In addition, should this property refer to a <code>ds:Reference</code>
	/// that in turn refers to a <code>ds:Object</code>, the verifier should
	/// check the values of attributes <code>MimeType</code> and
	/// <code>Encoding</code> as indicated below:
	/// 
	/// <ul>
	/// <li>If any of the aforementioned attributes is present in both
	/// <code>xades:DataObjectFormat</code> and in <code>ds:Object</code> elements,
	/// the verifier should check that their values are equal.
	/// <li>If any of the aforementioned attributes is present in
	/// <code>xades:DataObjectFormat</code> but NOT in <code>ds:Object</code>,
	/// the verifier should act as if this attribute was present within the
	/// <code>ds:Object</code> with the same value.
	/// </ul>
	/// 
	/// <p>Additional rules governing the acceptance of the XAdES signature as
	/// valid or not in the view of the contents of this property are out of the
	/// scope of the present document.
	/// 
	/// @author ahmety
	/// date: Oct 1, 2009
	/// </summary>
	public class DataObjectFormatValidator : Validator
	{
		private static Logger logger = log4net.LogManager.GetLogger(typeof(DataObjectFormatValidator));

		public virtual ValidationResult validate(XMLSignature aSignature, ECertificate certificate)
		{
			QualifyingProperties qp = aSignature.QualifyingProperties;
			if (qp == null)
			{
				return null;
			}

			SignedProperties sp = qp.SignedProperties;
			if (sp == null)
			{
				return null;
			}

			// data object properties
			SignedDataObjectProperties sdop = sp.SignedDataObjectProperties;
			if (sdop == null)
			{
				return null;
			}

			for (int k = 0; k < sdop.DataObjectFormatCount; k++)
			{
				DataObjectFormat dof = sdop.getDataObjectFormat(k);
				string @ref = dof.ObjectReference;
				string referenceId = @ref.Substring(1);
				Reference found = null;
				SignedInfo si = aSignature.SignedInfo;
				for (int i = 0; i < si.ReferenceCount; i++)
				{
					Reference reference = si.getReference(i);
					if (reference.Id != null && reference.Id.Equals(referenceId))
					{
						found = reference;
						break;
					}
				}
				if (found == null)
				{
					string failMessage = I18n.translate("validation.dataObjectFormat.noReference");
					logger.Warn(failMessage);
					return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.dataObjectFormat"),
                                                failMessage, null, GetType());
				}

				//Document doc = Resolver.resolve(found, aSignature.getContext());

				// if object check mime
				if (found.URI.StartsWith("#"))
				{
					string id = found.URI.Substring(1);
					for (int n = 0; n < aSignature.ObjectCount; n++)
					{
						XMLObject obj = aSignature.getObject(n);
						if (id.Equals(obj.Id))
						{
							logger.Debug("Referenced data belongs to object ");
							string mime = obj.MIMEType;
							string expectedMime = dof.MIMEType;
							if (mime != null && !(mime.ToUpper().Equals(expectedMime.ToUpper())))
							{
								string failMessage = I18n.translate("validation.dataObjectFormat.mimeMismatch", expectedMime, mime);
								logger.Warn(failMessage);
								return new ValidationResult(ValidationResultType.INVALID,
                                                            failMessage,
                                                            I18n.translate("validation.check.dataObjectFormat"),
                                                            null, GetType());
							}

							string encoding = obj.Encoding;
							string expectedEncoding = dof.Encoding;
							if (encoding != null && !(encoding.ToUpper().Equals(expectedEncoding.ToUpper())))
							{
								string failMessage = I18n.translate("validation.dataObjectFormat.encodingMismatch", expectedEncoding, encoding);
								logger.Warn(failMessage);
								return new ValidationResult(ValidationResultType.INVALID,
                                                            failMessage,
                                                            I18n.translate("validation.check.dataObjectFormat"),
                                                            null, GetType());
							}
						}
					}
				}

				return new ValidationResult(ValidationResultType.VALID,
                                            I18n.translate("validation.check.dataObjectFormat"),
                                            I18n.translate("validation.dataObjectFormat.valid"),
                                            null, GetType());
			}

			//DataObjectFormat property not found
			return null;
		}

		public virtual string Name
		{
			get
			{
				return this.GetType().Name;
			}
		}

	}

}