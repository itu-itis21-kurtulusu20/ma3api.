#include "VT_SampleObject.h"
#include <QVariant>
#include "EVeritabaniException.h"
#include <QSqlField>

using namespace esya;


const QString VT_SampleObject::TABLENAME = "TBL_SAMPLEOBJECT" ;

const QString VT_SampleObject::C_No		= "No" ;
const QString VT_SampleObject::C_Text	= "Text";


VT_SampleObject::VT_SampleObject(void)
{
}
VT_SampleObject::VT_SampleObject(QSqlQuery * iQuery)
: VTObject(iQuery)
{
}

VT_SampleObject::VT_SampleObject(	const qlonglong & iNo,
									const QString & iText 	)
:	VTObject(),
	mNo(iNo),
	mText(iText)
{
}




void VT_SampleObject::_constructObject(const QSqlRecord & rec)
{
	try
	{
		mNo		= rec.field(C_No).value().toLongLong();
		mText	= rec.field(C_Text).value().toString();
	}
	catch (...)
	{
		throwVTEXCEPTION(EVeritabaniException::GecersizQuery,QString("Sorgu objesi gecersiz "));	
	}
}

VTObject* VT_SampleObject::_newInstance()
{
	return new VT_SampleObject();
}


ParamList VT_SampleObject::toParamList()
{
	ParamList params;
	params.append(qMakePair(C_No,QVariant(mNo)));
	params.append(qMakePair(C_Text,QVariant(mText)));
	
	return params;
}

QString VT_SampleObject::insertQuery()
{
	QString sql= QString("INSERT INTO %1(%2,%3) VALUES(NULL,:%3)")
					.arg(TABLENAME)
					.arg(C_No).arg(C_Text);
	return sql;
}

QString	VT_SampleObject::selectQuery(const QString &iOrderColumn )
{
	QString sql= QString("SELECT %2,%3 FROM %1 ")
					.arg(TABLENAME)
					.arg(C_No).arg(C_Text);

	if (!iOrderColumn.trimmed().isEmpty())
	{
		sql.append(QString("ORDER BY %1 ").arg(iOrderColumn));
	}

	return sql;	

}

QString	VT_SampleObject::existsQuery()
{
	QString sql= QString("SELECT 1 FROM %1").arg(TABLENAME);
	return sql;	
}

QString	VT_SampleObject::deleteQuery()
{
	QString sql= QString("DELETE FROM %1 WHERE %2='%3'").arg(TABLENAME).arg(C_No).arg(mNo);
	return sql;	
}

bool VT_SampleObject::textVarMi(EVeritabani & iVT, const QString & iText)
{
	QString filter = QString(" %1='%2'").arg(C_Text).arg(iText);
	
	return objectExists(iVT,filter);

}

bool VT_SampleObject::noVarMi(EVeritabani & iVT , const qlonglong & iNo)
{
	QString filter = QString(" %1='%2'").arg(C_No).arg(iNo);
	
	return objectExists(iVT,filter);
}


const qlonglong &	VT_SampleObject::getNo()const
{
	return mNo;
}

const QString  & VT_SampleObject::getText() const
{
	return mText;
}

VT_SampleObject::~VT_SampleObject(void)
{
}
