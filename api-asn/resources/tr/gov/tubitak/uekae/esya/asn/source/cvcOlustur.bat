set asn1c=C:\acv642\bin\asn1c.exe

set basedir=..\out\tr\gov\tubitak\uekae
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

rem %asn1c% "cvc.asn" "cvcalgorithms.asn" -config "cvcConf.xml" -java -ber -compact -O %basedir% -print -dirs
set cvcdir=%basedir%\cvc
%asn1c% "cvc.asn" "cvcalgorithms.asn" -config "cvcConf.xml" -java -ber -compact -O %cvcdir% -print

rem %asn1c% "cvcalgorithms.asn" -config "cvcConf.xml" -java -ber -compact -O %basedir% -print
rem del %tempdir%\*.*
rem rmdir %tempdir%
pause
