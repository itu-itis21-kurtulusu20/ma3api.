#-------------------------------------------------
#
# Project created by QtCreator 2014-02-18T15:29:52
#
#-------------------------------------------------

QT       += core gui sql

TARGET = EsyaOrtak
TEMPLATE = lib
CONFIG += staticlib

SOURCES += \
    ../../resources/EsyaOrtakDLL_Mod/src/AbstractPersistenceMapper.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/AbstractRDBMapper.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/AtomicWork.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/BaseTransaction.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/BosSeviyeLogcu.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/DBLayer.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/DBSession.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/DebugSeviyeLogcu.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/DeepDebugSeviyeLogcu.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarException.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarKartlarRDMMapper.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarKullaniciManager.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarlar.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarLdap.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarLDAPRDBMapper.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarListenerThread.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarManager.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarManagerFactory.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarSinif.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarValueTool.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EDbLoggerThread.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EException.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EExplorerRefresher.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EFileHandle.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EFileLocker.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EGenelAyarManager.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EKulGrupBilgi.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EKullaniciAyarYonetici.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ELangManager.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EMemoryDBMapper.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EMemoryLeakDetector.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EOrtamDegiskeni.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EProcessInfo.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EProcessMemUsage.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ErrorSeviyeLogcu.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ESingFilterManager.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ESqliteLogger.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ESure.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EsyaUtils.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ESynchroniseManager.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EVeritabani.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EVeritabaniException.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EYardimAcici.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/EYerelAyarManager.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/FileUtil.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/GUIDGenerator.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/IMapper.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/InfoLogger.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/InvalidPatternException.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/Kronometre.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/MemoryTaskCollector.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ParametreListesi.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/PerformanceInfoSeviyeLogcu.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/PersistenceFacade.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ScheduleLauncherThread.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulePredictor.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/Scheduler.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulerTask.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulerValueMatcher.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulerValueParser.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ScheduleTaskExecuter.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/ScheduleTimerThread.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulingPattern.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/SignalSchedulerTask.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/SQliteAtomicWork.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/SqliteDBSession.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/TaskCollector.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/TaskTable.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/Transaction.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/VT_SampleObject.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/VTObject.cpp \
    ../../resources/EsyaOrtakDLL_Mod/src/WarningLogger.cpp \
    Logger.cpp \
    esyanaberortak.cpp

HEADERS += \
    ../../resources/EsyaOrtakDLL_Mod/src/AbstractPersistenceMapper.h \
    ../../resources/EsyaOrtakDLL_Mod/src/AbstractRDBMapper.h \
    ../../resources/EsyaOrtakDLL_Mod/src/AbstractSeviyeLogcu.h \
    ../../resources/EsyaOrtakDLL_Mod/src/AtomicWork.h \
    ../../resources/EsyaOrtakDLL_Mod/src/BaseTransaction.h \
    ../../resources/EsyaOrtakDLL_Mod/src/BosSeviyeLogcu.h \
    ../../resources/EsyaOrtakDLL_Mod/src/DBLayer.h \
    ../../resources/EsyaOrtakDLL_Mod/src/DBSession.h \
    ../../resources/EsyaOrtakDLL_Mod/src/DebugSeviyeLogcu.h \
    ../../resources/EsyaOrtakDLL_Mod/src/DeepDebugSeviyeLogcu.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarAlici.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarException.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarGDMAcikDosya.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarGDMDizin.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarGDMDizinBilgiManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarGDMOzneBilgiManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarGDMOzneBilgisi.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarGDMVarsayilanOzne.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarKartlar.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarKartlarRDMMapper.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarKullaniciManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarlar_DIL.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarlar.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarlardanDilBelirleyici.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarLdap.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarLDAPRDBMapper.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarListenerThread.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarManagerFactory.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarSinif.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarTanimlari.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EAyarValueTool.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EDbLoggerThread.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EException.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EExplorerRefresher.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EFileHandle.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EFileLocker.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EFileSecurity.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EFilter.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EGenelAyarManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EKulGrupBilgi.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EKullaniciAyarYonetici.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ELangManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ELinuxFileSecurity.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ELoggerFactory.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ELoggerRDMMapper.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ELokaldenDilBelirleyici.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EMemoryDBMapper.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EMemoryLeakDetector.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EOrtamDegiskeni.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EProcessInfo.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EProcessMemUsage.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ErrorSeviyeLogcu.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ESambaIstemcisi.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ESeviyeLogYonetici.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ESingFilterManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ESqliteLogger.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ESure.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EsyaOrtak_Ayar.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EsyaOrtak_Ortak.h \
    ../../resources/EsyaOrtakDLL_Mod/src/esyaOrtak.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EsyaOrtakDLL_DIL.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EsyaUtils.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ESynchroniseManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ETestUtil.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EVeritabani.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EVeritabaniException.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EWindowsFileSecurity.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EYardimAcici.h \
    ../../resources/EsyaOrtakDLL_Mod/src/EYerelAyarManager.h \
    ../../resources/EsyaOrtakDLL_Mod/src/FileUtil.h \
    ../../resources/EsyaOrtakDLL_Mod/src/GUIDGenerator.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ILogger.h \
    ../../resources/EsyaOrtakDLL_Mod/src/IMapper.h \
    ../../resources/EsyaOrtakDLL_Mod/src/InfoLogger.h \
    ../../resources/EsyaOrtakDLL_Mod/src/InvalidPatternException.h \
    ../../resources/EsyaOrtakDLL_Mod/src/Kronometre.h \
    ../../resources/EsyaOrtakDLL_Mod/src/MemoryTaskCollector.h \
    ../../resources/EsyaOrtakDLL_Mod/src/NTProcessInfo.h \
    ../../resources/EsyaOrtakDLL_Mod/src/OrtakDil.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ortaklibrary_global.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ParametreListesi.h \
    ../../resources/EsyaOrtakDLL_Mod/src/PerformanceInfoSeviyeLogcu.h \
    ../../resources/EsyaOrtakDLL_Mod/src/PerformansOlcumleri.h \
    ../../resources/EsyaOrtakDLL_Mod/src/PersistenceFacade.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SampleObjectModel.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ScheduleLauncherThread.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulePredictor.h \
    ../../resources/EsyaOrtakDLL_Mod/src/Scheduler.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulerTask.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulerValueMatcher.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulerValueParser.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ScheduleTaskExecuter.h \
    ../../resources/EsyaOrtakDLL_Mod/src/ScheduleTimerThread.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SchedulingPattern.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SignalSchedulerTask.h \
    ../../resources/EsyaOrtakDLL_Mod/src/smbmm.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SQliteAtomicWork.h \
    ../../resources/EsyaOrtakDLL_Mod/src/SqliteDBSession.h \
    ../../resources/EsyaOrtakDLL_Mod/src/stat-time.h \
    ../../resources/EsyaOrtakDLL_Mod/src/TaskCollector.h \
    ../../resources/EsyaOrtakDLL_Mod/src/TaskTable.h \
    ../../resources/EsyaOrtakDLL_Mod/src/Transaction.h \
    ../../resources/EsyaOrtakDLL_Mod/src/VT_SampleObject.h \
    ../../resources/EsyaOrtakDLL_Mod/src/VTObject.h \
    ../../resources/EsyaOrtakDLL_Mod/src/WarningLogger.h \
    Logger.h \
    esyanaberortak.h
unix {
    target.path = /usr/lib
    INSTALLS += target
}

