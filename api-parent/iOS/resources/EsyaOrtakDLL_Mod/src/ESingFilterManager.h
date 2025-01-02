#ifndef _E_SING_FILTER_MANAGER_H_
#define _E_SING_FILTER_MANAGER_H_

#include <QStringList>

class Q_DECL_EXPORT ESingFilterManager
{
	QStringList mKermenUzantiList;
	QStringList mDirSearcKermenFileFilterList;
	static ESingFilterManager * mpInstance;
	ESingFilterManager(void);
public:	
	static ESingFilterManager * getInstance();
	~ESingFilterManager(void);
	const QStringList & getKermenUzantiList() const;
	const QStringList & getDirSearcKermenFileFilter() const;
};
#endif
