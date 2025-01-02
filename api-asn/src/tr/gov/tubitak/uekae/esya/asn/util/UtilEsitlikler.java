package tr.gov.tubitak.uekae.esya.asn.util;


import com.objsys.asn1j.runtime.Asn1Choice;
import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1PrintableString;
import com.objsys.asn1j.runtime.Asn1Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.AttributeTypeAndValue;
import tr.gov.tubitak.uekae.esya.asn.x509.RDNSequence;
import tr.gov.tubitak.uekae.esya.asn.x509.RelativeDistinguishedName;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;

/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */

public class UtilEsitlikler {

     protected static Logger logger = LoggerFactory.getLogger(UtilEsitlikler.class);

     public UtilEsitlikler()
     {
     }

     public static boolean esitMi(Asn1Type aObj1,Asn1Type aObj2)
     {
          //null olma durumlarini incele
          if((aObj1==null) && (aObj2==null))
               return true;
          else if((aObj1==null) || (aObj2==null))
               return false;

          //Tipine gore dogru methodu cagiralim.
          if((aObj1 instanceof Asn1Choice) && (aObj2 instanceof Asn1Choice))
               return _esitMi((Asn1Choice)aObj1,(Asn1Choice)aObj2);

          if((aObj1 instanceof RDNSequence) && (aObj2 instanceof RDNSequence))
               return _esitMi((RDNSequence)aObj1,(RDNSequence)aObj2);

          if((aObj1 instanceof RelativeDistinguishedName) && (aObj2 instanceof RelativeDistinguishedName))
               return _esitMi((RelativeDistinguishedName)aObj1,(RelativeDistinguishedName)aObj2);

          if((aObj1 instanceof AttributeTypeAndValue) && (aObj2 instanceof AttributeTypeAndValue))
               return _esitMi((AttributeTypeAndValue)aObj1,(AttributeTypeAndValue)aObj2);

          if((aObj1 instanceof SubjectPublicKeyInfo) && (aObj2 instanceof SubjectPublicKeyInfo))
               return _esitMi((SubjectPublicKeyInfo)aObj1,(SubjectPublicKeyInfo)aObj2);

          if((aObj1 instanceof AlgorithmIdentifier) && (aObj2 instanceof AlgorithmIdentifier))
               return _esitMi((AlgorithmIdentifier)aObj1,(AlgorithmIdentifier)aObj2);

         // since no use of it, no class name should start with Asn1 (com.obj.runtime.Asn1Integer - starts with 'com')
/*          if(aObj1.getClass().getName().startsWith("Asn1"))
               return aObj1.equals(aObj2);*/
          
          try
          {
               Asn1DerEncodeBuffer encBuf1 = new Asn1DerEncodeBuffer();
               Asn1DerEncodeBuffer encBuf2 = new Asn1DerEncodeBuffer();
               aObj1.encode(encBuf1);
               aObj2.encode(encBuf2);
               return esitMi(encBuf1.getMsgCopy(),encBuf2.getMsgCopy());
          } catch (Exception ex)
          {
               logger.warn("Warning in UtilEsitlikler", ex);
               return false;
          }
          //return false;
     }

     public static boolean esitMi(byte[] aB1,byte[] aB2)
     {
          //null olma durumlarini incele
          if ( (aB1 == null) && (aB2 == null))
               return true;
          else if ( (aB1 == null) || (aB2 == null))
               return false;

          if(aB1.length != aB2.length) return false;

          for(int i = 0; i< aB1.length ; i++)
          {
               if(aB1[i]!=aB2[i])
                    return false;
          }

          return true;


     }

     public static boolean esitMi(int[] aB1,int[] aB2)
     {
          //null olma durumlarini incele
          if ( (aB1 == null) && (aB2 == null))
               return true;
          else if ( (aB1 == null) || (aB2 == null))
               return false;

          if(aB1.length != aB2.length) return false;

          for(int i = 0; i< aB1.length ; i++)
          {
               if(aB1[i]!=aB2[i])
                    return false;
          }

          return true;


     }


     private static boolean _esitMi(RDNSequence aRdns1,RDNSequence aRdns2)
     {
          RelativeDistinguishedName[] els1 = aRdns1.elements;
          RelativeDistinguishedName[] els2 = aRdns2.elements;

          if(els1.length != els2.length) return false;

          for(int i = 0; i< els1.length ; i++)
          {
               if(!esitMi(els1[i],els2[i]))
                    return false;
          }
          return true;
     }

     private static boolean _esitMi(RelativeDistinguishedName aRdn1,RelativeDistinguishedName aRdn2)
     {
         AttributeTypeAndValue[] els1 = aRdn1.elements;
         AttributeTypeAndValue[] els2 = aRdn2.elements;

         if(els1.length != els2.length) return false;

         for(int i = 0; i< els1.length ; i++)
         {
              if(!esitMi(els1[i],els2[i]))
                   return false;
         }
         return true;

     }

     private final static int _TAGPS = 19;
     private static boolean _esitMi(AttributeTypeAndValue aAtav1,AttributeTypeAndValue aAtav2)
     {
          if(! aAtav1.type.equals(aAtav2.type))
               return false;
          
          
          if( (aAtav1.value == null) && (aAtav2.value == null) )
               return true;
          else if ( (aAtav1.value == null) || (aAtav2.value == null) )
               return false;
          else if( (aAtav1.value.value == null) && (aAtav2.value.value == null) )
               return true;
          else if ( (aAtav1.value.value == null) || (aAtav2.value.value == null) )
               return false; 
          /** TODO
           * atav'in value'su printableString tipinde ise (yani tag'i 19 ise)
           * karsilastirma asagidaki gibi yapilir. incele !!! bu arada printable
           * String tag'i 19 ama boyle yazma.
           */
          else if ( (aAtav1.value.value[0] == _TAGPS) && (aAtav2.value.value[0] == _TAGPS) )
          {
               Asn1DerDecodeBuffer decBuf1 = new Asn1DerDecodeBuffer(aAtav1.value.value);
               Asn1DerDecodeBuffer decBuf2 = new Asn1DerDecodeBuffer(aAtav2.value.value);
               
               Asn1PrintableString asnStr1 = new Asn1PrintableString();
               Asn1PrintableString asnStr2 = new Asn1PrintableString();
               
               try
               {
                    asnStr1.decode(decBuf1);
                    asnStr2.decode(decBuf2);
               } catch (Exception e)
               {
                    logger.warn("Warning in UtilEsitlikler", e);
                    return false;
               }

/** RFC 3280 Page 113
The character string type PrintableString supports a very basic Latin
character set: the lower case letters 'a' through 'z', upper case
letters 'A' through 'Z', the digits '0' through '9', eleven special
characters ' = ( ) + , - . / : ? and space.
*/
               String str1 = _bosluklariTemizle(asnStr1.value);
               String str2 = _bosluklariTemizle(asnStr2.value);
               
               if(str1 == null && str2 == null)
                    return true;
               if(str1 == null || str2 == null)
                    return false;
               if(str1.equalsIgnoreCase(str2))
                    return true;
               else
                    return false;

          }
          
          else if(! esitMi(aAtav1.value.value,aAtav2.value.value))
               return false;

          return true;
     }

     private static boolean _esitMi(SubjectPublicKeyInfo aSPK1,SubjectPublicKeyInfo aSPK2)
     {
          if(! aSPK1.subjectPublicKey.equals(aSPK2.subjectPublicKey))
               return false;

          if(! esitMi(aSPK1.algorithm,aSPK2.algorithm))
               return false;

          return true;
     }


     private static boolean _esitMi(AlgorithmIdentifier aAID1,AlgorithmIdentifier aAID2)
     {
          if(! aAID1.algorithm.equals(aAID2.algorithm))
               return false;
          
          if( (aAID1.parameters==null) && (aAID2.parameters==null))
               return true;
           else if((aAID1.parameters==null) || (aAID2.parameters==null))
                return false;
           else return esitMi(aAID1.parameters.value,aAID2.parameters.value);
     }
     
    private static boolean _esitMi(Asn1Choice aName1,Asn1Choice aName2)
     {
          //choice ID'ler ayni olmali.
          if(aName1.getChoiceID() != aName2.getChoiceID())
               return false;

          return esitMi(aName1.getElement(),aName2.getElement());
     }
    
    private static String _bosluklariTemizle(String aStr)
    {
         if(aStr == null)
              return null;

         String str = null;
         String [] splittedStr = aStr.trim().split(" ");
         
         for(int i = 0; i < splittedStr.length; i++)
         {
              if(!splittedStr[i].equals(""))
              {
                   if(str == null)
                        str = splittedStr[i].trim();
                   else
                        str = str + " " + splittedStr[i].trim();
              }
         }
         
         return str;

    }
}