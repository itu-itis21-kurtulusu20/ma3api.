package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.RDNSequence;
import tr.gov.tubitak.uekae.esya.asn.x509.RelativeDistinguishedName;
import tr.gov.tubitak.uekae.esya.asn.x509.RoleSyntax;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zeldal.ozdemir
 *         date: Jan 29, 2010
 */
public class ERoleSyntax extends BaseASNWrapper<RoleSyntax> {

    public ERoleSyntax(RoleSyntax aObject) {
        super(aObject);
    }
    public ERoleSyntax(byte[] roleBytes) throws ESYAException {
        super(roleBytes,new RoleSyntax());
    }

    public boolean isRegisteredRole(Asn1ObjectIdentifier role) {
        if( mObject.roleName != null ){
            EGeneralName roleName = new EGeneralName(mObject.roleName);
            if(roleName.getRegisteredID() != null)
                return roleName.getRegisteredID().equals(role);
        }
        return false;
    }
}
