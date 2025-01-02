package tr.gov.tubitak.uekae.esya.asn.util;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1IA5String;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1PrintableString;
import com.objsys.asn1j.runtime.Asn1Type;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */

public class OID_tipEslestirmeleri
{
     protected static Logger logger = LoggerFactory.getLogger(OID_tipEslestirmeleri.class);

     private static final Logger LOGCU = LoggerFactory.getLogger(OID_tipEslestirmeleri.class);

     static public final Asn1ObjectIdentifier oid_id_at_name = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_name);
     static public final Asn1ObjectIdentifier oid_id_at_surname = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_surname);
     static public final Asn1ObjectIdentifier oid_id_at_givenName = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_givenName);
     static public final Asn1ObjectIdentifier oid_id_at_commonName = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_commonName);
     static public final Asn1ObjectIdentifier oid_id_at_localityName = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_localityName);
     static public final Asn1ObjectIdentifier oid_id_at_stateOrProvinceName = new
         Asn1ObjectIdentifier(_ExplicitValues.id_at_stateOrProvinceName);
     static public final Asn1ObjectIdentifier oid_id_at_organizationName = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_organizationName);
     static public final Asn1ObjectIdentifier oid_id_at_organizationalUnitName = new
         Asn1ObjectIdentifier(_ExplicitValues.id_at_organizationalUnitName);
     static public final Asn1ObjectIdentifier oid_id_at_title = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_title);
     static public final Asn1ObjectIdentifier oid_id_at_countryName = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_countryName);
     static public final Asn1ObjectIdentifier oid_id_at_serialNumber = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_serialNumber);
     static public final Asn1ObjectIdentifier oid_id_at_pseudonym = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_pseudonym);
     static public final Asn1ObjectIdentifier oid_id_domainComponent = new Asn1ObjectIdentifier(
         _ExplicitValues.id_domainComponent);
     static public final Asn1ObjectIdentifier oid_id_emailAddress = new Asn1ObjectIdentifier(
         _ExplicitValues.id_emailAddress);
     static public final Asn1ObjectIdentifier oid_id_at_organizationIdentifier = new Asn1ObjectIdentifier(
         _ExplicitValues.id_at_organizationIdentifier);

    //rmz ev ssl için konuldu.
    static public final Asn1ObjectIdentifier oid_id_at_businessCategory = new Asn1ObjectIdentifier(_ExplicitValues.id_at_businessCategory);
    static public final Asn1ObjectIdentifier oid_id_at_jurisdictionOfIncorporationLocalityName = new Asn1ObjectIdentifier(_ExplicitValues.id_at_jurisdictionOfIncorporationLocalityName);
    static public final Asn1ObjectIdentifier oid_id_at_jurisdictionOfIncorporationStateOrProvinceName= new Asn1ObjectIdentifier(_ExplicitValues.id_at_jurisdictionOfIncorporationStateOrProvinceName);
    static public final Asn1ObjectIdentifier oid_id_at_jurisdictionOfIncorporationCountryName= new Asn1ObjectIdentifier(_ExplicitValues.id_at_jurisdictionOfIncorporationCountryName);


    static AtavHash msHash = new AtavHash();

     static
     {
          try{
               msHash.put("cn", oid_id_at_commonName, tr.gov.tubitak.uekae.esya.asn.x509.X520CommonName.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      cn", er); }
          
          try{
          //               msHash.put("c", oid_id_at_countryName, tr.gov.tubitak.uekae.esya.asn.x509.X520countryName.class);
               msHash.put("c", oid_id_at_countryName, tr.gov.tubitak.uekae.esya.asn.x509.X520CountrySerialUTF8liUcubeName.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      c", er); }
          
          try{
               msHash.put("givenName", oid_id_at_givenName, tr.gov.tubitak.uekae.esya.asn.x509.X520name.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      givenName", er); }
          
          try{
               msHash.put("l", oid_id_at_localityName, tr.gov.tubitak.uekae.esya.asn.x509.X520LocalityName.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      l", er); }
          
          try{
               msHash.put("name", oid_id_at_name, tr.gov.tubitak.uekae.esya.asn.x509.X520name.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      name", er); }
          
          try{
               msHash.put("ou", oid_id_at_organizationalUnitName, tr.gov.tubitak.uekae.esya.asn.x509.X520OrganizationalUnitName.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      ou", er); }
          
          try{
               msHash.put("o", oid_id_at_organizationName, tr.gov.tubitak.uekae.esya.asn.x509.X520OrganizationName.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      o", er); }
          
          try{
               msHash.put("pseudonym", oid_id_at_pseudonym, tr.gov.tubitak.uekae.esya.asn.x509.X520Pseudonym.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      pseudonym", er); }
          
          try{
//             msHash.put("serialNumber", oid_id_at_serialNumber, tr.gov.tubitak.uekae.esya.asn.x509.X520SerialNumber.class);
               msHash.put("serialNumber", oid_id_at_serialNumber, tr.gov.tubitak.uekae.esya.asn.x509.X520CountrySerialUTF8liUcubeName.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      serialNumber", er); }
          
          try{
               msHash.put("st", oid_id_at_stateOrProvinceName, tr.gov.tubitak.uekae.esya.asn.x509.X520StateOrProvinceName.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      st", er); }
          
          try{
               msHash.put("stateOrProvince", oid_id_at_stateOrProvinceName, tr.gov.tubitak.uekae.esya.asn.x509.X520StateOrProvinceName.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      stateOrProvince", er); }
          
          try{
               msHash.put("sn", oid_id_at_surname, tr.gov.tubitak.uekae.esya.asn.x509.X520name.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      sn", er); }
          
          try{
               msHash.put("title", oid_id_at_title, tr.gov.tubitak.uekae.esya.asn.x509.X520Title.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      titlr", er); }
          
          try{
               msHash.put("dc", oid_id_domainComponent, com.objsys.asn1j.runtime.Asn1IA5String.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      dc", er); }
          
          try{
               msHash.put("e", oid_id_emailAddress, com.objsys.asn1j.runtime.Asn1IA5String.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      e", er); }


         //rmz ev ssl için konuldu.
         try{//ub-business-category INTEGER ::= 128 ile ub-locality-name INTEGER ::= 128 aynı olduğundan X520LocalityName tipi kullanıldı.
             msHash.put("businessCategory", oid_id_at_businessCategory, tr.gov.tubitak.uekae.esya.asn.x509.X520LocalityName.class);
         } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!     businessCategory", er); }

         try{
             msHash.put("jurisdictionOfIncorporationLocalityName", oid_id_at_jurisdictionOfIncorporationLocalityName, tr.gov.tubitak.uekae.esya.asn.x509.X520LocalityName.class);
         } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!     jurisdictionOfIncorporationLocalityName", er); }

         try{
             msHash.put("jurisdictionOfIncorporationStateOrProvinceName", oid_id_at_jurisdictionOfIncorporationStateOrProvinceName, tr.gov.tubitak.uekae.esya.asn.x509.X520StateOrProvinceName.class);
         } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      jurisdictionOfIncorporationStateOrProvinceName", er); }

         try{
             //               msHash.put("c", oid_id_at_countryName, tr.gov.tubitak.uekae.esya.asn.x509.X520countryName.class);
             msHash.put("jurisdictionOfIncorporationCountryName", oid_id_at_jurisdictionOfIncorporationCountryName, tr.gov.tubitak.uekae.esya.asn.x509.X520CountrySerialUTF8liUcubeName.class);
         } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      jurisdictionOfIncorporationCountryName", er); }

          try{
               msHash.put("organizationIdentifier", oid_id_at_organizationIdentifier, X520OrganizationIdentifier.class);
          } catch(Throwable er){LOGCU.error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      organizationIdentifier", er); }
     }


     public static void listeyeAtavEkle (AttributeTypeAndValue aAtav) throws Asn1Exception
     {
          int[] oidInt = aAtav.type.value;
          int i;
          String st = "" + oidInt[0];
          for (i = 1; i < oidInt.length; i++)
          {
               st += "." + oidInt[i];
          }

//          System.out.println(st);
//          aAtav.type

          LOGCU.debug(st + " oidli AttributeTypeAndValue eklenmeye calisilacak.");

          //tipinin ne oldugunu tam olarak bilemeyiz. Fakat asagidakilerden biri olma ihtimali yuksek:
          Class[] ihtimaller = new Class[]
                               {DirectoryString.class, Asn1PrintableString.class, Asn1IA5String.class};
          Asn1Type xx = null;
          Asn1DerDecodeBuffer decBuf;
          for (i = 0; i < ihtimaller.length; i++)
          {
               try
               {
                    xx = (Asn1Type) ihtimaller[i].newInstance();
                    decBuf = new Asn1DerDecodeBuffer(aAtav.value.value);
                    xx = AsnIO.derOku(xx, decBuf);
                    LOGCU.debug("Tip olarak " + ihtimaller[i].getName() + " secildi.");
                    break;
               } catch (Exception ex)
               {
                    LOGCU.debug(ihtimaller[i].getName() + " icine decode edilemedi.", ex);
               }
          }

          if (i < ihtimaller.length)
          {
               LOGCU.debug("Atav listesine ekleniyor.... " + st + " - " + xx.getClass().getName(), xx);
               msHash.put(st, aAtav.type, xx.getClass());
          }
          else
          {
               LOGCU.error("Atav listeye eklenemedi. " + st);
               throw new Asn1Exception("Atav listeye eklenemedi.");
          }

     }


     public static IkiliAttributeTandV tipDon (Asn1ObjectIdentifier aOid) throws Asn1Exception
     {
          Asn1Type xx = null;
          String st = null;
          st = msHash.don_oid_dn(aOid);

          //Bu oid bizim listemizde olmayabilir. Bu durumda exception atmaliyiz.
          if (st == null)
          {
               throw new Asn1Exception("Oid " + aOid.toString() + " bizim listemizde bulunmuyor.");
          }

          try
          {
               xx = (Asn1Type) msHash.don_oid_sinif(aOid).newInstance();
          } catch (IllegalAccessException ex)
          {
               logger.error("Error in UtilAtav", ex);
               throw new Asn1Exception(ex.toString());
          } catch (InstantiationException ex)
          {
               logger.error("Error in UtilAtav", ex);
               throw new Asn1Exception("Class instantiate edilemiyor: " + ex.toString());
          }

          return new IkiliAttributeTandV(st, xx, aOid);
     }


     public static IkiliAttributeTandV atavDon (String aDN, String aValue, boolean aTurkce) throws Asn1Exception
     {
          Asn1ObjectIdentifier oid = msHash.don_dn_oid(aDN);
          //Class x = hash.don_oid_sinif(oid);
          Class x = msHash.don_dn_sinif(aDN);

          //Bu Class bizim listemizde olmayabilir. Bu durumda exception atmaliyiz.
          if (x == null)
          {
               throw new Asn1Exception("Gecersiz Name formati.");
          }

          Constructor[] constructors = x.getConstructors();

          Constructor con = null;
          Object[] params = null;
          try
          {
               if(hasConstructor(constructors, java.lang.String.class)) {
                    con = x.getConstructor(new Class[]
                            {java.lang.String.class});
                    params = new Object[]
                            {aValue};
               } else if(hasConstructor(constructors, Byte.TYPE, com.objsys.asn1j.runtime.Asn1Type.class)) {
                    con = x.getConstructor(new Class[]
                            {Byte.TYPE,
                                    com.objsys.asn1j.runtime.Asn1Type.class});
                    Asn1Type xx = null;
                    byte bb = 0;
                    try
                    {
                         if (aTurkce && x!=X520CountrySerialUTF8liUcubeName.class)
                         {
                              bb = x.getField("_UTF8STRING").getByte(x);
                              xx = new Asn1UTF8String(aValue);
                         }
                         else
                         {
                              bb = x.getField("_PRINTABLESTRING").getByte(x);
                              xx = new Asn1PrintableString(aValue);
                         }
                    } catch (Exception ex2)
                    {
                         logger.error("Error in OID_tipEslestirmeleri", ex2);
                    }
                    params = new Object[]
                            {new Byte(bb), xx};
               } else {
                    throw new NoSuchMethodException("Can not find suitable Constructor!");
               }
          } catch (SecurityException ex) {
               logger.error("Error in OID_tipEslestirmeleri", ex);
          } catch (NoSuchMethodException ex) {
               logger.error("Error in OID_tipEslestirmeleri", ex);
          }

          Object val = null;
          try
          {
               val = con.newInstance(params);
          } catch (InvocationTargetException ex) {
               logger.error("Error in OID_tipEslestirmeleri", ex);
          } catch (IllegalArgumentException ex) {
               logger.error("Error in OID_tipEslestirmeleri", ex);
          } catch (IllegalAccessException ex) {
               logger.error("Error in OID_tipEslestirmeleri", ex);
          } catch (InstantiationException ex) {
               logger.error("Error in OID_tipEslestirmeleri", ex);
          }

          return new IkiliAttributeTandV(aDN, (Asn1Type) val, oid);
     }

     private static boolean hasConstructor(Constructor<?>[] constructors, Class... parameters) {
          for (Constructor<?> constructor : constructors) {
               if (Arrays.equals(constructor.getParameterTypes(), parameters)) {
                    return true;
               }
          }
          return false;
     }
}

class AtavHash
{
     private Hashtable<String, Asn1ObjectIdentifier> attr_dn_oid = new Hashtable<String, Asn1ObjectIdentifier>();
     private Hashtable<String, Class<?>> attr_dn_class = new Hashtable<String, Class<?>>();
     private Vector<Asn1ObjectIdentifier> oidVector = new Vector<Asn1ObjectIdentifier>();
     private Vector<String> dnVector = new Vector<String>();

     public void put (String aDN, Asn1ObjectIdentifier aOid, Class aSinif)
     {
          String dn = aDN.toUpperCase(Locale.US);
          attr_dn_oid.put(dn, aOid);
          attr_dn_class.put(dn, aSinif);
          oidVector.add(aOid);
          dnVector.add(dn);
     }


     public String don_oid_dn (Asn1ObjectIdentifier aOid)
     {
          for (int i = 0; i < oidVector.size(); i++)
          {
               if ( oidVector.get(i).equals(aOid))
                    return dnVector.get(i);
          }
          return null;
     }


     public Asn1ObjectIdentifier don_dn_oid (String aDN)
     {
          return attr_dn_oid.get(aDN.toUpperCase(Locale.US));
     }


     public Class don_dn_sinif (String aDN)
     {
          return attr_dn_class.get(aDN.toUpperCase(Locale.US));
     }


     public Class don_oid_sinif (Asn1ObjectIdentifier aOid)
     {
          return attr_dn_class.get(don_oid_dn(aOid));
     }

}