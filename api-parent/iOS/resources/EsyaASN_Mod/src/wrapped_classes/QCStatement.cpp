#include "QCStatement.h"
#include "EASNToStringUtils.h"

using namespace esya;

QCStatement::QCStatement(void)
: mStatementInfoPresent(false)
{
	mStatementID.numids = 0 ;
}

QCStatement::QCStatement(const QByteArray & iQS)
{
	constructObject(iQS);
}

QCStatement::QCStatement(const QByteArray & iStatementInfo, const ASN1TObjId &iStatementID)
: mStatementID(iStatementID), mStatementInfo(iStatementInfo)
{
	mStatementInfoPresent = mStatementInfo.size()>0;
}

QCStatement::QCStatement(const ASN1T_PKIXQUAL_QCStatement & iQS)
{
	copyFromASNObject(iQS);
}

QCStatement::QCStatement(const ASN1TObjId & iStatementID)
: mStatementID(iStatementID)
{
}

QCStatement::QCStatement(const QCStatement& iQS)
:mStatementInfo(iQS.getStatementInfo()),mStatementID(iQS.getStatementID()), mStatementInfoPresent(iQS.isStatementInfoPresent())
{
}

QCStatement& QCStatement::operator=(const QCStatement& iQS)
{
	mStatementInfo= iQS.getStatementInfo();
	mStatementID= iQS.getStatementID();
	return *this;
}


bool esya::operator==(const QCStatement & iRHS, const QCStatement& iLHS)
{
	return ( iRHS.getStatementID() == iLHS.getStatementID() && iRHS.getStatementInfo()==iRHS.getStatementInfo() );
}

bool esya::operator!=(const QCStatement & iRHS, const QCStatement& iLHS)
{
	return ( ! ( iRHS == iLHS ));
}


int QCStatement::copyFromASNObject(const ASN1T_PKIXQUAL_QCStatement & iQS) 
{
	mStatementID = iQS.statementId;
	mStatementInfoPresent = iQS.m.statementInfoPresent;

	if (iQS.m.statementInfoPresent && iQS.statementInfo.numocts>0)
		mStatementInfo = QByteArray((char*)iQS.statementInfo.data,iQS.statementInfo.numocts);

	return SUCCESS;
}


const QByteArray &QCStatement::getStatementInfo()const 
{
	return mStatementInfo;
}

const ASN1TObjId &QCStatement::getStatementID() const
{
	return mStatementID;
}

bool	QCStatement::isStatementInfoPresent()const
{
	return mStatementInfoPresent;
}


void QCStatement::setStatementInfo(const QByteArray& iSI)
{
	mStatementInfo= iSI;	
}

int QCStatement::copyToASNObject(ASN1T_PKIXQUAL_QCStatement & oQS) const 
{
	oQS.statementId= mStatementID;
	
	oQS.m.statementInfoPresent = mStatementInfoPresent;

	if (mStatementInfoPresent && mStatementInfo.size()>0)
	{
		oQS.statementInfo.data = (OSOCTET*)myStrDup(mStatementInfo.data(),mStatementInfo.size());
		oQS.statementInfo.numocts = mStatementInfo.size(); 
	}
	return SUCCESS;
}

void QCStatement::freeASNObject(ASN1T_PKIXQUAL_QCStatement & oQS)const
{
	if ( oQS.m.statementInfoPresent && oQS.statementInfo.numocts >0)
	{
		DELETE_MEMORY_ARRAY(oQS.statementInfo.data);
		oQS.statementInfo.numocts = 0;
	}
}


int QCStatement::copyQSs(const ASN1T_PKIXQUAL_QCStatements & iQSs, QList<QCStatement>& oList)
{
	return copyASNObjects<QCStatement>(iQSs,oList);
}

int QCStatement::copyQSs(const QList<QCStatement> iList ,ASN1T_PKIXQUAL_QCStatements & oQSs)
{
	return copyASNObjects<QCStatement>(iList,oQSs);
}

int QCStatement::copyQSs(const QByteArray & iASNBytes, QList<QCStatement>& oList)
{
	return copyASNObjects<ASN1T_PKIXQUAL_QCStatements,ASN1C_PKIXQUAL_QCStatements,QCStatement>(iASNBytes,oList);
}

int QCStatement::copyQSs(const QList<QCStatement>& iList , QByteArray & oASNBytes)
{
	return copyASNObjects<ASN1T_PKIXQUAL_QCStatements,ASN1C_PKIXQUAL_QCStatements,QCStatement>(iList,oASNBytes);
}

QCStatement::~QCStatement(void)
{
}


QString QCStatement::toString() const
{
	QString str = QString("%1\n%2").arg(EASNToStringUtils::oidToString(mStatementID)).arg(EASNToStringUtils::byteArrayToStr(mStatementInfo));
	return str;
}