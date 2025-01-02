#ifndef __E_PROCESS_INFO_H_
#define __E_PROCESS_INFO_H_
#ifdef WIN32
#include <Windows.h>
#endif
#include <QString>
#include <QMultiMap>
#include "EsyaOrtak_Ortak.h"
NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EProcessInfo
{
	QString mProgramYolu;
	QString mKomutSatiri;
	qlonglong mSessionID;
public:
	EProcessInfo(const QString & iProgramYolu,const QString & iKomutSatiri,qlonglong iSessionID);
	~EProcessInfo(void);

	static QMultiMap<QString,EProcessInfo> currentCmdFiles();
	static QMultiMap<QString,EProcessInfo> currentProcesses();	
	static bool programCalisiyorMu(const QString & iProgramAdi);
	static QList<EProcessInfo> programinCalisanOrnekleri(const QString & iProgramAdi);

	static int getCurrentProcessMemoryUsage();
	QString getProgramYolu() const;
	QString getKomutSatiri() const;
	qlonglong getSessionId() const;
};
NAMESPACE_END
#endif