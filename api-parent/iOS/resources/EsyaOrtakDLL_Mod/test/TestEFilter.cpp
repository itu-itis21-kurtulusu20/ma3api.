#include "TestEFilter.h"

#include "EFilter.h"

using namespace esya;

TestEFilter::TestEFilter(QObject *parent)
	: QObject(parent)
{

}

TestEFilter::~TestEFilter()
{

}


void TestEFilter::testSimpleUsage()
{
	//ANDFilter af(new ORFilter(new MatchFilter("Name","Ahmet"),new MatchFilter("Surname","Posbiyik")),new RangeFilter("Yas",12,23));

	ANDFilter af(QList<EFilter*>()<<
				new MatchFilter("Name","Ahmet")<<
				new MatchFilter("Surname","Posbiyik")<<
				new MatchFilter("Sex","Male")<<
				new RangeFilter("Yas",12,23)<<
				new InFilter("HairColor",QList<QVariant>()<<"Brown"<<"Yellow"<<"Black")<<
				new NotFilter(new MatchFilter("Character","Agresive"))
				);

	QString sql = af.toSQL();

	QList<QVariant> params = af.getParams();
	
	qDebug(qPrintable(sql));

	for(int i= 0 ; i<params.size() ; i++)
	{
		QString debugStr = QString("%1 : %2").arg(i+1).arg(params[i].toString());
		qDebug(qPrintable(debugStr));
	}

}