using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.attrcert;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.asn.depo;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    public static class CertStoreUtil
    {
        public static readonly String DEPO_DIZIN_ADI = ".sertifikadeposu";

        private static readonly String PUB_KEY_1_HASH = "E8pMzbTsYEPKmfc8vklz+/03HCw=";
        private static readonly byte[] PUB_KEY_1_PART_1 = { 58, 214, 99, 86, 93, 100, 104, 122, 67, 252, 36, 256 - 29, 256 - 123, 35, 47, 47, 11, 15, 89, 102, 256 - 16, 123, 256 - 13, 122, 256 - 52, 256 - 94, 101, 256 - 4, 120, 256 - 18, 103, 115, 105, 256 - 15, 256 - 125, 256 - 49, 256 - 40, 16, 102, 40, 121, 28, 12, 97, 79, 256 - 27, 256 - 110, 256 - 19, 56, 31, 256 - 9, 94, 256 - 62, 256 - 12, 256 - 22, 64, 256 - 95, 256 - 82, 90, 43, 81, 256 - 11, 16, 99, 58, 256 - 45, 256 - 66, 64, 256 - 55, 256 - 88, 103, 256 - 101, 256 - 78, 256 - 30, 256 - 93, 256 - 122, 123, 256 - 44, 256 - 115, 256 - 74, 256 - 37, 256 - 103, 256 - 93, 256 - 85, 66, 71, 256 - 102, 63, 66, 256 - 75, 256 - 96, 256 - 112, 256 - 114, 256 - 49, 86, 256 - 115, 256 - 57, 9, 101, 5, 90, 88, 94, 105, 76, 256 - 20, 40, 57, 54, 107, 57, 5, 256 - 81, 99, 256 - 6, 256 - 55, 256 - 13, 88, 82, 256 - 48, 78, 124, 256 - 106, 69, 256 - 34, 256 - 72, 256 - 36, 74, 124, 52, 1, 256 - 54, 256 - 79, 256 - 84, 33, 116, 126, 256 - 12, 22, 256 - 31, 120, 68, 68, 28, 127, 59, 256 - 124, 256 - 118, 256 - 85, 111, 59, 114, 256 - 122, 256 - 2, 256 - 80, 256 - 17, 256 - 81, 52, 39, 256 - 13, 6, 256 - 63, 256 - 86, 256 - 47, 256 - 75, 256 - 2, 256 - 20, 118, 107, 83, 256 - 83, 27, 16, 13, 62, 256 - 106, 256 - 90, 256 - 108, 59, 256 - 34, 256 - 126, 92, 256 - 42, 256 - 27, 256 - 21, 256 - 76, 120, 124, 73, 256 - 33, 256 - 13, 256 - 123, 17, 108, 31, 44, 102, 86, 256 - 73, 68, 2, 256 - 117, 108, 256 - 86, 26, 256 - 15, 105, 256 - 70, 97, 256 - 118, 256 - 43, 256 - 102, 256 - 124, 256 - 23, 19, 256 - 87, 88, 256 - 94, 22, 256 - 23, 17, 256 - 102, 256 - 93, 256 - 113, 79, 256 - 76, 256 - 19, 256 - 4, 66, 256 - 66, 79, 26, 80, 256 - 45, 256 - 58, 256 - 22, 256 - 15, 256 - 93, 30, 59, 256 - 38, 123, 82, 256 - 60, 256 - 20, 256 - 123, 256 - 128, 46, 101, 121, 18, 53, 256 - 14, 256 - 57, 82, 256 - 62, 256 - 103, 125, 256 - 112, 24, 11, 256 - 125, 256 - 81, 256 - 30, 20, 7, 22, 256 - 26, 61, 96, 256 - 68, 256 - 113, 61, 256 - 11, 256 - 88, 256 - 63, 55, 256 - 3, 256 - 112, 256 - 119, 85, 79, 256 - 80, 33, 256 - 29, 22, 1, 37, 256 - 84, 256 - 24, 256 - 86, 256 - 27, 91, 256 - 105, 108, 256 - 86, 37, 256 - 106, 16, 12, 100, 256 - 37, 256 - 60, 100, 256 - 18, 256 - 77, 256 - 113, 256 - 92, 72, 108, 110, 256 - 48, 256 - 78, 256 - 28, 43, 16, 256 - 77, 256 - 102, 256 - 39, 256 - 13, 43, 256 - 79, 256 - 103, 256 - 6, 256 - 95, 256 - 98, 85, 28, 112, 126, 90, 256 - 50, 256 - 2, 76, 256 - 70, 256 - 29, 256 - 124, 11, 256 - 6, 12, 71, 256 - 20, 256 - 32, 256 - 84, 256 - 61, 11, 44, 80, 256 - 10, 74, 256 - 60, 70, 15, 256 - 104, 256 - 85, 256 - 32, 33, 256 - 47, 109, 84, 10, 256 - 99, 256 - 90, 118, 44, 256 - 112, 256 - 107, 256 - 48, 256 - 31, 72, 256 - 2, 256 - 10, 256 - 112, 256 - 44, 256 - 57, 256 - 57, 256 - 86, 68, 256 - 41, 256 - 28, 0, 256 - 47, 110, 37, 77, 256 - 16, 256 - 63, 256 - 120, 12, 54, 256 - 116, 256 - 62, 256 - 107, 256 - 60, 12, 30, 256 - 31, 78, 256 - 125, 84, 256 - 94, 61, 256 - 57, 256 - 115, 46, 256 - 19, 256 - 32, 256 - 84, 66, 56, 0, 256 - 50, 256 - 109, 256 - 3, 256 - 20, 256 - 124, 256 - 77, 256 - 86, 12, 75, 256 - 87, 76, 125, 256 - 84, 256 - 115, 80, 256 - 18, 97, 0, 256 - 13, 8, 64, 256 - 42, 118, 256 - 119, 256 - 58, 256 - 57, 87, 256 - 48, 117, 256 - 15, 256 - 110, 97, 256 - 16, 33, 256 - 2, 256 - 114, 256 - 113, 0, 85, 27, 256 - 45, 256 - 109, 256 - 119, 256 - 97, 77, 88, 256 - 124, 256 - 55, 256 - 35, 76, 256 - 42, 256 - 21, 256 - 85, 256 - 17, 41, 96, 90, 256 - 39, 89, 127, 116, 256 - 76, 69, 14, 2, 256 - 110, 81, 112, 58, 48, 12, 256 - 97, 63, 90, 256 - 52, 103, 256 - 19, 256 - 66, 256 - 123, 256 - 106, 256 - 29, 5, 256 - 78, 256 - 57, 110, 256 - 7, 65, 256 - 20, 22, 65, 256 - 95, 256 - 55, 256 - 83, 95, 256 - 71, 256 - 14, 256 - 30, 256 - 4, 12, 256 - 49, 16, 256 - 112, 256 - 112, 90, 256 - 104, 256 - 104, 256 - 5, 256 - 17, 79, 256 - 88, 34, 256 - 65, 256 - 21, 256 - 51, 103, 87, 45, 256 - 102, 67, 256 - 73, 256 - 101, 55, 256 - 60, 256 - 30, 21, 256 - 105, 99, 256 - 41, 256 - 44, 256 - 38, 256 - 14, 256 - 10, 96, 256 - 12, 110, 102, 115, 32, 103 };
        private static readonly byte[] PUB_KEY_1_PART_2 = { 10, 84, 97, 116, 109, 105, 110, 115, 105, 122, 108, 101, 114, 46, 46, 46, 10, 10, 89, 101, 114, 121, 256 - 4, 122, 256 - 4, 32, 103, 256 - 10, 122, 108, 101, 114, 105, 110, 101, 32, 100, 101, 118, 32, 98, 105, 114, 32, 109, 101, 109, 101, 32, 103, 105, 98, 105, 32, 103, 256 - 10, 114, 256 - 4, 110, 256 - 4, 121, 111, 114, 46, 32, 77, 101, 109, 101, 121, 101, 32, 97, 256 - 16, 256 - 3, 122, 108, 97, 114, 256 - 3, 110, 256 - 3, 32, 117, 122, 97, 116, 256 - 3, 112, 32, 101, 109, 109, 101, 107, 32, 105, 256 - 25, 105, 110, 32, 98, 105, 114, 98, 105, 114, 108, 101, 114, 105, 110, 105, 32, 105, 116, 105, 121, 111, 114, 44, 32, 101, 122, 105, 121, 111, 114, 59, 32, 100, 97, 104, 97, 115, 256 - 3, 44, 32, 97, 108, 116, 116, 97, 110, 32, 97, 108, 116, 97, 32, 98, 105, 114, 98, 105, 114, 108, 101, 114, 105, 110, 100, 101, 110, 32, 110, 101, 102, 114, 101, 116, 32, 101, 100, 105, 121, 111, 114, 108, 97, 114, 46, 10, 10, 72, 101, 109, 101, 110, 32, 104, 101, 112, 115, 105, 32, 109, 111, 100, 101, 114, 110, 32, 100, 256 - 4, 110, 121, 97, 110, 256 - 3, 110, 32, 34, 121, 101, 110, 105, 34, 108, 105, 107, 32, 102, 101, 116, 105, 256 - 2, 105, 122, 109, 105, 32, 107, 97, 114, 256 - 2, 256 - 3, 115, 256 - 3, 110, 100, 97, 32, 98, 256 - 4, 121, 256 - 4, 108, 101, 110, 105, 121, 111, 114, 46, 32, 10, 10, 83, 97, 104, 105, 112, 32, 111, 108, 100, 117, 107, 108, 97, 114, 256 - 3, 32, 110, 101, 32, 118, 97, 114, 115, 97, 44, 32, 103, 256 - 10, 122, 108, 101, 114, 105, 110, 100, 101, 32, 118, 101, 32, 103, 256 - 10, 110, 256 - 4, 108, 108, 101, 114, 105, 110, 100, 101, 32, 256 - 25, 111, 107, 32, 256 - 25, 97, 98, 117, 107, 32, 101, 115, 107, 105, 121, 111, 114, 59, 32, 104, 101, 108, 101, 32, 100, 101, 32, 101, 115, 107, 105, 32, 100, 111, 115, 116, 108, 97, 114, 33, 46, 46, 10, 10, 66, 105, 114, 97, 122, 32, 100, 97, 32, 256 - 2, 97, 256 - 2, 256 - 3, 114, 256 - 3, 121, 111, 114, 108, 97, 114, 32, 107, 101, 110, 100, 105, 32, 104, 97, 108, 108, 101, 114, 105, 110, 101, 33, 32, 10, 10, 76, 97, 102, 256 - 3, 110, 32, 103, 101, 108, 105, 256 - 2, 105, 32, 121, 97, 44, 32, 116, 256 - 4, 112, 108, 256 - 4, 32, 100, 97, 108, 109, 97, 32, 109, 111, 100, 97, 115, 256 - 3, 32, 256 - 25, 256 - 3, 107, 256 - 3, 121, 111, 114, 44, 32, 104, 101, 109, 101, 110, 32, 100, 97, 108, 103, 256 - 3, 256 - 25, 32, 111, 108, 117, 121, 111, 114, 108, 97, 114, 59, 32, 97, 109, 97, 32, 98, 97, 107, 256 - 3, 121, 111, 114, 108, 97, 114, 32, 121, 105, 110, 101, 32, 109, 117, 116, 115, 117, 122, 108, 97, 114, 46, 32, 80, 105, 108, 111, 116, 108, 117, 107, 32, 109, 111, 100, 97, 32, 111, 108, 117, 121, 111, 114, 44, 32, 117, 256 - 25, 117, 256 - 2, 32, 100, 101, 114, 115, 105, 32, 97, 108, 256 - 3, 121, 111, 114, 108, 97, 114, 44, 32, 98, 114, 256 - 10, 118, 101, 108, 101, 114, 32, 102 };
        //private static final String PUB_KEY_1_HASH = "E8pMzbTsYEPKmfc8vklz+/03HCw=";
        //private static final byte[] PUB_KEY_1_PART_1 ={58, -42, 99, 86, 93, 100, 104, 122, 67, -4, 36, -29, -123, 35, 47, 47, 11, 15, 89, 102, -16, 123, -13, 122, -52, -94, 101, -4, 120, -18, 103, 115, 105, -15, -125, -49, -40, 16, 102, 40, 121, 28, 12, 97, 79, -27, -110, -19, 56, 31, -9, 94, -62, -12, -22, 64, -95, -82, 90, 43, 81, -11, 16, 99, 58, -45, -66, 64, -55, -88, 103, -101, -78, -30, -93, -122, 123, -44, -115, -74, -37, -103, -93, -85, 66, 71, -102, 63, 66, -75, -96, -112, -114, -49, 86, -115, -57, 9, 101, 5, 90, 88, 94, 105, 76, -20, 40, 57, 54, 107, 57, 5, -81, 99, -6, -55, -13, 88, 82, -48, 78, 124, -106, 69, -34, -72, -36, 74, 124, 52, 1, -54, -79, -84, 33, 116, 126, -12, 22, -31, 120, 68, 68, 28, 127, 59, -124, -118, -85, 111, 59, 114, -122, -2, -80, -17, -81, 52, 39, -13, 6, -63, -86, -47, -75, -2, -20, 118, 107, 83, -83, 27, 16, 13, 62, -106, -90, -108, 59, -34, -126, 92, -42, -27, -21, -76, 120, 124, 73, -33, -13, -123, 17, 108, 31, 44, 102, 86, -73, 68, 2, -117, 108, -86, 26, -15, 105, -70, 97, -118, -43, -102, -124, -23, 19, -87, 88, -94, 22, -23, 17, -102, -93, -113, 79, -76, -19, -4, 66, -66, 79, 26, 80, -45, -58, -22, -15, -93, 30, 59, -38, 123, 82, -60, -20, -123, -128, 46, 101, 121, 18, 53, -14, -57, 82, -62, -103, 125, -112, 24, 11, -125, -81, -30, 20, 7, 22, -26, 61, 96, -68, -113, 61, -11, -88, -63, 55, -3, -112, -119, 85, 79, -80, 33, -29, 22, 1, 37, -84, -24, -86, -27, 91, -105, 108, -86, 37, -106, 16, 12, 100, -37, -60, 100, -18, -77, -113, -92, 72, 108, 110, -48, -78, -28, 43, 16, -77, -102, -39, -13, 43, -79, -103, -6, -95, -98, 85, 28, 112, 126, 90, -50, -2, 76, -70, -29, -124, 11, -6, 12, 71, -20, -32, -84, -61, 11, 44, 80, -10, 74, -60, 70, 15, -104, -85, -32, 33, -47, 109, 84, 10, -99, -90, 118, 44, -112, -107, -48, -31, 72, -2, -10, -112, -44, -57, -57, -86, 68, -41, -28, 0, -47, 110, 37, 77, -16, -63, -120, 12, 54, -116, -62, -107, -60, 12, 30, -31, 78, -125, 84, -94, 61, -57, -115, 46, -19, -32, -84, 66, 56, 0, -50, -109, -3, -20, -124, -77, -86, 12, 75, -87, 76, 125, -84, -115, 80, -18, 97, 0, -13, 8, 64, -42, 118, -119, -58, -57, 87, -48, 117, -15, -110, 97, -16, 33, -2, -114, -113, 0, 85, 27, -45, -109, -119, -97, 77, 88, -124, -55, -35, 76, -42, -21, -85, -17, 41, 96, 90, -39, 89, 127, 116, -76, 69, 14, 2, -110, 81, 112, 58, 48, 12, -97, 63, 90, -52, 103, -19, -66, -123, -106, -29, 5, -78, -57, 110, -7, 65, -20, 22, 65, -95, -55, -83, 95, -71, -14, -30, -4, 12, -49, 16, -112, -112, 90, -104, -104, -5, -17, 79, -88, 34, -65, -21, -51, 103, 87, 45, -102, 67, -73, -101, 55, -60, -30, 21, -105, 99, -41, -44, -38, -14, -10, 96, -12, 110, 102, 115, 32, 103};
        //private static final byte[] PUB_KEY_1_PART_2 ={10, 84, 97, 116, 109, 105, 110, 115, 105, 122, 108, 101, 114, 46, 46, 46, 10, 10, 89, 101, 114, 121, -4, 122, -4, 32, 103, -10, 122, 108, 101, 114, 105, 110, 101, 32, 100, 101, 118, 32, 98, 105, 114, 32, 109, 101, 109, 101, 32, 103, 105, 98, 105, 32, 103, -10, 114, -4, 110, -4, 121, 111, 114, 46, 32, 77, 101, 109, 101, 121, 101, 32, 97, -16, -3, 122, 108, 97, 114, -3, 110, -3, 32, 117, 122, 97, 116, -3, 112, 32, 101, 109, 109, 101, 107, 32, 105, -25, 105, 110, 32, 98, 105, 114, 98, 105, 114, 108, 101, 114, 105, 110, 105, 32, 105, 116, 105, 121, 111, 114, 44, 32, 101, 122, 105, 121, 111, 114, 59, 32, 100, 97, 104, 97, 115, -3, 44, 32, 97, 108, 116, 116, 97, 110, 32, 97, 108, 116, 97, 32, 98, 105, 114, 98, 105, 114, 108, 101, 114, 105, 110, 100, 101, 110, 32, 110, 101, 102, 114, 101, 116, 32, 101, 100, 105, 121, 111, 114, 108, 97, 114, 46, 10, 10, 72, 101, 109, 101, 110, 32, 104, 101, 112, 115, 105, 32, 109, 111, 100, 101, 114, 110, 32, 100, -4, 110, 121, 97, 110, -3, 110, 32, 34, 121, 101, 110, 105, 34, 108, 105, 107, 32, 102, 101, 116, 105, -2, 105, 122, 109, 105, 32, 107, 97, 114, -2, -3, 115, -3, 110, 100, 97, 32, 98, -4, 121, -4, 108, 101, 110, 105, 121, 111, 114, 46, 32, 10, 10, 83, 97, 104, 105, 112, 32, 111, 108, 100, 117, 107, 108, 97, 114, -3, 32, 110, 101, 32, 118, 97, 114, 115, 97, 44, 32, 103, -10, 122, 108, 101, 114, 105, 110, 100, 101, 32, 118, 101, 32, 103, -10, 110, -4, 108, 108, 101, 114, 105, 110, 100, 101, 32, -25, 111, 107, 32, -25, 97, 98, 117, 107, 32, 101, 115, 107, 105, 121, 111, 114, 59, 32, 104, 101, 108, 101, 32, 100, 101, 32, 101, 115, 107, 105, 32, 100, 111, 115, 116, 108, 97, 114, 33, 46, 46, 10, 10, 66, 105, 114, 97, 122, 32, 100, 97, 32, -2, 97, -2, -3, 114, -3, 121, 111, 114, 108, 97, 114, 32, 107, 101, 110, 100, 105, 32, 104, 97, 108, 108, 101, 114, 105, 110, 101, 33, 32, 10, 10, 76, 97, 102, -3, 110, 32, 103, 101, 108, 105, -2, 105, 32, 121, 97, 44, 32, 116, -4, 112, 108, -4, 32, 100, 97, 108, 109, 97, 32, 109, 111, 100, 97, 115, -3, 32, -25, -3, 107, -3, 121, 111, 114, 44, 32, 104, 101, 109, 101, 110, 32, 100, 97, 108, 103, -3, -25, 32, 111, 108, 117, 121, 111, 114, 108, 97, 114, 59, 32, 97, 109, 97, 32, 98, 97, 107, -3, 121, 111, 114, 108, 97, 114, 32, 121, 105, 110, 101, 32, 109, 117, 116, 115, 117, 122, 108, 97, 114, 46, 32, 80, 105, 108, 111, 116, 108, 117, 107, 32, 109, 111, 100, 97, 32, 111, 108, 117, 121, 111, 114, 44, 32, 117, -25, 117, -2, 32, 100, 101, 114, 115, 105, 32, 97, 108, -3, 121, 111, 114, 108, 97, 114, 44, 32, 98, 114, -10, 118, 101, 108, 101, 114, 32, 102};
        private static readonly Dictionary<String, byte[]> PUB_KEY_MAP = new Dictionary<String, byte[]>();

        private static Dictionary<int, DigestAlg> HASH_TYPE_MAP = new Dictionary<int, DigestAlg>();


        public static readonly DigestAlg OZET_TIPI_SHA1 = DigestAlg.SHA1;
        //public static readonly DigestAlg OZET_TIPI_SHA224 = DigestAlg.SHA224;
        public static readonly DigestAlg OZET_TIPI_SHA256 = DigestAlg.SHA256;
        public static readonly DigestAlg OZET_TIPI_SHA384 = DigestAlg.SHA384;
        public static readonly DigestAlg OZET_TIPI_SHA512 = DigestAlg.SHA512;

        static CertStoreUtil()
        {
            int length = PUB_KEY_1_PART_1.Length;
            byte[] publickey = new byte[length];
            for (int i = 0; i < length; i++)
            {
                publickey[i] = (byte)(PUB_KEY_1_PART_1[i] ^ PUB_KEY_1_PART_2[i]);
            }
            PUB_KEY_MAP.Add(PUB_KEY_1_HASH, publickey);

            HASH_TYPE_MAP[OzetTipi.SHA1.getIntValue()] = OZET_TIPI_SHA1;
            //HASH_TYPE_MAP.Add(OzetTipi.SHA224.getIntValue(), OZET_TIPI_SHA224);
            HASH_TYPE_MAP[OzetTipi.SHA256.getIntValue()] = OZET_TIPI_SHA256;
            HASH_TYPE_MAP[OzetTipi.SHA384.getIntValue()] = OZET_TIPI_SHA384;
            HASH_TYPE_MAP[OzetTipi.SHA512.getIntValue()] = OZET_TIPI_SHA512;


        }

        private static DepoASNKokSertifika _depoEklenecekTOAsnKok(DepoKokSertifika aKok)
        {
            try
            {
                DepoASNKokSertifika sertifika = new DepoASNKokSertifika();
                ECertificate certificate = new ECertificate(aKok.getValue());
                DepoASNEklenecekKokSertifika eklenecek = asnCertTOAsnEklenecek(certificate.getObject(), aKok.getKokTipi(), aKok.getKokGuvenSeviyesi());
                sertifika.Set_eklenecekSertifika(eklenecek);
                return sertifika;
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Depo kök sertifika tipinden asn eklenecek kök sertifika tipine çevirim sırasında hata oluştu.", ex);
            }
        }

        private static DepoASNKokSertifika _depoSilinecekTOAsnKok(DepoSilinecekKokSertifika aKok)
        {
            try
            {
                DepoASNKokSertifika sertifika = new DepoASNKokSertifika();
                ECertificate certificate = new ECertificate(aKok.getValue());
                DepoASNSilinecekKokSertifika silinecek = asnCertTOAsnSilinecek(certificate.getObject());
                sertifika.Set_silinecekSertifika(silinecek);
                return sertifika;
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Depo silinecek kök sertifika tipinden asn silinecek kök sertifika tipine çevirim sırasında hata oluştu.", ex);
            }
        }

        public static DepoASNSilinecekKokSertifika asnCertTOAsnSilinecek(Certificate aCert)
        {
            CheckLicense();
            try
            {
                DepoASNSilinecekKokSertifika silinecek = new DepoASNSilinecekKokSertifika();
                silinecek.kokIssuerName = aCert.tbsCertificate.issuer;
                silinecek.kokSerialNumber = aCert.tbsCertificate.serialNumber;
                silinecek.kokSubjectName = aCert.tbsCertificate.subject;
                ECertificate esyacer = new ECertificate(aCert);
                silinecek.kokSertifikaValue = new Asn1OctetString(esyacer.getBytes());
                return silinecek;
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Asn sertifika tipinden asn silinecek kök sertifika tipine çevrim sırasında hata oluştu.", ex);
            }
        }

        public static void CheckLicense()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.ORTAK);
            }
            catch (LE ex)
            {
                throw new CertStoreException("Lisans kontrolü başarısız. " + ex.Message);
            }
        }

        public static DepoASNEklenecekKokSertifika asnCertTOAsnEklenecek(Certificate aCert, CertificateType aTip, SecurityLevel aSeviye)
        {

            CheckLicense();
            try
            {
                DepoASNEklenecekKokSertifika eklenecek = new DepoASNEklenecekKokSertifika();
                eklenecek.kokEndDate = aCert.tbsCertificate.validity.notAfter;
                eklenecek.kokStartDate = aCert.tbsCertificate.validity.notBefore;
                eklenecek.kokSerialNumber = aCert.tbsCertificate.serialNumber;
                eklenecek.kokIssuerName = aCert.tbsCertificate.issuer;
                eklenecek.kokSubjectName = aCert.tbsCertificate.subject;
                eklenecek.kokGuvenSeviyesi = KOKGuvenSeviyesi.ValueOf(aSeviye.getIntValue());
                eklenecek.kokSertifikaTipi = KokSertifikaTipi.ValueOf(aTip.getIntValue());
                ECertificate esyacer = new ECertificate(aCert);
                eklenecek.kokSertifikaValue = new Asn1OctetString(esyacer.getBytes());

                EKeyUsage ku = esyacer.getExtensions().getKeyUsage();
                if (ku != null)
                    eklenecek.kokKeyUsage = ku.getObject();
                //TODO keyusage in null olması durumunda hepsi var gibi kabul et
                ESubjectKeyIdentifier skeyid = esyacer.getExtensions().getSubjectKeyIdentifier();
                if (skeyid != null)
                {
                    eklenecek.kokSubjectKeyIdentifier = new Asn1OctetString(skeyid.getValue());
                }

                //byte[] digest = DigestUtil.digest(DigestAlg.SHA1, esyacer.getBytes());
                eklenecek.kokSertifikaHash = new Asn1OctetString(/*digest*/);
                return eklenecek;
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Asn sertifika tipinden asn eklenecek kök sertifika tipine çevirim sırasında hata oluştu.", ex);
            }

        }

        public static DepoSertifika eSYASertifikaTODepoSertifika(ECertificate aSertifika)
        {
            CheckLicense();

            DepoSertifika sertifika = new DepoSertifika();
            sertifika.setValue(aSertifika.getBytes());
            sertifika.setSerialNumber(aSertifika.getSerialNumber().GetData());
            sertifika.setIssuerName(normalizeName(aSertifika.getIssuer().getObject()));
            sertifika.setSubjectName(normalizeName(aSertifika.getSubject().getObject()));

            Asn1BitString ku = aSertifika.getExtensions().getKeyUsage().getObject();
            if (ku != null)
                sertifika.setKeyUsageStr(_keyUsageBoolTOBinary(ku));

            sertifika.setSubjectKeyID(aSertifika.getExtensions().getSubjectKeyIdentifier().getValue());
            sertifika.setEndDate(aSertifika.getNotAfter());
            sertifika.setStartDate(aSertifika.getNotBefore());
            String eposta = aSertifika.getEmail();
            if (eposta != null)
                sertifika.setEPosta(eposta);
            return sertifika;
        }


        private static DepoKokSertifika _eSYASertifikaTODepoKokSertifika(ECertificate aSertifika)
        {
            DepoKokSertifika sertifika = new DepoKokSertifika();
            sertifika.setValue(aSertifika.getBytes());
            sertifika.setSerialNumber(aSertifika.getSerialNumber().GetData());

            sertifika.setIssuerName(normalizeName(aSertifika.getIssuer().getObject()));
            sertifika.setSubjectName(normalizeName(aSertifika.getSubject().getObject()));

            EExtensions extensions = aSertifika.getExtensions();
            if (extensions != null)
            {
                EKeyUsage ku = extensions.getKeyUsage();
                if (ku != null)
                    sertifika.setKeyUsageStr(_keyUsageBoolTOBinary(ku.getObject()));

                byte[] skeyid = extensions.getSubjectKeyIdentifier().getValue();

                if (skeyid != null)
                    sertifika.setSubjectKeyIdentifier(skeyid);
            }
            sertifika.setEndDate(aSertifika.getNotAfter());
            sertifika.setStartDate(aSertifika.getNotBefore());

            return sertifika;
        }

        public static DepoKokSertifika asnCertTODepoEklenecek(Certificate aCert)
        {
            CheckLicense();
            try
            {
                ECertificate esyacer = new ECertificate(aCert);
                return _eSYASertifikaTODepoKokSertifika(esyacer);
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Asn sertifika tipinden depo kök sertifika tipine çevirim sırasında hata oluştu.", ex);
            }
        }

        private static DepoSilinecekKokSertifika _asnCertTODepoSilinecek(Certificate aCert)
        {
            try
            {
                DepoSilinecekKokSertifika sertifika = new DepoSilinecekKokSertifika();
                ECertificate esyacert = new ECertificate(aCert);
                sertifika.setValue(esyacert.getBytes());
                sertifika.setIssuerName(UtilName.name2byte(esyacert.getIssuer().getObject()));
                sertifika.setSubjectName(UtilName.name2byte(esyacert.getSubject().getObject()));
                sertifika.setSerialNumber(esyacert.getSerialNumber().GetData());
                return sertifika;
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Asn sertifika tipinden depo silinecek kök sertifika tipine çevirim sırasında hata oluştu.", ex);
            }
        }


        public static DepoSilinecekKokSertifika asnSilinecekToDepoSilinecek(DepoASNSilinecekKokSertifika aSilinecek)
        {
            CheckLicense();
            try
            {
                ECertificate certificate = new ECertificate(aSilinecek.kokSertifikaValue.mValue);
                return _asnCertTODepoSilinecek(certificate.getObject());
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Depo silinecek kök sertifika tipinden asn silinecek kök sertifika tipine çevirim sırasında hata oluştu.", ex);
            }
        }

        public static DepoKokSertifika asnEklenecekTODepoKok(DepoASNEklenecekKokSertifika aEklenecek)
        {
            CheckLicense();
            try
            {
                byte[] value = aEklenecek.kokSertifikaValue.mValue;
                ECertificate esyacer = new ECertificate(value);
                DepoKokSertifika kok = _eSYASertifikaTODepoKokSertifika(esyacer);
                kok.setKokTipi(CertificateType.getNesne(aEklenecek.kokSertifikaTipi.mValue));
                kok.setKokGuvenSeviyesi(SecurityLevel.getNesne(aEklenecek.kokGuvenSeviyesi.mValue));
                return kok;
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Asn eklenecek kök sertifika tipinden depo kök sertifika tipine çevirim sırasında hata oluştu.", ex);
            }
        }

        public static DepoNitelikSertifikasi asnAttributeCertToDepoNitelikSertifikasi(EAttributeCertificate aAttrCert)
        {
            DepoNitelikSertifikasi ns = new DepoNitelikSertifikasi();
            ns.setHolderDNName(aAttrCert.getHolder().getEntityName().getElement(0).ToString());
            ns.setValue(aAttrCert.getEncoded());
            return ns;
        }

        public static DepoSIL asnCRLTODepoSIL(byte[] aCRL)
        {
            CheckLicense();
            DepoSIL depoSIL = new DepoSIL();
            ECRL esyaSIL = new ECRL(aCRL);

            depoSIL.setIssuerName(normalizeName(esyaSIL.getIssuer().getObject()));
            depoSIL.setThisUpdate(esyaSIL.getThisUpdate());
            depoSIL.setNextUpdate(esyaSIL.getNextUpdate());

            BigInteger crlNumber = esyaSIL.getCRLNumber();
            if (crlNumber != null)
                depoSIL.setSILNumber(crlNumber.GetData());

            depoSIL.setValue(esyaSIL.getBytes());

            EExtension basecrlnumber = esyaSIL.getCRLExtensions().getDeltaCRLIndicator();
            if (basecrlnumber != null)
                depoSIL.setBaseSILNumber(basecrlnumber.getValue());

            if (basecrlnumber != null)
                depoSIL.setSILTipi(CRLType.DELTA);
            else
                depoSIL.setSILTipi(CRLType.BASE);

            return depoSIL;
        }

        public static DepoOCSPToWrite asnOCSPResponseTODepoOCSP(EOCSPResponse aOCSPResponse)
        {
            CheckLicense();
            try
            {
                //EBasicOCSPResponse resp = new EBasicOCSPResponse(aBasicOCSPResponse);
                EBasicOCSPResponse basicOCSPResponse = aOCSPResponse.getBasicOCSPResponse();

                //Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                //resp.getTbsResponseData().responderID.Encode(encBuf);
                //resp.getTbsResponseData().getObject().responderID.Encode(encBuf);

                //byte[] respID = encBuf.MsgCopy;
                byte[] respID = basicOCSPResponse.getTbsResponseData().getResponderID().getEncoded();

                DateTime? producedAt = basicOCSPResponse.getTbsResponseData().getProducedAt();//resp.getTbsResponseData().producedAt.GetTime();                
                DepoOCSPToWrite depoOcsp = new DepoOCSPToWrite();

                depoOcsp.setOCSPResponderID(respID);
                depoOcsp.setOCSPProducedAt(producedAt);
                depoOcsp.setBasicOCSPResponse(basicOCSPResponse.getEncoded());
                depoOcsp.setOCSPResponse(aOCSPResponse.getEncoded());
                
                return depoOcsp;

            }
            catch (Exception ex)
            {
                throw new CertStoreException("Asn tipi ocsp cevabi DepoOCSP nesnesine cevrilirken hata olustu.", ex);
            }

        }

        public static bool verifyDepoKokSertifika(DepoKokSertifika aKok)
        {
            CheckLicense();
            if (aKok.getKokGuvenSeviyesi().Equals(SecurityLevel.PERSONAL))
                return true;

            DepoASNKokSertifika asnkok = _depoEklenecekTOAsnKok(aKok);
            return _asnKokDogrula(asnkok, aKok.getSatirImzasi());
        }

        public static bool verifyDepoSilinecekKokSertifika(DepoSilinecekKokSertifika aKok)
        {
            CheckLicense();
            DepoASNKokSertifika asnkok = _depoSilinecekTOAsnKok(aKok);
            return _asnKokDogrula(asnkok, aKok.getSatirImzasi());
        }

        private static bool _asnKokDogrula(DepoASNKokSertifika aASNKok, byte[] aSatirImza)
        {
            try
            {
                Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                aASNKok.Encode(enc);
                byte[] dogrulanacak = enc.MsgCopy;

                DepoASNRawImza rawimza = new DepoASNRawImza();
                Asn1DerDecodeBuffer dec = new Asn1DerDecodeBuffer(aSatirImza);
                rawimza.Decode(dec);

                byte[] imza = rawimza.imza.mValue;
                byte[] pubkeyhash = rawimza.publicKeyHash.mValue;

                String base64pkhash = Convert.ToBase64String(pubkeyhash);//Base64.encode(pubkeyhash);
                byte[] pubkey = null;
                PUB_KEY_MAP.TryGetValue(base64pkhash, out pubkey);

                if (pubkey == null)
                {
                    return false;
                }

                //PublicKey key = new RSAKeyPairX509Codec().decodePublicKey(pubkey);
                PublicKey key = new PublicKey(pubkey);
                return SignUtil.verify(SignatureAlg.RSA_SHA1, dogrulanacak, imza, key);

            }
            catch (Exception ex)
            {
                throw new CertStoreException("Koksertifika dogrulamada hata olustu.", ex);
            }

        }


        public static bool verifyEncodedRootCertificate(byte[] aDogrulanacak, DepoASNRawImza aSatirImza)
        {
            CheckLicense();
            try
            {
                byte[] imza = aSatirImza.imza.mValue;
                byte[] pubkeyhash = aSatirImza.publicKeyHash.mValue;

                String base64pkhash = Convert.ToBase64String(pubkeyhash);//Base64.encode(pubkeyhash);
                byte[] pubkey = null;
                PUB_KEY_MAP.TryGetValue(base64pkhash, out pubkey);

                if (pubkey == null)
                {
                    return false;
                }

                //PublicKey key = new RSAKeyPairX509Codec().decodePublicKey(pubkey);
                PublicKey key = new PublicKey(pubkey);
                return SignUtil.verify(SignatureAlg.RSA_SHA1, aDogrulanacak, imza, key);

            }
            catch (Exception ex)
            {
                throw new CertStoreException("Koksertifika dogrulamada hata olustu.", ex);
            }

        }

        private static String _keyUsageBoolTOBinary(Asn1BitString aKullanim)
        {
            int length = aKullanim.numbits;
            int diff = 9 - length;
            //String ku = aKullanim.ToString();
            BitArray bits = new BitArray(aKullanim.ToBoolArray());
            StringBuilder sb = new StringBuilder();            
            foreach (bool bit in bits)
            {
                sb.Append(bit ? "1" : "0");
            }
            for (int i = 0; i < diff; i++)
            {
                sb.Append("0");
            }

            return sb.ToString();
        }

        public static byte[] getNormalizeName(Name aName)
        {
            try
            {
                return normalizeName(aName);
            }
            catch (Exception)
            {
                return null;
            }
        }

        public static byte[] normalizeName(Name aName)
        {
            CheckLicense();
            try
            {
                Name normalizedName = UtilName.string2Name(UtilName.name2String(aName), true);
                return UtilName.name2byte(normalizedName);
            }
            catch (Exception ex)
            {
                throw new CertStoreException("Normalization işleminde hata oluştu.", ex);
            }
        }

        public static List<DepoOzet> convertToDepoOzet(byte[] aValue, OzneTipi aObjectType)
        {
            CheckLicense();
            /*Set<Integer>*/
            Dictionary<int, DigestAlg>.KeyCollection keys = HASH_TYPE_MAP.Keys;
            List<DepoOzet> ozetler = new List<DepoOzet>();

            foreach (int key in keys)
            {
                try
                {
                    DepoOzet ozet = new DepoOzet();
                    byte[] ozetDeger = DigestUtil.digest(HASH_TYPE_MAP[key], aValue);
                    ozet.setHashValue(ozetDeger);
                    ozet.setHashType(key);
                    ozet.setObjectType(aObjectType.getIntValue());
                    ozetler.Add(ozet);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.StackTrace);
                }
            }

            return ozetler;

        }

        public static bool isOCSPResponseForCertificate(EBasicOCSPResponse aBasicOCSPResponse, ECertificate aCer)
        {
            ESingleResponse singleResponse = getOCSPResponseForCertificate(aBasicOCSPResponse, aCer);
            return singleResponse != null;
        }

        /**
         * BasicOcpsResponse icinde verilen bir sertifikaya ait iptal bilgisini geri doner
         *
         * @param aBasicOCSPResponse Icinde iptal bilgisi aranacak ocspresponse
         * @param aCer               Iptal bilgisi aranacak sertifika
         * @return Ilgili sertifikaya ait SingleResponse
         */
        public static ESingleResponse getOCSPResponseForCertificate(EBasicOCSPResponse aBasicOCSPResponse, ECertificate aCer)
        {
            CheckLicense();

            EResponseData responseData = aBasicOCSPResponse.getTbsResponseData();

            BigInteger serialNo = aCer.getSerialNumber();

            for (int i = 0; i < responseData.getSingleResponseCount(); i++)
            {
                ESingleResponse esr = responseData.getSingleResponse(i);
                ECertID certid = esr.getCertID();
                if (serialNo.Equals(certid.getSerialNumber()))
                {
                    if (_check(certid, aCer))
                        return esr;
                }
            }

            return null;
        }

        private static bool _check(ECertID aCertID, ECertificate aCer)
        {
            try
            {
                DigestAlg ozetAlg = DigestAlg.fromOID(aCertID.getHashAlgorithm().getAlgorithm().mValue);
                byte[] ozet = DigestUtil.digest(ozetAlg, aCer.getIssuer().getEncoded());
                return ozet.SequenceEqual(aCertID.getIssuerNameHash());
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                return false;
            }
        }


        public static DepoSertifikaOcsps toDepoSertifikaOcsps(ESingleResponse aSingleResponse, DepoOCSP aDepoOCSP, DepoSertifika aDepoSertifika)
        {
            CheckLicense();
            try
            {
                DepoSertifikaOcsps depoSertifikaOcsps = new DepoSertifikaOcsps();
                depoSertifikaOcsps.setOcspNo(aDepoOCSP.getOCSPNo());
                depoSertifikaOcsps.setSertifikaNo(aDepoSertifika.getSertifikaNo());
                depoSertifikaOcsps.setThisUpdate(aSingleResponse.getThisUpdate());
                depoSertifikaOcsps.setStatus(aSingleResponse.getCertificateStatus());
                DateTime? revocationDate = aSingleResponse.getRevocationTime();
                depoSertifikaOcsps.setRevocationTime(revocationDate);
                depoSertifikaOcsps.setRevocationReason(aSingleResponse.getRevocationReason());
                return depoSertifikaOcsps;
            }
            catch (Exception ex)
            {
                throw new CertStoreException("DepoSertifikaOcsps nesnesine çevrilirken hata oluştu.", ex);
            }
        }



    }
}
