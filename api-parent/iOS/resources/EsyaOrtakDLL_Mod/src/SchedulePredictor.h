#ifndef _SCHEDULE_PREDICTOR_H_
#define _SCHEDULE_PREDICTOR_H_

#include <QDateTime>

class SchedulingPattern;
class Q_DECL_EXPORT SchedulePredictor
{
	SchedulingPattern * mpSchedulingPattern;
	QDateTime mStartDateTime;
	bool mDeletePatternObj;
public:	
	SchedulePredictor(const QString & iSchedulingStr);
	SchedulePredictor(const QString & iScheduling,const QDateTime & iStartDateTime);
	SchedulePredictor(SchedulingPattern * ipSchedulingPattern,const QDateTime & iStartDateTime);
	QDateTime nextMatchingTime();
	virtual ~SchedulePredictor(void);
};

#endif