#include "SampleObjectModel.h"
#include "VT_SampleObject.h"

SampleObjectModel::SampleObjectModel(QObject *parent, QSqlDatabase db)
:	QSqlTableModel(parent,db)
{
	setTable(VT_SampleObject::TABLENAME);
}


QVariant SampleObjectModel::data(const QModelIndex &index,  int role) const 
{
	QVariant value = QSqlTableModel::data(index, role);

	if (index.column() == fieldIndex(VT_SampleObject::C_No) )
	{
		if (role == Qt::TextAlignmentRole)
			return QVariant(); // Insert alignment for sppecific types et. numeric alignment for numbers
	}
	else if ( (index.column() ==  fieldIndex(VT_SampleObject::C_Text) ) )
	{
		if (role == Qt::TextColorRole) // to make different color
			return Qt::darkBlue;
	}

	return QSqlTableModel::data(index,role);
}
