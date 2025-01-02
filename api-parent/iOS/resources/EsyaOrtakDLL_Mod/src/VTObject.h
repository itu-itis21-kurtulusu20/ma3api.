#ifndef __VTOBJECT__
#define __VTOBJECT__

#include <QSqlQuery>
#include "EVeritabani.h"


using namespace esya;

class Q_DECL_EXPORT VTObject
{

	virtual void		_constructObject(const QSqlRecord & iQuery) = 0 ;
	virtual VTObject*	_newInstance() = 0 ;

	bool mNewInstance;

public:
	VTObject(void);
	VTObject(QSqlQuery*);
	VTObject(const QSqlRecord&);

	virtual ParamList	toParamList() = 0;
	virtual QString		insertQuery() = 0;
	virtual QString		selectQuery(const QString &iOrderColumn  = QString()) = 0;
	virtual QString		existsQuery() = 0;
	virtual QString		deleteQuery() = 0;

	
	VTObject* constructObject(QSqlQuery * iQuery);
	VTObject* constructObject(const QSqlRecord& iRec);

	int		  insertObject( EVeritabani &iVT);
	int		  deleteObject( EVeritabani &iVT);
	bool	  objectExists(EVeritabani & iVT ,const QString & iFilter);
	VTObject *getObject(EVeritabani & iVT, const QString & iFilter);

	virtual ~VTObject(void);
};


#endif