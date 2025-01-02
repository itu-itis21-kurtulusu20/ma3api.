/* TUBITAK UEKAE - Muhammed Serdar SORAN */

package tr.gov.tubitak.uekae.esya.asn.util;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1IA5String;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1OpenType;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.AnotherName;
import tr.gov.tubitak.uekae.esya.asn.x509.AttributeTypeAndValue;
import tr.gov.tubitak.uekae.esya.asn.x509.DirectoryString;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.RDNSequence;
import tr.gov.tubitak.uekae.esya.asn.x509.RelativeDistinguishedName;
import tr.gov.tubitak.uekae.esya.asn.x509._ImplicitValues;

import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class UtilName
{
     protected static Logger logger = LoggerFactory.getLogger(UtilName.class);

     public static String name2String (Name aName)
     {
          RelativeDistinguishedName[] rdnArray = ( (RDNSequence) (aName.getElement())).elements;
          String r;
          int i;

          if ( (rdnArray == null) | (rdnArray.length == 0))
               return "";

          i = rdnArray.length - 1;
          r = UtilRDN.rdn2String(rdnArray[i--]);
          for (; i >= 0; i--)
          {
               r += ",";
               r += UtilRDN.rdn2String(rdnArray[i]);
          }

          return r;
     }

     public static String generalName2String(GeneralName aGenName)
     {
          if( (aGenName == null) || (aGenName.getElement()==null))
               return "";

          switch(aGenName.getChoiceID())
          {
          case GeneralName._DIRECTORYNAME: 
               return name2String((Name)aGenName.getElement());
          case GeneralName._DNSNAME: 
          case GeneralName._RFC822NAME: 
          case GeneralName._UNIFORMRESOURCEIDENTIFIER: 
               return ((Asn1IA5String)aGenName.getElement()).value;
          case GeneralName._IPADDRESS: 
               byte[] ip = ((Asn1OctetString)aGenName.getElement()).value;
               int i;
               String r = "";
               if( (ip==null) || (ip.length==0) )
                    return "";
               r += ip[0];
               for(i=1;i<ip.length;i++)
                    r+="."+ip[i];
               return r;
          case GeneralName._OTHERNAME:
              AnotherName othername = (AnotherName) aGenName.getElement();
              if(Arrays.equals(othername.type_id.value, _ImplicitValues.id_win_upn))
              {
        	  try
        	  {
                	  Asn1UTF8String s = new Asn1UTF8String();
                	  Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(othername.value.value);
                	  s.decode(decBuf);
                	  return s.value;
        	  }
        	  catch(Exception aEx){
                   logger.warn("Warning in UtilName", aEx);
              }
              }
          default:
               return "??";
          }

     }

     public static Name string2Name (String aDN, boolean aTurkce) throws Asn1Exception
     {
          RelativeDistinguishedName[] rdnsArray;
          Name name;

          // Verilen dn'i rdnlere bol ve her bir rdn'i vectore koy.
          // (Genelde rdn'lerimiz tek bir atav'dan olusuyor, normalde rdn=set of atav)
          Vector<String> rdnsVector = new Vector<String>();
          try
          {
               _parseEt(aDN, rdnsVector, aTurkce);
          } catch (Exception e)
          {
               logger.debug("Information for UtilName" , e);
               throw new Asn1Exception(e.getMessage());
          }

          int len = rdnsVector.size();
          if(len == 0)
          {
               throw new Asn1Exception("Verilen dn hatali: "+aDN);
          }

          // Vektordeki rnd'leri rdnArrayine koy.
          rdnsArray = new RelativeDistinguishedName[len];

          for (int i = 0; i < len; i++)
          {
               // Sira onemli sanirim!
               rdnsArray[i] = UtilRDN.string2rdn(rdnsVector.get(i), aTurkce);
          }

          // rdnArrayinden Name yapisini olustur ve don.
          name = new Name();
          name.set_rdnSequence(new RDNSequence(rdnsArray));
          return name;
     }


     /***
      * Verilen dn'i atav'lara boler ve her bir atav'i vektore koyar. Dn'i bolmeye
      * sagdan baslar. En sagdaki virgulun sag tarafina bakar ve atav mi diye kontrol
      * eder, atavsa bu atavi vektore koyar ve sol tarafi tekrar bolunmek uzere ayni
      * metoda gonderir. atav degilse atav bulana kadar virgullerden sola dogru ilerler.
      * @param aDn atavlarina bolunup vektore konacak olan dn stringi.
      * @param aVector atavlarin konacagi vektor
      * @param aUtf8 Verilen dn'in utf8 olup olmadigi
      * @throws Exception Arguman olarak verilen string dogru bir dn degilse Exception atar. 
      */
     private static void _parseEt(String aDn, Vector<String> aVector, boolean aUtf8) throws Exception
     {
          if(aDn == null || aDn.trim().length() == 0)
               return;

          if(aDn.trim().indexOf(',') == 0)
          {
               //System.out.println("\"" + aDn + "\"" + " atav degil! Tum dn hatali.");
               aVector.removeAllElements();
               throw new ESYAException("\"" + aDn + "\"" + " atav degil! Tum dn hatali.");
          }

          String atavAdayi, basKismi;

          int lastIndex = aDn.lastIndexOf(',');

          atavAdayi = aDn.substring(lastIndex+1);

          if(lastIndex != -1)
               basKismi = aDn.substring(0, lastIndex);
          else
               basKismi = null;


          while(!_atavMi(atavAdayi, aUtf8))
          {
               if(basKismi == null)
               {
                    //System.out.println("\"" + aDn + "\"" + " atav degil! Tum dn hatali.");
                    aVector.removeAllElements();
                    throw new ESYAException("\"" + aDn + "\"" + " atav degil! Tum dn hatali.");
               }
               lastIndex = basKismi.lastIndexOf(',');

               atavAdayi = basKismi.substring(lastIndex+1)+","+atavAdayi;
               if(lastIndex != -1)
                    basKismi = basKismi.substring(0, lastIndex);
               else
                    basKismi = null;
          }

          //System.out.println("\natavAdayi: "+atavAdayi);
          //System.out.println("basKismi : "+basKismi);

          atavAdayi = atavAdayi.trim();
          aVector.add(atavAdayi);
          //System.out.println("\"" + atavAdayi + "\"" + " bir atavdir. Vektore eklendi.");
          _parseEt(basKismi, aVector, aUtf8);

     }

     private static boolean _atavMi(String aStr, boolean aUtf8)
     {
          int k = aStr.indexOf('=');

          if(k == -1)
          {
               //System.out.println("\n\"" + aStr+ "\"" + " atav mi diye soruldu...");
               //System.out.println("Verilen stringde = isareti yok, dolayisiyla bu bir atav degil");
               return false;
          }

          String bas = aStr.substring(0, k).trim();
          String son = aStr.substring(k + 1).trim();

          try
          {
               OID_tipEslestirmeleri.atavDon(bas, son, aUtf8);
          } catch (Asn1Exception e)
          {
               logger.warn("Warning in UtilName", e);
               //System.out.println( "\"" + aStr + "\"" + " icinde \"=\" var ama bu isaretin solundaki bir attribute type degil");
               return false;
          }

          return true;
     }

     public static Name byte2Name(byte[] aName) throws Asn1Exception, IOException
     {

          Asn1DerDecodeBuffer buf = new Asn1DerDecodeBuffer(aName);
          Name name = new Name();

          name.decode(buf);

          return name;

     }

     public static byte[] name2byte(Name aName) throws Asn1Exception
     {
          Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
          aName.encode(encBuf);
          return encBuf.getMsgCopy();
     }

     public static boolean isUtf8String(Name aName)
     {
          if(aName == null)
               return false;
          RDNSequence rdnSeq = (RDNSequence)aName.getElement();
          if(rdnSeq == null)
               return false;
          RelativeDistinguishedName[] rdns = rdnSeq.elements;
          if(rdns == null | rdns.length == 0 || rdns[0] == null)
               return false;

          int i,j;
          AttributeTypeAndValue[] atavs;
          AttributeTypeAndValue atav;

          for(i=0; i<rdns.length; i++)
          {
               atavs = rdns[i].elements;
               if(atavs == null || atavs.length == 0)
                    continue;
               for(j=0; j<atavs.length; j++)
               {
                    atav = atavs[j];
                    if(atav == null || atav.value == null)
                         continue;
                    Asn1OpenType openType = atav.value;
                    Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                    Asn1DerDecodeBuffer decBuf;
                    DirectoryString dirStr = new DirectoryString();
                    try
                    {
                         openType.encode(encBuf);
                         decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
                         dirStr.decode(decBuf);
                    } catch (Exception e)
                    {
                         logger.warn("Warning in UtilName", e);
                         continue;
                    }

                    if(dirStr.getChoiceID() == DirectoryString._UTF8STRING)
                         return true;

               }
          }

          return false;
     }



     /*
     public static void main (String[] args)
     {

          com.objsys.asn1j.runtime.Diag.setTraceLevel(1000);
          ////Dosyayi oku
          byte[] b = null;
          //String File = "c:\\dogrulamadakicer.cer";
          String File = "c:\\gonCer.cer";
          try
          {
               java.io.FileInputStream fin = new java.io.FileInputStream(File);
               int i = fin.available();
               b = new byte[i];
               fin.read(b);
               fin.close();
          } catch (Exception e)
          {
               e.printStackTrace();
               return;
          }

          //decode et
          tr.gov.tubitak.uekae.esya.asn.x509.Certificate cer = new tr.gov.tubitak.uekae.esya.asn.
              x509.Certificate();
          com.objsys.asn1j.runtime.Asn1DerDecodeBuffer buf =
              new com.objsys.asn1j.runtime.Asn1DerDecodeBuffer(b);

          try
          {
               cer.decode(buf);
          } catch (Exception ex)
          {
               ex.printStackTrace();
               return;
          }

          //Icinden issuer al
          Name issuer = cer.tbsCertificate.subject;

          System.out.println(name2String(issuer));

          tr.gov.tubitak.uekae.esya.asn.x509.Certificate cer2 = new tr.gov.tubitak.uekae.esya.asn.
              x509.Certificate();

          try
          {
               cer2 = (tr.gov.tubitak.uekae.esya.asn.x509.Certificate) cer.clone();
          } catch (CloneNotSupportedException ex)
          {
               ex.printStackTrace();
          }

          System.out.println(name2String(cer2.tbsCertificate.issuer));

          //Name yeni = string2Name("dc=xx,o=serdar,c=tr+o=kaka,dc=ww");
          //Name yeni = string2Name("o=serdar,dc=ww");
          Name yeni = null;
          try
          {
               yeni = string2Name("dc=xx," +
                                  "cn=serdarcn," +
                                  "givenName=serdargivenName," +
                                  "name=serdarname," +
                                  "title=serdartitle," +
                                  "pseudonym=serdarpseudonym," +
                                  "dc=ww", true);
          } catch (Asn1Exception ex1)
          {
               ex1.printStackTrace();
          }

          com.objsys.asn1j.runtime.Asn1DerEncodeBuffer encBuf =
              new com.objsys.asn1j.runtime.Asn1DerEncodeBuffer();

          try
          {
               yeni.encode(encBuf);
          } catch (Asn1Exception ex)
          {
               ex.printStackTrace();
          }

          byte[] bb = encBuf.getMsgCopy();


          System.out.println(name2String(yeni));

     }
      */
}