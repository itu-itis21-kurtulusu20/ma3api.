package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.asn.attrcert._attrcertValues;
import tr.gov.tubitak.uekae.esya.asn.x509._ExplicitValues;

/**
 * User: zeldal.ozdemir
 * Date: 3/17/11
 * Time: 9:07 AM
 */
public class EAttrCertValues {
    public static final Asn1ObjectIdentifier oid_pe_ac_auditIdentity = new Asn1ObjectIdentifier(_attrcertValues.id_pe_ac_auditIdentity);
    public static final Asn1ObjectIdentifier oid_pe_aaControls = new Asn1ObjectIdentifier(_attrcertValues.id_pe_aaControls);
    public static final Asn1ObjectIdentifier oid_pe_ac_proxying = new Asn1ObjectIdentifier(_attrcertValues.id_pe_ac_proxying );
    public static final Asn1ObjectIdentifier oid_ce_targetInformation = new Asn1ObjectIdentifier(_attrcertValues.id_ce_targetInformation );
    public static final Asn1ObjectIdentifier oid_ce_noRevAvail = new Asn1ObjectIdentifier(new int[]{ 2, 5, 29, 56 } );
    public static final Asn1ObjectIdentifier oid_aca = new Asn1ObjectIdentifier(_attrcertValues.id_aca );
    public static final Asn1ObjectIdentifier oid_aca_authenticationInfo = new Asn1ObjectIdentifier(_attrcertValues.id_aca_authenticationInfo );
    public static final Asn1ObjectIdentifier oid_aca_accessIdentity = new Asn1ObjectIdentifier(_attrcertValues.id_aca_accessIdentity );
    public static final Asn1ObjectIdentifier oid_aca_chargingIdentity = new Asn1ObjectIdentifier(_attrcertValues.id_aca_chargingIdentity);
    public static final Asn1ObjectIdentifier oid_aca_group = new Asn1ObjectIdentifier(_attrcertValues.id_aca_group);
    public static final Asn1ObjectIdentifier oid_aca_encAttrs = new Asn1ObjectIdentifier(_attrcertValues.id_aca_encAttrs);
    public static final Asn1ObjectIdentifier oid_at_role = new Asn1ObjectIdentifier(_attrcertValues.id_at_role);
    public static final Asn1ObjectIdentifier oid_at_clearance = new Asn1ObjectIdentifier(_attrcertValues.id_at_clearance);
    public static final Asn1ObjectIdentifier oid_ad_ocsp = new Asn1ObjectIdentifier(_ExplicitValues.id_ad_ocsp);
}
