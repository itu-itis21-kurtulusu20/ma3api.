package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralNames;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.RoleSyntax;

/**
 * User: zeldal.ozdemir
 * Date: 3/23/11
 * Time: 2:54 PM
 */
public class ERoleSyntax extends BaseASNWrapper<RoleSyntax>{
    public ERoleSyntax(RoleSyntax aObject) {
        super(aObject);
    }

    public ERoleSyntax(byte[] aBytes) throws ESYAException {
        super(aBytes, new RoleSyntax());
    }

    public ERoleSyntax(EGeneralNames roleAuthority, EGeneralName roleName ) {
        super(new RoleSyntax(roleAuthority.getObject(),roleName.getObject()));
    }

    public ERoleSyntax( EGeneralName roleName ) {
        super(new RoleSyntax(roleName.getObject()));
    }

    public EGeneralNames getRoleAuthority() {
        return new EGeneralNames(mObject.roleAuthority);
    }

    public void setRoleAuthority(EGeneralNames roleAuthority) {
        mObject.roleAuthority = roleAuthority.getObject();
    }

    public EGeneralName getRoleName() {
        return new EGeneralName(mObject.roleName);
    }

    public void setRoleName(EGeneralName roleName) {
        mObject.roleName = roleName.getObject();
    }
}
