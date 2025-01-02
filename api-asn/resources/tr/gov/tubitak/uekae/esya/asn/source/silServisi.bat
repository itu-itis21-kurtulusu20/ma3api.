set asn1c=C:\acv642\bin\asn1c.exe

set basedir=..\outSilServisi\tr\gov\tubitak\uekae
set tempdir=%basedir%\temp

if NOT EXIST %basedir%\esya mkdir %basedir%\esya
if NOT EXIST %basedir%\esya\asn mkdir %basedir%\esya\asn

@echo **************************************************************
@echo --------------------------------------------------------------
rem %asn1c% "CMS.asn" "Explicit.asn" "crlfinder.asn" -config "silServisiconf.xml" -csharp -der -compact -O %cmsDir% -print

rem pause
set crlfinderdir=%basedir%\esya\asn\esya\crlfinder
if NOT EXIST %crlfinderdir% (
mkdir %crlfinderdir%
)
%asn1c% "crlfinder.asn" -config "silServisiconf.xml" -csharp -der -compact -O %crlfinderdir% -print

rem pause
set cmsDir=%basedir%\esya\asn\cms
if NOT EXIST %cmsDir% mkdir %cmsDir%
%asn1c% "CMS.asn" -config "silServisiconf.xml" -csharp -der -compact -O %cmsDir% -print

rem pause

set explicitDir=%basedir%\esya\asn\explicit
if NOT EXIST %explicitDir% mkdir %explicitDir%
%asn1c% "Explicit.asn" -config "silServisiconf.xml" -csharp -der -compact -O %explicitDir% -print

set algorithmsDir=%basedir%\esya\asn\algorithms
if NOT EXIST %algorithmsDir% mkdir %algorithmsDir%
%asn1c% "Algorithms.asn" -config "silServisiconf.xml" -csharp -der -compact -O %algorithmsDir% -print

@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
pause

