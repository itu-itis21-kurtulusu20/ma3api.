#-------------------------------------------------
#
# Project created by QtCreator 2014-02-18T16:07:23
#
#-------------------------------------------------

QT       += core gui xml

#QMAKE_INFO_PLIST = MyInfo.plist

QMAKE_CXXFLAGS += -stdlib=libc++
QMAKE_LFLAGS += -lc++

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = suleymanu
TEMPLATE = app


SOURCES += main.cpp\
    SmartCardManager.cpp \
    UIException.cpp

HEADERS  += mainwindow.h \
    ObjSmartCardManager.h \
    ObjBESSigner.h \
    SmartCardManager.h \
    UIException.h \
    ObjMainWindow.h

FORMS    += mainwindow.ui

INCLUDEPATH += $$PWD/../../projects/EsyaAsn
INCLUDEPATH += $$PWD/../../projects/EsyaCms
INCLUDEPATH += $$PWD/../../projects/EsyaSmartcard
INCLUDEPATH += $$PWD/../../projects/EsyaOrtak
INCLUDEPATH += $$PWD/../../projects/EsyaCrypto

INCLUDEPATH += $$PWD/../../resources/EsyaOrtakDLL_Mod/src
INCLUDEPATH += $$PWD/../../resources/EsyaCMS_Mod/src
INCLUDEPATH += $$PWD/../../resources/EsyaCMS_Mod/src/cms_asn
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtbersrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxsrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxersrc
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/include/rtxmlsrc

INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/generated_ASN/
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/wrapped_classes/
INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/src/wrapped_classes
DEPENDPATH += $$PWD/../../resources/EsyaASN_Mod/src/wrapped_classes

# asagiya yazdim
#INCLUDEPATH += $$PWD/../Development/ma3api/headers/akiscif

# ===== ESYA ORTAK =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaOrtak

#INCLUDEPATH += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
#DEPENDPATH += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaOrtak-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaOrtak.a

# ===== ASN1BER =====

unix: LIBS += -L$$PWD/../../resources/EsyaASN_Mod/libs/ -lasn1ber

#INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/libs
#DEPENDPATH += $$PWD/../../resources/EsyaASN_Mod/libs

unix: PRE_TARGETDEPS += $$PWD/../../resources/EsyaASN_Mod/libs/libasn1ber.a

# ===== ASN1RT =====

unix: LIBS += -L$$PWD/../../resources/EsyaASN_Mod/libs/ -lasn1rt

#INCLUDEPATH += $$PWD/../../resources/EsyaASN_Mod/libs
#DEPENDPATH += $$PWD/../../resources/EsyaASN_Mod/libs

unix: PRE_TARGETDEPS += $$PWD/../../resources/EsyaASN_Mod/libs/libasn1rt.a

# ===== ESYA ASN =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaAsn

#INCLUDEPATH += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
#DEPENDPATH += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaAsn-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaAsn.a

# ===== ESYA CMS =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaCms-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaCms

#INCLUDEPATH += $$PWD/../../builds/build-EsyaCms-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
#DEPENDPATH += $$PWD/../../builds/build-EsyaCms-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaCms-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaCms.a

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

# ===== AKIS =====

unix: LIBS += -L$$PWD/../../resources/libs/ -lakisIOSCIF

INCLUDEPATH += $$PWD/../../resources/headers/sakso
#DEPENDPATH += $$PWD/../../resources/libs

unix: PRE_TARGETDEPS += $$PWD/../../resources/libs/libakisIOSCIF.a

# ===== ESYA SMARTCARD =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaSmartcard-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaSmartcard

INCLUDEPATH += $$PWD/../../projects/EsyaSmartcard
#DEPENDPATH += $$PWD/../../builds/build-EsyaSmartcard-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaSmartcard-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaSmartcard.a

# ===== ESYA CRYPTO =====

unix: LIBS += -L$$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/ -lEsyaCrypto

#INCLUDEPATH += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug
#DEPENDPATH += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug

unix: PRE_TARGETDEPS += $$PWD/../../builds/build-EsyaCrypto-iphoneos_clang_Qt_5_2_1_for_iOS-Debug/libEsyaCrypto.a



OBJECTIVE_SOURCES += \
    mainwindow.mm \
    ObjSmartCardManager.mm \
    ObjBESSigner.mm \
    ObjMainWindow.mm
QMAKE_LFLAGS += -L//Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS7.1.sdk/System/Library/Frameworks/
unix: LIBS += -framework ExternalAccessory
unix: LIBS += -framework Security

