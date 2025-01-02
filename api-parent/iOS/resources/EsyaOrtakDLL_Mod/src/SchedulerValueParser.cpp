#include "SchedulerValueParser.h"
#include "EException.h"

using namespace esya;
int parseAliase(const QString & iAlias,const QStringList & iAliasesList)
{
	if(!iAliasesList.contains(iAlias,Qt::CaseInsensitive))
	{
		throw EException("Unknown Aliase");
	}
	return iAliasesList.indexOf(iAlias);	
}

SchedulerValueParser::SchedulerValueParser(void)
{
}

SchedulerValueParser::~SchedulerValueParser(void)
{
}


SimpleSchedulerValueParser::SimpleSchedulerValueParser(int iMinValue,int iMaxValue)
:mMaxValue(iMaxValue),mMinValue(iMinValue)
{
}

SimpleSchedulerValueParser::~SimpleSchedulerValueParser()
{
}

int SimpleSchedulerValueParser::parse(const QString & iStr)
{
	int intValue;
	bool ok;
	intValue = iStr.toInt(&ok);
	if (!ok)
	{
		throw new EException("Invalid Integer Value");
	}
	if (intValue<mMinValue || intValue>mMaxValue)
	{
		throw new EException("Value Out Of Range");
	}
	return intValue;
}

int SimpleSchedulerValueParser::getMinValue()
{
	return mMinValue;
}

int SimpleSchedulerValueParser::getMaxValue()
{
	return mMaxValue;
}

MinuteValueParser::MinuteValueParser()
:SimpleSchedulerValueParser(0,59)
{
}

HourValueParser::HourValueParser()
:SimpleSchedulerValueParser(0,23)
{
}

DayOfMonthValueParser::DayOfMonthValueParser()
:SimpleSchedulerValueParser(1,31)
{
}

DayOfYearValueParser::DayOfYearValueParser()
:SimpleSchedulerValueParser(1,366)
{
	QDate dt = QDate::currentDate();
	if(QDate::isLeapYear(dt.year()))
	{
		setMaxValue(366);
	}
	else
	{
		setMaxValue(365);		
	}
}

int DayOfMonthValueParser::parse(const QString & iStr)
{
	if (iStr.compare("L",Qt::CaseInsensitive) == 0)
	{
		return 32;
	}
	else
	{
		return SimpleSchedulerValueParser::parse(iStr);			
	}
}

MonthValueParser::MonthValueParser()
:SimpleSchedulerValueParser(1,12)
{	
	mMonthAliases<<"jan"
				 <<"feb"
				 <<"mar"
				 <<"apr"
				 <<"may"
				 <<"jun"
				 <<"jul"
				 <<"aug"
				 <<"sep"
				 <<"oct"
				 <<"nov"
				 <<"dec";			   
}

int MonthValueParser::parse(const QString & iStr)
{	
	try 
	{
		// try as a simple value
		return SimpleSchedulerValueParser::parse(iStr);
	} catch (EException & exc) 
	{
		return parseAliase(iStr,mMonthAliases);		
	}
}

WeekOfYearValueParser::WeekOfYearValueParser()
:SimpleSchedulerValueParser(1,SCHEDULER_MAX_YEAR_WEAK_COUNT)
{		
}

DayOfWeekValueParser::DayOfWeekValueParser()
:SimpleSchedulerValueParser(0,7)
{	
	mDayAliases<<"sun"
	           <<"mon"
			   <<"tue"
			   <<"wed"
			   <<"thu"
			   <<"fri"
			   <<"sat";			   
}

int DayOfWeekValueParser::parse(const QString & iStr)
{	
	try 
	{
		// try as a simple value
		return SimpleSchedulerValueParser::parse(iStr);
	} catch (EException & exc) 
	{
		return parseAliase(iStr,mDayAliases);		
	}
}
