using System;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class UtilRDN
    {
        private static readonly String SPLITTER = "##";

        public static String rdn2String(RelativeDistinguishedName aRdn)
        {
            AttributeTypeAndValue[] atav = aRdn.elements;
            String r;

            if ((atav == null) | (atav.Length == 0))
                return "";

            r = UtilAtav.atav2String(atav[0]);
            for (int i = 1; i < atav.Length; i++)
            {
                r += SPLITTER;
                r += UtilAtav.atav2String(atav[i]);
            }

            return r;
        }


        public static RelativeDistinguishedName string2rdn(String aRdn, bool aTurkce)
        {
            String[] atavstrings = aRdn.Split(SPLITTER.ToCharArray());
            int i = atavstrings.Length;
            AttributeTypeAndValue[] atavatavs = new AttributeTypeAndValue[i];
            RelativeDistinguishedName r;

            //System.out.println("rdn = "+rdn);


            i--;
            for (; i >= 0; i--)
            {
                //System.out.println(i +" - "+atavstrings[i]);
                atavatavs[i] = UtilAtav.string2atav(atavstrings[i], aTurkce);
            }

            //Olusturulan atav'lardan RelativeDistinguishedName yapisini olusturup donelim.
            r = new RelativeDistinguishedName(atavatavs);
            return r;
        }
    }
}
