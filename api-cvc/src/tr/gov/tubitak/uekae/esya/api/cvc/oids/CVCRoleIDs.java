package tr.gov.tubitak.uekae.esya.api.cvc.oids;

import tr.gov.tubitak.uekae.esya.api.asn.cvc.ECha;
import tr.gov.tubitak.uekae.esya.api.cvc.CVCVerifierException;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 11/21/11 - 11:56 AM <p>
 <b>Description</b>: <br>
 */
public enum CVCRoleIDs {
    DEV_ICC(0x00, "CV Certificates for ICD"),
    DEV_IFD(0x01, "CV Certificates for IFD"),
    RCA_ICC(0x11, "Cross certificates for ICC"),
    CA_ICC_CS(0x12, "CV certificates of CA for ICC"),
    RCA_IFD(0x21, "Cross certificates for IFD"),
    CA_IFD_CS(0x22, "CV certificates of CA for IFD"),
    RCA_ICC_IFD(0x31, "Cross certificates for ICC/IFD"),
    CA_ICC_IFD_CS(0x32, "CV certificates of CA for ICC/IFD"),


/*                        {"00", "Role of ICD Key for device authentication",
                            "The role is checked when starting an authentication procedure of the ICC"},

                    {"01", "Role of IFD Key for device authentication",
                            "Role checked when calling an applicable external authentication"},

                    {"11", "Cross certificates for ICC",
                            "Role of ICC Root Certification authority and (delegated) certification authority (for intermediate certificate verification)"},

                    {"12", "CV certificates of CA for ICC",
                            "Role of ICC Root Certification authority and (delegated) certification authority (for intermediate certificate verification)"},

                    {"21", "Cross certificates for IFD",
                            "Role of IFD Root Certification authority and (delegated) certification authority (for intermediate certificate verification)"},

                    {"22", "CV certificates of CA for IFD",
                            "Role of IFD Root Certification authority and (delegated) certification authority (for intermediate certificate verification)"},

                    {"31", "Cross certificates for ICC/IFD",
                            "Role of ICC/IFD Root Certification authority and (delegated) certification authority (for intermediate certificate verification)"},

                    {"32", "CV certificates of CA for ICC/IFD",
                            "Role of ICC/IFD Root Certification authority and (delegated) certification authority (for intermediate certificate verification)"}*/;
    private byte roleID;
    private String description;

    CVCRoleIDs(int roleID, String description) {
        this.description = description;

        this.roleID = (byte) roleID;
    }

    public byte getRoleID() {
        return roleID;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isSubAuth(ECha cha) throws CVCVerifierException {
        byte[] chaValues = cha.getByteValues();
        if (chaValues.length != 7)
            throw new CVCVerifierException("Length of CHA is " + chaValues.length + " expected:7");
        byte roleID = chaValues[6];
        if(roleID == CVCRoleIDs.CA_ICC_CS.getRoleID()
                || roleID == CVCRoleIDs.CA_IFD_CS.getRoleID()
                )
            return true;
        return false;
    }

    public static boolean isDevice(ECha cha) throws CVCVerifierException {
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

    public static boolean isRootAuth(ECha cha) throws CVCVerifierException {
        byte[] chaValues = cha.getByteValues();
        if (chaValues.length != 7)
            throw new CVCVerifierException("Length of CHA is " + chaValues.length + " expected:7");
        byte roleID = chaValues[6];
        return roleID == CVCRoleIDs.CA_ICC_IFD_CS.getRoleID();
    }
}
