using System;
using System.DirectoryServices;
namespace tr.gov.tubitak.uekae.esya.api.infra.directory
{
    //class StringComparer : IEqualityComparer<String>
    //{
    //    // Products are equal if their names and product numbers are equal.
    //    public bool Equals(String x, String y)
    //    {
    //        //Check whether the compared objects reference the same data.
    //        if (Object.ReferenceEquals(x, y)) return true;

    //        //Check whether any of the compared objects is null.
    //        if (Object.ReferenceEquals(x, null) || Object.ReferenceEquals(y, null))
    //            return false;

    //        return String.Equals(x, y, StringComparison.OrdinalIgnoreCase);
    //    }

    //    // If Equals() returns true for a pair of objects 
    //    // then GetHashCode() must return the same value for these objects.

    //    public int GetHashCode(String product)
    //    {
    //        //Check whether the object is null
    //        if (Object.ReferenceEquals(product, null)) return 0;

    //        //Get hash code for the Name field if it is not null.               
    //        return product == null ? 0 : product.GetHashCode();
    //    }
    //}


    //public class LdapAttribute
    //{
    //    public static readonly LdapAttribute CERTIFICATE = new LdapAttribute("usercertificate");
    //    public static readonly LdapAttribute CRL = new LdapAttribute("certificaterevocationlist");

    //    private String _attributeName;
    //    private LdapAttribute(String aAttributeName)
    //    {
    //        _attributeName = aAttributeName;
    //    }
    //    public String ToString()
    //    {
    //        return _attributeName;
    //    }
    //}
    public class SearchResults
    {
        private readonly SearchResultCollection _searchResultCollection;

        internal SearchResults(SearchResultCollection aSrc)
        {
            _searchResultCollection = aSrc;
            if (_searchResultCollection == null)
            {
                throw new Exception("SearchResults nesnesi null!");
            }
        }

        ~SearchResults()
        {
            if (_searchResultCollection != null)
            {
                _searchResultCollection.Dispose();
            }
            Console.WriteLine("~SearchResults CALISTI");
        }


        //public byte[] getCertificate()
        //{
        //    return getBinaryAttribute(LdapAttribute.CERTIFICATE);
        //}
        //public byte[] getCRL()
        //{
        //    return getBinaryAttribute(LdapAttribute.CRL);
        //}
        public byte[] getBinaryAttribute(String[] aAttribute)
        {
            if (aAttribute.Length > 1)
            {
                throw new Exception("Birden fazla attribute var (" + aAttribute.Length + ")");
            }
            byte[] attribute = null;
            if (_searchResultCollection.Count == 0)
            {
                return null;
            }
            else if (_searchResultCollection.Count == 1)
            {
                ResultPropertyCollection rpc = _searchResultCollection[0].Properties;             
                {
                    foreach (string property in rpc.PropertyNames)
                    {
                        if (property.Contains(aAttribute[0]))
                        {
                            if (rpc[property].Count > 1)    //ya 1 den fazla attribute var 
                            {
                                throw new Exception("Aranilan attribute icin (" + aAttribute.ToString() + ") birden fazla " + rpc[property].Count + " adet value var!");
                            }
                            else//ya da 1 attribute var!
                            {
                                return (byte[])rpc[property][0];
                            }                            
                        }                                       
                    }
                }
                
            }
            else
            {
                throw new Exception("Birden fazla (" + _searchResultCollection.Count + ") adet arama sonucu var!");
            }

            return attribute;

        }
    }
}
