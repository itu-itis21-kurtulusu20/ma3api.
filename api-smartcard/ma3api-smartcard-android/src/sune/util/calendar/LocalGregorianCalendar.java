/*
 * Copyright 2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package sune.util.calendar;

import sune.security.action.GetPropertyAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 *
 * @author Masayoshi Okutsu
 * @since 1.6
 */

public class LocalGregorianCalendar extends sune.util.calendar.BaseCalendar {
    private String name;
    private sune.util.calendar.Era[] eras;

    public static class Date extends BaseCalendar.Date {

        protected Date() {
            super();
        }

        protected Date(TimeZone zone) {
            super(zone);
        }

        private int gregorianYear = FIELD_UNDEFINED;

        public Date setEra(sune.util.calendar.Era era) {
            if (getEra() != era) {
                super.setEra(era);
                gregorianYear = FIELD_UNDEFINED;
            }
            return this;
        }

        public Date addYear(int localYear) {
            super.addYear(localYear);
            gregorianYear += localYear;
            return this;
        }

        public Date setYear(int localYear) {
            if (getYear() != localYear) {
                super.setYear(localYear);
                gregorianYear = FIELD_UNDEFINED;
            }
            return this;
        }

        public int getNormalizedYear() {
            return gregorianYear;
        }

        public void setNormalizedYear(int normalizedYear) {
            this.gregorianYear = normalizedYear;
        }

        void setLocalEra(sune.util.calendar.Era era) {
            super.setEra(era);
        }

        void setLocalYear(int year) {
            super.setYear(year);
        }

        public String toString() {
            String time = super.toString();
            time = time.substring(time.indexOf('T'));
            StringBuffer sb = new StringBuffer();
            sune.util.calendar.Era era = getEra();
            if (era != null) {
                String abbr = era.getAbbreviation();
                if (abbr != null) {
                    sb.append(abbr);
                }
            }
            sb.append(getYear()).append('.');
            sune.util.calendar.CalendarUtils.sprintf0d(sb, getMonth(), 2).append('.');
            sune.util.calendar.CalendarUtils.sprintf0d(sb, getDayOfMonth(), 2);
            sb.append(time);
            return sb.toString();
        }
    }

    static LocalGregorianCalendar getLocalGregorianCalendar(String name) {
        Properties calendarProps = null;
        try {
            String homeDir = AccessController.doPrivileged(
                new GetPropertyAction("java.home"));
            final String fname = homeDir + File.separator + "lib" + File.separator
                                 + "calendars.properties";
            calendarProps = (Properties) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                    Properties props = new Properties();
                    props.load(new FileInputStream(fname));
                    return props;
                }
            });
        } catch (PrivilegedActionException e) {
            throw new RuntimeException(e.getException());
        }

        // Parse calendar.*.eras
        String props = calendarProps.getProperty("calendar." + name + ".eras");
        if (props == null) {
            return null;
        }
        List<sune.util.calendar.Era> eras = new ArrayList<sune.util.calendar.Era>();
        StringTokenizer eraTokens = new StringTokenizer(props, ";");
        while (eraTokens.hasMoreTokens()) {
            String items = eraTokens.nextToken().trim();
            StringTokenizer itemTokens = new StringTokenizer(items, ",");
            String eraName = null;
            boolean localTime = true;
            long since = 0;
            String abbr = null;

            while (itemTokens.hasMoreTokens()) {
                String item = itemTokens.nextToken();
                int index = item.indexOf('=');
                // it must be in the key=value form.
                if (index == -1) {
                    return null;
                }
                String key = item.substring(0, index);
                String value = item.substring(index + 1);
                if ("name".equals(key)) {
                    eraName = value;
                } else if ("since".equals(key)) {
                    if (value.endsWith("u")) {
                        localTime = false;
                        since = Long.parseLong(value.substring(0, value.length() - 1));
                    } else {
                        since = Long.parseLong(value);
                    }
                } else if ("abbr".equals(key)) {
                    abbr = value;
                } else {
                    throw new RuntimeException("Unknown key word: " + key);
                }
            }
            sune.util.calendar.Era era = new sune.util.calendar.Era(eraName, abbr, since, localTime);
            eras.add(era);
        }
        sune.util.calendar.Era[] eraArray = new sune.util.calendar.Era[eras.size()];
        eras.toArray(eraArray);

        return new LocalGregorianCalendar(name, eraArray);
    }

    private LocalGregorianCalendar(String name, sune.util.calendar.Era[] eras) {
        this.name = name;
        this.eras = eras;
        setEras(eras);
    }

    public String getName() {
        return name;
    }

    public Date getCalendarDate() {
        return getCalendarDate(System.currentTimeMillis(), newCalendarDate());
    }

    public Date getCalendarDate(long millis) {
        return getCalendarDate(millis, newCalendarDate());
    }

    public Date getCalendarDate(long millis, TimeZone zone) {
        return getCalendarDate(millis, newCalendarDate(zone));
    }

    public Date getCalendarDate(long millis, sune.util.calendar.CalendarDate date) {
        Date ldate = (Date) super.getCalendarDate(millis, date);
        return adjustYear(ldate, millis, ldate.getZoneOffset());
    }

    private Date adjustYear(Date ldate, long millis, int zoneOffset) {
        int i;
        for (i = eras.length - 1; i >= 0; --i) {
            sune.util.calendar.Era era = eras[i];
            long since = era.getSince(null);
            if (era.isLocalTime()) {
                since -= zoneOffset;
            }
            if (millis >= since) {
                ldate.setLocalEra(era);
                int y = ldate.getNormalizedYear() - era.getSinceDate().getYear() + 1;
                ldate.setLocalYear(y);
                break;
            }
        }
        if (i < 0) {
            ldate.setLocalEra(null);
            ldate.setLocalYear(ldate.getNormalizedYear());
        }
        ldate.setNormalized(true);
        return ldate;
    }

    public Date newCalendarDate() {
        return new Date();
    }

    public Date newCalendarDate(TimeZone zone) {
        return new Date(zone);
    }

    public boolean validate(sune.util.calendar.CalendarDate date) {
        Date ldate = (Date) date;
        sune.util.calendar.Era era = ldate.getEra();
        if (era != null) {
            if (!validateEra(era)) {
                return false;
            }
            ldate.setNormalizedYear(era.getSinceDate().getYear() + ldate.getYear());
        } else {
            ldate.setNormalizedYear(ldate.getYear());
        }
        return super.validate(ldate);
    }

    private boolean validateEra(sune.util.calendar.Era era) {
        // Validate the era
        for (int i = 0; i < eras.length; i++) {
            if (era == eras[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean normalize(sune.util.calendar.CalendarDate date) {
        if (date.isNormalized()) {
            return true;
        }

        normalizeYear(date);
        Date ldate = (Date) date;

        // Normalize it as a Gregorian date and get its millisecond value
        super.normalize(ldate);

        boolean hasMillis = false;
        long millis = 0;
        int year = ldate.getNormalizedYear();
        int i;
        sune.util.calendar.Era era = null;
        for (i = eras.length - 1; i >= 0; --i) {
            era = eras[i];
            if (era.isLocalTime()) {
                sune.util.calendar.CalendarDate sinceDate = era.getSinceDate();
                int sinceYear = sinceDate.getYear();
                if (year > sinceYear) {
                    break;
                }
                if (year == sinceYear) {
                    int month = ldate.getMonth();
                    int sinceMonth = sinceDate.getMonth();
                    if (month > sinceMonth) {
                        break;
                    }
                    if (month == sinceMonth) {
                        int day = ldate.getDayOfMonth();
                        int sinceDay = sinceDate.getDayOfMonth();
                        if (day > sinceDay) {
                            break;
                        }
                        if (day == sinceDay) {
                            long timeOfDay = ldate.getTimeOfDay();
                            long sinceTimeOfDay = sinceDate.getTimeOfDay();
                            if (timeOfDay >= sinceTimeOfDay) {
                                break;
                            }
                            --i;
                            break;
                        }
                    }
                }
            } else {
                if (!hasMillis) {
                    millis  = super.getTime(date);
                    hasMillis = true;
                }

                long since = era.getSince(date.getZone());
                if (millis >= since) {
                    break;
                }
            }
        }
        if (i >= 0) {
            ldate.setLocalEra(era);
            int y = ldate.getNormalizedYear() - era.getSinceDate().getYear() + 1;
            ldate.setLocalYear(y);
        } else {
            // Set Gregorian year with no era
            ldate.setEra(null);
            ldate.setLocalYear(year);
            ldate.setNormalizedYear(year);
        }
        ldate.setNormalized(true);
        return true;
    }

    void normalizeMonth(sune.util.calendar.CalendarDate date) {
        normalizeYear(date);
        super.normalizeMonth(date);
    }

    void normalizeYear(sune.util.calendar.CalendarDate date) {
        Date ldate = (Date) date;
        // Set the supposed-to-be-correct Gregorian year first
        // e.g., Showa 90 becomes 2015 (1926 + 90 - 1).
        sune.util.calendar.Era era = ldate.getEra();
        if (era == null || !validateEra(era)) {
            ldate.setNormalizedYear(ldate.getYear());
        } else {
            ldate.setNormalizedYear(era.getSinceDate().getYear() + ldate.getYear() - 1);
        }
    }

    /**
     * Returns whether the specified Gregorian year is a leap year.
     * @see #isLeapYear(sune.util.calendar.Era, int)
     */
    public boolean isLeapYear(int gregorianYear) {
        return CalendarUtils.isGregorianLeapYear(gregorianYear);
    }

    public boolean isLeapYear(Era era, int year) {
        if (era == null) {
            return isLeapYear(year);
        }
        int gyear = era.getSinceDate().getYear() + year - 1;
        return isLeapYear(gyear);
    }

    public void getCalendarDateFromFixedDate(CalendarDate date, long fixedDate) {
        Date ldate = (Date) date;
        super.getCalendarDateFromFixedDate(ldate, fixedDate);
        adjustYear(ldate, (fixedDate - EPOCH_OFFSET) * DAY_IN_MILLIS, 0);
    }
}
