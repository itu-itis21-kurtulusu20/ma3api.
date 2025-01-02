#-------------------------------------------------
#
# Project created by QtCreator 2014-02-26T09:22:48
#
#-------------------------------------------------

QT       -= gui
QMAKE_CXXFLAGS += -stdlib=libc++
QMAKE_LFLAGS += -lc++


TARGET = EsyaSmartcard
TEMPLATE = lib
CONFIG += staticlib

SOURCES += APDUSmartCard.cpp \
    APDUSigner.cpp \
    SignatureSchemeFactory.cpp \
    RSAScheme.cpp \
    SmartCardException.cpp \
    deneme.cpp

HEADERS += \
    BaseSigner.h \
    APDUSigner.h \
    SignatureSchemeFactory.h \
    SignatureScheme.h \
    RSAScheme.h \
    APDUSmartCard.h \
    SmartCardException.h \
    deneme.h
unix {
    target.path = /usr/lib
    INSTALLS += target
}

INCLUDEPATH += $$PWD/../../projects/EsyaOrtak
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


# ===== ESYA ORTAK =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaOrtak

INCLUDEPATH += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
DEPENDPATH += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaOrtak.a

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

# ===== AKIS CIF ===== bunu yenisi ile degistirdim 11st nov '14

unix: LIBS += -L$$PWD/../../resources/libs/ -lakisIOSCIF

INCLUDEPATH += $$PWD/../../resources/headers/akis
DEPENDPATH += $$PWD/../../resources/libs

unix: PRE_TARGETDEPS += $$PWD/../../resources/libs/libakisIOSCIF.a

# ===== ESYA CRYPTO =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaCrypto

INCLUDEPATH += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
DEPENDPATH += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaCrypto.a

# ===== ESYA ASN =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaAsn

INCLUDEPATH += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
DEPENDPATH += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaAsn.a



#OBJECTIVE_SOURCES += \
#    Dene.mm
QMAKE_LFLAGS += -L//Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS8.1.sdk/System/Library/Frameworks/
unix: LIBS += -framework ExternalAccessory

OBJECTIVE_SOURCES +=
















