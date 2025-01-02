#ifndef PERSISTENCEFACADE_H
#define PERSISTENCEFACADE_H

#include <QObject>
#include <QSqlDatabase>
#include "IMapper.h"


#define KERMEN_CLASS_ELOGGER "ELogger"
#define KERMEN_CLASS_EAYAR_KARTLAR "EAyarKartlar"
#define KERMEN_CLASS_EAYAR_LDAP "EAyarLDAP"

class PersistenceFacade : public QObject
{
	Q_OBJECT
	static PersistenceFacade * mInstance;
public:
	Q_DECL_EXPORT static PersistenceFacade * getInstance();
	Q_DECL_EXPORT PersistenceFacade(QObject *parent=0);
	Q_DECL_EXPORT IMapper * getMapper(QSqlDatabase * ipSqliteDb,const QString &iClassName);
	Q_DECL_EXPORT ~PersistenceFacade();

private:
	
};

#endif // PERSISTENCEFACADE_H
