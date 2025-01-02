package tr.gov.tubitak.uekae.esya.api.asn;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

import tr.gov.tubitak.uekae.esya.asn.x509._ExplicitValues;
import tr.gov.tubitak.uekae.esya.asn.x509._ImplicitValues;

/**
 * @author ahmety
 * date: Jan 21, 2010
 */
public class Constants
{
    public static final Asn1ObjectIdentifier IMP_ANY_POLICY             = new Asn1ObjectIdentifier(_ImplicitValues.anyPolicy);
    public static final Asn1ObjectIdentifier EXP_ID_AD_OCSP             = new Asn1ObjectIdentifier(_ExplicitValues.id_ad_ocsp);
    public static final Asn1ObjectIdentifier EXP_ID_AD_CAISSUERS        = new Asn1ObjectIdentifier(_ExplicitValues.id_ad_caIssuers);
    public static final Asn1ObjectIdentifier EXP_ID_AT_SERIAL_NUMBER    = new Asn1ObjectIdentifier(_ExplicitValues.id_at_serialNumber);
    public static final Asn1ObjectIdentifier EXP_ID_EMAIL_ADDRESS       = new Asn1ObjectIdentifier(_ExplicitValues.id_emailAddress);
    public static final Asn1ObjectIdentifier EXP_ID_COMMON_NAME			= new Asn1ObjectIdentifier(_ExplicitValues.id_at_commonName);
    public static final Asn1ObjectIdentifier IMP_ID_KP_OCSPSIGNING      = new Asn1ObjectIdentifier(_ImplicitValues.id_kp_OCSPSigning);
    public static final Asn1ObjectIdentifier IMP_ID_KP_TIMESTAMPING		= new Asn1ObjectIdentifier(_ImplicitValues.id_kp_timeStamping);
}
