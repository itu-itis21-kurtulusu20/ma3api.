using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml
{

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	/// <summary>
	/// Regisrty for own constructed xml signature prefixes.
	/// 
	/// @author ahmety
	/// date: Apr 3, 2009
	/// </summary>
	public class NamespacePrefixMap
	{
		private readonly IDictionary<string, string> uRI2prefix = new Dictionary<string, string>();

		public NamespacePrefixMap()
		{
			init();
		}

		private void init()
		{
			uRI2prefix[Constants.NS_XMLDSIG] = "ds";
			uRI2prefix[Constants.NS_XMLDSIG_11] = "dsig11";
			uRI2prefix[Constants.NS_XMLDSIG_MORE] = "dsmore";
			uRI2prefix[Constants.NS_XADES_1_3_2] = "xades";
			uRI2prefix[Constants.NS_XADES_1_4_1] = "xades141";
            uRI2prefix[Constants.NS_MA3] = "ma3";
		}


		public virtual string getPrefix(string aNamespaceURI)
		{
			if (uRI2prefix.ContainsKey(aNamespaceURI))
			{
				return uRI2prefix[aNamespaceURI];
			}

			return null;
		}

		public virtual void setPrefix(string aNamespaceURI, string prefix)
		{
			uRI2prefix[aNamespaceURI] = prefix;
		}

	}

}