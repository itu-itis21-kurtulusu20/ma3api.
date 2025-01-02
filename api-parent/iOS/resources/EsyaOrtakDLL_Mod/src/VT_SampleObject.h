
#ifndef __VT_PFX__
#define __VT_PFX__

#include "VTObject.h"
#include <QByteArray>
#include <QDateTime>
#include "EVeritabani.h"


class VT_SampleObject : public VTObject
{
	qlonglong	mNo;	// Primary Key of the table ID column.
	QString		mText;


	virtual void _constructObject(const QSqlRecord & rec);
	virtual	VTObject*	_newInstance();

public:
	
	// Column Indexes
	enum {	CI_No,
			CI_Text
		};

	// VT Table name for sample object
	static const QString TABLENAME;

	// Column Names
	static const QString C_No;
	static const QString C_Text;

	VT_SampleObject(void);
	VT_SampleObject(QSqlQuery * iQuery);
	VT_SampleObject(	const qlonglong & iNo,
						const QString & iText	);

	virtual ~VT_SampleObject(void);

	// virtual functions that must be implemented in each concrete class
	ParamList	toParamList();
	QString		insertQuery();
	QString		selectQuery(const QString &iOrderColumn  = QString());
	QString		deleteQuery();
	QString		existsQuery();
	

	/* Sample query functions*/
	bool noVarMi(EVeritabani & iVT, const qlonglong & iNo);
	bool textVarMi(EVeritabani & iVT, const QString & iText);

	
	// Getters
	const qlonglong &	getNo()const;
	const QString  &	getText() const;

};


#endif 