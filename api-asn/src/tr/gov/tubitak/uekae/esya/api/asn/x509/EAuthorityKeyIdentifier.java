package tr.gov.tubitak.uekae.esya.api.asn.x509;


import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.AuthorityKeyIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import java.math.BigInteger;

/**
 * @author ayetgin
 */
public class EAuthorityKeyIdentifier
        extends BaseASNWrapper<AuthorityKeyIdentifier> 
        implements ExtensionType
{

    public EAuthorityKeyIdentifier(AuthorityKeyIdentifier aObject)
    {
        super(aObject);
    }

    public EAuthorityKeyIdentifier(Asn1OctetString aKeyID) throws ESYAException
    {
        this(new AuthorityKeyIdentifier(aKeyID, null, null));
    }

    public EAuthorityKeyIdentifier(byte[] aBytes) throws ESYAException {
        super(aBytes, new AuthorityKeyIdentifier());
    }

    public byte[] getKeyIdentifier(){
        return (mObject.keyIdentifier==null) ? null : mObject.keyIdentifier.value;
    }
    public EGeneralNames getAuthorityCertIssuer(){
        return (mObject.authorityCertIssuer==null) ? null : new EGeneralNames(mObject.authorityCertIssuer);

    }
    public BigInteger getAuthorityCertSerialNumber(){
        return (mObject.authorityCertSerialNumber==null) ? null : mObject.authorityCertSerialNumber.value; 
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_authorityKeyIdentifier, aCritic, this);
    }

    @Override
    public String toString()
    {
         StringBuilder result = new StringBuilder();
         
         if (mObject.keyIdentifier != null)
         {
              result.append(CertI18n.message(CertI18n.AKI_ID) + "=" + mObject.keyIdentifier.toString() + "\n");
         }
         if (mObject.authorityCertIssuer != null)
         {
              result.append(CertI18n.message(CertI18n.AKI_ISSUER) + "=" +
                       UtilName.name2String(((Name) mObject.authorityCertIssuer.elements[0].getElement())) + "\n");
         }
         if (mObject.authorityCertSerialNumber != null)
         {
              result.append(CertI18n.message(CertI18n.AKI_SERIAL) + "=" +
                       mObject.authorityCertSerialNumber.value.toString() + "\n");
         }
         return result.toString();
    }
}
