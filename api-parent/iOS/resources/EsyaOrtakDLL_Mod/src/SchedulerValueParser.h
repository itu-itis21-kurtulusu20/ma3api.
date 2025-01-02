#ifndef _SCHEDULER_VALUE_PARSER_H_
#define _SCHEDULER_VALUE_PARSER_H_

#define SCHEDULE_PATTERN_MONTH_NUM_JAN 0
#define SCHEDULE_PATTERN_MONTH_NUM_FEB 1
#define SCHEDULE_PATTERN_MONTH_NUM_MAR 2
#define SCHEDULE_PATTERN_MONTH_NUM_APR 3
#define SCHEDULE_PATTERN_MONTH_NUM_MAY 4
#define SCHEDULE_PATTERN_MONTH_NUM_JUN 5
#define SCHEDULE_PATTERN_MONTH_NUM_JUL 6
#define SCHEDULE_PATTERN_MONTH_NUM_AUG 7
#define SCHEDULE_PATTERN_MONTH_NUM_SEP 8
#define SCHEDULE_PATTERN_MONTH_NUM_OCT 9
#define SCHEDULE_PATTERN_MONTH_NUM_NOV 10
#define SCHEDULE_PATTERN_MONTH_NUM_DEC 11

#include <QStringList>

#define PARSER_NAME_DAY_OF_MONTH "DayOfMonthValueParser"

#define SCHEDULER_MAX_YEAR_WEAK_COUNT 53

class SchedulerValueParser
{
public:
	SchedulerValueParser(void);
	virtual ~SchedulerValueParser(void);
	virtual int parse(const QString & iStr)=0;
	virtual int getMinValue()=0;
	virtual int getMaxValue()=0;
	virtual QString getParserName(){return "UNDEFINED";};
};

class SimpleSchedulerValueParser:public SchedulerValueParser
{
	int mMinValue;
	int mMaxValue;
public:
	SimpleSchedulerValueParser(int iMinValue,int iMaxValue);
	virtual ~SimpleSchedulerValueParser(void);
	virtual int parse(const QString & iStr);
	void setMaxValue(int iMaxValue){mMaxValue=iMaxValue;};
	virtual int getMinValue();
	virtual int getMaxValue();
};

class MinuteValueParser:public SimpleSchedulerValueParser
{	
public:
	MinuteValueParser();
	virtual  ~MinuteValueParser(void){};	
};

class HourValueParser:public SimpleSchedulerValueParser
{	
public:
	HourValueParser();
	virtual  ~HourValueParser(void){};	
};

class DayOfMonthValueParser:public SimpleSchedulerValueParser
{	
public:
	DayOfMonthValueParser();
	virtual  ~DayOfMonthValueParser(void){};
	virtual int parse(const QString & iStr);
	virtual QString getParserName(){return PARSER_NAME_DAY_OF_MONTH;};
};

class DayOfYearValueParser:public SimpleSchedulerValueParser
{	
public:
	DayOfYearValueParser();
	virtual  ~DayOfYearValueParser(void){};	
};

class MonthValueParser:public SimpleSchedulerValueParser
{	
	QStringList mMonthAliases;
public:
	MonthValueParser();
	virtual  ~MonthValueParser(void){};
	virtual int parse(const QString & iStr);
};

class WeekOfYearValueParser:public SimpleSchedulerValueParser
{		
public:
	WeekOfYearValueParser();
	virtual  ~WeekOfYearValueParser(void){};	
};

class DayOfWeekValueParser:public SimpleSchedulerValueParser
{	
	QStringList mDayAliases;
public:
	DayOfWeekValueParser();
	virtual  ~DayOfWeekValueParser(void){};
	virtual int parse(const QString & iStr);
};

#endif