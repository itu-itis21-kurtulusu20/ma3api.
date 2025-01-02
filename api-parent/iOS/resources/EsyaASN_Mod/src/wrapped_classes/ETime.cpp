#include "ETime.h"

using namespace esya;
NAMESPACE_BEGIN(esya)
ETime::ETime(void)
{
}

ETime::ETime(int iType, const QString & iTime )
:mType(iType),mTime(iTime)
{
}

ETime::ETime(const QByteArray & iTime)
{
	constructObject(iTime);
}

ETime::ETime(const ASN1T_EXP_Time & iTime)
{
	copyFromASNObject(iTime);
}

ETime::ETime(const ETime& iTime)
: mTime(iTime.getTime()),mType(iTime.getType())
{
}

ETime & ETime::operator=(const ETime & iTime)
{
	mTime = iTime.getTime();
	mType = iTime.getType();
	return *this;
}

bool operator==(const ETime & iRHS,const ETime & iLHS)
{
	return ( iRHS.getType() == iLHS.getType() && iRHS.getTime()==iLHS.getTime() );
}

bool operator!=(const ETime & iRHS ,const ETime & iLHS)
{
	return !( iRHS == iLHS );
}
	

QString ETime::getTime()const
{
	return mTime;
}

int ETime::getType()const
{
	return mType;
}

int ETime::copyFromASNObject(const ASN1T_EXP_Time &iTime)
{
	mType = iTime.t;
	mTime = QString(iTime.u.generalTime);
	return SUCCESS;
}

int ETime::copyToASNObject(ASN1T_EXP_Time &oTime)const
{
	oTime.t = mType;

	if (mType == T_EXP_Time_generalTime )
	{
		oTime.u.generalTime = myStrDup(mTime);
	}
	else 
	{
		oTime.u.utcTime = myStrDup(mTime);		
	}
	return SUCCESS;
}

void ETime::freeASNObject(ASN1T_EXP_Time & oTime)const
{
	if (oTime.t == T_EXP_Time_generalTime )
	{
		DELETE_MEMORY_ARRAY( oTime.u.generalTime )
	}
	else if (oTime.t == T_EXP_Time_utcTime )
	{
		DELETE_MEMORY_ARRAY(oTime.u.utcTime)
	}
}

QString ETime::toString(const QString & iFormat ) const
{
	return toDateTime().toString(iFormat); 
}

ETime::ETime(const QDateTime& iDT)
{
	*this = ETime::fromDateTime(iDT);
}

ETime ETime::fromDateTime(const QDateTime& iDT) 
{
	ETime t;
	t.mType = T_EXP_Time_generalTime;

	QString st = iDT.toString("yyyyMMddhhmmss");
	st.append("Z");

	t.mTime = st;
	return t;
}

QDateTime ETime::toDateTime()const
{
	QString ctime;
	switch (mType)
	{
	case T_EXP_Time_generalTime:
		{
			ctime = mTime;
			break;
		}
	case T_EXP_Time_utcTime:
		{
			if (mTime.left(2).toInt()<50)
				ctime = QString("20%1").arg(mTime);
			else
				ctime = QString("19%1").arg(mTime);
			break;
		}
	}

	long delay = 0 ;

	if ( ctime.right(1) != "Z")
	{
		QString delayHour = ctime.right(4).left(2);
		QString delayMin = ctime.right(4).right(2);
		QString sign = ctime.right(5).left(1);

		delay	=  delayHour.toInt()*3600 + delayMin.toInt()*60 ;
		delay = (sign == "-") ? delay:-delay;

		ctime = ctime.left(ctime.length()-5);
	}
	else ctime.remove("Z");

	int dotIndex = ctime.indexOf(".");
	int len = ctime.size();

	if (dotIndex<0 )
		ctime += ".000";
	else
	{
		for(int i = dotIndex ;i<dotIndex+3; i++)
		{
			if (i>=len)
				ctime.append("0");
		}
	}
	QDateTime dt =  QDateTime::fromString(ctime,"yyyyMMddhhmmss.zzz");
	dt = dt.addSecs(delay);
	dt.setTimeSpec(Qt::UTC);
	dt = dt.toLocalTime();
	return dt;
}


ETime::~ETime(void)
{
}
NAMESPACE_END
