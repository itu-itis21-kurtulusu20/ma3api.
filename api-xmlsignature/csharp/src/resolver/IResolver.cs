namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;


	/// <summary>
	/// @author ahmety
	/// date: May 14, 2009
	/// </summary>
	public interface IResolver
	{

		bool isResolvable(string aURI, Context aBaglam);

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document resolve(String aURI, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aBaglam) throws java.io.IOException;
		Document resolve(string aURI, Context aBaglam);
	}

}