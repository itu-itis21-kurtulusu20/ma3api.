#ifndef _E_EXPLORER_REFRESHER_H_
#define _E_EXPLORER_REFRESHER_H_

#include <QStringList>

class Q_DECL_EXPORT EExplorerRefresher
{
	QStringList mEntryList;
public:
	EExplorerRefresher(const QString & iEntry);
	EExplorerRefresher(const QStringList & iEntryList);
	void refreshExplorer();
	~EExplorerRefresher(void);
};
#endif