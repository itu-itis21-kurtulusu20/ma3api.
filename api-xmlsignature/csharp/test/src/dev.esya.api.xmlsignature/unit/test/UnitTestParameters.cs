using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common.crypto;

namespace tr.gov.tubitak.esya.api.xmlsignature.unit.test
{
    public class UnitTestParameters
    {
        public static readonly String SIGNATURE_ALGORITHM = Algorithms.SIGNATURE_RSA_SHA256;

        public const int SMARTCARD_SLOTNO0 = 0;
        public const int SMARTCARD_SLOTNO1 = 1;
        public const int SMARTCARD_SLOTNO2 = 2;
        public const int SMARTCARD_SLOTNO_REVOKED = 3;

        public const String TEST_EDEFTER_FILE_NAME = "/testdefter.xml";
        public const String TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME = "/xades_testdefter_bes_signed.xml";
        public const String TEST_EDEFTER_EST_ENVELOPED_SIG_FILE_NAME = "/xades_testdefter_est_signed.xml";
        public const String TEST_EDEFTER_ESC_ENVELOPED_SIG_FILE_NAME = "/xades_testdefter_esc_signed.xml";
        public const String TEST_EDEFTER_ESX_ENVELOPED_SIG_FILE_NAME = "/xades_testdefter_esx_signed.xml";
        public const String TEST_EDEFTER_ESXL_ENVELOPED_SIG_FILE_NAME = "/xades_testdefter_esxl_signed.xml";
        public const String TEST_EDEFTER_ESA_ENVELOPED_SIG_FILE_NAME = "/xades_testdefter_esa_signed.xml";

        public const String BES_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_bes_revoked_enveloping.xml";
        public const String BES_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_bes_revoked_enveloped.xml";
        public const String BES_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_bes_revoked_detached.xml";

        public const  String BES_ENVELOPING_SIG_FILE_NAME = "/xades_bes_enveloping.xml";
        public const String BES_ENVELOPED_SIG_FILE_NAME = "/xades_bes_enveloped.xml";
        public const String BES_DETACHED_SIG_FILE_NAME = "/xades_bes_detached.xml";

        public const String EPES_ENVELOPING_SIG_FILE_NAME = "/xades_epes_enveloping.xml";
        public const String EPES_ENVELOPED_SIG_FILE_NAME = "/xades_epes_enveloped.xml";
        public const String EPES_DETACHED_SIG_FILE_NAME = "/xades_epes_detached.xml";

        public const String EST_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_est_revoked_enveloping.xml";
        public const String EST_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_est_revoked_enveloped.xml";
        public const String EST_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_est_revoked_detached.xml";

        public const String EST_ENVELOPING_SIG_FILE_NAME = "/xades_est_enveloping.xml";
        public const String EST_ENVELOPED_SIG_FILE_NAME = "/xades_est_enveloped.xml";
        public const String EST_DETACHED_SIG_FILE_NAME = "/xades_est_detached.xml";

        public const String ESC_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_esc_revoked_enveloping.xml";
        public const String ESC_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_esc_revoked_enveloped.xml";
        public const String ESC_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_esc_revoked_detached.xml";

        public const String ESC_ENVELOPING_SIG_FILE_NAME = "/xades_esc_enveloping.xml";
        public const String ESC_ENVELOPED_SIG_FILE_NAME = "/xades_esc_enveloped.xml";
        public const String ESC_DETACHED_SIG_FILE_NAME = "/xades_esc_detached.xml";

        public const String ESX_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_esx_revoked_enveloping.xml";
        public const String ESX_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_esx_revoked_enveloped.xml";
        public const String ESX_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_esx_revoked_detached.xml";

        public const String ESX_ENVELOPING_SIG_FILE_NAME = "/xades_esx_enveloping.xml";
        public const String ESX_ENVELOPED_SIG_FILE_NAME = "/xades_esx_enveloped.xml";
        public const String ESX_DETACHED_SIG_FILE_NAME = "/xades_esx_detached.xml";

        public const String ESXL_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_esxl_revoked_enveloping.xml";
        public const String ESXL_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_esxl_revoked_enveloped.xml";
        public const String ESXL_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_esxl_revoked_detached.xml";

        public const String ESXL_ENVELOPING_SIG_FILE_NAME = "/xades_esxl_enveloping.xml";
        public const String ESXL_ENVELOPED_SIG_FILE_NAME = "/xades_esxl_enveloped.xml";
        public const String ESXL_DETACHED_SIG_FILE_NAME = "/xades_esxl_detached.xml";

        public const String ESA_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_esa_revoked_enveloping.xml";
        public const String ESA_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_esa_revoked_enveloped.xml";
        public const String ESA_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_esa_revoked_detached.xml";

        public const String ESA_ENVELOPING_SIG_FILE_NAME = "/xades_esa_enveloping.xml";
        public const String ESA_ENVELOPED_SIG_FILE_NAME = "/xades_esa_enveloped.xml";
        public const String ESA_DETACHED_SIG_FILE_NAME = "/xades_esa_detached.xml";

        public const String ESA_ARCHIVE_TS_ENVELOPING_SIG_FILE_NAME = "/xades_esa_ts_added_enveloping.xml";
        public const String ESA_ARCHIVE_TS_ENVELOPED_SIG_FILE_NAME = "/xades_esa_ts_added_enveloped.xml";
        public const String ESA_ARCHIVE_TS_DETACHED_SIG_FILE_NAME = "/xades_esa_ts_added_detached.xml";

        public const String PARALLEL_ENVELOPED_SIG_FILE_NAME = "/xades_parallel_enveloped.xml";
        public const String PARALLEL_ENVELOPING_SIG_FILE_NAME = "/xades_parallel_enveloping.xml";
        public const String COUNTER_ENVELOPING_SIG_FILE_NAME = "/xades_counter_enveloping.xml";
        public const String CREATED_COUNTER_ON_PARALLEL_SIG_FILE_NAME = "/xades_created_counter_on_parallel_enveloping.xml";
        public const String COUNTER_ON_PARALLEL_SIG_FILE_NAME = "/xades_counter_added_on_parallel_enveloping.xml";
        public const String PARALLEL_ADDED_SIG_FILE_NAME = "/xades_parallel_added_on_signature_enveloping.xml";


        public const String BES_ENVELOPING_SIGNED_WITH_FREE_LICENSE = "/xades_bes_enveloping_with_freelicense.xml";
        public const String BES_ENVELOPED_SIGNED_WITH_FREE_LICENSE = "/xades_bes_enveloped_with_freelicense.xml";
        public const String BES_DETACHED_SIGNED_WITH_FREE_LICENSE = "/xades_bes_detached_with_freelicense.xml";

        public const String EST_ENVELOPING_UPGRADED_WITH_TEST_LICENSE = "/xades_est_enveloping_with_testlicense.xml";
        public const String EST_ENVELOPED_UPGRADED_WITH_TEST_LICENSE = "/xades_est_enveloped_with_testlicense.xml";
        public const String EST_DETACHED_UPGRADED_WITH_TEST_LICENSE = "/xades_est_detached_with_testlicense.xml";

        //Revocation control from OCSP
        public const String PFX_FILE = "QCA1_2.p12";
        public const String PFX_PASSWORD = "123456";
        //Revocation control from CRL
        public const String PFX_2_FILE = "QCA1_1.p12";
        public const String PFX_2_PASSWORD = "123456";
        //Monetary Limit.
        public const String PFX_3_FILE = "QCA1_19.p12";
        public const String PFX_3_PASSWORD = "123456";
        //Revoked @ CRL
        public const String PFX_REVOKED_FILE = "QCA1_10.p12";
        public const String PFX_REVOKED_PASSWORD = "123456";

        public const String ERROR_SIG_FILE_NAME = "/";

        public const String PLAINFILENAME= "../../../docs/samples/signatures/sample.txt";
        public const String PLAINFILENAME2="/sample2.txt";
        public const String PLAINFILEMIMETYPE="text/plain";
        public const String PLAINFILE2MIMETYPE="text/plain";

        public const String POLICY_FILE_NAME2 = "xades_2_16_792_1_61_0_1_5070_3_1_1.xml";
        public const String POLICY_FILE_NAME3 = "xades_2_16_792_1_61_0_1_5070_3_2_1.xml";
        public const String POLICY_FILE_NAME4 = "xades_2_16_792_1_61_0_1_5070_3_3_1.xml";

        public static int[] OID_POLICY_P2 = new int[]{  2, 16, 792, 1, 61, 0, 1, 5070, 3, 1, 1};
        public static int[] OID_POLICY_P3 = new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 2, 1 };
        public static int[] OID_POLICY_P4 = new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 3, 1 };

        public const String PROFILE2_ENVELOPING_SIG_FILE_NAME = "/xades_p2_enveloping.xml";
        public const String PROFILE2_ENVELOPED_SIG_FILE_NAME = "/xades_p2_enveloped.xml";
        public const String PROFILE2_DETACHED_SIG_FILE_NAME = "/xades_p2_detached.xml";

        public const String PROFILE3_ENVELOPING_SIG_FILE_NAME = "/xades_p3_enveloping.xml";
        public const String PROFILE3_ENVELOPED_SIG_FILE_NAME = "/xades_p3_enveloped.xml";
        public const String PROFILE3_DETACHED_SIG_FILE_NAME = "/xades_p3_detached.xml";

        public const String PROFILE4_ENVELOPING_SIG_FILE_NAME = "/xades_p4_enveloping.xml";
        public const String PROFILE4_ENVELOPED_SIG_FILE_NAME = "/xades_p4_enveloped.xml";
        public const String PROFILE4_DETACHED_SIG_FILE_NAME = "/xades_p4_detached.xml";
    }
}
