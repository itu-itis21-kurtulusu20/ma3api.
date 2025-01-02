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


rem %asn1c% "SelfCvc.asn" "cvcalgorithms.asn" -config "SelfCvcConf.xml" -java -ber -compact -O %basedir% -print -dirs
set cvcdir=%basedir%\SelfDesc
%asn1c% "SelfCvc.asn" "cvcalgorithms.asn" -config "SelfCvcConf.xml" -java -ber -compact -O %cvcdir% -print

@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------

rem %asn1c% "cvcalgorithms.asn" -config "SelfCvcConf.xml" -java -ber -compact -O %basedir% -print
rem del %tempdir%\*.*
rem rmdir %tempdir%

@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
pause
