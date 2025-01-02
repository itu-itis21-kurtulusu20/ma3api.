#ifndef EAYARLISTENERTHREAD_H
#define EAYARLISTENERTHREAD_H

#include <QThread>
#include <QFileSystemWatcher>

namespace esya
{

	class EAyarCacheManager;

	class EAyarListenerThread : public QThread
	{
		Q_OBJECT

		QFileSystemWatcher mWatcher;
		EAyarCacheManager* mpParentCacheManager;

	public:
		EAyarListenerThread(EAyarCacheManager* ipParent);
		void run();

		private slots:
			void onAyarlarChanged(const QString &);
			void onUpdateAyarlar();

	};


}


#endif // EAYARLISTENERTHREAD_H
