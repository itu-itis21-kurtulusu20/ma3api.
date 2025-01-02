using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using IdGenerator = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;


	/// <summary>
	/// <p>The <code>Manifest</code> element provides a list of
	/// <code>Reference</code>s. The difference from the list in
	/// <code>SignedInfo</code> is that it is application defined which, if any, of
	/// the digests are actually checked against the objects referenced and what to
	/// do if the object is inaccessible or the digest compare fails. If a
	/// <code>Manifest</code> is pointed to from <code>SignedInfo</code>, the digest
	/// over the <code>Manifest</code> itself will be checked by the core signature
	/// validation behavior. The digests within such a <code>Manifest</code> are
	/// checked at the application's discretion. If a <code>Manifest</code> is
	/// referenced from another <code>Manifest</code>, even the overall digest of
	/// this two level deep <code>Manifest</code> might not be checked.</p>
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;complexType name="ManifestType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence>
	///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Reference" maxOccurs="unbounded"/>
	///       &lt;/sequence>
	///       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 12, 2009
	/// </summary>
	public class Manifest : BaseElement
	{

	    private readonly IList<Reference> mReferences = new List<Reference>(1);


		public Manifest(Context aBaglam) : base(aBaglam)
		{
			addLineBreak();
		}

		/// <summary>
		///  Construct Manifest from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public Manifest(Element aElement, Context aContext) : base(aElement, aContext)
		{

			Element[] refElements = selectChildren(Constants.NS_XMLDSIG, Constants.TAG_REFERENCE);

			if (refElements.Length == 0)
			{
				// En az bir reference olmali.
				throw new XMLSignatureException("xml.WrongContent", Constants.TAG_REFERENCE, Constants.TAG_MANIFEST);
			}

			this.mReferences = new List<Reference>(refElements.Length);
			foreach (Element refElement in refElements)
			{
				Reference @ref = new Reference(refElement, aContext);
				mReferences.Add(@ref);
			}
		}

		public virtual Reference getReference(int index)
		{
			if (index >= 0 && index < mReferences.Count)
			{
				return mReferences[index];
			}
			return null;
		}

		public virtual Reference getReferenceByURI(string aURI)
		{
			foreach (Reference @ref in mReferences)
			{
				if (@ref.URI.Equals(aURI))
				{
					return @ref;
				}
			}
			return null;
		}

        public virtual string addReference(string aDocumentURI, Transforms aTransforms, DigestMethod aDigestMethod, string aType)
		{
			Reference reference = new Reference(mContext);

			string referenceId = mContext.IdGenerator.uret(IdGenerator.TYPE_REFERENCE);
			reference.Id = referenceId;

			if (aTransforms != null)
			{
				reference.setTransforms(aTransforms);
			}

			DigestMethod digestAlg = (aDigestMethod == null) ? mContext.Config.AlgorithmsConfig.DigestMethod : aDigestMethod;
			reference.DigestMethod = digestAlg;

			reference.URI = aDocumentURI;

			if (aType != null)
			{
				reference.Type = aType;
			}

			addReference(reference);

			return referenceId;
		}

		public virtual void addReference(Reference aReference)
		{
			mElement.AppendChild(aReference.Element);
			addLineBreak();

			mReferences.Add(aReference);
		}

		public virtual int ReferenceCount
		{
			get
			{
				return mReferences.Count;
			}
		}

		// base element
		public override string Namespace
		{
			get
			{
				return Constants.NS_XMLDSIG;
			}
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAG_MANIFEST;
			}
		}
	}

}