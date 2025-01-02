#include "SchedulerValueMatcher.h"
#include "SchedulerValueParser.h"

SchedulerValueMatcher::SchedulerValueMatcher(void)
{
}

SchedulerValueMatcher::~SchedulerValueMatcher(void)
{
}

AlwaysTrueValueMatcher::AlwaysTrueValueMatcher()
{
}

AlwaysTrueValueMatcher::~AlwaysTrueValueMatcher(void)
{
}

bool AlwaysTrueValueMatcher::match(int iValue)
{
	return true;
}

IntArrayValueMatcher::IntArrayValueMatcher(QList<int> iValueList)
:mValueList(iValueList)
{
}

IntArrayValueMatcher::~IntArrayValueMatcher(void)
{
}

bool IntArrayValueMatcher::match(int iValue)
{
	if (mValueList.contains(iValue))
	{
		return true;
	}
	return false;	
}

DayOfMonthValueMatcher::DayOfMonthValueMatcher(QList<int> iValueList)
:IntArrayValueMatcher(iValueList)
{
	mLastDays<<31
			 <<28
			 <<31
			 <<30
			 <<31
			 <<30
			 <<31
			 <<31
			 <<30
			 <<31
			 <<30
			 <<31;
}

DayOfMonthValueMatcher::~DayOfMonthValueMatcher(void)
{
}

bool DayOfMonthValueMatcher::_isLastDayOfMonth(int iValue, int iMonth, bool iIsLeapYear) 
{
	if (iIsLeapYear && iMonth == 2) 
	{
		return iValue == 29;
	}
	else
	{
		return iValue == mLastDays[iMonth - 1];
	}
}

bool DayOfMonthValueMatcher::match(int iValue,int iMonth, bool iIsLeapYear)
{
	bool retValue =  (IntArrayValueMatcher::match(iValue) ||
			(iValue >27 && IntArrayValueMatcher::match(32) && _isLastDayOfMonth(iValue,iMonth,iIsLeapYear)));
	return retValue;
}

void IntArrayValueMatcher::addValueToEachElement(int iStartValue,SchedulerValueParser * ipParser)
{
	QList<int> newValues;
	for (int k=0;k<mValueList.size();k++)
	{
		if (k==0)
		{
			newValues<<iStartValue;
			continue;
		}				
		int newValue = newValues.last()+(mValueList.at(k)-mValueList.at(k-1));
		newValue = newValue % ipParser->getMaxValue();
		if (!newValues.contains(newValue))
		{
			newValues<<newValue;
		}		
	}
	mValueList = newValues;
}