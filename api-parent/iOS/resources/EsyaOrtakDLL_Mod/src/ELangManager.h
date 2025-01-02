#ifndef _E_LANG_MANAGER_H_
#define _E_LANG_MANAGER_H_

#include <QMap>
class Q_DECL_EXPORT ELangManager
{
	QMap<QString,QString> mLangMap;
	static ELangManager * mpInstance;
	ELangManager(void);
	void _initLangMap();
public:
	static ELangManager * getInstance();	
	virtual ~ELangManager(void);
	QMap<QString,QString> getLangMap();
};
#endif
