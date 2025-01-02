#-------------------------------------------------
#
# Project created by QtCreator 2014-10-02T08:09:56
#
#-------------------------------------------------

#burda xml i ben ekledim
QT       += core gui xml

QMAKE_CXXFLAGS += -stdlib=libc++
QMAKE_LFLAGS += -lc++

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = SampleProject
TEMPLATE = app


SOURCES +=\
    SmartCardManager.cpp \
    main.cpp \
    UIException.cpp

HEADERS  += \
    SmartCardManager.h \
    ObjSmartCardManager.h \
    mainwindow.h \
    UIException.h \
    ObjBESSigner.h \
    ObjMainWindow.h

FORMS    += \
    mainwindow.ui

INCLUDEPATH += $$PWD/../../resources/EsyaOrtakDLL_Mod/src

INCLUDEPATH += $$PWD/../../resources/EsyaCMS_Mod/src
INCLUDEPATH += $$PWD/../../resources/EsyaCMS_Mod/src/cms_asn

INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtbersrc

INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/generated_ASN
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/wrapped_classes


INCLUDEPATH += $$PWD/../../resources/headers/akis
INCLUDEPATH += $$PWD/../../resources/headers/milko


INCLUDEPATH += $$PWD/../../projects/EsyaOrtak
INCLUDEPATH += $$PWD/../../projects/EsyaCrypto
INCLUDEPATH += $$PWD/../../projects/EsyaSmartcard
INCLUDEPATH += $$PWD/../../projects/EsyaCms


# ===== AKIS =====
unix: LIBS += -L$$PWD/../../resources/libs/ -lakisIOSCIF
unix: PRE_TARGETDEPS += $$PWD/../../resources/libs/libakisIOSCIF.a
# ===== MILKO =====
unix: LIBS += -L$$PWD/../../resources/libs/ -lMilkoLib
unix: PRE_TARGETDEPS += $$PWD/../../resources/libs/libMilkoLib.a

# ===== ASN1BER =====
unix: LIBS += -L$$PWD/../../resources/EsyaASN_Mod/libs/ -lasn1ber
unix: PRE_TARGETDEPS += $$PWD/../../resources/EsyaASN_Mod/libs/libasn1ber.a
# ===== ASN1RT =====
unix: LIBS += -L$$PWD/../../resources/EsyaASN_Mod/libs/ -lasn1rt
unix: PRE_TARGETDEPS += $$PWD/../../resources/EsyaASN_Mod/libs/libasn1rt.a

# ===== ESYA ORTAK =====
unix: LIBS += -L$$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaOrtak
unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaOrtak.a
# ===== ESYA ASN =====
unix: LIBS += -L$$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaAsn
unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaAsn.a
# ===== ESYA CRYPTO =====
unix: LIBS += -L$$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaCrypto
unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaCrypto.a
# ===== ESYA SMARTCARD =====
unix: LIBS += -L$$PWD/../../builds/build-EsyaSmartcard-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaSmartcard
unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaSmartcard-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaSmartcard.a
# ===== ESYA CMS =====
unix: LIBS += -L$$PWD/../../builds/build-EsyaCms-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaCms
unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaCms-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaCms.a


OBJECTIVE_SOURCES += \
    ObjSmartCardManager.mm \
    mainwindow.mm \
    ObjBESSigner.mm \
    ObjMainWindow.mm

QMAKE_LFLAGS += -L//Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS7.1.sdk/System/Library/Frameworks/

unix: LIBS += -framework ExternalAccessory
unix: LIBS += -framework Security
unix: LIBS += -framework CoreBluetooth




