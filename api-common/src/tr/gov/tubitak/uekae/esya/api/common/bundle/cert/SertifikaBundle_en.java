package tr.gov.tubitak.uekae.esya.api.common.bundle.cert;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelBundle_en;


public class SertifikaBundle_en extends GenelBundle_en
{
     private static Object[][] mSertifikaContents =
     {

          {CertI18n.TAB1,"Certificate"},
          {CertI18n.TAB2,"Advanced View"},
          {CertI18n.TAB3,"Certification Path"},

          {CertI18n.SERTIFIKA_DOSYA,"Certificate Files"},
          {CertI18n.DATE_AFTER,"Date After"},
          {CertI18n.DATE_BEFORE,"Date Before"},
          {CertI18n.SERTIFIKA_GOSTER,"View Certificate"},
          {CertI18n.BC_TYPE,"Subject Type"},
          {CertI18n.BC_CA,"Certificate Authority"},
          {CertI18n.BC_CA_DEGIL,"Not CA"},
          {CertI18n.BC_PATH,"Path Length Constraint"},

          {CertI18n.POLICY_IDENTIFIER,"Policy Identifier"},
          {CertI18n.POLICY_ID,"Policy ID"},
          {CertI18n.POLICY_INFO,"Policy Info"},
          {CertI18n.POLICY_QUALIFIER,"Policy Qualifier"},
          {CertI18n.POLICY_NOTICE,"User Notice"},
          {CertI18n.POLICY_CPS,"CPS"},

          {CertI18n.AKI_ID,"AKI"},
          {CertI18n.AKI_ISSUER,"Certificate Issuer"},
          {CertI18n.AKI_SERIAL,"Certificate Serial Number"},

          {CertI18n.PM_IDP,"Issuer Domain Policy"},
          {CertI18n.PM_SDP,"Subject Domain Policy"},

          {CertI18n.NITELIKLI_COMPLIANCE,"This is an ETSI TS 101 862 Compliant Qualified Digital Certificate."},
          {CertI18n.NITELIKLI_TK,"This certificate complies with the Telekomünikasyon Kurumu Qualified Digital Certificate Profile."},
          {CertI18n.NITELIKLI_MONEY_LIMIT,"Money Limit"},

          {CertI18n.KULLANIM,"Key Usage"},
          {CertI18n.BILGI,"Certificate Info"},
          {CertI18n.TIP,"Type"},
          {CertI18n.ESHS,"CA"},
          {CertI18n.BASLANGIC_TARIHI,"Valid From"},
          {CertI18n.BITIS_TARIHI,"Valid To"},
          {CertI18n.IMZA_SERTIFIKASI,"Signing Certificate"},

          {CertI18n.KONTROL_SONUCU,"Control Result"},

          {CertI18n.DELTA_CRL_INDICATOR_KONTROLU,"Delta Crl Indicator Control"},
          {CertI18n.DELTA_CRL_INDICATOR_VAR,"Delta Crl Indicator Exists"},
          {CertI18n.DELTA_CRL_INDICATOR_YOK,"No Delta Crl Indicator"},

          {CertI18n.FRESHEST_CRL_KONTROLU,"Freshest Crl Control"},
          {CertI18n.FRESHEST_CRL_VAR,"Freshest Crl Exists"},
          {CertI18n.FRESHEST_CRL_YOK,"No Freshest Crl"},

          {CertI18n.OCSP_CEVABI_KONTROLCU,"OCSP Response Control"},

          {CertI18n.BASARILI,"Successful"},
          {CertI18n.MALFORMED_REQUEST,"Malformed Request"},
          {CertI18n.INTERNAL_ERROR,"Internal Error"},
          {CertI18n.TRY_LATER,"Try Later"},
          {CertI18n.SIG_REQUIRED,"Signature Required"},
          {CertI18n.UNAUTHORIZED,"Unauthorized"},

          {CertI18n.OCSP_IMZALAYAN_SERTIFIKA_KONTROLU,"OCSP Signer Certificate Control"},

          {CertI18n.IMZALAYAN_SERTIFIKA_GECERLI,"Signer Certificate Valid"},
          {CertI18n.IMZALAYAN_SERTIFIKA_GECERSIZ,"Signer Certificate Invalid"},


          //OCSPImzaKontrolcu
          {CertI18n.OCSP_IMZA_KONTROLU,"OCSP Response Signature Control"},

          {CertI18n.CEVAP_IMZALI_DEGIL,"OCSP Response Not Signed"},
          {CertI18n.CEVAP_YAPISI_BOZUK,"OCSP Response Structure Corrupt"},
          {CertI18n.CEVAPTA_SERTIFIKA_YOK,"OCSP Response Does Not Include a Certificate"},
          {CertI18n.SERTIFIKA_OCSP_SERTIFIKASI_DEGIL,"OCSP Response Certificate Is Not an OCSP Certificate"},
          {CertI18n.DOGRULAMA_KRIPTO_HATASI,"Validation Crypto Error"},
          {CertI18n.IMZA_DOGRULANAMADI,"OCSP Response Signature Could Not Be Validated"},
          {CertI18n.IMZA_DOGRULANDI,"OCSP Response Signature Validated"},
          //OCSPdenIptalKontrolcu
          {CertI18n.OCSPDEN_IPTAL_KONTROLU,"CRL control from OCSP"},
          {CertI18n.OCSP_CEVABI_GECERSIZ,"Invalid OCSP answer"},
          {CertI18n.OCSP_CEVABI_BULUNAMADI,"OCSP answer is not found"},
          {CertI18n.SM_SERTIFIKASI_BULUNAMADI,"Issuer certificate is not found"},
          {CertI18n.SERTIFIKA_OCSPDE_GECERLI,"Certificate is good in OCSP answer"},
          {CertI18n.SERTIFIKA_OCSPDE_GECERLI_DEGIL,"Certificate is revoked in OCSP answer"},

          //SildenIptalKontrolcu
          {CertI18n.SILDEN_IPTAL_KONTROLU,"Revocation check from CRL"},

          {CertI18n.SIL_GECERSIZ,"CRL is invalid"},
          {CertI18n.SERTIFIKA_LISTEDE,"Certificate is revoked in CRL"},
          {CertI18n.SERTIFIKA_LISTEDE_DEGIL,"Certificate is not in CRL"},
          {CertI18n.SIL_BULUNAMADI,"CRL is not found"},
          {CertI18n.BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS,"Certificate in Base CRL is not in Delta CRL"},
          {CertI18n.SERTIFIKA_DELTA_SILDE,"Certificate is revoked in Delta CRL"},
          {CertI18n.SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS,"Certificate is not in Delta CRL"},

          //SeriNoPozitifKontrolcu
          {CertI18n.SERI_NO_KONTROLU,"Serial number control"},

          {CertI18n.SERI_NO_POZITIF,"Serial number is positive"},
          {CertI18n.SERI_NO_NEGATIF,"Serial number is negative"},

          //SertifikaEklentiKontrolcu
          {CertI18n.SERTIFIKA_EKLENTI_KONTROLU,"Certificate extension control"},

          {CertI18n.EKLENTI_YOK,"No extensions in certificate"},
          {CertI18n.GECERSIZ_EKLENTI,"Invalid extension in certificate"},
          {CertI18n.AYNI_EXTENSION_BIRDEN_FAZLA,"More than one the same extension in the certificate"},
          {CertI18n.EKLENTILER_GECERLI,"Extensions in the certificate are valid"},

          //SertifikaTarihKontrolcu
          {CertI18n.SERTIFIKA_TARIH_KONTROLU,"Certificate validity control"},

          {CertI18n.SERTIFIKA_TARIH_GECERLI,"Certificate is not expired"},
          {CertI18n.SERTIFIKA_TARIH_GECERSIZ,"Certificate is expired"},
          {CertI18n.SERTIFIKA_TARIH_BILGISI_BOZUK,"Certificate date is invalid"},
          {CertI18n.SERTIFIKA_SURESI_DOLMUS,"Certificate is expired"},
          
          //SignatureAlgAyniMiKontrolcu
          {CertI18n.SIGNATURE_ALG_AYNIMI_KONTROLU,"Checking Signature Algorithm is Same or not"},

          {CertI18n.SIGNATURE_ALG_AYNI,"Signature Algorithm values are Same"},
          {CertI18n.SIGNATURE_ALG_FARKLI,"Signature Algorithm values are Different"},

          //VersiyonKontrolcu
          {CertI18n.SERTIFIKA_VERSIYON_KONTROLU,"Certificate Version Check"},

          {CertI18n.EXTENSION_VAR_DOGRU,"There is a Version 3 Extension"},
          {CertI18n.EXTENSION_VAR_YANLIS,"There is a Extension, But Its not Version 3"},
          {CertI18n.UID_VAR_DOGRU,"There is a UID with Version 2 or higher"},
          {CertI18n.UID_VAR_YANLIS,"There is a UID, But Its nor Version 2 or higher"},
          {CertI18n.BASIT_ALANLAR_VAR_DOGRU,"There are Basic Fields with Version 1 or higher"},
          {CertI18n.BASIT_ALANLAR_VAR_YANLIS,"There are Basic Fields, But Those are not Version 1 or higher"},

          //BasicConstraintCAKontrolcu
          {CertI18n.BASIC_CONST_CA_KONTROLU,"Checking CA Value in Basic Constraints"},

          {CertI18n.BASIC_CONST_EKLENTI_YOK,"Basic Constraints Extension does not Exist"},
          {CertI18n.BASIC_CONST_EKLENTISI_BOZUK,"Basic Constraint Extension is Corrupted"},
          {CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_YOK,"There is No CA value of Basic Constraints Extension"},
          {CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS,"CA Value of Basic Constraints Extension is Wrong"},
          {CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU,"CA Value of Basic Constraints Extension is True"},

          //KeyIdentifierKontrolcu
          {CertI18n.KEY_IDENTIFIER_KONTROLU,"Key Identifier Check"},

          {CertI18n.SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK,"Certificate Authority Key Identifier does not Exist"},
          {CertI18n.SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK,"Certificate Authority Subject Key Identifier does not Exist"},
          {CertI18n.AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ,"Certificate Authority Subject Key Identifier is not Same with Certificate Authority Key Identifier"},
          {CertI18n.AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU,"Certificate Authority Subject Key Identifier is Same with Certificate Authority Key Identifier"},

          //PathLenConstraintKontrolcu
          {CertI18n.PATH_LEN_CONSTRAINT_KONTROLU,"Path Length Constraint Check"},

          {CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK,"There is no Path Length Constraint Value in Basic Constraints Extension"},
          {CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF,"Path Length Constraint Value in Basic Constraints Extension is Negative"},
          {CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI,"Path Length Constraint Value in Basic Constraints Extension has Exceeded"},
          {CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI,"Path Length Constraint Value in Basic Constraints Extension is Valid"},

          {CertI18n.SERTIFIKA_IMZA_KONTROLU,"Certificate Signature Validation"},

          {CertI18n.SERTIFIKA_IMZALI_DEGIL,"Unsigned Certificate"},
          {CertI18n.SERTIFIKA_YAPISI_BOZUK,"Corrupted Certificate"},
          {CertI18n.SERTIFIKADA_ACIK_ANAHTAR_YOK,"No Public Key In Certificate"},
          {CertI18n.SERTIFIKADA_ACIK_ANAHTAR_BOZUK,"Corrupted Public Key in Certificate"},
          {CertI18n.SERTIFIKA_IMZA_DOGRULANAMADI,"Certificate Signature is not Validated"},
          {CertI18n.SERTIFIKA_IMZA_DOGRULANDI,"Certificate Signature Validated"},

//          {CertI18n.KEY_USAGE_YOK,"No Key Usage Extension"},
//          {CertI18n.KEY_USAGE_BOZUK,"Corrupted Key Usage Extension"},
//          {CertI18n.KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL,"KeyCertSign Bit is not Asserted in Key Usage Extension"},
//          {CertI18n.KEY_USAGE_SERTIFIKA_IMZALAYICI,"KeyCertSign Bit is Asserted in Key Usage Extension"},


          //SilImzaKontrolcu
          {CertI18n.SIL_IMZA_KONTROLU,"CRL Signature Validation"},

          {CertI18n.SIL_IMZALI_DEGIL,"Unsigned CRL"},
          {CertI18n.SIL_YAPISI_BOZUK,"Corrupted CRL"},
          {CertI18n.SIL_IMZA_DOGRULANAMADI,"CRL Signature is not Validated"},
          {CertI18n.SIL_IMZA_DOGRULANDI,"CRL Signature Validated"},

          //SilKeyUsageKontrolcu
          {CertI18n.SIL_KEY_USAGE_KONTROLU,"CRL Key Usage Extension Control"},

          {CertI18n.KEY_USAGE_SIL_IMZALAYICI_DEGIL,"CRLSign Bit is not Asserted in Key Usage Extension"},
          {CertI18n.KEY_USAGE_SIL_IMZALAYICI,"CRLSign Bit is Asserted in Key Usage Extension"},

          //SilNameKontrolcu
          {CertI18n.SIL_NAME_KONTROLU,"CRL Name Validation"},

          //SilEklentiKontrolcu
          {CertI18n.SIL_EKLENTI_KONTROLU,"CRL Extension Validation"},

          //SilTarihKontrolcu
          {CertI18n.SIL_TARIH_KONTROLU,"CRL Date Validation"},

          {CertI18n.GECERLI,"Valid Certificate"},
          {CertI18n.IPTAL_KONTROLU_SORUNLU,"Problem in Revocation Control"},
          {CertI18n.ZINCIR_SORUNLU,"Problem in Certification Path"},
          {CertI18n.IPTAL_EDILMIS,"Revoked Certificate"},
          {CertI18n.ASKIDA,"On Hold Certificate"},

     };

     private static Object[][] mContents = new Object[mGenelContents.length + mSertifikaContents.length][];

     static
     {
          System.arraycopy(mGenelContents, 0, mContents, 0, mGenelContents.length);
          System.arraycopy(mSertifikaContents, 0, mContents, mGenelContents.length, mSertifikaContents.length);
     }

     @Override
     public Object[][] getContents () //NOPMD
     {
          return mContents;
     }
}
