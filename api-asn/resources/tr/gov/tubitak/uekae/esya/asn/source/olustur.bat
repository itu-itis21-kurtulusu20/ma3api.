rem use java or csharp keyword to generate codes of required platform
rem e.g. Java code generation use "olustur.bat java"

if %1==java (set fileExt=java) else (if %1==csharp (set fileExt=cs) else goto :eof)

set asn1c=C:\acv664\bin\asn1c.exe

set basedir=..\out\%1\tr\gov\tubitak\uekae
set tempdir=%basedir%\temp

if NOT EXIST %basedir%\esya mkdir %basedir%\esya
if NOT EXIST %basedir%\esya\asn mkdir %basedir%\esya\asn

set basedir=%basedir%\esya\asn

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c% "pkixtsp.asn" "etsiqc.asn"  "Algorithms.asn" "CMP.asn" "CRMF.asn" "PKCS10.asn" "PKIXqualified.asn" "ocsp.asn" "AttrCert.asn" "ESYA.asn" "pkcs1pkcs8" "depo.asn" -config "conf.xml" -%1 -der -compact -O %basedir% -print -dirs

%asn1c% "CMS.asn" "pkcs12.asn" "pkcs9.asn" "pkcs5.asn" "pkcs15.asn" -config "conf.xml" -%1 -ber -compact -O %basedir% -print -dirs


@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

set cmsdir=%basedir%\cms

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c%  "derCMS.asn" "etsi101733.asn" -config "conf.xml" -%1 -der -compact -O %cmsdir% -print


set crlfinderdir=%basedir%\esya\crlfinder

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c%  "crlfinder.asn" -config "conf.xml" -%1 -der -compact -O %crlfinderdir% -print


set cvcdir=%basedir%\cvc

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c% "cvc.asn" "cvcalgorithms.asn" -config "cvcConf.xml" -%1 -ber -compact -O %cvcdir% -print
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

set signaturepoliciesdir=%basedir%\signaturepolicies

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c% "signaturepolicies.asn" -config "conf.xml" -%1 -der -compact -O %signaturepoliciesdir% -print


@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

set algodir=%basedir%\algorithms

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c%  "aes.asn" -config "conf.xml" -%1 -der -compact -O %algodir% -print
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

set x509dir=%basedir%\x509
if EXIST %x509dir% ( echo %x509dir% exists
) ELSE (
echo generating %x509dir%
mkdir %x509dir%
)

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c%  "Explicit.asn" "Implicit.asn" "Kerberos.asn" -config "conf.xml" -%1 -der -compare -compact -O %x509dir% -print
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa



if NOT EXIST %tempdir% (
mkdir %tempdir%
)

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c%  "Explicit.asn" "Implicit.asn" -config "Explicitconf.xml" -%1 -der -O %tempdir% -print
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

copy %tempdir%\X520SerialNumber.%fileExt% %x509dir%\
copy %tempdir%\X520countryName.%fileExt% %x509dir%\
copy %tempdir%\SubjectKeyIdentifier.%fileExt% %x509dir%\
copy %tempdir%\SubjectAltName.%fileExt% %x509dir%\
del %tempdir%\*.*
rmdir %tempdir%


set pasaportdir=%basedir%\pasaport
if EXIST %pasaportdir% ( echo %pasaportdir% exists
) ELSE (
echo generating %pasaportdir%
mkdir %pasaportdir%
)

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c%  "PasaportMasterlist.asn" -config "Explicitconf.xml" -%1 -der -O %pasaportdir% -print
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa




@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

set scencryptedpackagedir=%basedir%\scencryptedpackage

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c%  "encryptedDataPackage.asn" "scobject.asn" -config "conf.xml" -%1 -der -compact -O %scencryptedpackagedir% -print


