/* TUBITAK UEKAE - Muhammed Serdar SORAN */

package tr.gov.tubitak.uekae.esya.asn.util;

import com.objsys.asn1j.runtime.Asn1ConsVioException;
import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1OpenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.x509.AttributeTypeAndValue;

import java.io.IOException;

public class UtilAtav
{
     ////
     private static final Logger LOGCU = LoggerFactory.getLogger(UtilAtav.class);

     public static String atavValue2String (AttributeTypeAndValue atav)
     {
    	 IkiliAttributeTandV ik = atav2IkiliAttributeTandV(atav);
    	 return ik.stringValue();
     }
     
     public static String atav2String (AttributeTypeAndValue atav)
     {

          IkiliAttributeTandV ik = atav2IkiliAttributeTandV(atav);

          return ik.mst + "=" + ik.stringValue();

     }
     
     private static IkiliAttributeTandV atav2IkiliAttributeTandV(AttributeTypeAndValue atav)
     {
    	 IkiliAttributeTandV ik = null;
         try
         {
              ik = OID_tipEslestirmeleri.tipDon(atav.type);
         } catch (Asn1Exception ex)
         {
              //Buraya gelmesi durumunda atav icindeki oid taninmiyor demektir.
              //Bu oid'i yaklasik bir sekilde ekleyerek yeniden deneyelim.
              LOGCU.info("Yeni bir AttributeTypeAndValue eklenecek.", ex);
              try
              {
                   OID_tipEslestirmeleri.listeyeAtavEkle(atav);
                   ik = OID_tipEslestirmeleri.tipDon(atav.type);
              } catch (Asn1Exception ex1)
              {
                   throw new ESYARuntimeException("AttributeTypeAndValue String'e cevrilemedi....", ex1);
              }
         }

         try
         {
              Asn1DerEncodeBuffer enbuf =
                  new Asn1DerEncodeBuffer();

              atav.value.encode(enbuf);
              //byte[] encoded = enbuf.getMsgCopy();
              //System.out.println(gnu.crypto.util.Util.toString(encoded));

              Asn1DerDecodeBuffer buf =
                  new Asn1DerDecodeBuffer(enbuf.getInputStream());

              ik.mType.decode(buf);
         } catch (IOException ex)
         {
              LOGCU.error("Bir hata cikti. Attribute dogru alinamamis olabilir.",ex);
         } catch (Asn1ConsVioException ex)
         {
              LOGCU.error("Constraintlerle ilgili bir hata. Attribute dogru alinamamis olabilir (kucuk ihtimal...)",ex);
         } catch (Asn1Exception ex)
         {
              LOGCU.error("Bir hata cikti. Attribute dogru alinamamis olabilir.",ex);
         }
         return ik;
     }
     


     public static AttributeTypeAndValue string2atav (String atav, boolean aTurkce) throws
         Asn1Exception
     {
          int k = atav.indexOf('=');
          /** @todo k==-1 ise exception at... */
          if(k==-1)
          {
               throw new Asn1Exception("Gecersiz Name formati.");
          }
          String bas = atav.substring(0, k).trim();
          String son = atav.substring(k + 1).trim();

          IkiliAttributeTandV ik = OID_tipEslestirmeleri.atavDon(bas, son, aTurkce);
          //Olusturulan atav'lardan RelativeDistinguishedName yapisini olusturup donelim.

          Asn1OpenType openT = null;
//          Asn1DerEncodeBuffer enbuf = new Asn1DerEncodeBuffer();
//          ik.mType.encode(enbuf);
//          openT = new Asn1OpenType();

          try
          {
//               openT.decode(new Asn1DerDecodeBuffer(enbuf.getInputStream()));
               openT = UtilOpenType.toOpenType(ik.mType);
          } catch (IOException ex)
          {
              LOGCU.error("Error in UtilAtav", ex);
          }

          AttributeTypeAndValue r = new AttributeTypeAndValue(ik.mOid, openT);
          return r;
     }

}