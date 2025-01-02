package tr.gov.tubitak.uekae.esya.asn.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.asn.PKIXqualified._PKIXqualifiedValues;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;
import tr.gov.tubitak.uekae.esya.asn.x509.DirectoryString;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.RoleSyntax;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectDirectoryAttributes;
import tr.gov.tubitak.uekae.esya.asn.x509._ImplicitValues;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OpenType;
import com.objsys.asn1j.runtime.Asn1PrintableString;
import com.objsys.asn1j.runtime.Asn1Type;
import com.objsys.asn1j.runtime.Asn1UTF8String;

public class UtilSubjectDirectoryAttr
{

     static public final Asn1ObjectIdentifier oid_pda_countryOfCitizenship = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_countryOfCitizenship);
     static public final Asn1ObjectIdentifier oid_pda_countryOfResidence = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_countryOfResidence);
     static public final Asn1ObjectIdentifier oid_pda_dateOfBirth = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_dateOfBirth);
     static public final Asn1ObjectIdentifier oid_pda_gender = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_gender);
     static public final Asn1ObjectIdentifier oid_pda_placeOfBirth = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_placeOfBirth);
     //bu eslestirmede etsi 101862 kullanilmistir.
     static public final Asn1ObjectIdentifier oid_at_role = new Asn1ObjectIdentifier(_ImplicitValues.id_at_role);
     //bu eslestirme Windows smartcard logon icin yazilmis bir yazidan alinmistir.
     static public final Asn1ObjectIdentifier oid_win_upn = new Asn1ObjectIdentifier(_ImplicitValues.id_win_upn);

     static DirAttrHash hash = new DirAttrHash();

     private static final Logger LOGCU = LoggerFactory.getLogger(UtilSubjectDirectoryAttr.class);

     static
     {
//          try
//          {
//               //Bu eslestirmeler yapilirken RFC3739 kullanilmistir.
//               hash.put(oid_pda_countryOfCitizenship, Class.forName("com.objsys.asn1j.runtime.Asn1PrintableString"));
//               hash.put(oid_pda_countryOfResidence, Class.forName("com.objsys.asn1j.runtime.Asn1PrintableString"));
//               hash.put(oid_pda_dateOfBirth, Class.forName("com.objsys.asn1j.runtime.Asn1GeneralizedTime"));
//               hash.put(oid_pda_gender, Class.forName("com.objsys.asn1j.runtime.Asn1PrintableString"));
//               hash.put(oid_pda_placeOfBirth, Class.forName("tr.gov.tubitak.uekae.esya.asn.x509.DirectoryString"));
//               //bu eslestirmede etsi 101862 kullanilmistir.
////               hash.put(oid_at_role, Class.forName("tr.gov.tubitak.uekae.esya.genel.veritabani.DBObjects.Rol"));
//          } catch (ClassNotFoundException ex)
//          {
//               LOGCU.fatal("Subject Directory Attribuet tiplerinden biri icin class bulunamadi", ex);
//               ex.printStackTrace();
//          }
          
          //Bu eslestirmeler yapilirken RFC3739 kullanilmistir.
          hash.put(oid_pda_countryOfCitizenship, com.objsys.asn1j.runtime.Asn1PrintableString.class);
          hash.put(oid_pda_countryOfResidence, com.objsys.asn1j.runtime.Asn1PrintableString.class);
          hash.put(oid_pda_dateOfBirth, com.objsys.asn1j.runtime.Asn1GeneralizedTime.class);
          hash.put(oid_pda_gender, com.objsys.asn1j.runtime.Asn1PrintableString.class);
          hash.put(oid_pda_placeOfBirth, tr.gov.tubitak.uekae.esya.asn.x509.DirectoryString.class);
          //bu eslestirmede etsi 101862 kullanilmistir.
//          hash.put(oid_at_role, tr.gov.tubitak.uekae.esya.genel.veritabani.DBObjects.Rol.class);

     }


     //private static Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

     public static EAttribute attributeasPrintableFromString (Asn1ObjectIdentifier oid, String st)
     {
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		Asn1OpenType[] tempOpen = new Asn1OpenType[1];

		Asn1PrintableString val = new Asn1PrintableString(st);

		try {
			val.encode(encBuf);
		} catch (Exception ex1) {
			LOGCU.error("Buraya hic gelmemeli", ex1);
		}
		tempOpen[0] = new Asn1OpenType(encBuf.getMsgCopy());
		// _SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
		return new EAttribute(oid, tempOpen);

     }


     public static EAttribute countryOfCitizenFromString (String c)
     {
          if (c == null || c.length() != 2)
          {
               LOGCU.error("C=" + c + " iken bu fonksiyon cagrilamaz.");
               return null;
          }
          return attributeasPrintableFromString(oid_pda_countryOfCitizenship, c);
     }


     public static EAttribute countryOfResidenceFromString (String c)
     {
          if (c == null || c.length() != 2)
          {
               LOGCU.error("C=" + c + " iken bu fonksiyon cagrilamaz.");
               return null;
          }
          return attributeasPrintableFromString(oid_pda_countryOfResidence, c);
     }


     public static EAttribute dateOfBirthFromDate (Date d)
     {
          Asn1OpenType[] tempOpen = new Asn1OpenType[1];

  		  Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
          
  		  Asn1GeneralizedTime val = new Asn1GeneralizedTime();
          Calendar cl = Calendar.getInstance();
          cl.setTime(d);
          try
          {
               val.setTime(cl);
          } catch (Asn1Exception ex)
          {
               LOGCU.error("Tarih=" + d + " iken bu fonksiyon cagrilamiyor.", ex);
               return null;
          }

          try
          {
               val.encode(encBuf);
          } catch (Exception ex1)
          {
               LOGCU.error("Buraya hic gelmemeli", ex1);
          }
          tempOpen[0] = new Asn1OpenType(encBuf.getMsgCopy());
//          _SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
          return new EAttribute(oid_pda_dateOfBirth, tempOpen);
     }


     public static EAttribute genderFromString (String g)
     {
          if (g == null || g.length() != 1)
          {
               LOGCU.error("gender=" + g + " iken bu fonksiyon cagrilamaz.");
               return null;
          }
          return attributeasPrintableFromString(oid_pda_gender, g);
     }


     public static EAttribute placeOfBirthFromString (String sehir)
     {
          Asn1OpenType[] tempOpen = new Asn1OpenType[1];

  		  Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
          DirectoryString val = new DirectoryString();
          val.set_utf8String(new Asn1UTF8String(sehir));
          
          try
          {
               val.encode(encBuf);
          } catch (Exception ex1)
          {
               LOGCU.error("Buraya hic gelmemeli", ex1);
          }
          tempOpen[0] = new Asn1OpenType(encBuf.getMsgCopy());
//          _SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
          return new EAttribute(oid_pda_placeOfBirth, tempOpen);
     }


     public static EAttribute roleFromArray (int[] r)
     {
          Asn1OpenType[] tempOpen = new Asn1OpenType[1];

  		  Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
          GeneralName roleName = new GeneralName();
          roleName.set_registeredID(new Asn1ObjectIdentifier(r));
          RoleSyntax val = new RoleSyntax(roleName);

          try
          {
               val.encode(encBuf);
          } catch (Exception ex1)
          {
               LOGCU.error("Buraya hic gelmemeli", ex1);
          }
          tempOpen[0] = new Asn1OpenType(encBuf.getMsgCopy());
//          _SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
          return new EAttribute(oid_at_role, tempOpen);
     }

//     public static Attribute uPNFromString(String aUPN)
//     {
//          Asn1OpenType[] tempOpen = new Asn1OpenType[1];
//
//
//          Asn1UTF8String upnUTF8 = new Asn1UTF8String(aUPN);
//          safeEncBuf.reset();
//          try
//          {
//               upnUTF8.encode(safeEncBuf);
//          } catch (Exception ex1)
//          {
//               LOGCU.error("Buraya hic gelmemeli", ex1);
//          }
//          Asn1OpenType upnOpenType = new Asn1OpenType(safeEncBuf.getMsgCopy());
//
//          AnotherName upnAnother = new AnotherName(new Asn1ObjectIdentifier(_ImplicitValues.id_win_upn),upnOpenType);
//
//          GeneralName val = new GeneralName();
//          val.set_otherName(upnAnother);
//
//          safeEncBuf.reset();
//          try
//          {
//               val.encode(safeEncBuf);
//          } catch (Exception ex1)
//          {
//               LOGCU.error("Buraya hic gelmemeli", ex1);
//          }
//          tempOpen[0] = new Asn1OpenType(safeEncBuf.getMsgCopy());
//          _SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
//          return new Attribute(oid_win_upn, temp);
//     }

     public static void getOneAttribute(Asn1ObjectIdentifier aOID,Asn1Type aAsn,SubjectDirectoryAttributes aSubjectDirAttrs)
         throws IOException, Asn1Exception
     {
          Attribute[] attrAr = aSubjectDirAttrs.elements;
          for(int i = 0 ; i < attrAr.length ; i++)
               if(attrAr[i].type.equals(aOID))
               {
                    if(attrAr[i].values == null ) return;
                    Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(attrAr[i].values.elements[0].value);
                    aAsn = AsnIO.derOku(aAsn, decBuf);
                    return;
               }
     }

     public static RoleSyntax getRol(SubjectDirectoryAttributes aSubjectDirAttrs)
     {
          RoleSyntax r = new RoleSyntax();
          try
          {
               getOneAttribute(oid_at_role, r, aSubjectDirAttrs);
          } catch (Exception ex)
          {
               LOGCU.error("Rol okunamadi.",ex);
               return null;
          }
          return r;
     }
}




class DirAttrHash
{
     private Vector oidVector = new Vector();
     private Vector sinifVector = new Vector();

     public void put (Asn1ObjectIdentifier oid, Class sinif)
     {
          oidVector.add(oid);
          sinifVector.add(sinif);
     }


     public Class don_oid_sinif (Asn1ObjectIdentifier oid)
     {
          for (int i = 0; i < oidVector.size(); i++)
          {
               if ( ( (Asn1ObjectIdentifier) oidVector.get(i)).equals(oid))
                    return (Class) sinifVector.get(i);
          }
          return null;
     }

}