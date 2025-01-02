#ifndef _SCHEDULER_VALUE_MATCHER_H_
#define _SCHEDULER_VALUE_MATCHER_H_

#include <QList>
class SchedulerValueParser;

class SchedulerValueMatcher
{
public:
	SchedulerValueMatcher(void);
	virtual ~SchedulerValueMatcher(void);
	virtual bool match(int iValue)=0;	
};

class AlwaysTrueValueMatcher:public SchedulerValueMatcher
{
public:
	AlwaysTrueValueMatcher(void);
	virtual ~AlwaysTrueValueMatcher(void);
	bool match(int iValue);
};

class IntArrayValueMatcher:public SchedulerValueMatcher
{
	QList<int> mValueList;
public:
	IntArrayValueMatcher(QList<int> iValueList);
	virtual ~IntArrayValueMatcher(void);
	bool match(int iValue);
	void addValueToEachElement(int iStartValue,SchedulerValueParser * ipParser);
};

class DayOfMonthValueMatcher:public IntArrayValueMatcher
{
	QList<int> mLastDays;
	bool _isLastDayOfMonth(int iValue, int iMonth, bool iIsLeapYear);
public:
	DayOfMonthValueMatcher(QList<int> iValueList);
	virtual ~DayOfMonthValueMatcher(void);	
	bool match(int iValue,int iMonth, bool iIsLeapYear);
};

#endif
