using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    //todo Annotation!
    //@ApiClass
    public static class CertStoreSQLs
    {
        /*
      * SQL ler
      */
        public static readonly String SERTIFIKA_TABLE_CREATE = "CREATE TABLE SERTIFIKA" +
                "(SertifikaNo INTEGER PRIMARY KEY," +
                "EklenmeTarihi DATETIME NOT NULL," +
                "SertifikaValue BLOB NOT NULL," +
                "SerialNumber BLOB NOT NULL,IssuerName BLOB NOT NULL," +
                "StartDate DATETIME NOT NULL,EndDate DATETIME NOT NULL," +
                "SubjectName BLOB NOT NULL,SubjectNameStr VARCHAR,KeyUsageStr VARCHAR(9)," +
                "SubjectKeyIdentifier BLOB,EPosta VARCHAR(100)," +                
                "PrivateKeyValue BLOB,PKCS11Lib VARCHAR(100),PKCS11Id BLOB,CardSerialNumber VARCHAR, X400Address VARCHAR(100))";


        public static readonly String KOKSERTIFIKA_TABLE_CREATE = "CREATE TABLE KOKSERTIFIKA" +
                "(KOKSertifikaNo INTEGER PRIMARY KEY," +
                "KOKEklenmeTarihi DATETIME NOT NULL," +
                "KOKSertifikaValue BLOB NOT NULL," +
                "KOKSerialNumber BLOB NOT NULL,KOKIssuerName BLOB NOT NULL," +
                "KOKStartDate DATETIME NOT NULL,KOKEndDate DATETIME NOT NULL," +
                "KOKSubjectName BLOB NOT NULL,KOKKeyUsageStr VARCHAR(9)," +
                "KOKSubjectKeyIdentifier BLOB,KOKSertifikaTipi INTEGER NOT NULL," +
                "KOKGuvenSeviyesi INTEGER NOT NULL,KOKSatirImzasi BLOB)";

        public static readonly String DIZIN_TABLE_CREATE = "CREATE TABLE DIZIN" +
                "(DizinNo INTEGER PRIMARY KEY," +
                "EklenmeTarihi DATETIME NOT NULL," +
                "DizinAdi VARCHAR(100) UNIQUE NOT NULL)";

        public static readonly String SIL_TABLE_CREATE = "CREATE TABLE SIL" +
                "(SILNo INTEGER  PRIMARY KEY," +
                "EklenmeTarihi DATETIME NOT NULL," +
                "SILValue BLOB NOT NULL," +
                "IssuerName BLOB NOT NULL,ThisUpdate DATETIME NOT NULL," +
                "NextUpdate DATETIME NOT NULL," +
                "SILNumber BLOB,BaseSILNumber BLOB," +
                "SILTipi INTEGER NOT NULL)";

        public static readonly String DIZINSERTIFIKA_TABLE_CREATE = "CREATE TABLE DIZINSERTIFIKA" +
                "(DizinNo INTEGER NOT NULL,SertifikaNo INTEGER NOT NULL," +
                "PRIMARY KEY(DizinNo,SertifikaNo))";

        public static readonly String DIZINSIL_TABLE_CREATE = "CREATE TABLE DIZINSIL" +
                "(DizinNo INTEGER NOT NULL,SILNo INTEGER NOT NULL," +
                "PRIMARY KEY(DizinNo,SILNo))";

        public static readonly String SILINECEKKOKSERTIFIKA_TABLE_CREATE = "CREATE TABLE SILINECEKKOKSERTIFIKA" +
                "(KOKSertifikaNo INTEGER PRIMARY KEY," +
                "KOKEklenmeTarihi DATETIME NOT NULL," +
                "KOKSertifikaValue BLOB NOT NULL,KOKSerialNumber BLOB NOT NULL," +
                "KOKIssuerName BLOB NOT NULL,KOKSubjectName BLOB NOT NULL," +
                "KOKSatirImzasi BLOB)";

        public static readonly String SISTEMPARAMETRELERI_TABLE_CREATE = "CREATE TABLE SISTEMPARAMETRELERI" +
                "(ParametreAdi VARCHAR(40) NOT NULL PRIMARY KEY,ParametreDegeri BLOB)";

        public static readonly String HASH_TABLE_CREATE = "CREATE TABLE HASH" +
        "(HashNo INTEGER PRIMARY KEY,ObjectType INTEGER  NOT NULL," +
        "ObjectNo INTEGER  NOT NULL,HashType INTEGER NOT NULL,HashValue BLOB NOT NULL)";

        public static readonly String OCSP_TABLE_CREATE = "CREATE TABLE OCSP" +
        "(OCSPNo INTEGER PRIMARY KEY,OCSPResponderID BLOB  NOT NULL," +
        "OCSPProducedAt DATETIME NOT NULL,OCSPValue BLOB NOT NULL)";

        public static readonly String SERTIFIKAOCSPS_TABLE_CREATE = "CREATE TABLE SERTIFIKAOCSPS" +
        "(OCSPNo INTEGER,SertifikaNo INTEGER,ThisUpdate DATETIME NOT NULL," +
        "Status INTEGER NOT NULL,RevocationTime DATETIME,RevocationReason INTEGER," +
        "PRIMARY KEY (OCSPNo,SertifikaNo))";

        public static readonly String NITELIK_SERTIFIKASI_TABLE_CREATE = "CREATE TABLE NITELIKSERTIFIKASI" +
        "(NitelikSertifikasiNo INTEGER NOT NULL, SertifikaNo INTEGER NOT NULL,"+
        "NitelikSertifikasiValue BLOB NOT NULL,HolderDNName VARCHAR(100) NOT NULL,"+
        "PRIMARY KEY(NitelikSertifikasiNo))";


        public static readonly String SERTIFIKA_INDEX_CREATE = "CREATE UNIQUE INDEX SERTIFIKA_1IND ON SERTIFIKA(SerialNumber,IssuerName)";

        public static readonly String NITELIK_SERTIFIKASI_INDEX_CREATE = "CREATE UNIQUE INDEX NITELIK_SERTIFIKASI_1IND ON NITELIKSERTIFIKASI(NitelikSertifikasiValue)";
        
        public static readonly String KOKSERTIFIKA_INDEX_CREATE = "CREATE UNIQUE INDEX KOKSERTIFIKA_1IND ON KOKSERTIFIKA(KOKSerialNumber,KOKIssuerName)";

        public static readonly String SILINECEKKOKSERTIFIKA_INDEX_CREATE = "CREATE UNIQUE INDEX SILINECEKKOKSERTIFIKA_1IND ON SILINECEKKOKSERTIFIKA(KOKSerialNumber,KOKIssuerName)";

        public static readonly String HASH_INDEX_CREATE = "CREATE UNIQUE INDEX HASH_1ND ON HASH (ObjectType,ObjectNo,HashType)";

        public static readonly String DEPO_PAROLA_SP_INSERT = "INSERT INTO SISTEMPARAMETRELERI VALUES ('DepoParolasiDogrulama',NULL)";

        public static readonly String VERSIYON_SP_INSERT = "INSERT INTO SISTEMPARAMETRELERI VALUES ('Versiyon',1.0)";

        public static readonly String LAST_RUN_SQL_SP_INSERT = "INSERT INTO SISTEMPARAMETRELERI VALUES ('LastRunSQL',0)";

    }
}
