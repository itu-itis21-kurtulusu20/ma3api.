:: Projenin gerektirdigi jar dosyalarini lokal repoya kuran script dosyasi.
:: Bu script, paket icindeki lib dizini icinde calistirilabilir.
:: Altta kurulmasi gereken parametreler:
:: - mvn: Maven yolu.
:: - ma3api_version: API surumu.

set mvn="C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2022.2\plugins\maven\lib\maven3\bin\mvn"
set ma3api_version=2.3.25-SNAPSHOT

CALL %mvn% install:install-file  -Dfile=Assd_api-0.0.1.jar   -DgroupId=com.phison.gti    -DartifactId=Assd_api   -Dversion=0.0.1    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=akiscif-3.3.20.jar   -DgroupId=tubitak.akis    -DartifactId=akiscif   -Dversion=3.3.20    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=armeabi-0.0.1.jar    -DgroupId=com.phison.gti    -DartifactId=armeabi   -Dversion=0.0.1    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=asn1rt-6.6.4.jar    -DgroupId=com.objsys.asn1j.runtime    -DartifactId=asn1rt   -Dversion=6.6.4    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=cryptolib-0.0.1.jar   -DgroupId=com.phison.gti    -DartifactId=cryptolib   -Dversion=0.0.1    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=dianta2-api-1.0.3.jar   -DgroupId=com.turktrust    -DartifactId=dianta2-api   -Dversion=1.0.3    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=gtijni-0.0.1.jar   -DgroupId=com.phison.gti    -DartifactId=gtijni   -Dversion=0.0.1    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-asic-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-asic    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-asn-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-asn    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-turktelekommssprovider-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-turktelekommssprovider    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-certstore-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-certstore    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-certvalidation-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-certvalidation    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-cmssignature-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-cmssignature    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-common-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-common    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-crypto-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-crypto    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-crypto-gnuprovider-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-crypto-gnuprovider    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-crypto-sunprovider-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-crypto-sunprovider    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-infra-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-infra    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-mssclient-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-mssclient    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-pades-pdfbox-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-pades-pdfbox    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-signature-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-signature    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-smartcard-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-smartcard    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-smartcard-android-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-smartcard-android    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-smartcard-android-acs-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-smartcard-android-acs    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-smartcard-android-ccid-%ma3api_version%.jar   -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-smartcard-android-ccid   -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-turkcellmssprovider-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-turkcellmssprovider    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-vodafonemssprovider-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-vodafonemssprovider    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=ma3api-xmlsignature-%ma3api_version%.jar    -DgroupId=tr.gov.tubitak.uekae.esya.api    -DartifactId=ma3api-xmlsignature    -Dversion=%ma3api_version%    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=pscjni-0.0.1.jar   -DgroupId=com.phison.gti    -DartifactId=pscjni   -Dversion=0.0.1    -Dpackaging=jar   -DgeneratePom=true
CALL %mvn% install:install-file  -Dfile=trustapicore-1.0.0.jar   -DgroupId=com.turktrust    -DartifactId=trustapicore   -Dversion=1.0.0    -Dpackaging=jar   -DgeneratePom=true
