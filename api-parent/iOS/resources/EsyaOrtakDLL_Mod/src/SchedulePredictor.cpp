#include "SchedulePredictor.h"
#include "SchedulingPattern.h"
#include <QDebug>
#include "EException.h"

SchedulePredictor::SchedulePredictor(const QString & iScheduling,const QDateTime & iStartDateTime)
{		
	mDeletePatternObj=true;
	mpSchedulingPattern = new SchedulingPattern(iScheduling);
	mStartDateTime = iStartDateTime;
}

SchedulePredictor::SchedulePredictor(SchedulingPattern * ipPattern,const QDateTime & iStartDateTime)
{		
	mDeletePatternObj=false;
	mpSchedulingPattern = ipPattern;
	mStartDateTime = iStartDateTime;
}


SchedulePredictor::SchedulePredictor(const QString & iSchedulingStr)
{
	mDeletePatternObj = true;
	mpSchedulingPattern = new SchedulingPattern(iSchedulingStr);
	mStartDateTime = QDateTime::currentDateTime();
}


SchedulePredictor::~SchedulePredictor(void)
{
	if (mDeletePatternObj)
	{
		DELETE_MEMORY(mpSchedulingPattern);
	}	
}

QDateTime SchedulePredictor::nextMatchingTime()
{
	qDebug()<<"Predictor Start Time"<<mStartDateTime.toString();
	QDateTime retDt;
	QDateTime stopDt = mStartDateTime.addYears(1);	
	mStartDateTime=mStartDateTime.addSecs(60);
	if(mpSchedulingPattern->match(mStartDateTime.toTime_t()))	
	{
		retDt = mStartDateTime;		
	}
	if (retDt.isNull())
	{
		while(mStartDateTime<stopDt)
		{
			mStartDateTime=mStartDateTime.addSecs(60);		
			if(mpSchedulingPattern->match(mStartDateTime.toTime_t()))
			{
				retDt = mStartDateTime;			
				break;
			}
		}
	}
	qDebug()<<"Predictor Result Time"<<retDt.toString();
	return retDt;	
}
	/*
	int matcherSize = mpSchedulingPatter->getMatcherSize();
	for (int k=0;k<matcherSize;k++)
	{
		QDateTime time = QDateTime::setTime_t(mTime/1000);
	}
}

	public synchronized long nextMatchingTime() {
		// Go a minute ahead.
		time += 60000;
		// Is it matching?
		if (schedulingPattern.match(time)) {
			return time;
		}
		// Go through the matcher groups.
		int size = schedulingPattern.matcherSize;
		long[] times = new long[size];
		for (int k = 0; k < size; k++) {
			// Ok, split the time!
			GregorianCalendar c = new GregorianCalendar();
			c.setTimeInMillis(time);
			int minute = c.get(Calendar.MINUTE);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
			int month = c.get(Calendar.MONTH);
			int year = c.get(Calendar.YEAR);
			// Gets the matchers.
			ValueMatcher minuteMatcher = (ValueMatcher) schedulingPattern.minuteMatchers.get(k);
			ValueMatcher hourMatcher = (ValueMatcher) schedulingPattern.hourMatchers.get(k);
			ValueMatcher dayOfMonthMatcher = (ValueMatcher) schedulingPattern.dayOfMonthMatchers.get(k);
			ValueMatcher dayOfWeekMatcher = (ValueMatcher) schedulingPattern.dayOfWeekMatchers.get(k);
			ValueMatcher monthMatcher = (ValueMatcher) schedulingPattern.monthMatchers.get(k);
			for (;;) { // day of week
				for (;;) { // month
					for (;;) { // day of month
						for (;;) { // hour
							for (;;) { // minutes
								if (minuteMatcher.match(minute)) {
									break;
								} else {
									minute++;
									if (minute > 59) {
										minute = 0;
										hour++;
									}
								}
							}
							if (hour > 23)  {
								hour = 0;
								dayOfMonth++;
							}
							if (hourMatcher.match(hour)) {
								break;
							} else {
								hour++;
								minute = 0;
							}
						}
						if (dayOfMonth > 31) {
							dayOfMonth = 1;
							month++;
						}
						if (month > Calendar.DECEMBER) {
							month = Calendar.JANUARY;
							year++;
						}
						if (dayOfMonthMatcher instanceof DayOfMonthValueMatcher) {
							DayOfMonthValueMatcher aux = (DayOfMonthValueMatcher) dayOfMonthMatcher;
							if (aux.match(dayOfMonth, month + 1, c.isLeapYear(year))) {
								break;
							} else {
								dayOfMonth++;
								hour = 0;
								minute = 0;
							}
						} else if (dayOfMonthMatcher.match(dayOfMonth)) {
							break;
						} else {
							dayOfMonth++;
							hour = 0;
							minute = 0;
						}
					}
					if (monthMatcher.match(month + 1)) {
						break;
					} else {
						month++;
						dayOfMonth = 1;
						hour = 0;
						minute = 0;
					}
				}
				// Is this ok?
				c = new GregorianCalendar();
				c.set(Calendar.MINUTE, minute);
				c.set(Calendar.HOUR_OF_DAY, hour);
				c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				c.set(Calendar.MONTH, month);
				c.set(Calendar.YEAR, year);
				// Day-of-month/month/year compatibility check.
				int oldDayOfMonth = dayOfMonth;
				int oldMonth = month;
				int oldYear = year;
				dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
				month = c.get(Calendar.MONTH);
				year = c.get(Calendar.YEAR);
				if (month != oldMonth || dayOfMonth != oldDayOfMonth
						|| year != oldYear) {
					// Take another spin!
					continue;
				}
				// Day of week.
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				if (dayOfWeekMatcher.match(dayOfWeek - 1)) {
					break;
				} else {
					dayOfMonth++;
					hour = 0;
					minute = 0;
					if (dayOfMonth > 31) {
						dayOfMonth = 1;
						month++;
						if (month > Calendar.DECEMBER) {
							month = Calendar.JANUARY;
							year++;
						}
					}
				}
			}
			// Seems it matches!
			times[k] = (c.getTimeInMillis() / (1000 * 60)) * 1000 * 60;
		}
		// Which one?
		long min = Long.MAX_VALUE;
		for (int k = 0; k < size; k++) {
			if (times[k] < min) {
				min = times[k];
			}
		}
		// Updates the object current time value.
		time = min;
		// Here it is.
		return time;
	}

	/**
	 * It returns the next matching moment as a {@link Date} object.
	 * 
	 * @return The next matching moment as a {@link Date} object.
	 */
	/*public synchronized Date nextMatchingDate() {
		return new Date(nextMatchingTime());
	}
	}
*/