#ifndef __E_FILE_LOGGER_H_
#define __E_FILE_LOGGER_H_
#include "ILogger.h"

using namespace esya;
NAMESPACE_BEGIN(esya)
class EFileLogger :
	public ILogger
{		
	Q_OBJECT;	
	int mMaxDosyaBoyu;
	void _dosyaBoyuKontroluYap();
	int _maxLogDosyaBoyuOku();
	QString _getDosyayaYazilacakStr(const ELogEntry & iLogEntry);
	int _ayarlardaMaxLogDosyaBoyuOku();	
public:
	EFileLogger(const QString & iLogFilePath,ELogEntry::PRIORITY iEsikSeviyesi);
	void maxDosyaBoyuBelirle();
	void _writeToLog(const ELogEntry & iLogEntry);
	~EFileLogger(void);
};

NAMESPACE_END
#endif
