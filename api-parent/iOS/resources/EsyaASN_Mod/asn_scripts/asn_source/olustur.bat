
set asn1c=E:\Programs\asn1c\acv644\bin\asn1c.exe

rem set basedir=..\out\tr\gov\tubitak\uekae
rem set tempdir=%basedir%\temp

rem if NOT EXIST %basedir%\esya mkdir %basedir%\esya
rem if NOT EXIST %basedir%\esya\asn mkdir %basedir%\esya\asn
rem set basedir=%basedir%\esya\asn

set outputdir=output
if NOT EXIST %outputdir% mkdir %outputdir%
set streamoutputdir=streamoutput
if NOT EXIST %streamoutputdir% mkdir %streamoutputdir%

rem set params=-config "conf.xml" -c++ -bitMacros -genCompare -genCopy -genPrint -genPrtToStrm -stream -warnings 
set params=-c++ -bitMacros -genCompare -genCopy -genPrint -genPrtToStrm -warnings -pdu *
set streamconf=-config "streamconf.xml" 
set derconf=-config "conf.xml" 

set basicAsnFiles="Usefuldefinitions.asn" "Explicit.asn" "Implicit.asn" "pkcs1pkcs8.asn" "asnaes.asn" "Algorithms.asn" "PKCS10.asn" "CRMF.asn" "CMP.asn" "PKIXqualified.asn" "etsiqc.asn" "ocsp.asn" "AttrCert.asn" "ESYA.asn" "ASN_SertifikaDeposu.asn" "pkcs5v2.asn"
set berAsnFiles="pkcs7.asn" "CMS.asn" "pkcs12.asn"
set tekrarAsnFiles="pkixtsp.asn" "etsi101733.asn"


rem derCMS dýþýndaki tipler
%asn1c% %basicAsnFiles% %berAsnFiles% "pkcs12.asn" %params% %derconf% -der -O %outputdir%
pause
rem derCms içindeki tipler için
%asn1c% "derCMS.asn" %tekrarAsnFiles% %params% %derconf% -der -O %outputdir%
pause

@echo BER KODLARI OLUÞTURULUYOR

%asn1c% %berAsnFiles% %params% %derconf% -ber -O %outputdir%

@echo BÝTTÝ

exit

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------

rem alttaki yorum kaldýrýlacak
rem %asn1c% %basicAsnFiles% "derCMS.asn" %berAsnFiles% %tekrarAsnFiles% %params% %streamconf% -cfile streamasn.cpp -hfile streamasn.h -noencode -stream -ber -maxlines 1000 -O %streamoutputdir%


pause

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------

rem %asn1c%  "pkixtsp.asn" "etsiqc.asn" "pkcs7.asn" "Algorithms.asn" "CMP.asn" "CRMF.asn" "PKCS10.asn" "PKIXqualified.asn" "ocsp.asn" "derCMS.asn" "AttrCert.asn" "ESYA.asn" "pkcs1pkcs8" -config "conf.xml" -java -der -compact -O %basedir% -print -dirs
pause
rem %asn1c% %basicAsnFiles%  %params% %derconf% -der -O %outputdir%
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
pause


pause
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
pause
rem %asn1c%  %tekrarAsnFiles% %params% %derconf% -der -O %outputdir%

@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa



pause




rem 
rem set algodir=%basedir%\algorithms
rem 
rem @echo **************************************************************
rem @echo **************************************************************
rem @echo **************************************************************
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem %asn1c%  "asnaes.asn" -config "conf.xml" -java -der -compact -O %algodir% -print
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem 
rem set x509dir=%basedir%\x509
rem if EXIST %x509dir% ( echo %x509dir% exists
rem ) ELSE (
rem echo generating %x509dir%
rem mkdir %x509dir%
rem )
rem 
rem @echo **************************************************************
rem @echo **************************************************************
rem @echo **************************************************************
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem %asn1c%  "Explicit.asn" "Implicit.asn" -config "conf.xml" -java -der -compact -O %x509dir% -print
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem 
rem 
rem 
rem if NOT EXIST %tempdir% (
rem mkdir %tempdir%
rem )
rem 
rem @echo **************************************************************
rem @echo **************************************************************
rem @echo **************************************************************
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem %asn1c%  "Explicit.asn" "Implicit.asn" -config "Explicitconf.xml" -java -der -O %tempdir% -print
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo --------------------------------------------------------------
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem @echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
rem 
rem copy %tempdir%\X520SerialNumber.java %x509dir%\
rem copy %tempdir%\X520countryName.java %x509dir%\
rem copy %tempdir%\SubjectKeyIdentifier.java %x509dir%\
rem copy %tempdir%\SubjectAltName.java %x509dir%\
rem del %tempdir%\*.*
rem rmdir %tempdir%

