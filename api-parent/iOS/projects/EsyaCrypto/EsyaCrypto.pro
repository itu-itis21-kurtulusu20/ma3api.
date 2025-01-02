#-------------------------------------------------
#
# Project created by QtCreator 2014-03-10T13:35:25
#
#-------------------------------------------------

QT       -= gui

TARGET = EsyaCrypto
TEMPLATE = lib
CONFIG += staticlib

SOURCES += AlgorithmList.cpp \
    CryptoException.cpp

HEADERS += AlgorithmList.h \
    CryptoFunctions.h \
    KriptoUtils.h \
    CryptoException.h
unix {
    target.path = /usr/lib
    INSTALLS += target
}

# - yeni projeler -
# ortak
INCLUDEPATH += $$PWD/../../projects/EsyaOrtak

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


# ===== ESYA ASN =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaAsn

INCLUDEPATH += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
DEPENDPATH += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaAsn.a


# objective-c sources

OBJECTIVE_SOURCES += \
    CryptoFunctions.mm \
    KriptoUtils.mm

QMAKE_LFLAGS += -L//Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS7.1.sdk/System/Library/Frameworks/
unix: LIBS += -framework Security

