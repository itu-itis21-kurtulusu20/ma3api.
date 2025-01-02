using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ERoleSyntax : BaseASNWrapper<RoleSyntax>
    {
        public ERoleSyntax(RoleSyntax aObject)
            : base(aObject)
        {
            //super(aObject);
        }
        public ERoleSyntax(byte[] aRoleBytes)
            : base(aRoleBytes, new RoleSyntax())
        {
            //super(roleBytes,new RoleSyntax());
        }

        public bool isRegisteredRole(Asn1ObjectIdentifier aRole)
        {
            if (mObject.roleName != null)
            {
                EGeneralName roleName = new EGeneralName(mObject.roleName);
                if (roleName.getRegisteredID() != null)
                    return roleName.getRegisteredID().Equals(aRole);
            }
            return false;
        }

    }
}
