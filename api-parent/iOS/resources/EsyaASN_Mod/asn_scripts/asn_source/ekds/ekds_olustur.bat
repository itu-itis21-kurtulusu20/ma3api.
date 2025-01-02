
set asn1c=C:\acv584\bin\asn1c.exe

set outputdir=ekds_output
if NOT EXIST %outputdir% mkdir %outputdir%

set params=-config "ekds_conf.xml" -c++ -bitMacros -genCompare -genCopy -genPrint -genPrtToStrm -warnings -pdu *

set basicAsnFiles="EkdsBaseDataDefs.asn" "EkdsCommonDataObjectDefs.asn" "EkdsElectronicIdentityCardApp.asn" "EkdsEmergencyDataApp.asn" "EkdsMediaDefs.asn" "EkdsRemoteAuthenticationApp.asn"


%asn1c% %basicAsnFiles% %params% -ber -O %outputdir%
pause
exit


