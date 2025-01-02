using System;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.cvc.oids
{
    public class CVCRoleIDs
    {
        public static readonly CVCRoleIDs DEV_ICC = new CVCRoleIDs(0x00, "CV Certificates for ICD");
        public static readonly CVCRoleIDs DEV_IFD = new CVCRoleIDs(0x01, "CV Certificates for IFD");
        public static readonly CVCRoleIDs RCA_ICC = new CVCRoleIDs(0x11, "Cross certificates for ICC");
        public static readonly CVCRoleIDs CA_ICC_CS = new CVCRoleIDs(0x12, "CV certificates of CA for ICC");
        public static readonly CVCRoleIDs RCA_IFD = new CVCRoleIDs(0x21, "Cross certificates for IFD");
        public static readonly CVCRoleIDs CA_IFD_CS = new CVCRoleIDs(0x22, "CV certificates of CA for IFD");
        public static readonly CVCRoleIDs RCA_ICC_IFD = new CVCRoleIDs(0x31, "Cross certificates for ICC/IFD");
        public static readonly CVCRoleIDs CA_ICC_IFD_CS = new CVCRoleIDs(0x32, "CV certificates of CA for ICC/IFD");


        private byte roleID;
        private String description;

        CVCRoleIDs(int roleID, String description)
        {
            this.description = description;

            this.roleID = (byte)roleID;
        }

        public byte getRoleID()
        {
            return roleID;
        }

        public String getDescription()
        {
            return description;
        }

        public static bool isSubAuth(ECha cha)
        {
            byte[] chaValues = cha.getByteValues();
            if (chaValues.Length != 7)
                throw new CVCVerifierException("Length of CHA is " + chaValues.Length + " expected:7");
            byte roleID = chaValues[6];
            if (roleID == CVCRoleIDs.CA_ICC_CS.getRoleID()
                    || roleID == CVCRoleIDs.CA_IFD_CS.getRoleID()
                    )
                return true;
            return false;
        }

        public static bool isDevice(ECha cha)
        {
            byte[] chaValues = cha.getByteValues();
            /*        if (chaValues.length != 7)
                        throw new CVCVerifierException("Length of CHA is " + chaValues.length + " expected:7");
                    byte roleID = chaValues[6];
                    if(roleID == CVCRoleIDs.DEV_ICC.getRoleID()
                            || roleID == CVCRoleIDs.DEV_IFD.getRoleID()
                            )
                        return true;*/
            return true; // since its not like that
        }

        public static bool isRootAuth(ECha cha)
        {
            byte[] chaValues = cha.getByteValues();
            if (chaValues.Length != 7)
                throw new CVCVerifierException("Length of CHA is " + chaValues.Length + " expected:7");
            byte roleID = chaValues[6];
            return roleID == CVCRoleIDs.CA_ICC_IFD_CS.getRoleID();
        }
    }
}
