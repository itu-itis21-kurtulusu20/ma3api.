#include "VTObject.h"

#include "EVeritabaniException.h"

using namespace esya;

VTObject::VTObject(void)
: mNewInstance(true)
{
}

VTObject::VTObject(QSqlQuery *iQuery)
: mNewInstance(false)
{
	constructObject(iQuery);
}

VTObject::VTObject(const QSqlRecord &iRec)
: mNewInstance(false)
{
	constructObject(iRec);
}

VTObject* VTObject::constructObject(QSqlQuery * iQuery)
{
	if (!iQuery || !iQuery->isActive())
		throwVTEXCEPTION(EVeritabaniException::GecersizQuery,QString("Obje oluþturulamadý"));

	_constructObject(iQuery->record());

	return this;
}

VTObject* VTObject::constructObject(const QSqlRecord & iRec)
{
	_constructObject(iRec);

	return this;
}



int VTObject::insertObject( EVeritabani &iVT)
{
	try
	{
		iVT.sorguYap(insertQuery(),toParamList());
		return SUCCESS;
	}
	catch (...)
	{
		throwVTEXCEPTION(EVeritabaniException::OzneEklenemedi,QString("Obje eklenemedi"));	
	}
}

int VTObject::deleteObject( EVeritabani &iVT)
{
	try
	{
		iVT.sorguYap(deleteQuery(),ParamList());
		return SUCCESS;
	}
	catch (...)
	{
		throwVTEXCEPTION(EVeritabaniException::OzneSilinemedi,QString("Obje silinemedi"));	
	}
}


bool VTObject::objectExists(EVeritabani & iVT ,const QString & iFilter)
{
	try
	{
		QString sql = QString("%1 WHERE %2").arg(existsQuery()).arg(iFilter);
		QSqlQuery * q = iVT.sorguYap(sql,ParamList());
		bool b = q->next();
		q->finish();
		DELETE_MEMORY(q);
		return b;
	}
	catch (...)
	{
		throwVTEXCEPTION(EVeritabaniException::QueryCalistirilamadi,QString("Sorgu objesi gecersiz "));	
	}
}

VTObject * VTObject::getObject(EVeritabani & iVT,const QString & iFilter)
{
	try
	{
		VTObject * newObject = NULL;
		QString sql = QString("%1 WHERE %2").arg(selectQuery()).arg(iFilter);
		QSqlQuery * q = iVT.sorguYap(sql,ParamList());
		bool b = q->next();
		if (b)
		{
			newObject = _newInstance()->constructObject(q);
		}
		q->finish();
		DELETE_MEMORY(q);
		return newObject; 
	}
	catch (...)
	{
		throwVTEXCEPTION(EVeritabaniException::QueryCalistirilamadi,QString("Sorgu objesi gecersiz "));	
	}
}


VTObject::~VTObject(void)
{
}
