namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;

	/// <summary>
	/// An Explicit Policy based Electronic Signature (XAdES-EPES) extends the
	/// definition of an electronic signature to conform to the identified signature
	/// policy. A XAdES-EPES builds up on a XMLDSIG or XAdES-BES forms by
	/// incorporating the <code>SignaturePolicyIdentifier</code> element. This signed
	/// property indicates that a signature policy MUST be used for signature 
	/// validation. It MAY explicitly identify the signature policy. Other
	/// properties may be required by the mandated policy.
	/// 
	/// @author ahmety
	/// date: Oct 19, 2009
	/// </summary>
	public class EPES : BES
	{

		public EPES(Context aContext, XMLSignature aSignature) : base(aContext, aSignature)
		{
		}
	}

}