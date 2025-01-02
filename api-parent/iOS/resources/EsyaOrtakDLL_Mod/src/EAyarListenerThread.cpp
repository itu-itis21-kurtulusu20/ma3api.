#include "EAyarListenerThread.h"
#include "EAyarAlici.h"
#include "FileUtil.h"

#include <QEventLoop>
#include <QTimer>

#define SLEEP_TIME 3000


using namespace esya;

EAyarListenerThread::EAyarListenerThread(EAyarCacheManager* ipParent)
:	mpParentCacheManager(ipParent)
{
	Q_ASSERT(mpParentCacheManager);
	mWatcher.addPath(QString("%1/%2").arg(FileUtil::genelAyarPath()).arg(KERMEN_GENEL_AYARLAR_FILE_NAME));
	mWatcher.addPath(QString("%1/%2").arg(FileUtil::yerelAyarPath()).arg(KERMEN_YEREL_AYARLAR_FILE_NAME));
	QObject::connect(&mWatcher,SIGNAL(fileChanged(const QString&)),this,SLOT(onAyarlarChanged(const QString & )),Qt::QueuedConnection);
}

void EAyarListenerThread::run()
{
	exec();
}

void EAyarListenerThread::onAyarlarChanged(const QString &iFilePath)
{
	QObject::disconnect(&mWatcher,SIGNAL(fileChanged(const QString&)),this,SLOT(onAyarlarChanged(const QString & )));

	QTimer::singleShot(SLEEP_TIME,this,SLOT(onUpdateAyarlar()));
}


void EAyarListenerThread::onUpdateAyarlar()
{
	mpParentCacheManager->updateCache();
	QObject::connect(&mWatcher,SIGNAL(fileChanged(const QString&)),this,SLOT(onAyarlarChanged(const QString & )),Qt::QueuedConnection);
}
