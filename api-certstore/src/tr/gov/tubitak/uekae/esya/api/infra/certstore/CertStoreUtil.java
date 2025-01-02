package tr.gov.tubitak.uekae.esya.api.infra.certstore;

import com.objsys.asn1j.runtime.Asn1BitString;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.BitArray;
import tr.gov.tubitak.uekae.esya.api.asn.attrcert.EAttributeCertificate;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ECertID;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponseData;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ESingleResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzneTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SILTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoNitelikSertifikasi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOCSP;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOCSPToWrite;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSIL;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifikaOcsps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSilinecekKokSertifika;
import tr.gov.tubitak.uekae.esya.asn.depo.DepoASNEklenecekKokSertifika;
import tr.gov.tubitak.uekae.esya.asn.depo.DepoASNKokSertifika;
import tr.gov.tubitak.uekae.esya.asn.depo.DepoASNRawImza;
import tr.gov.tubitak.uekae.esya.asn.depo.DepoASNSilinecekKokSertifika;
import tr.gov.tubitak.uekae.esya.asn.depo.KOKGuvenSeviyesi;
import tr.gov.tubitak.uekae.esya.asn.depo.KokSertifikaTipi;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.PublicKey;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.jar.Manifest;

public class CertStoreUtil
{
    private static Logger logger = LoggerFactory.getLogger(CertStoreUtil.class);

	public static final String DEPO_DIZIN_ADI = ".sertifikadeposu";

    private static HashMap<String, byte[]> PUB_KEY_MAP = new HashMap<>();

    private static HashMap<Integer, DigestAlg> HASH_TYPE_MAP = new HashMap<Integer, DigestAlg>();
    public static final DigestAlg OZET_TIPI_SHA1 = DigestAlg.SHA1;
    //public static final DigestAlg OZET_TIPI_SHA224 = DigestAlg.SHA224;
    public static final DigestAlg OZET_TIPI_SHA256 = DigestAlg.SHA256;
    public static final DigestAlg OZET_TIPI_SHA384 = DigestAlg.SHA384;
    public static final DigestAlg OZET_TIPI_SHA512 = DigestAlg.SHA512;

    private static final Boolean isCertStoreCountryTurkey;

    private static final String PUB_KEY_1_HASH = "E8pMzbTsYEPKmfc8vklz+/03HCw=";
    private static final byte[] PUB_KEY_1_PART_1 = {58, -42, 99, 86, 93, 100, 104, 122, 67, -4, 36, -29, -123, 35, 47, 47, 11, 15, 89, 102, -16, 123, -13, 122, -52, -94, 101, -4, 120, -18, 103, 115, 105, -15, -125, -49, -40, 16, 102, 40, 121, 28, 12, 97, 79, -27, -110, -19, 56, 31, -9, 94, -62, -12, -22, 64, -95, -82, 90, 43, 81, -11, 16, 99, 58, -45, -66, 64, -55, -88, 103, -101, -78, -30, -93, -122, 123, -44, -115, -74, -37, -103, -93, -85, 66, 71, -102, 63, 66, -75, -96, -112, -114, -49, 86, -115, -57, 9, 101, 5, 90, 88, 94, 105, 76, -20, 40, 57, 54, 107, 57, 5, -81, 99, -6, -55, -13, 88, 82, -48, 78, 124, -106, 69, -34, -72, -36, 74, 124, 52, 1, -54, -79, -84, 33, 116, 126, -12, 22, -31, 120, 68, 68, 28, 127, 59, -124, -118, -85, 111, 59, 114, -122, -2, -80, -17, -81, 52, 39, -13, 6, -63, -86, -47, -75, -2, -20, 118, 107, 83, -83, 27, 16, 13, 62, -106, -90, -108, 59, -34, -126, 92, -42, -27, -21, -76, 120, 124, 73, -33, -13, -123, 17, 108, 31, 44, 102, 86, -73, 68, 2, -117, 108, -86, 26, -15, 105, -70, 97, -118, -43, -102, -124, -23, 19, -87, 88, -94, 22, -23, 17, -102, -93, -113, 79, -76, -19, -4, 66, -66, 79, 26, 80, -45, -58, -22, -15, -93, 30, 59, -38, 123, 82, -60, -20, -123, -128, 46, 101, 121, 18, 53, -14, -57, 82, -62, -103, 125, -112, 24, 11, -125, -81, -30, 20, 7, 22, -26, 61, 96, -68, -113, 61, -11, -88, -63, 55, -3, -112, -119, 85, 79, -80, 33, -29, 22, 1, 37, -84, -24, -86, -27, 91, -105, 108, -86, 37, -106, 16, 12, 100, -37, -60, 100, -18, -77, -113, -92, 72, 108, 110, -48, -78, -28, 43, 16, -77, -102, -39, -13, 43, -79, -103, -6, -95, -98, 85, 28, 112, 126, 90, -50, -2, 76, -70, -29, -124, 11, -6, 12, 71, -20, -32, -84, -61, 11, 44, 80, -10, 74, -60, 70, 15, -104, -85, -32, 33, -47, 109, 84, 10, -99, -90, 118, 44, -112, -107, -48, -31, 72, -2, -10, -112, -44, -57, -57, -86, 68, -41, -28, 0, -47, 110, 37, 77, -16, -63, -120, 12, 54, -116, -62, -107, -60, 12, 30, -31, 78, -125, 84, -94, 61, -57, -115, 46, -19, -32, -84, 66, 56, 0, -50, -109, -3, -20, -124, -77, -86, 12, 75, -87, 76, 125, -84, -115, 80, -18, 97, 0, -13, 8, 64, -42, 118, -119, -58, -57, 87, -48, 117, -15, -110, 97, -16, 33, -2, -114, -113, 0, 85, 27, -45, -109, -119, -97, 77, 88, -124, -55, -35, 76, -42, -21, -85, -17, 41, 96, 90, -39, 89, 127, 116, -76, 69, 14, 2, -110, 81, 112, 58, 48, 12, -97, 63, 90, -52, 103, -19, -66, -123, -106, -29, 5, -78, -57, 110, -7, 65, -20, 22, 65, -95, -55, -83, 95, -71, -14, -30, -4, 12, -49, 16, -112, -112, 90, -104, -104, -5, -17, 79, -88, 34, -65, -21, -51, 103, 87, 45, -102, 67, -73, -101, 55, -60, -30, 21, -105, 99, -41, -44, -38, -14, -10, 96, -12, 110, 102, 115, 32, 103};
    private static final byte[] PUB_KEY_1_PART_2 = {10, 84, 97, 116, 109, 105, 110, 115, 105, 122, 108, 101, 114, 46, 46, 46, 10, 10, 89, 101, 114, 121, -4, 122, -4, 32, 103, -10, 122, 108, 101, 114, 105, 110, 101, 32, 100, 101, 118, 32, 98, 105, 114, 32, 109, 101, 109, 101, 32, 103, 105, 98, 105, 32, 103, -10, 114, -4, 110, -4, 121, 111, 114, 46, 32, 77, 101, 109, 101, 121, 101, 32, 97, -16, -3, 122, 108, 97, 114, -3, 110, -3, 32, 117, 122, 97, 116, -3, 112, 32, 101, 109, 109, 101, 107, 32, 105, -25, 105, 110, 32, 98, 105, 114, 98, 105, 114, 108, 101, 114, 105, 110, 105, 32, 105, 116, 105, 121, 111, 114, 44, 32, 101, 122, 105, 121, 111, 114, 59, 32, 100, 97, 104, 97, 115, -3, 44, 32, 97, 108, 116, 116, 97, 110, 32, 97, 108, 116, 97, 32, 98, 105, 114, 98, 105, 114, 108, 101, 114, 105, 110, 100, 101, 110, 32, 110, 101, 102, 114, 101, 116, 32, 101, 100, 105, 121, 111, 114, 108, 97, 114, 46, 10, 10, 72, 101, 109, 101, 110, 32, 104, 101, 112, 115, 105, 32, 109, 111, 100, 101, 114, 110, 32, 100, -4, 110, 121, 97, 110, -3, 110, 32, 34, 121, 101, 110, 105, 34, 108, 105, 107, 32, 102, 101, 116, 105, -2, 105, 122, 109, 105, 32, 107, 97, 114, -2, -3, 115, -3, 110, 100, 97, 32, 98, -4, 121, -4, 108, 101, 110, 105, 121, 111, 114, 46, 32, 10, 10, 83, 97, 104, 105, 112, 32, 111, 108, 100, 117, 107, 108, 97, 114, -3, 32, 110, 101, 32, 118, 97, 114, 115, 97, 44, 32, 103, -10, 122, 108, 101, 114, 105, 110, 100, 101, 32, 118, 101, 32, 103, -10, 110, -4, 108, 108, 101, 114, 105, 110, 100, 101, 32, -25, 111, 107, 32, -25, 97, 98, 117, 107, 32, 101, 115, 107, 105, 121, 111, 114, 59, 32, 104, 101, 108, 101, 32, 100, 101, 32, 101, 115, 107, 105, 32, 100, 111, 115, 116, 108, 97, 114, 33, 46, 46, 10, 10, 66, 105, 114, 97, 122, 32, 100, 97, 32, -2, 97, -2, -3, 114, -3, 121, 111, 114, 108, 97, 114, 32, 107, 101, 110, 100, 105, 32, 104, 97, 108, 108, 101, 114, 105, 110, 101, 33, 32, 10, 10, 76, 97, 102, -3, 110, 32, 103, 101, 108, 105, -2, 105, 32, 121, 97, 44, 32, 116, -4, 112, 108, -4, 32, 100, 97, 108, 109, 97, 32, 109, 111, 100, 97, 115, -3, 32, -25, -3, 107, -3, 121, 111, 114, 44, 32, 104, 101, 109, 101, 110, 32, 100, 97, 108, 103, -3, -25, 32, 111, 108, 117, 121, 111, 114, 108, 97, 114, 59, 32, 97, 109, 97, 32, 98, 97, 107, -3, 121, 111, 114, 108, 97, 114, 32, 121, 105, 110, 101, 32, 109, 117, 116, 115, 117, 122, 108, 97, 114, 46, 32, 80, 105, 108, 111, 116, 108, 117, 107, 32, 109, 111, 100, 97, 32, 111, 108, 117, 121, 111, 114, 44, 32, 117, -25, 117, -2, 32, 100, 101, 114, 115, 105, 32, 97, 108, -3, 121, 111, 114, 108, 97, 114, 44, 32, 98, 114, -10, 118, 101, 108, 101, 114, 32, 102};

    private static final String PUB_KEY_2_HASH = "OgLaGFk0EnlSOyxYSXU7DTpsA+Y="; //Kıbrıs
    private static final byte[] PUB_KEY_2_PART_1 = {35, 21, 107, 29, 34, -65, 28, -59, -34, 20, -92, 80, -2, -16, -19, -39, -42, 91, -115, 50, -59, -15, -58, -63, 35, -117, 114, 25, -19, -97, 65, 85, -80, 104, 30, 110, 75, 94, 81, -105, -112, -13, 40, 2, -119, 25, -26, 13, 119, -105, 57, -5, -76, -11, -69, -3, 7, 114, 28, -93, 117, 121, -11, -83, 66, -29, -90, 62, 17, 75, 1, -3, 89, -22, 45, -118, 45, 46, 114, -60, 48, -44, -13, -25, -39, 53, 66, 101, -19, 11, -108, -73, 36, 8, -54, 126, -22, 89, 81, -80, 67, 112, 109, -114, -106, -4, 10, -55, -44, 69, -115, -91, 73, -43, 116, -70, 45, -67, -92, -69, -31, 79, 84, 127, -1, -3, 36, 76, 38, -58, -89, 98, 41, 100, -64, 106, -42, 5, 110, 13, -14, -121, 94, 72, -57, 1, 109, -98, 61, 30, 1, -124, 88, 51, -22, 46, 13, 31, 107, -22, 108, 26, 103, 118, 8, -83, 74, 52, -118, 67, -57, 57, -100, -55, 67, -87, -1, 91, 67, -110, 76, -72, 10, -67, -128, 88, 14, -12, 43, 89, -17, 50, 12, -67, -105, -40, 30, -7, 78, 27, 83, -23, -96, -117, 74, -100, 82, -97, 100, -67, -126, 36, 39, -86, -33, 10, 77, -45, 113, -65, 99, -106, -121, -47, -84, 80, 121, -80, -64, -66, 47, -99, 104, -127, -44, -66, -53, -109, -100, -53, 86, 35, -34, -98, 83, -5, 52, -60, -75, 42, 102, 46, 58, -38, -110, -60, -98, 72, -78, -79, 43, -48, 69, 81, 60, 92, 37, 102, -102, 64, -112, 82, 61, 3, 29, 63, 106, -30, 93, 108, 10, 88, 3, -122, 20, 75, -103, -88, -12, -59, -104, -88, 26, -107, -88, -33, 6, 103, 57, 84, 80, 46, 72, 54, 71, -65, -100, 48, 17, 109, 87, -109, -39, 7, -101, 102, -39, -65, 0, -126, 70, -51, -117, -88, -29, 22, -82, 79, 85, -105, -80, 111, -110, -58, -83, -99, -114, 122, 78, -6, -96, -106, -85, 63, -6, -3, -1, -88, -7, -73, -42, 121, -102, -111, -50, 50, 110, 7, 94, 46, 98, -38, -71, -61, 18, 20, -65, -35, 47, -21, 51, 107, 34, 99, -111, 10, -81, 113, 110, 69, 42, -114, -67, 62, 26, -53, -56, -57, 22, -4, -80, -112, 77, 77, -107, 67, -7, -117, -124, 68, 90, -88, -41, -2, 34, 10, 76, -56, 32, 38, 78, -81, -101, 59, -39, -6, -128, -124, -13, 114, -38, 13, 87, -42, 95, 111, -81, -65, -24, 62, -120, -121, -71, -13, -54, -77, -43, -106, -124, 114, 29, 56, 109, -54, 26, 119, 62, -113, -88, -64, -30, -76, 64, 33, 28, 90, -26, -31, -17, -62, 41, -48, 50, 86, 83, 70, -68, -36, 79, -15, -14, 83, 51, 108, 111, -87, -76, 72, -80, -118, -28, -122, -9, -71, -11, -68, 55, -126, -24, -118, -88, 16, 109, 38, -125, 74, -16, 60, -77, -2, -110, 92, 127, 20, 64, 111, -126, -86, 83, 109, -125, -79, -116, 97, -56, -15, 54, -60, -70, 117, -21, 85, 82, 25, 95, -115, -68, -106, 117, -94, -76, -86, -49, 51, -81, 112, 126, 106, -59, -19, -54, 90, 75, 95, -102, 44, -13, 2, -43, -117};
    private static final byte[] PUB_KEY_2_PART_2 = {19, -105, 105, 63, 18, -78, 26, -52, -12, -110, -20, -42, 9, -3, -20, -40, -41, 94, -115, 49, 71, -13, -55, -63, 19, 9, 112, 19, -17, 29, 67, 84, -80, -38, -108, -63, -77, -51, 20, 17, -117, -110, 97, -114, -104, -128, -2, -89, -49, -50, 107, -92, -69, -119, 126, -56, 89, 92, 17, -120, 89, -57, -28, -117, 76, 77, -102, 66, -112, 100, -123, 9, -34, 87, -17, 10, 54, 26, 114, 118, 6, -55, 91, 97, 122, 71, 123, 100, -119, -71, -78, -50, -103, -19, 69, -89, -20, -64, -26, 21, -19, -72, 115, -121, -52, 60, -65, -29, 108, 52, -34, 40, 116, -89, 1, 5, 90, -47, -16, -53, 75, 90, -76, 51, 98, 85, 11, -15, 76, 113, -115, 106, -9, 72, 73, 101, -57, 18, 0, -34, -105, -31, -77, 44, -36, -74, 25, -33, 51, -34, -78, 92, -84, 14, 98, -113, 106, -51, 55, -112, -45, -46, -57, -82, -57, -71, -77, 123, 4, 108, 114, -80, -53, -97, 118, 41, 78, -108, 37, 116, -37, 8, -2, 38, 53, -3, -78, -80, -60, -8, -34, -56, -84, 20, 28, -31, 21, -88, -115, 24, -81, 97, 0, -80, -12, -33, 83, -71, 77, -92, -104, -57, 97, -98, -127, 20, -12, 93, 15, -93, -90, -71, 102, -117, -38, -116, -54, -3, 123, -20, -62, -70, -58, 119, -53, -18, -89, -44, 98, 6, 5, -71, 36, -10, -105, 17, 111, 80, -98, 110, 102, -96, -20, -1, 54, -97, -60, -67, -126, 65, 14, -47, 85, 53, -15, 127, 11, 20, 98, 125, 83, 89, 72, -90, 74, 3, 124, 29, -49, -58, -19, -40, 115, 105, -121, 13, 97, 79, -36, 117, -104, -124, -89, -64, -126, 73, -27, -56, -86, 41, 91, -116, 119, 97, -109, -103, -32, 71, 10, -90, 127, -59, 110, 35, 28, 14, -7, -96, -40, -101, 69, -21, -122, -114, 116, -93, 119, -111, 116, 54, -25, 69, -89, 74, -50, 14, 123, 2, 1, -20, -71, -85, 91, -61, 120, -106, 38, 125, -39, 66, -11, 25, 120, -121, -82, -106, -51, -18, -8, -119, -55, -10, -46, 81, 66, 58, -107, -76, 98, 112, -81, 68, -66, 60, -121, 20, -91, 121, 13, 45, 64, -88, 110, 32, -75, 31, 35, -7, 111, 90, 18, 98, -82, 125, -6, -120, -110, -50, -13, -18, -3, 87, 72, -91, -79, -28, 54, -36, 55, -9, 77, 63, -49, -66, -119, 40, -65, -30, -33, -1, 103, 115, 11, -54, 51, -39, 126, 11, 104, -29, -57, 68, -12, -118, -108, -57, 80, -78, 106, 94, -29, 97, 62, -118, 33, -44, -62, 4, 83, -117, 60, 3, 3, 48, 124, 67, 82, -52, 126, 122, 106, -124, -53, 42, 21, -52, 57, 111, 67, -124, 119, 86, 106, 13, 55, 49, 55, 52, 5, 11, -117, -99, 102, 33, -38, -127, 33, -125, -124, -1, 84, -88, 93, 106, -59, -124, 74, 116, 65, 18, -18, 54, -108, 50, 33, -35, 109, -56, -34, 43, -70, -60, 63, 95, -70, -35, -66, 33, 75, 95, 83, 101, 87, 36, -86, 60, 70, 126, -121, -102, 46, -63, 63, 78, 45, -114, -43, -58, 30, -122, -49, 20, 67, -41, 89, 46, -16, 3, -43, -118};

    //private static final String PUB_KEY_3_HASH = "62rYYOTuL1/viVHkI/6W0upU6i0="; //TBTK
    private static final byte[] PUB_KEY_3_PART_1 = {53, 19, -27, -119, 57, 24, 5, 91, -54, -92, -62, -17, 6, -128, -119, 13, -22, -87, 94, -72, -9, 20, -21, 96, -46, 31, 33, -19, 65, 8, -75, 81, 114, 114, 98, 22, 91, 118, -108, 118, -20, 84, -13, 0, -93, -35, 19, 35, 87, 47, 2, 114, 111, -112, -92, 63, 109, 68, -76, 113, -21, 103, -57, -25, 5, -10, 86, 30, 71, -116, -60, 98, -13, -59, -27, -27, -128, 46, 79, 14, -96, 60, 46, 36, 61, -29, -117, -63, 88, -31, -95, -25, 107, -25, -36, 93, -113, 52, 117, 82, 48, 8, -14, 58, 48, 123, 40, -99, 69, -78, -42, -39, -101, -38, 52, -88, -49, -14, 19, -39, 115, 116, -14, 99, -116, -93, 24, -8, 111, 118, -9, 86, 119, -13, -28, -99, 95, -119, 98, -17, 33, 0, 63, -116, -51, 39, 69, -36, -122, -2, -25, 84, 48, 22, -110, 70, -104, -81, 57, 117, -49, -83, -56, 84, 15, 28, 72, -70, 29, 25, 4, -42, -93, 25, 95, -37, -94, 45, 108, 21, 58, 122, 5, -6, -29, -77, -110, -58, 88, 33, 41, 119, -20, -65, 23, -105, 117, 103, -6, 44, -43, -39, -45, -56, -86, 91, -1, 59, -114, 77, 42, 69, 83, 29, 18, 85, 10, 78, 12, -29, 65, 37, -39, 96, -87, -48, -85, -17, 35, -89, -47, -117, 93, 67, -51, -59, 63, 63, 58, -68, 70, -119, -116, -106, 7, 107, 49, -127, -7, -34, 101, 27, 54, 48, -108, 95, -19, 99, -44, 106, -82, 109, -47, 91, 101, -7, -1, -62, 23, -109, -67, -17, 74, -39, -105, -7, 7, -113, 39, -100, 113, 102, 72, 1, -31, 10, -46, -1, 68, -71, 94, -36, 5, 12, -75, 19, -73, -3, -51, 12, -118, -23, -12, -87, -77, 113, -91, -107, 8, 69, 17, -81, -101, 29, 123, 32, 35, -104, 71, 102, -85, 31, -102, -70, -77, 33, 110, -78, 22, 75, -86, 121, 11, -37, -38, 125, 53, 56, -94, -107, -6, -47, 111, -27, -71, -117, 112, -46, 126, 44, 98, 118, 30, 55, -104, 82, -19, -48, 78, 83, -1, -64, -107, -91, -95, -17, 58, -127, 90, -19, -66, 121, 48, -94, 14, -91, -56, 18, -76, 73, 14, 29, -7, -28, 51, -34, 64, 66, -35, 105, 16, 5, -88, -77, 31, -117, -107, 123, 5, -27, 16, 73, 96, 113, 58, -57, 98, -42, -73, -113, 87, -37, 73, -38, 61, -56, 86, 37, -30, -69, -109, 102, 30, -121, 23, -5, -87, 84, 22, -121, 26, 47, -81, 32, -25, 104, 117, 5, -66, 78, 23, 120, 36, 99, -27, -16, 2, 109, 104, -35, 61, -101, 123, 21, -95, 108, -117, 34, 92, 4, 100, -66, -1, -29, -75, 64, -77, 68, 105, 51, -121, -102, -68, 16, 19, -117, 3, 84, 41, 97, -30, 115, -119, 27, -72, -12, 31, 91, -56, 98, -106, -125, 94, -39, 77, 71, 115, 36, -85, 91, 122, 15, 54, 121, 124, 103, -61, 70, 80, -92, -95, 114, -22, -22, -44, 23, -29, -93, -63, 98, -87, -42, 75, -93, 60, -91, -86, 57, 31, 125, -108, 64, -94, 87, -104, 38, -114, 84, 117, 111, -68, 48, 65, 125, -51, 126, -8, 33, -117, -105};
    private static final byte[] PUB_KEY_3_PART_2 = {5, -111, -25, -85, 9, 21, 3, 82, -32, 34, -118, 105, -15, -115, -120, 12, -21, -84, 94, -69, 117, 22, -28, 96, -30, -99, 35, -25, 67, -118, -73, 80, 114, -88, -28, -63, -17, -78, -45, -54, 114, 114, -92, 114, -72, -54, 85, -31, 84, 31, -57, 96, -79, 120, -20, -33, 47, 12, 119, 54, 25, -120, 16, 125, -71, 20, 126, -59, 117, 60, -4, -38, 11, -126, -89, -95, -38, -96, -28, 12, 4, -122, 60, -52, 104, 111, 56, -47, 103, 5, 70, -120, 99, -40, -17, -66, 49, 106, 106, 77, 82, -116, 58, 126, -33, 67, -54, -58, 89, 91, 2, -95, 99, 91, 4, 121, -18, 127, -64, -29, -98, -15, 43, -44, -17, -45, -59, 106, -54, 13, -12, -100, 119, 94, -112, 22, -50, -54, -16, 3, 93, 88, -41, -55, 56, -47, 111, 4, 59, 68, 110, -43, 72, -127, -38, -12, 2, -26, -72, -29, 60, -29, -56, -111, -2, -55, 111, -117, -78, 96, -103, 105, -74, 104, -90, -62, -38, 34, 43, -14, 54, 89, 96, -75, 65, -103, 125, -128, 16, 4, -3, 111, -85, 50, -25, 84, 27, -107, -69, -83, 33, 123, 77, -105, -99, 2, -86, -91, 65, 74, 39, -79, 32, -63, 57, 61, -110, 4, 2, -107, 109, 82, 60, 115, 106, 16, -6, -57, -92, -121, 113, 120, -105, 68, 121, -28, 27, 48, 24, 116, -73, -64, -52, 57, 107, 117, -33, -77, 62, -25, -79, 61, 87, 91, -15, 111, -127, -16, -99, 48, 40, -82, 70, 85, 43, 51, 41, -47, 86, -53, -58, 15, -34, 58, 50, -118, -19, 124, 23, 79, 113, 113, -106, -58, 8, 43, 3, -22, -33, -12, 72, -61, -122, 21, -124, 7, -5, 46, -4, 25, -125, 62, -94, 101, 44, -34, 71, 80, -45, 96, 7, -100, -81, -80, -38, 27, -91, -36, -50, -41, -32, -23, -63, 118, 33, 71, 101, 84, 108, -22, 38, -11, 54, -88, -47, 34, 39, -47, -19, 98, -15, -30, -28, 35, -19, -127, -72, 44, 124, 42, -100, -20, 94, -116, -87, -38, 110, -65, -92, 0, 50, 23, 109, -21, -36, -122, -15, 31, -85, -111, -32, -31, -102, -116, -15, -73, 81, -94, -80, -128, 119, -110, 9, -7, -62, 86, 95, 51, -120, 41, 16, -96, -62, -111, -51, 21, 71, 36, -14, -104, -12, -65, 10, -59, -39, 112, 5, 11, -16, 126, 121, -111, -82, 76, 109, 123, -29, 59, 96, -63, 25, 119, -122, -122, -1, -99, 22, 127, -8, -20, -37, -121, 122, -124, 53, -7, 38, 56, 40, 80, 79, 39, 15, 71, 8, -110, 107, -30, -45, -124, -84, -89, -80, 105, -73, 49, -28, 117, -52, -42, -45, 102, 72, -24, 15, -87, -34, 125, -62, -104, -114, -71, -16, 49, 5, -65, 110, 123, -81, -121, 90, 2, -34, -54, -24, -128, -98, -2, 68, 86, 95, 76, -116, -108, 7, -9, -14, -125, 113, -78, -97, 96, -104, 102, 14, -53, 79, 39, 111, -76, -26, 64, -14, -23, -89, -14, -100, 5, 13, 108, -31, -76, 38, 70, 81, 38, -81, 99, -8, 113, -32, 92, -43, -31, -123, -103, -48, 51, -4, -96, 116, 80, 28, 51, -66, 124, -5, 32, -117, -106};
    static {
        addInitialPubKey(PUB_KEY_1_PART_1, PUB_KEY_1_PART_2);
        addInitialPubKey(PUB_KEY_2_PART_1, PUB_KEY_2_PART_2);
        addInitialPubKey(PUB_KEY_3_PART_1, PUB_KEY_3_PART_2);

        isCertStoreCountryTurkey = isCertStoreCountryTurkey();

        HASH_TYPE_MAP.put(OzetTipi.SHA1.getIntValue(), OZET_TIPI_SHA1);
        //HASH_TYPE_MAP.put(OzetTipi.SHA224.getIntValue(), OZET_TIPI_SHA224);
        HASH_TYPE_MAP.put(OzetTipi.SHA256.getIntValue(), OZET_TIPI_SHA256);
        HASH_TYPE_MAP.put(OzetTipi.SHA384.getIntValue(), OZET_TIPI_SHA384);
        HASH_TYPE_MAP.put(OzetTipi.SHA512.getIntValue(), OZET_TIPI_SHA512);

    }

    private static void addInitialPubKey(byte[] pubKeyPart1, byte[] pubKeyPart2) {
        int lengthOfPubKey = pubKeyPart1.length;
        byte[] publicKey = new byte[lengthOfPubKey];
        for (int i = 0; i < lengthOfPubKey; i++) {
            publicKey[i] = (byte) (pubKeyPart1[i] ^ pubKeyPart2[i]);
        }
        try {
            String pubKeySHA1Hash = Base64.encode(DigestUtil.digest(DigestAlg.SHA1, publicKey));
            PUB_KEY_MAP.put(pubKeySHA1Hash, publicKey);
            String pubKeySHA512Hash = Base64.encode(DigestUtil.digest(DigestAlg.SHA512, publicKey));
            PUB_KEY_MAP.put(pubKeySHA512Hash, publicKey);
        } catch (CryptoException e) {
            logger.error("Error initializing public key", e);
        }
    }

    public static void addPubKey(String hash, byte[] publicKey) {
        PUB_KEY_MAP.put(hash, publicKey);
    }

    private static boolean isCertStoreCountryTurkey() {
        try {
            String resource = "/" + CertStoreUtil.class.getName().replace(".", "/") + ".class";
            String fullPath = CertStoreUtil.class.getResource(resource).toString();
            String archivePath = fullPath.substring(0, fullPath.length() - resource.length());

            URL url = new URL(archivePath + "/META-INF/MANIFEST.MF");

            Manifest mf = null;
            try {
                mf = new Manifest(url.openStream());
            } catch (IOException e) {
                logger.trace("Türkiye's public key will be used.");
                return true;
            }

            String cc = mf.getMainAttributes().getValue("cc");
            if(cc == null){
                logger.info("Türkiye's public key will be used.");
                return true;
            }
            logger.info("KKTC's public key will be used.");
            return false;
        } catch (Exception e) {
            logger.error("isCertStoreCountryTurkey() failed. Türkiye's public key will be used.", e);
            return true;
        }
    }

    private static DepoASNKokSertifika _depoEklenecekTOAsnKok(DepoKokSertifika aKok)
            throws CertStoreException {
        try {
            DepoASNKokSertifika sertifika = new DepoASNKokSertifika();

            ECertificate certificate = new ECertificate(aKok.getValue());

            DepoASNEklenecekKokSertifika eklenecek = asnCertTOAsnEklenecek(certificate.getObject(), aKok.getKokTipi(), aKok.getKokGuvenSeviyesi());
            sertifika.set_eklenecekSertifika(eklenecek);
            return sertifika;
        } catch (Exception e) {
            throw new CertStoreException("Depo koksertifika tipinden asn eklenecekkoksertifika tipine cevirim sirasinda hata olustu.", e);
        }
    }

    private static DepoASNKokSertifika _depoSilinecekTOAsnKok(DepoSilinecekKokSertifika aKok)
            throws CertStoreException {
        try {
            DepoASNKokSertifika sertifika = new DepoASNKokSertifika();
            ECertificate certificate = new ECertificate(aKok.getValue());

            DepoASNSilinecekKokSertifika silinecek = asnCertTOAsnSilinecek(certificate.getObject());
            sertifika.set_silinecekSertifika(silinecek);
            return sertifika;
        } catch (Exception e) {
            throw new CertStoreException("Depo silinecekkoksertifika tipinden asn silinecekkoksertifika tipine cevirim sirasinda hata olustu.", e);
        }
    }

    public static DepoASNSilinecekKokSertifika asnCertTOAsnSilinecek(Certificate aCert)
            throws CertStoreException {
        try 
        {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } 
        catch (LE e)
        {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }

        try {
            DepoASNSilinecekKokSertifika silinecek = new DepoASNSilinecekKokSertifika();
            silinecek.kokIssuerName = aCert.tbsCertificate.issuer;
            silinecek.kokSerialNumber = aCert.tbsCertificate.serialNumber;
            silinecek.kokSubjectName = aCert.tbsCertificate.subject;
            ECertificate esyacer = new ECertificate(aCert);
            silinecek.kokSertifikaValue = new Asn1OctetString(esyacer.getEncoded());
            return silinecek;
        } catch (Exception e) {
            throw new CertStoreException("asn certificate tipinden asn silinecekkoksertifika tipine cevrim sirasinda hata olustu.", e);
        }
    }

    public static DepoASNEklenecekKokSertifika asnCertTOAsnEklenecek(Certificate aCert, SertifikaTipi aTip, GuvenlikSeviyesi aSeviye)
            throws CertStoreException 
    {
        try 
        {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } 
        catch (LE e)
        {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }

        try {
            DepoASNEklenecekKokSertifika eklenecek = new DepoASNEklenecekKokSertifika();
            eklenecek.kokEndDate = aCert.tbsCertificate.validity.notAfter;
            eklenecek.kokStartDate = aCert.tbsCertificate.validity.notBefore;
            eklenecek.kokSerialNumber = aCert.tbsCertificate.serialNumber;
            eklenecek.kokIssuerName = aCert.tbsCertificate.issuer;
            eklenecek.kokSubjectName = aCert.tbsCertificate.subject;
            eklenecek.kokGuvenSeviyesi = KOKGuvenSeviyesi.valueOf(aSeviye.getIntValue());
            eklenecek.kokSertifikaTipi = KokSertifikaTipi.valueOf(aTip.getIntValue());
            ECertificate esyacer = new ECertificate(aCert);
            eklenecek.kokSertifikaValue = new Asn1OctetString(esyacer.getEncoded());

            EKeyUsage ku = esyacer.getExtensions().getKeyUsage();
            if (ku != null)
                eklenecek.kokKeyUsage = ku.getObject();
            //TODO keyusage in null olması durumunda hepsi var gibi kabul et
            ESubjectKeyIdentifier skeyid = esyacer.getExtensions().getSubjectKeyIdentifier();
            if (skeyid != null) {
                eklenecek.kokSubjectKeyIdentifier = new Asn1OctetString(skeyid.getValue());
            }

            //byte[] digest = DigestUtil.digest(DigestAlg.SHA1, esyacer.getEncoded());
            eklenecek.kokSertifikaHash = new Asn1OctetString(/*digest*/);
            return eklenecek;
        } catch (Exception e) {
            throw new CertStoreException("asn certificate tipinden asn eklenecekkoksertifika tipine cevirim sirasinda hata olustu.", e);
        }

    }

    public static DepoSertifika eSYASertifikaTODepoSertifika(ECertificate aSertifika)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }

        DepoSertifika sertifika = new DepoSertifika();
        sertifika.setValue(aSertifika.getEncoded());
        sertifika.setSerialNumber(aSertifika.getSerialNumber().toByteArray());
        sertifika.setIssuerName(normalizeName(aSertifika.getIssuer().getObject()));
        sertifika.setSubjectName(normalizeName(aSertifika.getSubject().getObject()));

        Asn1BitString ku = aSertifika.getExtensions().getKeyUsage().getObject();
        if (ku != null)
            sertifika.setKeyUsageStr(_keyUsageBoolTOBinary(ku));

        sertifika.setSubjectKeyID(aSertifika.getExtensions().getSubjectKeyIdentifier().getValue());
        sertifika.setEndDate(new Date(aSertifika.getNotAfter().getTimeInMillis()));
        sertifika.setStartDate(new Date(aSertifika.getNotBefore().getTimeInMillis()));
        String eposta = aSertifika.getEmail();
        if (eposta != null)
            sertifika.setEPosta(eposta);
        return sertifika;
    }


    private static DepoKokSertifika _eSYASertifikaTODepoKokSertifika(ECertificate aSertifika)
            throws CertStoreException {
        DepoKokSertifika sertifika = new DepoKokSertifika();
        sertifika.setValue(aSertifika.getEncoded());
        sertifika.setSerialNumber(aSertifika.getSerialNumber().toByteArray());

        sertifika.setIssuerName(normalizeName(aSertifika.getIssuer().getObject()));
        sertifika.setSubjectName(normalizeName(aSertifika.getSubject().getObject()));

        EKeyUsage ku = aSertifika.getExtensions().getKeyUsage();
        if (ku != null)
            sertifika.setKeyUsageStr(_keyUsageBoolTOBinary(ku.getObject()));

        ESubjectKeyIdentifier skeyid = aSertifika.getExtensions().getSubjectKeyIdentifier();
        if (skeyid != null)
            sertifika.setSubjectKeyIdentifier(skeyid.getValue());

        sertifika.setEndDate(new Date(aSertifika.getNotAfter().getTimeInMillis()));
        sertifika.setStartDate(new Date(aSertifika.getNotBefore().getTimeInMillis()));

        return sertifika;
    }

    public static DepoKokSertifika asnCertTODepoEklenecek(Certificate aCert)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        try {
            ECertificate esyacer = new ECertificate(aCert);
            return _eSYASertifikaTODepoKokSertifika(esyacer);
        } catch (Exception e) {
            throw new CertStoreException("asn certificate tipinden depo koksertifika tipine cevirim sirasinda hata olustu.", e);
        }
    }

    private static DepoSilinecekKokSertifika _asnCertTODepoSilinecek(Certificate aCert)
            throws CertStoreException {
        try {
            DepoSilinecekKokSertifika sertifika = new DepoSilinecekKokSertifika();
            ECertificate esyacert = new ECertificate(aCert);
            sertifika.setValue(esyacert.getEncoded());
            sertifika.setIssuerName(UtilName.name2byte(esyacert.getIssuer().getObject()));
            sertifika.setSubjectName(UtilName.name2byte(esyacert.getSubject().getObject()));
            sertifika.setSerialNumber(esyacert.getSerialNumber().toByteArray());
            return sertifika;
        } catch (Exception e) {
            throw new CertStoreException("asn certificate tipinden depo silinecekkoksertifika tipine cevirim sirasinda hata olustu.", e);
        }
    }


    public static DepoSilinecekKokSertifika asnSilinecekToDepoSilinecek(DepoASNSilinecekKokSertifika aSilinecek)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        try {
            ECertificate certificate = new ECertificate(aSilinecek.kokSertifikaValue.value);
            return _asnCertTODepoSilinecek(certificate.getObject());
        } catch (Exception e) {
            throw new CertStoreException("depo silinecekkoksertifika tipinden asn silinecekkoksertifika tipine cevirim sirasinda hata olustu.", e);
        }
    }

    public static DepoKokSertifika asnEklenecekTODepoKok(DepoASNEklenecekKokSertifika aEklenecek)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        try {
            byte[] value = aEklenecek.kokSertifikaValue.value;
            ECertificate esyacer = new ECertificate(value);
            DepoKokSertifika kok = _eSYASertifikaTODepoKokSertifika(esyacer);
            kok.setKokTipi(SertifikaTipi.getNesne(aEklenecek.kokSertifikaTipi.getValue()));
            kok.setKokGuvenSeviyesi(GuvenlikSeviyesi.getNesne(aEklenecek.kokGuvenSeviyesi.getValue()));
            return kok;
        } catch (Exception e) {
            throw new CertStoreException("asn eklenecekkoksertifika tipinden depo koksertifika tipine cevirim sirasinda hata olustu.", e);
        }
    }
    
    public static DepoNitelikSertifikasi asnAttributeCertToDepoNitelikSertifikasi(EAttributeCertificate aAttrCert)
    {
    	DepoNitelikSertifikasi ns = new DepoNitelikSertifikasi();
    	ns.setHolderDNName(aAttrCert.getHolder().getEntityName().getElement(0).toString());
    	ns.setValue(aAttrCert.getEncoded());
    	return ns;
    }
    

    public static DepoSIL asnCRLTODepoSIL(byte[] aCRL)
            throws CertStoreException, IOException, ESYAException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new ESYAException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        DepoSIL depoSIL = new DepoSIL();
        ECRL esyaSIL = new ECRL(aCRL);

        depoSIL.setIssuerName(normalizeName(esyaSIL.getIssuer().getObject()));
        depoSIL.setThisUpdate(new java.sql.Date(esyaSIL.getThisUpdate().getTimeInMillis()));
        depoSIL.setNextUpdate(new java.sql.Date(esyaSIL.getNextUpdate().getTimeInMillis()));

        BigInteger crlNumber = esyaSIL.getCRLNumber();
        if (crlNumber != null)
            depoSIL.setSILNumber(crlNumber.toByteArray());

        depoSIL.setValue(esyaSIL.getEncoded());

        EExtension basecrlnumber = esyaSIL.getCRLExtensions().getDeltaCRLIndicator();
        if (basecrlnumber != null)
            depoSIL.setBaseSILNumber(basecrlnumber.getValue());

        if (basecrlnumber != null)
            depoSIL.setSILTipi(SILTipi.DELTA);
        else
            depoSIL.setSILTipi(SILTipi.BASE);

        return depoSIL;
    }


    public static DepoOCSPToWrite asnOCSPResponseTODepoOCSP(EOCSPResponse aOCSPResponse)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        try {
            EBasicOCSPResponse basicOCSPResponse = aOCSPResponse.getBasicOCSPResponse();

            byte[] respID = basicOCSPResponse.getTbsResponseData().getResponderID().getEncoded();

            Date producedAt = new Date(basicOCSPResponse.getTbsResponseData().getProducetAt().getTime().getTime());

            DepoOCSPToWrite depoOcsp = new DepoOCSPToWrite();

            depoOcsp.setOCSPResponderID(respID);
            depoOcsp.setOCSPProducedAt(producedAt);
            depoOcsp.setBasicOCSPResponse(basicOCSPResponse.getEncoded());
            depoOcsp.setOCSPResponse(aOCSPResponse.getEncoded());
            return depoOcsp;

        } catch (Exception e) {
            throw new CertStoreException("Asn tipi ocsp cevabi DepoOCSP nesnesine cevrilirken hata olustu.", e);
        }

    }

    public static boolean verifyDepoKokSertifika(DepoKokSertifika aKok)
            throws CertStoreException {
        return verifyDepoKokSertifika(aKok, null);
    }

    public static boolean verifyDepoKokSertifika(DepoKokSertifika aKok, String hash)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        if (aKok.getKokGuvenSeviyesi().equals(GuvenlikSeviyesi.PERSONAL))
            return true;

        DepoASNKokSertifika asnkok = _depoEklenecekTOAsnKok(aKok);
        return _asnKokDogrula(asnkok, aKok.getSatirImzasi(), hash);
    }

    public static boolean verifyDepoSilinecekKokSertifika(DepoSilinecekKokSertifika aKok)
            throws CertStoreException {
        return verifyDepoSilinecekKokSertifika(aKok, null);
    }

    private static boolean verifyDepoSilinecekKokSertifika(DepoSilinecekKokSertifika aKok, String hash)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        DepoASNKokSertifika asnkok = _depoSilinecekTOAsnKok(aKok);
        return _asnKokDogrula(asnkok, aKok.getSatirImzasi(), hash);
    }

    private static boolean _asnKokDogrula(DepoASNKokSertifika aASNKok, byte[] aSatirImza, String requestedAnchorHash)
            throws CertStoreException {
        try {
            DepoASNRawImza rawimza = new DepoASNRawImza();
            AsnIO.derOku(rawimza, aSatirImza);

            byte[] imza = rawimza.imza.value;
            byte[] pubkeyhash = rawimza.publicKeyHash.value;

            SignatureAlg signatureAlg = SignatureAlg.RSA_SHA512;
            if (pubkeyhash.length == 20) // İleride bütün imzalar RSA_SHA512 olacak ve bu if kalkacak. Eskileri de desteklemek için length'e göre değişiklik yaptık.
                signatureAlg = SignatureAlg.RSA_SHA1;

            PublicKey key = getPublickeyForStore(requestedAnchorHash);

            byte[] dogrulanacak = AsnIO.derEncode(aASNKok);
            boolean signResult = SignUtil.verify(signatureAlg, dogrulanacak, imza, key);
            if (signResult == false) {
                logger.debug("Can not validate signature of Trusted Root Certificate. Param: " + requestedAnchorHash);
            }
            return signResult;

        } catch (Exception e) {
            throw new CertStoreException("Koksertifika dogrulamada hata olustu.", e);
        }
    }

    private static PublicKey getPublickeyForStore(String requestedAnchorHash) throws CryptoException, CertStoreException {
        byte[] publicKeyBytes;

        if (requestedAnchorHash == null) {
            if (isCertStoreCountryTurkey) {
                publicKeyBytes = PUB_KEY_MAP.get(PUB_KEY_1_HASH); //Türkiye
            } else {
                publicKeyBytes = PUB_KEY_MAP.get(PUB_KEY_2_HASH); // Kıbrıs
            }
        } else {
            publicKeyBytes = PUB_KEY_MAP.get(requestedAnchorHash);
            if (publicKeyBytes == null) {
                throw new CertStoreException("Can not find public key for given anchor hash: " + requestedAnchorHash);
            }
        }

        return KeyUtil.decodePublicKey(AsymmetricAlg.RSA, publicKeyBytes);
    }


    public static boolean verifyEncodedRootCertificate(byte[] aDogrulanacak, DepoASNRawImza aSatirImza)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        try {
            byte[] imza = aSatirImza.imza.value;
            byte[] pubkeyhash = aSatirImza.publicKeyHash.value;

            String base64pkhash = Base64.encode(pubkeyhash);
            byte[] pubkey = PUB_KEY_MAP.get(base64pkhash);

            if (pubkey == null) {
                return false;
            }

            PublicKey key = KeyUtil.decodePublicKey(AsymmetricAlg.RSA, pubkey);

            SignatureAlg signatureAlg = SignatureAlg.RSA_SHA512;
            if (pubkeyhash.length == 20) // İleride bütün imzalar RSA_SHA512 olacak ve bu if kalkacak. Eskileri de desteklemek için length'e göre değişiklik yaptık.
                signatureAlg = SignatureAlg.RSA_SHA1;

            return SignUtil.verify(signatureAlg, aDogrulanacak, imza, key);

        } catch (Exception e) {
            throw new CertStoreException("Koksertifika dogrulamada hata olustu.", e);
        }

    }

    private static String _keyUsageBoolTOBinary(Asn1BitString aKullanim) {
        int length = aKullanim.numbits;
        int diff = 9 - length;
        BitArray bitArray = new BitArray(length, aKullanim.value);
        //String ss = bitArray.toString();
        String ku = bitArray.toString().replaceAll(" ", "");
        StringBuffer sb = new StringBuffer(ku);
        for (int i = 0; i < diff; i++) {
            sb.append("0");
        }

        return sb.toString();
    }

    public static byte[] getNormalizeName(Name aName) {
        try {
            return normalizeName(aName);
        } catch (Exception e) {
            logger.warn("Warning in CertStoreUtil", e);
            return null;
        }
    }

    public static byte[] normalizeName(Name aName)
            throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        try {
            Name normalizedName = UtilName.string2Name(UtilName.name2String(aName), true);
            return UtilName.name2byte(normalizedName);
        } catch (Exception e) {
            throw new CertStoreException("Normalization isleminde hata olustu.", e);
        }
    }

    public static List<DepoOzet> convertToDepoOzet(byte[] aValue, OzneTipi aObjectType) {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }

        Set<Integer> keys = HASH_TYPE_MAP.keySet();
        List<DepoOzet> ozetler = new ArrayList<DepoOzet>();

        for (Integer i : keys) {
            try {
                DepoOzet ozet = new DepoOzet();
                byte[] ozetDeger = DigestUtil.digest(HASH_TYPE_MAP.get(i), aValue);
                ozet.setHashValue(ozetDeger);
                ozet.setHashType(i);
                ozet.setObjectType(aObjectType.getIntValue());
                ozetler.add(ozet);
            } catch (Exception e) {
                logger.error("Error in CertStoreUtil", e);
            }
        }

        return ozetler;

    }

    public static boolean isOCSPResponseForCertificate(EBasicOCSPResponse aBasicOCSPResponse, ECertificate aCer) {
        ESingleResponse singleResponse = getOCSPResponseForCertificate(aBasicOCSPResponse, aCer);
        return (singleResponse != null);
    }

    /**
     * BasicOcpsResponse icinde verilen bir sertifikaya ait iptal bilgisini geri doner
     *
     * @param aBasicOCSPResponse Icinde iptal bilgisi aranacak ocspresponse
     * @param aCer               Iptal bilgisi aranacak sertifika
     * @return Ilgili sertifikaya ait SingleResponse
     */
    public static ESingleResponse getOCSPResponseForCertificate(EBasicOCSPResponse aBasicOCSPResponse, ECertificate aCer) {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }

        EResponseData responseData = aBasicOCSPResponse.getTbsResponseData();

        BigInteger serialNo = aCer.getSerialNumber();

        for (int i = 0; i < responseData.getSingleResponseCount(); i++) {
            ESingleResponse esr = responseData.getSingleResponse(i);
            ECertID certid = esr.getCertID();
            if (serialNo.equals(certid.getSerialNumber())) {
                if (_check(certid, aCer))
                    return esr;
            }
        }

        return null;
    }

    private static boolean _check(ECertID aCertID, ECertificate aCer) {
        try {
            DigestAlg ozetAlg = DigestAlg.fromOID(aCertID.getHashAlgorithm().getAlgorithm().value);
            byte[] ozet = DigestUtil.digest(ozetAlg, aCer.getIssuer().getEncoded());
            return Arrays.equals(ozet, aCertID.getIssuerNameHash());
        } catch (Exception e) {
            logger.error("Error in CertStoreUtil", e);
            return false;
        }
    }


    public static DepoSertifikaOcsps toDepoSertifikaOcsps(ESingleResponse aSingleResponse, DepoOCSP aDepoOCSP, DepoSertifika aDepoSertifika) throws CertStoreException {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        try {
            DepoSertifikaOcsps depoSertifikaOcsps = new DepoSertifikaOcsps();
            depoSertifikaOcsps.setOcspNo(aDepoOCSP.getOCSPNo());
            depoSertifikaOcsps.setSertifikaNo(aDepoSertifika.getSertifikaNo());
            depoSertifikaOcsps.setThisUpdate(new Date(aSingleResponse.getThisUpdate().getTime().getTime()));
            depoSertifikaOcsps.setStatus(new Long(aSingleResponse.getCertificateStatus()));
            java.util.Date revocationDate = null;
            if(aSingleResponse.getRevocationTime() != null)
            revocationDate = aSingleResponse.getRevocationTime().getTime();
            Date revocationTime = null;
            if (revocationDate != null)
                revocationTime = new Date(revocationDate.getTime());
            depoSertifikaOcsps.setRevocationTime(revocationTime);
            depoSertifikaOcsps.setRevocationReason(new Long(aSingleResponse.getRevokationReason()));
            return depoSertifikaOcsps;
        } catch (Exception e) {
            throw new CertStoreException("DepoSertifikaOcsps nesnesine cevrilirken hata olustu.", e);
        }
    }
}
