#-------------------------------------------------
#
# Project created by QtCreator 2014-02-19T08:25:28
#
#-------------------------------------------------

QT       -= gui

TARGET = EsyaAsn
TEMPLATE = lib
CONFIG += staticlib

SOURCES += esyaasn.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/algorithms.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/algorithmsCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/algorithmsCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/algorithmsDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/algorithmsEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/algorithmsPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/algorithmsPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ASN_SertifikaDeposu.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ASN_SertifikaDeposuCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ASN_SertifikaDeposuCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ASN_SertifikaDeposuDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ASN_SertifikaDeposuEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ASN_SertifikaDeposuPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ASN_SertifikaDeposuPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/asnaes.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/asnaesCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/asnaesCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/asnaesDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/asnaesEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/asnaesPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/asnaesPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/attrcert.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/attrcertCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/attrcertCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/attrcertDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/attrcertEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/attrcertPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/attrcertPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmp.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmpCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmpCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmpDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmpEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmpPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmpPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cms.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmsCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmsCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmsDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmsEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmsPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmsPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/crmf.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/crmfCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/crmfCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/crmfDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/crmfEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/crmfPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/crmfPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercms.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercmsCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercmsCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercmsDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercmsEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercmsPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercmsPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/esya.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/esyaCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/esyaCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/esyaDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/esyaEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/esyaPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/esyaPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733Compare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733Copy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733Dec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733Enc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733Print.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733PrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsiqc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsiqcCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsiqcCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsiqcDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsiqcEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsiqcPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsiqcPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/Explicit.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ExplicitCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ExplicitCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ExplicitDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ExplicitEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ExplicitPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ExplicitPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/Implicit.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ImplicitCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ImplicitCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ImplicitDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ImplicitEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ImplicitPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ImplicitPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ocsp.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ocspCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ocspCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ocspDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ocspEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ocspPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ocspPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs1pkcs8.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs1pkcs8Compare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs1pkcs8Copy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs1pkcs8Dec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs1pkcs8Enc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs1pkcs8Print.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs1pkcs8PrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs5v2.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs5v2Compare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs5v2Copy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs5v2Dec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs5v2Enc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs5v2Print.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs5v2PrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs7.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs7Compare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs7Copy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs7Dec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs7Enc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs7Print.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs7PrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs10.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs10Compare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs10Copy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs10Dec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs10Enc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs10Print.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs10PrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs12.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs12Compare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs12Copy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs12Dec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs12Enc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs12Print.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs12PrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/PKIXqualified.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/PKIXqualifiedCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/PKIXqualifiedCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/PKIXqualifiedDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/PKIXqualifiedEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/PKIXqualifiedPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/PKIXqualifiedPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtsp.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtspCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtspCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtspDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtspEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtspPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtspPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/UsefulDefinitions.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/UsefulDefinitionsCompare.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/UsefulDefinitionsCopy.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/UsefulDefinitionsDec.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/UsefulDefinitionsEnc.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/UsefulDefinitionsPrint.cpp \
    ../../resources/EsyaASN_Mod/src/generated_ASN/UsefulDefinitionsPrtToStrm.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AAControls.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AccessDescription.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ACClearAttrs.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AdministrationDomainName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AlgorithmIdentifier.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AnotherName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttCertIssuer.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttCertValidityPeriod.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Attribute.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttributeCertificate.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttributeCertificateInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttributeTypeAndValue.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttributeValue.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttrSpec.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AuthorityInfoAccess.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AuthorityKeyIdentifier.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BasicConstraints.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BilinmeyenEklenti.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BuiltInDomainDefinedAttribute.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BuiltInStandardAttributes.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CDP.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CertificateIssuer.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CertificatePolicies.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CertStatus.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ClassList.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Clearance.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ContentInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CountryName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CRLDistributionPoints.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CRLNumber.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Curve.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNEklenecekKokSertifika.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNImza.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNImzalar.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNKokSertifika.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNKokSertifikalar.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNRawImza.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNSilinecekKokSertifika.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DigestInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DistributionPoint.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DistributionPointName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DSASignature.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNException.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNStreamingUtils.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNToStringUtils.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNWrapper.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EBitString.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECCCMSSharedInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECertChoices.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECertificate.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECertificateList.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECParameters.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECPrivateKey.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECRL.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Eklenti.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EklentiFabrikasi.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ESeqOfList.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ESSCertID.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ETime.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ExtendedKeyUsage.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Extension.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ExtensionAttribute.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ExtensionGenerator.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ExtKeyUsage.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/FieldID.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/GeneralName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/GeneralNames.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/GeneralSubtree.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Holder.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IetfAttrSyntax_values_element.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IetfAttrSyntax.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/InhibitAnyPolicy.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IssuerAndSerialNumber.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IssuerSerial.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IssuingDistributionPoint.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/KeyAgreeRecipientIdentifier.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/KeyPurposeId.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/KeyUsage.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/MQVUserKeyingMaterial.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Name.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/NameConstraints.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/NameUtils.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ObjectDigestInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ORAddress.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OrganizationalUnitName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OriginatorIdentifierOrKey.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OriginatorInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ortak.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OtherKeyAttribute.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OutputStream.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PBES2_Params.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PBKDF2_Params.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PBKDF2_Salt.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PersonalName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PKCS7Data.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PKCS7Hash.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PKCS7IssuerSerial.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PKCS12PbeParams.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyConstraints.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyInformation.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyMappings.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyMappingsElement.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyQualifierInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PrivateKeyInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ProxyInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/QCStatement.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/QCStatements.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ReasonFlags.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RecipientEncryptedKey.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RecipientEncryptedKeys.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RecipientIdentifier.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RecipientKeyIdentifier.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RelativeDistinguishedName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RevocationInfoChoice.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RevokedCertificatesElement.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RevokedInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RoleSyntax.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RSAPrivateKey.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RSAPublicKey.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SecurityCategory.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SerialNumber.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Signature.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SignerIdentifier.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SigningCertificate.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SMIMECapabilities.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SMIMECapability.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SMIMEEncryptionKeyPreference.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SpecifiedECDomain.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SubjectAlternativeName.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SubjectKeyIdentifier.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SubjectPublicKeyInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SvceAuthInfo.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Target.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/TargetCert.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Targets.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/TBSCertificate.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/TBSCertList.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/V2Form.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SigningCertificateV2.cpp \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ESSCertIDv2.cpp

HEADERS += esyaasn.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/algorithms.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ASN_SertifikaDeposu.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/asnaes.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/attrcert.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cmp.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/cms.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/crmf.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercms.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/esya.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsiqc.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/Explicit.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/Implicit.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/ocsp.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs1pkcs8.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs5v2.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs7.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs10.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkcs12.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/PKIXqualified.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtsp.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/rtkey.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/UsefulDefinitions.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AAControls.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AccessDescription.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ACClearAttrs.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AdministrationDomainName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AlgorithmIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AnotherName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/asnlib_global.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttCertIssuer.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttCertValidityPeriod.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Attribute.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttributeCertificate.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttributeCertificateInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttributeTypeAndValue.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttributeValue.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AttrSpec.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AuthorityInfoAccess.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AuthorityKeyIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/AY_Eklenti.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BasicConstraints.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BilinmeyenEklenti.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BlokIsleyici.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BuiltInDomainDefinedAttribute.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/BuiltInStandardAttributes.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CDP.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CertificateIssuer.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CertificatePolicies.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CertStatus.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ClassList.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Clearance.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ContentInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CountryName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CRLDistributionPoints.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/CRLNumber.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Curve.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNEklenecekKokSertifika.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNImza.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNImzalar.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNKokSertifika.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNKokSertifikalar.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNRawImza.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DepoASNSilinecekKokSertifika.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DigestInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DistributionPoint.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DistributionPointName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/DSASignature.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNException.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNStreamingUtils.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNToStringUtils.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNWrapper.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EASNWrapperTemplate.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EBitString.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECCCMSSharedInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECertChoices.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECertificate.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECertificateList.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECParameters.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECPrivateKey.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ECRL.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Eklenti.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EklentiFabrikasi.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ESeqOfList.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ESSCertID.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/EsyaASN_DIL.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ETime.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ExtendedKeyUsage.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Extension.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ExtensionAttribute.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ExtensionGenerator.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ExtKeyUsage.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/FieldID.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/GeneralName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/GeneralNames.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/GeneralSubtree.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Holder.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IetfAttrSyntax_values_element.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IetfAttrSyntax.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/InhibitAnyPolicy.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IssuerAndSerialNumber.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IssuerSerial.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/IssuingDistributionPoint.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/KeyAgreeRecipientIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/KeyPurposeId.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/KeyUsage.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/MQVUserKeyingMaterial.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/myasndefs.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/myerrors.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Name.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/NameConstraints.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/NameUtils.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/NetworkAddress.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/NumericUserIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ObjectDigestInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ORAddress.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OrganizationalUnitName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OrganizationName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OriginatorIdentifierOrKey.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OriginatorInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ortak.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OtherKeyAttribute.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/OutputStream.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PBES2_Params.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PBKDF2_Params.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PBKDF2_Salt.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PersonalName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PKCS7Data.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PKCS7Hash.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PKCS7IssuerSerial.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PKCS12PbeParams.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyConstraints.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyInformation.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyMappings.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyMappingsElement.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PolicyQualifierInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PrivateDomainName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/PrivateKeyInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ProxyInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/QCStatement.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/QCStatements.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ReasonFlags.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RecipientEncryptedKey.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RecipientEncryptedKeys.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RecipientIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RecipientKeyIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RelativeDistinguishedName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RevocationInfoChoice.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RevokedCertificatesElement.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RevokedInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RoleSyntax.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RSAPrivateKey.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/RSAPublicKey.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SecurityCategory.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SerialNumber.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Signature.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SignerIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SigningCertificate.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SMIMECapabilities.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SMIMECapability.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SMIMEEncryptionKeyPreference.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SpecifiedECDomain.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SubjectAlternativeName.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SubjectKeyIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SubjectPublicKeyInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SvceAuthInfo.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Target.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/TargetCert.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/Targets.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/TBSCertificate.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/TBSCertList.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/TerminalIdentifier.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/V2Form.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/dercms.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/etsi101733.h \
    ../../resources/EsyaASN_Mod/src/generated_ASN/pkixtsp.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/SigningCertificateV2.h \
    ../../resources/EsyaASN_Mod/src/wrapped_classes/ESSCertIDv2.h
unix {
    target.path = /usr/lib
    INSTALLS += target
}

INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include
DEPENDPATH += $$PWD/../../resources/EsyaASN_Mod/include
INCLUDEPATH += $$PWD/../../resources/EsyaOrtakDLL_Mod/src
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/generated_ASN/
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/wrapped_classes/
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtbersrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxersrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxmlsrc
DEPENDPATH += $$PWD/../../resources/EsyaASN_Mod/include

# esya ortak

unix: LIBS += -L$$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaOrtak

INCLUDEPATH += $$PWD/
DEPENDPATH += $$PWD/

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaOrtak.a

# asn1ber

unix: LIBS += -L$$PWD/../../resources/EsyaASN_Mod/libs/ -lasn1ber

INCLUDEPATH += $$PWD/
DEPENDPATH += $$PWD/

unix: PRE_TARGETDEPS += $$PWD/../../resources/EsyaASN_Mod/libs/libasn1ber.a

# asn1rt

unix: LIBS += -L$$PWD/../../resources/EsyaASN_Mod/libs/ -lasn1rt

INCLUDEPATH += $$PWD/
DEPENDPATH += $$PWD/

unix: PRE_TARGETDEPS += $$PWD/../../resources/EsyaASN_Mod/libs/libasn1rt.a


