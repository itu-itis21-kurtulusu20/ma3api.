#ifndef SAMPLEOBJECTMODEL_H
#define SAMPLEOBJECTMODEL_H

#include <QSqlTableModel>
#include <QSqlDatabase>

class SampleObjectModel : public QSqlTableModel
{

public:
	SampleObjectModel(QObject *parent ,QSqlDatabase db );
	QVariant	  data(const QModelIndex &index, int role = Qt::DisplayRole) const;

private:

};

#endif // SAMPLEOBJECTMODEL_H
