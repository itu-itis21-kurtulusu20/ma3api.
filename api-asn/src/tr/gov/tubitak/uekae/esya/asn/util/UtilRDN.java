/* TUBITAK UEKAE - Muhammed Serdar SORAN */

package tr.gov.tubitak.uekae.esya.asn.util;

import tr.gov.tubitak.uekae.esya.asn.x509.AttributeTypeAndValue;
import tr.gov.tubitak.uekae.esya.asn.x509.RelativeDistinguishedName;

import com.objsys.asn1j.runtime.Asn1Exception;

public class UtilRDN
{

     private static final String SPLITTER = "##";

     public static String rdn2String (RelativeDistinguishedName aRdn)
     {
          AttributeTypeAndValue[] atav = aRdn.elements;
          String r;

          if ((atav == null) | (atav.length == 0))
               return "";

          r = UtilAtav.atav2String(atav[0]);
          for (int i = 1; i < atav.length; i++)
          {
               r += SPLITTER;
               r += UtilAtav.atav2String(atav[i]);
          }

          return r;
     }


     public static RelativeDistinguishedName string2rdn (String aRdn,boolean aTurkce) throws Asn1Exception
     {
          String[] atavstrings = aRdn.split(SPLITTER);
          int i = atavstrings.length;
          AttributeTypeAndValue[] atavatavs = new AttributeTypeAndValue[i];
          RelativeDistinguishedName r;

          //System.out.println("rdn = "+rdn);


          i--;
          for (; i >= 0; i--)
          {
               //System.out.println(i +" - "+atavstrings[i]);
               atavatavs[i] = UtilAtav.string2atav(atavstrings[i],aTurkce);
          }

          //Olusturulan atav'lardan RelativeDistinguishedName yapisini olusturup donelim.
          r = new RelativeDistinguishedName(atavatavs);
          return r;
     }

}