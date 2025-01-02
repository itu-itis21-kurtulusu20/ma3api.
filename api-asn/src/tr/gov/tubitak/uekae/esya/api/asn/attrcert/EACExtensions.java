package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1Type;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAuthorityInfoAccessSyntax;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAuthorityKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRLDistributionPoints;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.asn.attrcert.Targets;
import tr.gov.tubitak.uekae.esya.asn.attrcert._attrcertValues;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

/**
 * @author ayetgin
 */
public class EACExtensions extends BaseASNWrapper<Extensions>
{
    public static final Asn1ObjectIdentifier OID_AUDIT_IDENTITY = new Asn1ObjectIdentifier(_attrcertValues.id_pe_ac_auditIdentity);
    public static final Asn1ObjectIdentifier OID_AC_TARGETING = new Asn1ObjectIdentifier(_attrcertValues.id_ce_targetInformation);
    public static final Asn1ObjectIdentifier OID_AUTHORITY_KEY_ID = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_authorityKeyIdentifier);
    public static final Asn1ObjectIdentifier OID_AUTHORITY_INFO_ACCESS = new Asn1ObjectIdentifier(_ImplicitValues.id_pe_authorityInfoAccess);
    public static final Asn1ObjectIdentifier OID_CRL_DIST_POINTS = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLDistributionPoints);
    public static final Asn1ObjectIdentifier OID_NO_REVOCATION_AVAIL = EAttrCertValues.oid_ce_noRevAvail;

    public EACExtensions(Extensions aObject)
    {
        super(aObject);
    }

    public EACExtensions(EExtension[] aExtensions)
    {
        super(new Extensions());
        mObject.elements = unwrapArray(aExtensions);
    }

    public int getExtensionCount()
    {
        return mObject.getLength();
    }

    public EExtension getExtension(int aIndex)
    {
        return new EExtension(mObject.elements[aIndex]);
    }

    public EExtension getAuditIdentity()
    {
        return getExtension(OID_AUDIT_IDENTITY);
    }

    public ETargets getTargets()
    {
        return wrap(OID_AC_TARGETING, new Targets(), ETargets.class);
    }

    public EAuthorityKeyIdentifier getAuthorityKeyIdentifier()
    {
        return wrap(OID_AUTHORITY_KEY_ID, new AuthorityKeyIdentifier(), EAuthorityKeyIdentifier.class);
    }

    public EAuthorityInfoAccessSyntax getAuthorityInfoAccess()
    {
        return wrap(OID_AUTHORITY_INFO_ACCESS, new AuthorityInfoAccessSyntax(), EAuthorityInfoAccessSyntax.class);
    }

    public ECRLDistributionPoints getCRLDistributionPoints(){
        return wrap(OID_CRL_DIST_POINTS, new CRLDistributionPoints(), ECRLDistributionPoints.class);
    }

    public EExtension getNoRevocationAvailable(){
        return getExtension(OID_NO_REVOCATION_AVAIL);
    }

    public EExtension getExtension(Asn1ObjectIdentifier aOID)
    {
        Extension[] extAr = mObject.elements;
        for (Extension extension : extAr)
            if (extension.extnID.equals(aOID)) {
                return new EExtension(extension);
            }
        return null;
    }

    private <W extends BaseASNWrapper, T extends Asn1Type> W wrap(Asn1ObjectIdentifier aOid, T aObject, Class<W> aWrappper)
    {
        T t = decode(aOid, aObject);
        if (t != null) {
            try {
                return aWrappper.getConstructor(aObject.getClass()).newInstance(t);
            }
            catch (Exception x) {
                logger.error("Constructor bulunamadı! " + x);
            }
        }
        return null;
    }


    private <T extends Asn1Type> T decode(Asn1ObjectIdentifier aOid, T aObject)
    {
        try {
            EExtension ext = getExtension(aOid);
            if (ext != null) {
                aObject = (T) AsnIO.derOku(aObject,new Asn1DerDecodeBuffer(ext.getObject().extnValue.value));
                return aObject;
            }
        }
        catch (Exception x) {
            logger.debug(x.getMessage(), x);
        }
        return null;
    }

}
