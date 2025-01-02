#-------------------------------------------------
#
# Project created by QtCreator 2014-02-19T11:33:50
#
#-------------------------------------------------

QT += xml core gui
TARGET = EsyaCms
TEMPLATE = lib
CONFIG += staticlib

SOURCES += \
    ../../resources/EsyaCMS_Mod/src/SignerParam.cpp \
    ../../resources/EsyaCMS_Mod/src/SignParam.cpp \
    ../../resources/EsyaCMS_Mod/src/ImzaDogrulamaSonucu.cpp \
    ../../resources/EsyaCMS_Mod/src/ImzaDogrulamaAlgoritmasi.cpp \
    ../../resources/EsyaCMS_Mod/src/KontrolcuSonucu.cpp \
    ../../resources/EsyaCMS_Mod/src/ECMSUtil.cpp \
    ../../resources/EsyaCMS_Mod/src/OzetKontrolcu.cpp \
    ../../resources/EsyaCMS_Mod/src/ImzaKontrolcu.cpp \
    ../../resources/EsyaCMS_Mod/src/ImzaKriptoKontrolcu.cpp \
    ../../resources/EsyaCMS_Mod/src/cms_asn/SignedData.cpp \
    ../../resources/EsyaCMS_Mod/src/cms_asn/EncapContentInfo.cpp \
    ../../resources/EsyaCMS_Mod/src/cms_asn/SignerInfo.cpp \
    CMSSignature.cpp \
    CMSContainer.cpp \
    SignableBytes.cpp \
    ELisansIcerigi.cpp \
    EXMLLisansKontrolcu.cpp \
    CMSException.cpp

HEADERS += \
    SignatureContainer.h \
    Signature.h \
    CMSSignature.h \
    CMSContainer.h \
    Signable.h \
    SignableBytes.h \
    ELisansIcerigi.h \
    EXMLLisansKontrolcu.h \
    CMSException.h
unix {
    target.path = /usr/lib
    INSTALLS += target
}

# - yeni projeler -
# ortak
INCLUDEPATH += $$PWD/../../projects/EsyaOrtak
# kripto
INCLUDEPATH += $$PWD/../../projects/EsyaCrypto

# - kermenin kodlari -
# ortak
INCLUDEPATH += $$PWD/../../resources/EsyaOrtakDLL_Mod/src
# asn rt
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtbersrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxersrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxmlsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include
# asn generated
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/generated_ASN/
# asn wrapper
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/wrapped_classes
# cms
INCLUDEPATH += $$PWD/../../resources/EsyaCMS_Mod/src
INCLUDEPATH += $$PWD/../../resources/EsyaCMS_Mod/src/cms_asn

# akis --- buna gerek var mi burda?
#INCLUDEPATH += $$PWD/../../resources/headers/akis


# ===== ESYA ORTAK =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaOrtak

#INCLUDEPATH += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
#DEPENDPATH += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaOrtak.a

# ===== ESYA ASN =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaAsn

#INCLUDEPATH += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
#DEPENDPATH += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaAsn.a

# ===== IBS READER ===== bunu da udea ile degistiriyorum
#unix: LIBS += -L$$PWD/../../resources/libs/ -liBSReader
#INCLUDEPATH += $$PWD/../../resources/libs
#DEPENDPATH += $$PWD/../../resources/libs
#unix: PRE_TARGETDEPS += $$PWD/../../resources/libs/libiBSReader.a

# ===== UDEA LIB =====

unix: LIBS += -L$$PWD/../../resources/libs/ -lMilkoLib

INCLUDEPATH += $$PWD/../../resources/headers/milko
#DEPENDPATH += $$PWD/../../resources/libs

unix: PRE_TARGETDEPS += $$PWD/../../resources/libs/libMilkoLib.a

# ===== ESYA CRYPTO =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaCrypto

#INCLUDEPATH += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
#DEPENDPATH += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaCrypto.a

# ===== ESYA SMARTCARD =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaSmartcard-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaSmartcard

INCLUDEPATH += $$PWD/../../projects/EsyaSmartcard
#DEPENDPATH += $$PWD/../../builds/build-EsyaSmartcard-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaSmartcard-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaSmartcard.a




OBJECTIVE_SOURCES +=

QMAKE_LFLAGS += -L//Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS7.1.sdk/System/Library/Frameworks/
unix: LIBS += -framework Security



