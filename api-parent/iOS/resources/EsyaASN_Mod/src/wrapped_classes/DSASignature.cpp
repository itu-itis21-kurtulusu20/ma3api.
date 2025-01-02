#include "DSASignature.h"
#include "EASNException.h"

namespace esya{

DSASignature::DSASignature(void)
{
}

DSASignature::DSASignature(const QByteArray & iDS)
{
	constructObject(iDS);
}

DSASignature::DSASignature(const QString& iR, const QString& iS)
:	mR(iR),
	mS(iS)
{
}

DSASignature::DSASignature(const QByteArray& iR, const QByteArray& iS)
{
	QString stR = iR.toHex();
	mR  = "0x"+stR;

	QString stS = iS.toHex();
	mS  = "0x"+stS;
}

DSASignature::DSASignature(const ASN1T_ALGOS_DssSigValue& iDS)
{
	copyFromASNObject(iDS);
}

DSASignature::DSASignature(const DSASignature& iDS)
:	mR(iDS.getR()),
	mS(iDS.getS())
{
}


DSASignature& DSASignature::operator=(const DSASignature& iDS)
{
	mR = iDS.getR(); 
	mS = iDS.getS();
	return (*this);
}

bool operator==( const DSASignature& iRHS, const DSASignature& iLHS)
{
	return ( ( iRHS.getR() == iLHS.getR()) && 
			 ( iRHS.getS() == iLHS.getS()) ) ;
}

bool operator!=( const DSASignature& iRHS, const DSASignature& iLHS)
{
	return ( ! ( iRHS == iLHS ) );
}

int DSASignature::copyFromASNObject(const ASN1T_ALGOS_DssSigValue & iDS)
{
	mR = QString(iDS.r);
	mS = QString(iDS.s);

	return SUCCESS;
}

int DSASignature::copyToASNObject(ASN1T_ALGOS_DssSigValue &oDS)const
{
	oDS.r = myStrDup(mR);
	oDS.s = myStrDup(mS);

	return SUCCESS;
}

void DSASignature::freeASNObject(ASN1T_ALGOS_DssSigValue& oDS)const
{
	DELETE_MEMORY_ARRAY(oDS.r);
	DELETE_MEMORY_ARRAY(oDS.s);
}

const QString & DSASignature::getR() const
{
	return mR;
}

const QString & DSASignature::getS() const 
{
	return mS;
}

void DSASignature::setR(const QString& iR)
{
	mR= iR;
}

void DSASignature::setS(const QString & iS) 
{
	mS= iS;
}


DSASignature::~DSASignature(void)
{
}

QByteArray reverse(const QByteArray & iData)
{
	QByteArray r;
	for (int i = iData.size()-1;i>=0;i-- )
	{
		r.append(iData[i]);
	}
	return r;
}

QByteArray DSASignature::convertToDER(const QByteArray & iP1363)
{
	int l = iP1363.size();
	if (l%2 != 0)
	{
		throw EASNException("Not encoded as P1363");
	}
	
	QByteArray s = iP1363;//reverse(iP1363);
	
	QByteArray bR	= s.left(l/2);
	QByteArray bS	= s.right(l/2);

	
	DSASignature ds(bR,bS);

	return ds.getEncodedBytes();

}

QByteArray DSASignature::convertToP1363(const QByteArray & iDER)
{
	DSASignature ds(iDER);

	QByteArray p1363;

	QString stR= ds.getR();
	QString stS= ds.getS();

	QByteArray r = QByteArray::fromHex(stR.remove("0x").toLocal8Bit());
	QByteArray s = QByteArray::fromHex(stS.remove("0x").toLocal8Bit());

	p1363.append(r);
	p1363.append(s);

	return p1363;

}

}