set asn1c=C:\acv625\bin\asn1c.exe

set basedir=..\out\tr\gov\tubitak\uekae


if NOT EXIST %basedir%\ekds mkdir %basedir%\ekds
if NOT EXIST %basedir%\ekds\asn mkdir %basedir%\ekds\asn

set basedir=..\out\tr\gov\tubitak\uekae\ekds\asn

@echo **************************************************************
@echo **************************************************************
@echo **************************************************************
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
%asn1c%  "EkdsBaseDataDefs.asn" "EkdsCommonDataObjectDefs.asn" "EkdsElectronicIdentityCardApp.asn" "EkdsEmergencyDataApp.asn" "EkdsMediaDefs.asn" "EkdsRemoteAuthenticationApp.asn" "StandardRemoteAuthenticationApp.asn" -config "ekdsconf.xml" -java -der -compact -O %basedir% -print -dirs
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo --------------------------------------------------------------
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
@echo aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa


