package tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;

import tr.gov.tubitak.uekae.esya.asn.pkixtsp._pkixtspValues;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

public class EPKIXTSPValues {

    public static final Asn1ObjectIdentifier OID_ct_TSTInfo = new Asn1ObjectIdentifier(_pkixtspValues.id_ct_TSTInfo);
    public static final Asn1ObjectIdentifier OID_esya_ts_reqex = new Asn1ObjectIdentifier(_pkixtspValues.id_esya_ts_reqex);
    public static final Asn1ObjectIdentifier OID_ts_policy = new Asn1ObjectIdentifier(_pkixtspValues.id_ts_policy); 
}
