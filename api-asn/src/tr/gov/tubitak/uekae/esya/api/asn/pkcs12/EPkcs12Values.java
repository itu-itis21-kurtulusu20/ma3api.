package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.asn.pkcs12._pkcs12Values;

/**
 * User: zeldal.ozdemir
 * Date: 1/25/11
 * Time: 4:03 PM
 */
public class EPkcs12Values {
   public static final Asn1ObjectIdentifier OID_RSADSI = new Asn1ObjectIdentifier(_pkcs12Values.rsadsi);
   public static final Asn1ObjectIdentifier OID_PKCS = new Asn1ObjectIdentifier(_pkcs12Values.pkcs);
   public static final Asn1ObjectIdentifier OID_PKCS_12 = new Asn1ObjectIdentifier(_pkcs12Values.pkcs_12);
   public static final Asn1ObjectIdentifier OID_PKCS_12PBEIDS = new Asn1ObjectIdentifier(_pkcs12Values.pkcs_12PbeIds);
   public static final Asn1ObjectIdentifier OID_PBEWITHSHAAND128BITRC4 = new Asn1ObjectIdentifier(_pkcs12Values.pbeWithSHAAnd128BitRC4);
   public static final Asn1ObjectIdentifier OID_PBEWITHSHAAND40BITRC4 = new Asn1ObjectIdentifier(_pkcs12Values.pbeWithSHAAnd40BitRC4);
   public static final Asn1ObjectIdentifier OID_PBEWITHSHAAND3_KEYTRIPLEDES_CBC = new Asn1ObjectIdentifier(_pkcs12Values.pbeWithSHAAnd3_KeyTripleDES_CBC);
   public static final Asn1ObjectIdentifier OID_PBEWITHSHAAND2_KEYTRIPLEDES_CBC = new Asn1ObjectIdentifier(_pkcs12Values.pbeWithSHAAnd2_KeyTripleDES_CBC);
   public static final Asn1ObjectIdentifier OID_PBEWITHSHAAND128BITRC2_CBC = new Asn1ObjectIdentifier(_pkcs12Values.pbeWithSHAAnd128BitRC2_CBC);
   public static final Asn1ObjectIdentifier OID_PBEWITHSHAAND40BITRC2_CBC = new Asn1ObjectIdentifier(_pkcs12Values.pbewithSHAAnd40BitRC2_CBC );
   public static final Asn1ObjectIdentifier OID_bagtypes = new Asn1ObjectIdentifier(_pkcs12Values.bagtypes );
   public static final Asn1ObjectIdentifier OID_certTypes = new Asn1ObjectIdentifier(_pkcs12Values.certTypes );
   public static final Asn1ObjectIdentifier OID_bagtypes_KeyBag = new Asn1ObjectIdentifier(_pkcs12Values.bagtypes_KeyBag );
   public static final Asn1ObjectIdentifier OID_bagtypes_ShroudedKeyBag = new Asn1ObjectIdentifier(_pkcs12Values.bagtypes_ShroudedKeyBag );
   public static final Asn1ObjectIdentifier OID_bagtypes_CertBag = new Asn1ObjectIdentifier(_pkcs12Values.bagtypes_CertBag );
   public static final Asn1ObjectIdentifier OID_bagtypes_CRLBag = new Asn1ObjectIdentifier(_pkcs12Values.bagtypes_CRLBag );
   public static final Asn1ObjectIdentifier OID_bagtypes_SecretBag = new Asn1ObjectIdentifier(_pkcs12Values.bagtypes_SecretBag );
   public static final Asn1ObjectIdentifier OID_bagtypes_SafeContentsBag = new Asn1ObjectIdentifier(_pkcs12Values.bagtypes_SafeContentsBag );
   public static final Asn1ObjectIdentifier OID_certTypes_x509Certificate = new Asn1ObjectIdentifier(_pkcs12Values.certTypes_x509Certificate );
   public static final Asn1ObjectIdentifier OID_certTypes_sdsiCertificate = new Asn1ObjectIdentifier(_pkcs12Values.certTypes_sdsiCertificate );

}
