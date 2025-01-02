#include "SchedulingPattern.h"
#include "EException.h"
#include "InvalidPatternException.h"

using namespace esya;

SchedulingPattern::SchedulingPattern(const QString & iPatternAsString)
:mPatternAsString(iPatternAsString),mMatcherSize(0)
{
	_initPattern();
}

SchedulingPattern::~SchedulingPattern(void)
{
}

void SchedulingPattern::_initPattern()
{
	QStringList subPatternList = mPatternAsString.split(SCHEDULE_PATTERN_COMBINE_CHARACTER);
	if (subPatternList.isEmpty())
	{
		throw InvalidPatternException("Invalid Pattern <"+mPatternAsString+">");
	}

	QDateTime curDt = QDateTime::currentDateTime();

	Q_FOREACH(QString localPattern,subPatternList)
	{
		QStringList localElementList = localPattern.split(SCHEDULE_PATTERN_ELEMENT_PARSE_CHARACTER);
		if (localElementList.size()!=SCHEDULE_PATTERN_ELEMENT_SIZE)
		{
			throw InvalidPatternException("Invalid Pattern <"+localPattern+">");
		}

		//Construct Minute Matcher List
		SchedulerValueParser * pMinuteValueParser = new MinuteValueParser();
		try
		{	
			QString minituElementStr = localElementList[0];
			SchedulerValueMatcher * pMatcher = _buildValueMatcher(minituElementStr,pMinuteValueParser);
			if (minituElementStr.contains("/"))
			{
				int minute = curDt.time().minute();
				IntArrayValueMatcher * pLocalIntArrayMatcher = (IntArrayValueMatcher *)pMatcher;
				if (pLocalIntArrayMatcher)
				{
					pLocalIntArrayMatcher->addValueToEachElement(minute,pMinuteValueParser);
				}				
			}
			mpMinuteMatcherList.append(pMatcher);
			DELETE_MEMORY(pMinuteValueParser);
		}		
		catch (EException &exc)
		{
			DELETE_MEMORY(pMinuteValueParser);
			throw InvalidPatternException("Invalid Pattern <"+mPatternAsString+"> Error parsing minutes field.");
		}

		//Construct Hour Matcher List
		SchedulerValueParser * pHourValueParser = new HourValueParser();
		try
		{			
			QString hourElementStr = localElementList[1];
			SchedulerValueMatcher * pMatcher = _buildValueMatcher(hourElementStr,pHourValueParser);
			if (hourElementStr.contains("/"))
			{
				int localHour = curDt.time().hour();
				IntArrayValueMatcher * pLocalIntArrayMatcher = (IntArrayValueMatcher *)pMatcher;
				if (pLocalIntArrayMatcher)
				{
					pLocalIntArrayMatcher->addValueToEachElement(localHour,pHourValueParser);
				}				
			}
			mpHourMatcherList.append(pMatcher);
			DELETE_MEMORY(pHourValueParser);
		}		
		catch (EException &exc)
		{
			DELETE_MEMORY(pHourValueParser);
			throw InvalidPatternException("Invalid Pattern <"+mPatternAsString+"> Error parsing hours field.");
		}

		//Construct Day Of Month Matcher List
		SchedulerValueParser * pDayOfMonthValueParser = new DayOfMonthValueParser();
		try
		{			
			SchedulerValueMatcher * pMatcher = _buildValueMatcher(localElementList[2],pDayOfMonthValueParser);
			mpDayOfMonthMatcherList.append(pMatcher);
			DELETE_MEMORY(pDayOfMonthValueParser);
		}		
		catch (EException &exc)
		{
			DELETE_MEMORY(pDayOfMonthValueParser);
			throw InvalidPatternException("Invalid Pattern <"+mPatternAsString+"> Error parsing day of month field.");
		}

		//Construct Month Matcher List
		SchedulerValueParser * pMonthValueParser = new MonthValueParser();
		try
		{			
			SchedulerValueMatcher * pMatcher = _buildValueMatcher(localElementList[3],pMonthValueParser);
			mpMonthMatcherList.append(pMatcher);
			DELETE_MEMORY(pMonthValueParser);
		}		
		catch (EException &exc)
		{
			DELETE_MEMORY(pMonthValueParser);
			throw InvalidPatternException("Invalid Pattern <"+mPatternAsString+"> Error parsing month field.");
		}		

		//Construct Day Of Week Matcher List
		SchedulerValueParser * pDayOfWeekValueParser = new DayOfWeekValueParser();
		try
		{			
			SchedulerValueMatcher * pMatcher = _buildValueMatcher(localElementList[4],pDayOfWeekValueParser);
			mpDayOfWeekMatcherList.append(pMatcher);
			DELETE_MEMORY(pDayOfWeekValueParser);
		}		
		catch (EException &exc)
		{
			DELETE_MEMORY(pDayOfWeekValueParser);
			throw InvalidPatternException("Invalid Pattern <"+mPatternAsString+"> Error parsing day of week field.");
		}

		//Week Of Year Matcher List
		SchedulerValueParser * pWeekOfYearParser = new WeekOfYearValueParser();
		try
		{			
			QString dayOfYearElementStr = localElementList[5]; 
			SchedulerValueMatcher * pMatcher = _buildValueMatcher(dayOfYearElementStr,pWeekOfYearParser);
			if (dayOfYearElementStr.contains("/"))
			{
				int localDay = curDt.date().day();
				IntArrayValueMatcher * pLocalIntArrayMatcher = (IntArrayValueMatcher *)pMatcher;
				if (pLocalIntArrayMatcher)
				{
					pLocalIntArrayMatcher->addValueToEachElement(localDay,pWeekOfYearParser);
				}				
			}
			mpWeekOfYearMatcherList.append(pMatcher);
			DELETE_MEMORY(pWeekOfYearParser);
		}		
		catch (EException &exc)
		{
			DELETE_MEMORY(pWeekOfYearParser);
			throw InvalidPatternException("Invalid Pattern <"+mPatternAsString+"> Error parsing week of year field.");
		}

		//Day Of Year Matcher List
		SchedulerValueParser * pDayOfYearParser = new DayOfYearValueParser();
		try
		{			
			SchedulerValueMatcher * pMatcher = _buildValueMatcher(localElementList[6],pDayOfYearParser);
			mpDayOfYearMatcherList.append(pMatcher);
			DELETE_MEMORY(pDayOfYearParser);
		}		
		catch (EException &exc)
		{
			DELETE_MEMORY(pDayOfYearParser);
			throw InvalidPatternException("Invalid Pattern <"+mPatternAsString+"> Error parsing day of year field.");
		}
		mMatcherSize++;
	}	
}

QList<int> SchedulingPattern::_parseRange(const QString & iStr,SchedulerValueParser * ipParser)
{
	if (iStr.compare(SCHEDULE_PATTERN_ELEMENT_ALL_CHARACTER)==0)
	{
		int min = ipParser->getMinValue();
		int max = ipParser->getMaxValue();
		QList<int> retList;
		for (int i=min;i<=max;i++)
		{
			retList<<i;
		}
		return retList;
	}
	QStringList valueList = iStr.split(SCHEDULE_PATTERN_ELEMENT_RANGE_CHARACTER);
	if (valueList.size()<1  || valueList.size()>2)
	{
		throw EException("Syntax Error");
	}

	QString listValue1Str=valueList[0];
	int intValue1;
	try
	{
		intValue1 = ipParser->parse(listValue1Str);
	}		
	catch (EException &exc)
	{
		throw exc.append("Invalid Value "+listValue1Str);
	}
	if (valueList.size()==1)
	{
		QList<int> retList;
		retList<<intValue1;
		return retList;
	}
	else
	{
		QString listValue2Str=valueList[1];
		int intValue2;
		try
		{
			intValue2 = ipParser->parse(listValue2Str);
		}		
		catch (EException &exc)
		{
			throw exc.append("Invalid Value "+listValue2Str);
		}
		QList<int> retValueList;
		if (intValue1<intValue2)
		{
			for(int i=intValue1;i<=intValue2;i++)
			{
				retValueList<<i;
			}
		}
		else if(intValue1>intValue2)
		{
			int min = ipParser->getMinValue();
			int max = ipParser->getMaxValue();
			for (int i = intValue1; i <= max; i++) 
			{
				retValueList<<i;				
			}
			for (int i = min; i <= intValue2; i++) 
			{
				retValueList<<i;							
			}
		}
		else
		{// v1 == v2
			retValueList<<intValue1;
		}
		return retValueList;
	}	
}

QList<int> SchedulingPattern::_parseListElement(const QString & iElementStr,SchedulerValueParser * ipParser)
{
	QStringList list = iElementStr.split("/");
	if (list.size()<1 || list.size()>2)
	{
		throw EException("Syntax Error");
	}
	QList<int> values;
	try
	{
		values = _parseRange(list.at(0),ipParser);
	}	
	catch (EException &exc)
	{
		throw exc.append("Invalid Range");
	}
	if (list.size()==2)
	{
		QString dStr = list.at(1);
		int div;
		bool ok;
		div = dStr.toInt(&ok);
		if (!ok)
		{
			throw EException("invalid divisor. "+dStr);
		}
		if (div<1)
		{
			throw EException("Non positive divisor. "+dStr);
		}
		QList<int> values2;		
		for (int i = 0; i < values.size(); i += div) 
		{
			values2<<values.at(i);
		}
		return values2;
	}
	else
	{	
		return values;
	}
}

SchedulerValueMatcher * SchedulingPattern::_buildValueMatcher(const QString & iStr,SchedulerValueParser * ipParser)
{	
	if (iStr.length() == 1 && (iStr.compare(SCHEDULE_PATTERN_ELEMENT_ALL_CHARACTER)==0))
	{
		return new AlwaysTrueValueMatcher();
	}

	QList<int> valueList;
	QStringList subValues = iStr.split(SCHEDULE_PATTERN_ELEMENT_SUBLIST_CHARACTER);
	Q_FOREACH(QString subValue,subValues)
	{
		QList<int> localList;
		try
		{
			localList = _parseListElement(subValue,ipParser);
		}		
		catch (EException &exc)
		{
			exc.append("invalid field <" + iStr
				+ ">, invalid element <" + subValue + ">");				
		}
		for (int k=0;k<localList.size();k++)
		{
			int localValue = localList.at(k);
			if (!valueList.contains(localValue))
			{
				valueList<<localValue;
			}
		}
	}
	if (valueList.isEmpty())
	{
		throw EException("invalid field <" + iStr + ">");
	}

	if (ipParser->getParserName() == PARSER_NAME_DAY_OF_MONTH)
	{
		return new DayOfMonthValueMatcher(valueList);
	}	
	else 
	{
		return new IntArrayValueMatcher(valueList);
	}
}

bool SchedulingPattern::match(long iMilis)
{
	QDateTime dateTimeValue;
	dateTimeValue.setTime_t(iMilis);

	QTime timeValue = dateTimeValue.time();
	int minute = timeValue.minute();
	int hour = timeValue.hour();

	QDate dateValue = dateTimeValue.date();
	int dayOfMonth = dateValue.day();
	int month = dateValue.month();
	int dayOfWeek = dateValue.dayOfWeek();
	int year = dateValue.year();
	int weekOfYear = dateValue.weekNumber();
	int dayOfYear = dateValue.dayOfYear();

	for (int i = 0; i < mMatcherSize; i++) 
	{
		SchedulerValueMatcher * pMinuteMatcher = (SchedulerValueMatcher *)mpMinuteMatcherList.at(i);
		SchedulerValueMatcher * pHourMatcher = (SchedulerValueMatcher *)mpHourMatcherList.at(i);
		SchedulerValueMatcher * pDayOfMonthMatcher = (SchedulerValueMatcher *)mpDayOfMonthMatcherList.at(i);
		SchedulerValueMatcher * pMonthMatcher = (SchedulerValueMatcher *)mpMonthMatcherList.at(i);
		SchedulerValueMatcher * pDayOfWeekMatcher = (SchedulerValueMatcher *)mpDayOfWeekMatcherList.at(i);
		SchedulerValueMatcher * pWeekOfWeekMatcher = (SchedulerValueMatcher *)mpWeekOfYearMatcherList.at(i);
		SchedulerValueMatcher * pDayOfYearMatcher = (SchedulerValueMatcher *)mpDayOfYearMatcherList.at(i);

		bool eval;
		eval = pMinuteMatcher->match(minute) &&
			   pHourMatcher->match(hour) &&			   
			   pMonthMatcher->match(month) &&
			   pDayOfWeekMatcher->match(dayOfWeek) &&
			   pDayOfYearMatcher->match(dayOfYear) &&
			   pWeekOfWeekMatcher->match(weekOfYear);
		if (eval)
		{
			DayOfMonthValueMatcher * pDOMMatcher = dynamic_cast<DayOfMonthValueMatcher*>(pDayOfMonthMatcher);
			if (pDOMMatcher)
			{
				eval = pDOMMatcher->match(dayOfMonth, month,QDate::isLeapYear(year));
			}
			else
			{
				eval = pDayOfMonthMatcher->match(month);
			}			
		}
		if (eval) 
		{
			return true;
		}
	}
	return false;
}
