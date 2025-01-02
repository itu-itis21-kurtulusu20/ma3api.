package dev.esya.api.smartcard.dirak.ec;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.*;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ConstantsExtended;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure.CK_EDDSA_PARAMS_STRUCTURE;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure.CK_MECHANISM_STRUCTURE;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;

public class DirakEdDSATest {

    static SmartCard sc;
    static long slotID;
    static long sessionID;
    static long pubKeyID;
    static long privKeyID;

    static final String PIN = "12345678";

    static final String testVectorFilePath = "unit-test-resources/crypto/eddsa/ed25519.vec";

    // for the existing test cases, mechanism parameters are few; use preset fields
    static final CK_MECHANISM_STRUCTURE mechanismWithoutPH;
    static final CK_MECHANISM_STRUCTURE mechanismWithPH;

    // redirects the test result to an assertion statement, resulting in immediate halt if test has failed
    static final boolean failFast = false;

    // this field is utilized to determine if there were failed test cases when "fail fast" is not used
    // - note: for the "not fail-fast" case, the final decision for a test is expected to be implemented in that test method
    static boolean isTestFailure = false;

    // set up

    @BeforeClass
    public static void beforeClass() throws Exception {

        sc = new SmartCard(CardType.DIRAKHSM);
        slotID = CardTestUtil.getSlot(sc);

        sessionID = sc.openSession(slotID);
        sc.login(sessionID, PIN);
    }

    @AfterClass
    public static void afterClass() throws Exception {

        // logout & close session

        if (sessionID != 0) {

            // try logout (ignoring the CKR_USER_NOT_LOGGED_IN error)
            try {
                sc.logout(sessionID);
            } catch (PKCS11Exception e) {

                // ignore CKR_USER_NOT_LOGGED_IN: rethrow if not so
                if (e.getErrorCode() != PKCS11Constants.CKR_USER_NOT_LOGGED_IN) {
                    throw e;
                }
            }

            sc.closeSession(sessionID);
        }
    }

    @After
    public void afterTest() {

        // reset this flag
        isTestFailure = false;
    }

    static {
        // for the existing test cases, mechanism parameters are few; use preset fields

        CK_EDDSA_PARAMS_STRUCTURE params = new CK_EDDSA_PARAMS_STRUCTURE(
            false,
            null,
            0L
        );

        mechanismWithoutPH = new CK_MECHANISM_STRUCTURE(PKCS11ConstantsExtended.CKM_EDDSA, params);

        params = new CK_EDDSA_PARAMS_STRUCTURE(
            true,
            null,
            0L
        );

        mechanismWithPH = new CK_MECHANISM_STRUCTURE(PKCS11ConstantsExtended.CKM_EDDSA, params);
    }

    // tests

    /**
     * Test EdDSA signing with values provided from a test vector file
     */
    @Test
    public void eddsaSignTestWithTestVec() throws Exception {

        final BufferedReader reader = getBufferedReader();

        int testNumber = 1;

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {

            boolean digestData = false;
            if (line.contains("[SHA-256]")) {
                digestData = true;
                line = reader.readLine();
                if (line.trim().startsWith("#"))
                    line = reader.readLine();
            }

            // detect start of test data
            if (!line.contains("Privkey")) {
                if (!StringUtil.isNullorEmpty(line)) {
                    System.out.println(MessageFormat.format("Skipping line: \"{0}\"", line));
                }
                continue;
            }

            // get fields
            String tmp = line.substring(line.lastIndexOf(" ") + 1);

            final byte[] privKey = StringUtil.hexToByte(tmp);

            line = reader.readLine();

            tmp = line.substring(line.lastIndexOf(" ") + 1);
            final byte[] pubKey = StringUtil.hexToByte(tmp);

            line = reader.readLine();

            if (line.lastIndexOf(" ") != line.indexOf(" ")) {
                tmp = line.substring(line.lastIndexOf(" ") + 1);
            } else {
                tmp = "";
            }

            byte[] msg = tmp.length() / 2 != 0 ? StringUtil.hexToByte(tmp) : null;

            line = reader.readLine();
            tmp = line.substring(line.lastIndexOf(" ") + 1);

            final byte[] expectedSignedData = StringUtil.hexToByte(tmp);

            line = reader.readLine();

            //phFlag yoksa boş satır okunacak.
            boolean phFlag = false;
            if (line.contains("phFlag")) {
                tmp = line.substring(line.lastIndexOf(" ") + 1);
                phFlag = Boolean.parseBoolean(tmp);
            }

            if (msg == null) {
                msg = new byte[0];
            }

            if(digestData) {
                msg = DigestUtil.digest(DigestAlg.SHA256, msg);
            }

            // sign and verify
            final byte[] signedData = createKeyPairAndSign(null, pubKey, privKey, msg, phFlag);
            if (msg.length > 0) {
                if (!verify(msg, signedData, phFlag)) {
                    handleTestFailure(MessageFormat.format("Test #{0} failed: verification failure", testNumber));
                }
            } else {
                System.out.println("Skipping verification for zero-length input");
            }

            // compare signed data
            if (!Arrays.equals(expectedSignedData, signedData)) {
                String failureMessage = MessageFormat.format(
                    "{0}\n{1}\n{2}",
                    MessageFormat.format("Test #{0} failed", testNumber),
                    "- Expected: " + StringUtil.toHexString(expectedSignedData),
                    "- Actual: " + StringUtil.toHexString(signedData)
                );
                handleTestFailure(failureMessage);
            }

            testNumber++;
        }

        Assert.assertFalse("There are failed tests", isTestFailure);
    }

    @Test
    public void wycheproofTest() throws Exception {
        final BufferedReader reader = getBufferedReader("unit-test-resources/crypto/eddsa/ed25519_wycheproof.vec");

        byte[] privkey = null;
        byte[] pubkey = null;
        byte[] data = null;
        byte[] expectedSignature = null;

        int testNumber = 0;
        boolean isTestExpected = false;

        for (String line = reader.readLine(); isTestExpected || line != null; line = reader.readLine()) {
            if (line == null) {
                line = "";
            }

            String trimmedLine = line.trim();

            // upon reaching an empty line, assume all related fields from a given test case to have been read;
            // if any of these are missing, the case will be skipped
            if (trimmedLine.isEmpty()) {

                // if all necessary fields are present
                if (
                    !(
                        privkey == null ||
                        pubkey == null ||
                        data == null ||
                        expectedSignature == null
                    )
                ) {
                    testNumber++;

                    // reset "test expected" flag
                    isTestExpected = false;

                    // sign and verify
                    final byte[] signature = createKeyPairAndSign(null, pubkey, privkey, data, false);

                    if (data.length > 0) {
                        if (!verify(data, signature, false)) {
                            handleTestFailure(MessageFormat.format("Test #{0} failed: verification failure", testNumber));
                        }
                    } else {
                        System.out.println("Skipping verification for zero-length input");
                    }

                    if (!Arrays.equals(expectedSignature, signature)) {
                        String failureMessage = MessageFormat.format(
                            "{0}\n{1}\n{2}",
                            MessageFormat.format("Test #{0} failed", testNumber),
                            "- Expected: " + StringUtil.toHexString(expectedSignature),
                            "- Actual: " + StringUtil.toHexString(signature)
                        );

                        handleTestFailure(failureMessage);
                    }
                } else {
                    System.out.println("Cannot perform test yet: test values are not ready and a test case is assumed at the moment (reached an empty line in the input file)");
                }
            } else { // line is not empty: process the content

                // a line beginning with the '#' character is expected to be a comment line

                // (if not a comment line)
                if (!trimmedLine.startsWith("#")) {

                    // variables in the vector files are defined in a key-value fashion, separated by the equals character '=':
                    // <key> = <value>
                    String[] tokens = getKeyValue(line);
                    if (tokens != null) {
                        final String key   = tokens[0];
                        final String value = tokens.length > 1 ? tokens[1] : null;

                        byte[] valueBytes = value == null ? new byte[0] : StringUtil.hexToByte(value);

                        boolean isUnexpectedKey = false;
                        switch (key) {
                            case "Privkey":
                                privkey = valueBytes;
                                break;
                            case "Pubkey":
                                pubkey = valueBytes;
                                break;
                            case "Msg":
                                data = valueBytes;
                                break;
                            case "Signature":
                                expectedSignature = valueBytes;
                                break;
                            default:
                                System.out.println("Unexpected key: " + key);
                                isUnexpectedKey = true;
                                break;
                        }

                        if (!isUnexpectedKey) {
                            isTestExpected = true;
                            continue;
                        }
                    }
                }

                // if a meaningful input was not read in the above "if" block: print the read line, informing the tester
                System.out.println(MessageFormat.format("Skipping line: \"{0}\"", line));
            }
        }

        // do the assertion last in the "not fail-fast" mode
        Assert.assertFalse("There are failed tests", isTestFailure);
    }

    /**
     * Sign and verify using EdDSA mechanism for preset values
     */
    @Test
    public void eddsaSignTestWithPresetTestValues() throws Exception {

        final String keyLabel = "EdDSAKeyTest_" + System.currentTimeMillis();

        final byte[] edPubValue = StringUtil.hexToByte("ec172b93ad5e563bf4932c70e1245034c35467ef2efd4d64ebf819683467e2bf");
        final byte[] edPrivValue = StringUtil.hexToByte("833fe62409237b9d62ec77587520911e9a759cec1d19755b7da901b96dca3d42");
        final byte[] signData = StringUtil.hexToByte("616263");

        // sign and verify
        byte[] signedData = createKeyPairAndSign(keyLabel, edPubValue, edPrivValue, signData, false);
        Assert.assertTrue(
            "Verification failed",
            verify(signData, signedData, false)
        );
    }

    // utilities

    // the "reader" to read contents of a test vector file, line-by-line
    static BufferedReader getBufferedReader() throws FileNotFoundException {
        return getBufferedReader(testVectorFilePath);
    }

    static BufferedReader getBufferedReader(String testVectorFilePath) throws FileNotFoundException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final URL resourceURL = classloader.getResource(testVectorFilePath);
        assert resourceURL != null;
        return new BufferedReader(new FileReader(resourceURL.getPath()));
    }

    // utility method to perform both key pair creation and signing
    static byte[] createKeyPairAndSign(String keyLabel, final byte[] edPubValue, final byte[] edPrivValue, byte[] data, boolean phFlag) throws PKCS11Exception, SmartCardException {

        // create key pair
        createKeyPair(keyLabel, edPubValue, edPrivValue);

        // sign
        return DirakEdDSATest.sign(data, phFlag);
    }

    // create key pair
    // - note that the key pair is set as non-token, ensuring deletion at the end of the session
    static void createKeyPair(String keyLabel, final byte[] edPubValue, final byte[] edPrivValue) throws PKCS11Exception {

        if (keyLabel == null) {
            keyLabel = "EdDSAKeyTest_" + System.currentTimeMillis();
        }

        // create key pair

        CK_ATTRIBUTE[] edPubTemplate = {
            new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11ConstantsExtended.CKK_EC_EDWARDS),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PUBLIC_KEY),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, keyLabel),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_POINT, edPubValue),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, false)
        };

        CK_ATTRIBUTE[] edPrivTemplate = {
            new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11ConstantsExtended.CKK_EC_EDWARDS),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, keyLabel),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN, true),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, edPrivValue),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_DERIVE, true)
        };

        pubKeyID = sc.getPKCS11().C_CreateObject(sessionID, edPubTemplate);
        privKeyID = sc.getPKCS11().C_CreateObject(sessionID, edPrivTemplate);
    }

    // utility method to pass the predefined mechanism objects related to the provided PH flag
    static byte[] sign(byte[] data, boolean phFlag) throws PKCS11Exception, SmartCardException {
        return DirakEdDSATest.sign(data, phFlag ? mechanismWithPH : mechanismWithoutPH);
    }

    static byte[] sign(byte[] data, CK_MECHANISM_STRUCTURE mechanism) throws PKCS11Exception, SmartCardException {

        // adjust data content in case of null input
        if (data == null) {
            data = new byte[0];
        }

        // sign
        return sc.signDataWithDIRAK(sessionID, privKeyID, mechanism, data);
    }

    // utility method to pass the predefined mechanism objects related to the provided PH flag
    static boolean verify(byte[] data, byte[] signedData, boolean phFlag) throws PKCS11Exception, ESYAException {
        return DirakEdDSATest.verify(data, signedData, phFlag ? mechanismWithPH : mechanismWithoutPH);
    }

    static boolean verify(byte[] data, byte[] signedData, CK_MECHANISM_STRUCTURE mechanism) throws PKCS11Exception, ESYAException {

        try {
            sc.verifyDataWithDIRAK(sessionID, pubKeyID, data, signedData, mechanism);
            return true;
        } catch (PKCS11Exception e) {
            System.out.println("Verification failed: " + e.getMessage());

            // try avoiding CKR_OPERATION_ACTIVE: logout, close session, open session, login
            if (e.getErrorCode() == PKCS11Constants.CKR_ARGUMENTS_BAD || e.getErrorCode() == PKCS11Constants.CKR_OPERATION_ACTIVE) {
                sc.logout(sessionID);
                sc.closeSession(sessionID);
                sessionID = sc.openSession(slotID);
                sc.login(sessionID, PIN);
            }

            return false;
        }
    }

    // the format is as follows:
    //   <key> = <value>
    static String[] getKeyValue(String input) {
        String[] tokens = input.trim().split("\\s*=\\s*");
        return tokens.length <= 2 ? tokens : null;
    }

    // handles test failure case regarding the "fail fast" flag
    // - note: for the "not fail-fast" case, the final decision for a test is expected to be implemented in that test method
    static void handleTestFailure(String failureMessage) {
        if (failFast) {
            Assert.fail(failureMessage);
        } else {
            System.out.println(failureMessage);
            isTestFailure = true;
        }
    }
}
