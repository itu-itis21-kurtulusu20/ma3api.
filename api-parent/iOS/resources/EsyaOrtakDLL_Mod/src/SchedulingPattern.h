#ifndef _SCHEDULING_PATTERN_H_
#define _SCHEDULING_PATTERN_H_

#define SCHEDULE_PATTERN_COMBINE_CHARACTER "|"
#define SCHEDULE_PATTERN_ELEMENT_PARSE_CHARACTER " "
#define SCHEDULE_PATTERN_ELEMENT_ALL_CHARACTER "*"
#define SCHEDULE_PATTERN_ELEMENT_SUBLIST_CHARACTER ","
#define SCHEDULE_PATTERN_ELEMENT_RANGE_CHARACTER "-"

#define SCHEDULE_PATTERN_ELEMENT_SIZE 7

#define SCHEDULE_PATTERN_DAY_NUM_SUNDAY		0
#define SCHEDULE_PATTERN_DAY_NUM_MONDAY		1
#define SCHEDULE_PATTERN_DAY_NUM_TUESDAY	2
#define SCHEDULE_PATTERN_DAY_NUM_WEDNESDAY	3
#define SCHEDULE_PATTERN_DAY_NUM_THURSDAY	4
#define SCHEDULE_PATTERN_DAY_NUM_FRIDAY		5
#define SCHEDULE_PATTERN_DAY_NUM_SATURDAY	6


#include <QString>
#include "SchedulerValueMatcher.h"
#include "SchedulerValueParser.h"
#include <QList>
class SchedulingPattern
{
	QString mPatternAsString;
	void _initPattern();
	QList<int> _parseRange(const QString & iStr,SchedulerValueParser * ipParser);
	QList<int> _parseListElement(const QString & iElementStr,SchedulerValueParser * ipParser);
	SchedulerValueMatcher * _buildValueMatcher(const QString & iStr,SchedulerValueParser * ipParser);
public:
	SchedulingPattern(const QString & iPatternAsString);
	virtual ~SchedulingPattern(void);
	bool match(long iMilis);
	int getMatcherSize(){return mMatcherSize;};
private:
	QList<SchedulerValueMatcher *> mpMinuteMatcherList;
	QList<SchedulerValueMatcher *> mpHourMatcherList;
	QList<SchedulerValueMatcher *> mpDayOfMonthMatcherList;
	QList<SchedulerValueMatcher *> mpMonthMatcherList;
	QList<SchedulerValueMatcher *> mpDayOfWeekMatcherList;
	QList<SchedulerValueMatcher *> mpWeekOfYearMatcherList;
	QList<SchedulerValueMatcher *> mpDayOfYearMatcherList;
	int mMatcherSize;
};
#endif