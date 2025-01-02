package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.asn.esya._esyaValues;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 5/11/11 - 1:33 PM <p>
 <b>Description</b>: <br>
 implemented instead of ESYAOID.
 */
public class EESYAOID {
   public static final Asn1ObjectIdentifier oid_uekae = new Asn1ObjectIdentifier(_esyaValues.id_uekae);
   public static final Asn1ObjectIdentifier oid_esya = new Asn1ObjectIdentifier(_esyaValues.id_esya);
   public static final Asn1ObjectIdentifier oid_servisler = new Asn1ObjectIdentifier(_esyaValues.id_servisler);
   public static final Asn1ObjectIdentifier oid_refno = new Asn1ObjectIdentifier(_esyaValues.id_refno);
   public static final Asn1ObjectIdentifier oid_kartSeriNo = new Asn1ObjectIdentifier(_esyaValues.id_kartSeriNo);
   public static final Asn1ObjectIdentifier oid_kartUreticiNo = new Asn1ObjectIdentifier(_esyaValues.id_kartUreticiNo);
   public static final Asn1ObjectIdentifier oid_sablonNo = new Asn1ObjectIdentifier(_esyaValues.id_sablonNo);
   public static final Asn1ObjectIdentifier oid_sertTalepNo = new Asn1ObjectIdentifier(_esyaValues.id_sertTalepNo);
   public static final Asn1ObjectIdentifier oid_cmpResponseWaitTime = new Asn1ObjectIdentifier(_esyaValues.id_cmpResponseWaitTime);
   public static final Asn1ObjectIdentifier oid_cmpYeniSertifikaDurumu = new Asn1ObjectIdentifier(_esyaValues.id_cmpYeniSertifikaDurumu);
   public static final Asn1ObjectIdentifier oid_cmpTekIstekHatadaTumIstekGeriAl = new Asn1ObjectIdentifier(_esyaValues.id_cmpTekIstekHatadaTumIstekGeriAl);

   public static final Asn1ObjectIdentifier oid_cmpServiceConfigType = new Asn1ObjectIdentifier(_esyaValues.id_cmpServiceConfigType);

   public static final Asn1ObjectIdentifier oid_cmpCAControlReqMsg = new Asn1ObjectIdentifier(_esyaValues.id_cmpCAControlReqMsg);
   public static final Asn1ObjectIdentifier oid_cmpCAControlRepMsg = new Asn1ObjectIdentifier(_esyaValues.id_cmpCAControlRepMsg);

   public static final Asn1ObjectIdentifier oid_siparisDetayNo = new Asn1ObjectIdentifier(_esyaValues.id_siparisDetayNo);
   public static final Asn1ObjectIdentifier oid_cvcSablonTipi = new Asn1ObjectIdentifier(_esyaValues.id_cvcSablonTipi);
   public static final Asn1ObjectIdentifier oid_ESYAyonetici = new Asn1ObjectIdentifier(_esyaValues.id_ESYAyonetici);
   public static final Asn1ObjectIdentifier oid_ESYAkayitci = new Asn1ObjectIdentifier(_esyaValues.id_ESYAkayitci);
   public static final Asn1ObjectIdentifier oid_ESYAdenetci = new Asn1ObjectIdentifier(_esyaValues.id_ESYAdenetci);
   public static final Asn1ObjectIdentifier oid_TK_nesoid = new Asn1ObjectIdentifier(_esyaValues.id_TK_nesoid); // TK: Telekomünikasyon Kurumu
   public static final Asn1ObjectIdentifier oid_TK_nesoid_2 = new Asn1ObjectIdentifier(_esyaValues.id_TK_nesoid_2);

   public static final Asn1ObjectIdentifier oid_istemci = new Asn1ObjectIdentifier(_esyaValues.id_istemci);
   public static final Asn1ObjectIdentifier oid_kripto = new Asn1ObjectIdentifier(_esyaValues.id_kripto);
   public static final Asn1ObjectIdentifier oid_esyapwri = new Asn1ObjectIdentifier(_esyaValues.id_esyapwri);
   public static final Asn1ObjectIdentifier oid_ESYA_SDO = new Asn1ObjectIdentifier(_esyaValues.id_ESYA_SDO);
   public static final Asn1ObjectIdentifier oid_ESYA_CardSerialNumber = new Asn1ObjectIdentifier(_esyaValues.id_ESYA_CardSerialNumber);

}
