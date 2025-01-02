#include "QCStatements.h"
#include "EsyaASN_DIL.h"

using namespace esya;

QCStatements::QCStatements(void)
{
}

QCStatements::QCStatements(const ASN1T_PKIXQUAL_QCStatements &iQSs)
{
	copyFromASNObject(iQSs);
}

QCStatements::QCStatements(const QByteArray &iQSs)
{
	constructObject(iQSs);
}

QCStatements::QCStatements(const QCStatements&iQSs)
: mList(iQSs.getList())
{
}

QCStatements::QCStatements(const QList<QCStatement>&iQSs)
: mList(iQSs)
{
}

QCStatements & QCStatements::operator=(const QCStatements&iQSs)
{
	mList = iQSs.getList();
	return *this;
}

bool esya::operator==(const QCStatements & iRHS, const QCStatements& iLHS)
{
	return (iRHS.getList()==iLHS.getList());
}

bool esya::operator!=(const QCStatements & iRHS, const QCStatements& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int QCStatements::copyFromASNObject(const ASN1T_PKIXQUAL_QCStatements &iQSs)
{
	QCStatement().copyQSs(iQSs,mList);
	return SUCCESS;
}

int QCStatements::copyToASNObject(ASN1T_PKIXQUAL_QCStatements& oQSs)const
{
	QCStatement().copyQSs(mList,oQSs);
	return SUCCESS;
}

void QCStatements::freeASNObject(ASN1T_PKIXQUAL_QCStatements & oQSs)const
{
	QCStatement().freeASNObjects(oQSs);
}

/////////////////////////////////////////////////////////////////

const QList<QCStatement> &QCStatements::getList() const
{
	return mList;
}

void QCStatements::setList(const QList<QCStatement> &iList) 
{
	mList = iList;
}

QString QCStatements::toString() const
{
	return QString();
}

QStringList QCStatements::toStringList() const 
{
	QStringList stList;
	
	for (int i = 0; i<mList.size();i++)
		stList<<mList[i].toString();

	return stList; 
}


int QCStatements::indexOf(const ASN1TObjId iStatementId)const
{
	for (int i = 0 ; i < mList.size() ; i++)
	{
		if (mList[i].getStatementID() == iStatementId)
			return i;
	}
	return -1;
}

void  QCStatements::appendStatement(const QCStatement& iStatement)
{
	mList.append(iStatement);
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString QCStatements::eklentiAdiAl()			const 
{
	return DIL_EXT_NITELIKLISERTIFIKAIBARESI;
}

QString QCStatements::eklentiKisaDegerAl()	const 
{
	return toStringList().join(",");
}

QString QCStatements::eklentiUzunDegerAl()	const 
{
	return toStringList().join("\n");
}

AY_Eklenti* QCStatements::kendiniKopyala() const 
{
	return (AY_Eklenti* )new QCStatements(*this);
}


QCStatements::~QCStatements(void)
{
}
