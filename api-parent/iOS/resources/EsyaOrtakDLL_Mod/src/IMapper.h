#ifndef IMAPPER_H
#define IMAPPER_H

#include <QObject>

class IMapper : public QObject
{
public:
	Q_DECL_EXPORT IMapper(QObject *parent=0);
	IMapper(const IMapper &);
	IMapper & operator=(const IMapper &);
	Q_DECL_EXPORT ~IMapper();
	Q_DECL_EXPORT virtual QList<QObject * > getAll()=0;
	Q_DECL_EXPORT virtual QObject * get(int iObjID)=0;	
	Q_DECL_EXPORT virtual int put(int iObjID,QObject * iObj)=0;	
	Q_DECL_EXPORT virtual bool deleteFromDB(int iObjID)=0;
};

#endif // IMAPPER_H
