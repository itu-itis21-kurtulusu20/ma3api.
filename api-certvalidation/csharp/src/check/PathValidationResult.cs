using System;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check
{
    public class PathValidationResult
    {
        public static readonly PathValidationResult SUCCESS = new PathValidationResult(_enum.SUCCESS, Resource.PVR_SUCCESS);
        public static readonly PathValidationResult SERIALNUMBER_NOT_POSITIVE = new PathValidationResult(_enum.SERIALNUMBER_NOT_POSITIVE, Resource.PVR_SERIALNUMBER_NOT_POSITIVE);
        public static readonly PathValidationResult CERTIFICATE_EXTENSIONS_FAILURE = new PathValidationResult(_enum.CERTIFICATE_EXTENSIONS_FAILURE, Resource.PVR_CERTIFICATE_EXTENSIONS_FAILURE);
        public static readonly PathValidationResult CERTIFICATE_EXPIRED = new PathValidationResult(_enum.CERTIFICATE_EXPIRED, Resource.PVR_CERTIFICATE_EXPIRED);
        public static readonly PathValidationResult SIGNATURE_ALGORITHM_DIFFERENT = new PathValidationResult(_enum.SIGNATURE_ALGORITHM_DIFFERENT, Resource.PVR_SIGNATURE_ALGORITHM_DIFFERENT);
        public static readonly PathValidationResult VERSION_CONTROL_FAILURE = new PathValidationResult(_enum.VERSION_CONTROL_FAILURE, Resource.PVR_VERSION_CONTROL_FAILURE);

        public static readonly PathValidationResult REVOCATION_CONTROL_FAILURE = new PathValidationResult(_enum.REVOCATION_CONTROL_FAILURE, Resource.PVR_REVOCATION_CONTROL_FAILURE);
        public static readonly PathValidationResult CERTIFICATE_REVOKED = new PathValidationResult(_enum.CERTIFICATE_REVOKED, Resource.PVR_CERTIFICATE_REVOKED);

        public static readonly PathValidationResult SIGNATURE_CONTROL_FAILURE = new PathValidationResult(_enum.SIGNATURE_CONTROL_FAILURE, Resource.PVR_SIGNATURE_CONTROL_FAILURE);
        public static readonly PathValidationResult BASICCONSTRAINTS_FAILURE = new PathValidationResult(_enum.BASICCONSTRAINTS_FAILURE, Resource.PVR_BASICCONSTRAINTS_FAILURE);
        public static readonly PathValidationResult CDP_CONTROL_FAILURE = new PathValidationResult(_enum.CDP_CONTROL_FAILURE, Resource.PVR_CDP_CONTROL_FAILURE);
        public static readonly PathValidationResult KEYID_CONTROL_FAILURE = new PathValidationResult(_enum.KEYID_CONTROL_FAILURE, Resource.PVR_KEYID_CONTROL_FAILURE);
        public static readonly PathValidationResult NAMECONSTRAINTS_CONTROL_FAILURE = new PathValidationResult(_enum.NAMECONSTRAINTS_CONTROL_FAILURE, Resource.PVR_NAMECONSTRAINTS_FAILURE);
        public static readonly PathValidationResult PATHLENCONSTRAINTS_FAILURE = new PathValidationResult(_enum.PATHLENCONSTRAINTS_FAILURE, Resource.PVR_PATHLENCONSTRAINTS_FAILURE);
        public static readonly PathValidationResult POLICYCONSTRAINTS_CONTROL_FAILURE = new PathValidationResult(_enum.POLICYCONSTRAINTS_CONTROL_FAILURE, Resource.PVR_POLICYCONSTRAINTS_CONTROL_FAILURE);
        public static readonly PathValidationResult KEYUSAGE_CONTROL_FAILURE = new PathValidationResult(_enum.KEYUSAGE_CONTROL_FAILURE, Resource.PVR_KEYUSAGE_CONTROL_FAILURE);
        public static readonly PathValidationResult NAME_CONTROL_FAILURE = new PathValidationResult(_enum.NAME_CONTROL_FAILURE, Resource.PVR_NAME_CONTROL_FAILURE);

        public static readonly PathValidationResult CRL_EXPIRED = new PathValidationResult(_enum.CRL_EXPIRED, Resource.PVR_CRL_EXPIRED);
        public static readonly PathValidationResult CRL_EXTENSIONS_CONTROL_FAILURE = new PathValidationResult(_enum.CRL_EXTENSIONS_CONTROL_FAILURE, Resource.PVR_CRL_EXTENSIONS_CONTROL_FAILURE);

        public static readonly PathValidationResult CRL_SIGNATURE_CONTROL_FAILURE = new PathValidationResult(_enum.CRL_SIGNATURE_CONTROL_FAILURE, Resource.PVR_CRL_SIGNATURE_CONTROL_FAILURE);
        public static readonly PathValidationResult CRL_KEYUSAGE_CONTROL_FAILURE = new PathValidationResult(_enum.CRL_KEYUSAGE_CONTROL_FAILURE, Resource.PVR_KEYUSAGE_CONTROL_FAILURE);
        public static readonly PathValidationResult CRL_NAME_CONTROL_FAILURE = new PathValidationResult(_enum.CRL_NAME_CONTROL_FAILURE, Resource.PVR_CRL_NAME_CONTROL_FAILURE);

        public static readonly PathValidationResult OCSP_RESPONSESTATUS_CONTROL_FAILURE = new PathValidationResult(_enum.OCSP_RESPONSESTATUS_CONTROL_FAILURE, Resource.PVR_OCSP_RESPONSESTATUS_CONTROL_FAILURE);
        public static readonly PathValidationResult OCSP_SIGNATURE_CONTROL_FAILURE = new PathValidationResult(_enum.OCSP_SIGNATURE_CONTROL_FAILURE, Resource.PVR_OCSP_SIGNATURE_CONTROL_FAILURE);
        public static readonly PathValidationResult OCSP_RESPONSEDATE_EXPIRED = new PathValidationResult(_enum.OCSP_RESPONSEDATE_EXPIRED, Resource.OCSP_RESPONSEDATE_EXPIRED);
        public static readonly PathValidationResult OCSP_RESPONSEDATE_INVALID = new PathValidationResult(_enum.OCSP_RESPONSEDATE_INVALID, Resource.OCSP_RESPONSEDATE_INVALID);


        public static readonly PathValidationResult CRL_FRESHESTCRL_CONTROL_FAILURE = new PathValidationResult(_enum.CRL_FRESHESTCRL_CONTROL_FAILURE, Resource.PVR_CRL_FRESHESTCRL_CONTROL_FAILURE);
        public static readonly PathValidationResult CRL_DELTACRLINDICATOR_CONTROL_FAILURE = new PathValidationResult(_enum.CRL_DELTACRLINDICATOR_CONTROL_FAILURE, Resource.PVR_CRL_DELTACRLINDICATOR_CONTROL_FAILURE);

        //public static readonly PathValidationResult POLICYCONSTRAINTS_CONTROL_FAILURE = new PathValidationResult(_enum.POLICYCONSTRAINTS_CONTROL_FAILURE);
        public static readonly PathValidationResult POLICYMAPPING_CONTROL_FAILURE = new PathValidationResult(_enum.POLICYMAPPING_CONTROL_FAILURE, Resource.PVR_POLICYMAPPING_CONTROL_FAILURE);
        //public static readonly PathValidationResult NAMECONSTRAINTS_CONTROL_FAILURE = new PathValidationResult(_enum.NAMECONSTRAINTS_CONTROL_FAILURE);

        public static readonly PathValidationResult INVALID_PATH = new PathValidationResult(_enum.INVALID_PATH, Resource.PVR_INVALID_PATH);
        public static readonly PathValidationResult INCOMPLETE_VALIDATION = new PathValidationResult(_enum.INCOMPLETE_VALIDATION, Resource.PVR_INCOMPLETE_VALIDATION);
        public static readonly PathValidationResult QCC_NO_STATEMENT_ID = new PathValidationResult(_enum.QCC_NO_STATEMENT_ID, Resource.PVR_QCC_NO_STATEMENT_ID);
        public static readonly PathValidationResult QCC_NO_USER_NOTICE = new PathValidationResult(_enum.QCC_NO_USER_NOTICE,Resource.PVR_QCC_NO_USER_NOTICE);
        public static readonly PathValidationResult EXTENDED_KEYUSAGE_CONTROL_FAILURE = new PathValidationResult(_enum.EXTENDED_KEYUSAGE_CONTROL_FAILURE, Resource.PVR_EXTENDED_KEYUSAGE_CONTROL_FAILURE);

        public static readonly PathValidationResult WRONG_FORMAT_QCC_STATEMENT = new PathValidationResult(_enum.WRONG_FORMAT_QCC_STATEMENT, Resource.WRONG_FORMAT_QCC_STATEMENT);

        enum _enum
        {
            SUCCESS = 0,

            SERIALNUMBER_NOT_POSITIVE = 1,
            CERTIFICATE_EXTENSIONS_FAILURE = 2,
            CERTIFICATE_EXPIRED = 3,
            SIGNATURE_ALGORITHM_DIFFERENT = 4,
            VERSION_CONTROL_FAILURE = 5,

            REVOCATION_CONTROL_FAILURE = 6,
            CERTIFICATE_REVOKED = 7,

            SIGNATURE_CONTROL_FAILURE = 8,
            BASICCONSTRAINTS_FAILURE = 9,
            CDP_CONTROL_FAILURE = 10,
            KEYID_CONTROL_FAILURE = 11,
            NAMECONSTRAINTS_CONTROL_FAILURE = 12,
            PATHLENCONSTRAINTS_FAILURE = 13,
            POLICYCONSTRAINTS_CONTROL_FAILURE = 14,
            KEYUSAGE_CONTROL_FAILURE = 15,
            NAME_CONTROL_FAILURE = 16,

            CRL_EXPIRED = 17,
            CRL_EXTENSIONS_CONTROL_FAILURE = 18,

            CRL_SIGNATURE_CONTROL_FAILURE = 19,
            CRL_KEYUSAGE_CONTROL_FAILURE = 20,
            CRL_NAME_CONTROL_FAILURE = 21,

            OCSP_RESPONSESTATUS_CONTROL_FAILURE = 22,
            OCSP_SIGNATURE_CONTROL_FAILURE = 23,
            OCSP_RESPONSEDATE_EXPIRED = 24,
            OCSP_RESPONSEDATE_INVALID = 25,

            CRL_FRESHESTCRL_CONTROL_FAILURE = 26,
            CRL_DELTACRLINDICATOR_CONTROL_FAILURE = 27,

            //POLICYCONSTRAINTS_CONTROL_FAILURE = 26,
            POLICYMAPPING_CONTROL_FAILURE = 28,
            //NAMECONSTRAINTS_CONTROL_FAILURE = 28,

            INVALID_PATH = 29,
            INCOMPLETE_VALIDATION = 30,

            QCC_NO_STATEMENT_ID = 31,
            QCC_NO_USER_NOTICE = 32,
            EXTENDED_KEYUSAGE_CONTROL_FAILURE=33,
            WRONG_FORMAT_QCC_STATEMENT = 34
        }

        readonly _enum mCode;
        readonly String mMessageId;
        
        PathValidationResult(_enum aCode, String aMessageId)
        {
            mCode = aCode;
            mMessageId = aMessageId;
        }
        
        public int getCode()
        {
            return (int)mCode;
        }

        public String getMessage()
        {
            return Resource.message(mMessageId);
        }

        public override string ToString()
        {
            return getMessage();
        }
    }
}
