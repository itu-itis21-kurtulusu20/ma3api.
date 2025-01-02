package dev.esya.api.xmlsignature.legacy;

import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 22.11.2012
 * Time: 10:40
 * To change this template use File | Settings | File Templates.
 */
public class UnitTestParameters {

    public static final String SIGNATURE_ALGORITHM = Algorithms.SIGNATURE_RSA_SHA256;
    public static final SignatureAlg SIGNATURE_ALG = SignatureAlg.RSA_SHA256;

    public static final int SMARTCARD_SLOTNO0 = 0;
    public static final int SMARTCARD_SLOTNO1 = 1;
    public static final int SMARTCARD_SLOTNO2 = 2;
    public static final int SMARTCARD_SLOTNO_REVOKED = 3;

    public static final String TEST_EDEFTER_FILE_NAME = "/testdefter.xml";
    public static final String TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME = "/xades_bes_testdefter_signed.xml";
    public static final String TEST_EDEFTER_EST_ENVELOPED_SIG_FILE_NAME = "/xades_est_testdefter_signed.xml";
    public static final String TEST_EDEFTER_ESC_ENVELOPED_SIG_FILE_NAME = "/xades_esc_testdefter_signed.xml";
    public static final String TEST_EDEFTER_ESX_ENVELOPED_SIG_FILE_NAME = "/xades_esx_testdefter_signed.xml";
    public static final String TEST_EDEFTER_ESXL_ENVELOPED_SIG_FILE_NAME = "/xades_esxl_testdefter_signed.xml";
    public static final String TEST_EDEFTER_ESA_ENVELOPED_SIG_FILE_NAME = "/xades_esa_testdefter_signed.xml";

    public static final String BES_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_bes_revoked_enveloping.xml";
    public static final String BES_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_bes_revoked_enveloped.xml";
    public static final String BES_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_bes_revoked_detached.xml";

    public final static String BES_ENVELOPING_SIG_FILE_NAME = "/xades_bes_enveloping.xml";
    public final static String BES_ENVELOPED_SIG_FILE_NAME = "/xades_bes_enveloped.xml";
    public final static String BES_DETACHED_SIG_FILE_NAME = "/xades_bes_detached.xml";

    public final static String EPES_ENVELOPING_SIG_FILE_NAME = "/xades_epes_enveloping.xml";
    public final static String EPES_ENVELOPED_SIG_FILE_NAME = "/xades_epes_enveloped.xml";
    public final static String EPES_DETACHED_SIG_FILE_NAME = "/xades_epes_detached.xml";

    public static final String EST_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_est_revoked_enveloping.xml";
    public static final String EST_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_est_revoked_enveloped.xml";
    public static final String EST_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_est_revoked_detached.xml";

    public final static String EST_ENVELOPING_SIG_FILE_NAME = "/xades_est_enveloping.xml";
    public final static String EST_ENVELOPED_SIG_FILE_NAME = "/xades_est_enveloped.xml";
    public final static String EST_DETACHED_SIG_FILE_NAME = "/xades_est_detached.xml";

    public static final String ESC_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_esc_revoked_enveloping.xml";
    public static final String ESC_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_esc_revoked_enveloped.xml";
    public static final String ESC_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_esc_revoked_detached.xml";

    public final static String ESC_ENVELOPING_SIG_FILE_NAME = "/xades_esc_enveloping.xml";
    public final static String ESC_ENVELOPED_SIG_FILE_NAME = "/xades_esc_enveloped.xml";
    public final static String ESC_DETACHED_SIG_FILE_NAME = "/xades_esc_detached.xml";

    public static final String ESX_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_esx_revoked_enveloping.xml";
    public static final String ESX_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_esx_revoked_enveloped.xml";
    public static final String ESX_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_esx_revoked_detached.xml";

    public final static String ESX_ENVELOPING_SIG_FILE_NAME = "/xades_esx_enveloping.xml";
    public final static String ESX_ENVELOPED_SIG_FILE_NAME = "/xades_esx_enveloped.xml";
    public final static String ESX_DETACHED_SIG_FILE_NAME = "/xades_esx_detached.xml";

    public static final String ESXL_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_esxl_revoked_enveloping.xml";
    public static final String ESXL_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_esxl_revoked_enveloped.xml";
    public static final String ESXL_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_esxl_revoked_detached.xml";

    public final static String ESXL_ENVELOPING_SIG_FILE_NAME = "/xades_esxl_enveloping.xml";
    public final static String ESXL_ENVELOPED_SIG_FILE_NAME = "/xades_esxl_enveloped.xml";
    public final static String ESXL_DETACHED_SIG_FILE_NAME = "/xades_esxl_detached.xml";

    public static final String ESA_REVOKED_ENVELOPING_SIG_FILE_NAME = "/xades_esa_revoked_enveloping.xml";
    public static final String ESA_REVOKED_ENVELOPED_SIG_FILE_NAME = "/xades_esa_revoked_enveloped.xml";
    public static final String ESA_REVOKED_DETACHED_SIG_FILE_NAME = "/xades_esa_revoked_detached.xml";

    public final static String ESA_ENVELOPING_SIG_FILE_NAME = "/xades_esa_enveloping.xml";
    public final static String ESA_ENVELOPED_SIG_FILE_NAME = "/xades_esa_enveloped.xml";
    public final static String ESA_DETACHED_SIG_FILE_NAME = "/xades_esa_detached.xml";

    public final static String ESA_ARCHIVE_TS_ENVELOPING_SIG_FILE_NAME = "/xades_esa_ts_added_enveloping.xml";
    public final static String ESA_ARCHIVE_TS_ENVELOPED_SIG_FILE_NAME = "/xades_esa_ts_added_enveloped.xml";
    public final static String ESA_ARCHIVE_TS_DETACHED_SIG_FILE_NAME = "/xades_esa_ts_added_detached.xml";

    public final static String PARALLEL_ENVELOPED_SIG_FILE_NAME = "/xades_parallel_enveloped.xml";
    public final static String PARALLEL_ENVELOPING_SIG_FILE_NAME = "/xades_parallel_enveloping.xml";
    public final static String COUNTER_ENVELOPING_SIG_FILE_NAME = "/xades_counter_enveloping.xml";
    public final static String CREATED_COUNTER_ON_PARALLEL_SIG_FILE_NAME = "/xades_created_counter_on_parallel_enveloping.xml";
    public final static String COUNTER_ON_PARALLEL_SIG_FILE_NAME = "/xades_counter_added_on_parallel_enveloping.xml";
    public final static String PARALLEL_ADDED_SIG_FILE_NAME = "/xades_parallel_added_on_signature_enveloping.xml";

    public final static String BES_ENVELOPING_SIGNED_WITH_FREE_LICENSE = "/xades_bes_enveloping_with_freelicense.xml";
    public final static String BES_ENVELOPED_SIGNED_WITH_FREE_LICENSE = "/xades_bes_enveloped_with_freelicense.xml";
    public final static String BES_DETACHED_SIGNED_WITH_FREE_LICENSE = "/xades_bes_detached_with_freelicense.xml";

    public final static String EST_ENVELOPING_UPGRADED_WITH_TEST_LICENSE = "/xades_est_enveloping_with_testlicense.xml";
    public final static String EST_ENVELOPED_UPGRADED_WITH_TEST_LICENSE = "/xades_est_enveloped_with_testlicense.xml";
    public final static String EST_DETACHED_UPGRADED_WITH_TEST_LICENSE = "/xades_est_detached_with_testlicense.xml";

    public final static String PFX_FILE = "yavuz.kahveci_238778@ug.net.pfx";
    public final static String PFX_PASSWORD = "238778";
    public final static String PFX_2_FILE = "suleyman.uslu_283255@ug.net.pfx";
    public final static String PFX_2_PASSWORD = "283255";
    public final static String PFX_3_FILE = "ramazan.girgin_327147@ug.net.pfx";
    public final static String PFX_3_PASSWORD = "327147";
    public final static String PFX_REVOKED_FILE = "yavuz.kahveci_437710_iptal@ug.net.pfx";
    public final static String PFX_REVOKED_PASSWORD = "437710";

    public final static String ERROR_SIG_FILE_NAME = "/";

    public final static String PLAINFILENAME="docs/samples/signatures/sample.txt";
    public final static String PLAINFILENAME2="docs/samples/signatures/sample2.txt";
    public final static String PLAINFILEMIMETYPE="text/plain";
    public final static String PLAINFILE2MIMETYPE="text/plain";

    public final static String POLICY_FILE_NAME2 = "profilyeni.pdf";
    public final static String POLICY_FILE_NAME3 = "profilyeni.pdf";
    public final static String POLICY_FILE_NAME4 = "profilyeni.pdf";

    public static int[] OID_POLICY_P2 = new int[]{  2, 16, 792, 1, 61, 0, 1, 5070, 3, 1, 1};
    public static int[] OID_POLICY_P3 = new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 2, 1 };
    public static int[] OID_POLICY_P4 = new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 3, 1 };

    public final static String PROFILE2_ENVELOPING_SIG_FILE_NAME = "/xades_p2_enveloping.xml";
    public final static String PROFILE2_ENVELOPED_SIG_FILE_NAME = "/xades_p2_enveloped.xml";
    public final static String PROFILE2_DETACHED_SIG_FILE_NAME = "/xades_p2_detached.xml";

    public final static String PROFILE3_ENVELOPING_SIG_FILE_NAME = "/xades_p3_enveloping.xml";
    public final static String PROFILE3_ENVELOPED_SIG_FILE_NAME = "/xades_p3_enveloped.xml";
    public final static String PROFILE3_DETACHED_SIG_FILE_NAME = "/xades_p3_detached.xml";

    public final static String PROFILE4_ENVELOPING_SIG_FILE_NAME = "/xades_p4_enveloping.xml";
    public final static String PROFILE4_ENVELOPED_SIG_FILE_NAME = "/xades_p4_enveloped.xml";
    public final static String PROFILE4_DETACHED_SIG_FILE_NAME = "/xades_p4_detached.xml";
}
