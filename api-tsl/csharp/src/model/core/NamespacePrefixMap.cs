using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.core
{
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
            uRI2prefix[Constants.NS_TSL] = "tsl";
            uRI2prefix[Constants.NS_TSLX] = "tslx";
            uRI2prefix[Constants.NS_ECC] = "ecc";
            uRI2prefix[Constants.NS_XADES] = "xades";
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
