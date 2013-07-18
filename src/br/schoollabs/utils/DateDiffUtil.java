package br.schoollabs.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;

/**
 * Class to specify a time interval.
 * <p>
 * Useful for specifing "expiration dates", where you
 * may want to change an expiration date from 
 * a number of days to a number of months to a number
 * of years.  
 * <p>
 * If you hard code a "number of days"
 * check in your code, and the requirement changes to expire a value
 * in 1 year, then you can't just specify 365 days, because
 * you have to take leap years into account.
 * <p>
 * Another example is if a requirement changes from "expire in 90 days"
 * to "expire in 3 months". These things are not the same.
 * Three months from March 1st is June 1st, but 90 days from
 * March 1st is May 30th. 
 * <p>
 * When using this class, changing an expiration value is as easy
 * as changing the line:
 * <pre>
 *   TimeInterval ti = new TimeInterval(90,Units.DAY);
 * </pre>
 * To:
 * <pre>
 *   TimeInterval ti = new TimeInterval(3,Units.MONTH);
 * </pre>
 * <p>
 * To do a date comparison using a TimeInterval, use the 
 * static method "dateDiff".
 * 
 * @author Christopher Pierce
 *
 */
@SuppressLint("DefaultLocale")
public class DateDiffUtil {

    /**
     * Enumeration of the valid Units you can use
     * for a time interval.
     * Values are:
     * <ul>
     * <li>YEAR</li>
     * <li>MONTH</li>
     * <li>DAY</li>
     * <li>HOUR</li>
     * <li>MINUTE</li>
     * <li>SECOND</li>
     * </ul>
     */
    public static enum Units { YEAR, MONTH, DAY, HOUR, MINUTE, SECOND }
        
    private int interval;
    private Units units;
    
    /**
     * Simple Constructor.
     * 
     * @param interval The duration of the interval.
     * @param units The units of measure for the duration.
     */
    public DateDiffUtil(int interval, Units units) {
        this.interval = interval;
        this.units = units;
    }
    
    /**
     * Convenience constructor for
     * setting the interval as a string.
     * <p>
     * Calls the "setIntervalAsString" method.
     * <p>
     * 
     * @see #setIntervalAsString(java.lang.String)
     * 
     * @param intervalStr The Interval expressed as a String value.
     */
    
    public DateDiffUtil(String intervalStr) {
        this.setIntervalAsString(intervalStr);
    }
    
    
    /**
     * The Duration of the interval.
     * The actual significance of this value
     * is dependent upon the Units.
     * 
     * @return Numeric part of the duration.
     */
    public int getInterval() {
        return interval;
    }

    
    /**
     * The Duration of the interval.
     * The actual significance of this value
     * is dependent upon the Units.
     * 
     * @param interval Numeric part of the duration.
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * The Unit of Measure for the interval.
     * Specifies how to interpret the interval
     * value (days, years, etc.)
     * 
     * @return An instance of the enum Units.
     */
    public Units getUnits() {
        return units;
    }

    /**
     * The Unit of Measure for the interval.
     * Specifies how to interpret the interval
     * value (days, years, etc.)
     * 
     * @param units An instance of the enum Units.
     */
    public void setUnits(Units units) {
        this.units = units;
    }
    
    
    /**
     * Convenience method to set the interval as a string.
     * <p>
     * Useful for specifing time intervals in configuration
     * files, so you can specify something like:
     * <pre>
     * some.timeinterval=5 days
     * other.timeinterval=1 year
     * </pre>
     * You would then pass the the string "5 days" or
     * "1 year" into this method.
     * <p>
     * The intervalStr value should be an integer, followed
     * by a a unit of measurement name.  Putting white space(s)
     * between the integer an unit is optional.
     * <p>
     * Below are the valid values for the the unit of measurement names.
     * All value checks are case insensitive except where noted.
     * <pre>
     * YEAR = "year", "years", "y"
     * MONTH = "month", "months", "M" (The "M" is a case sensitive check)
     * DAY = "day", "days", "d"
     * HOUR = "hour", "hours", "h"
     * MINUTE = "minute", "minutes", "m" (The "m" is a case sensitive check)
     * SECONDS = "second", "seconds", "s"
     * </pre>
     * 
     * <p>
     * Some example values:
     * <pre>
     * 1 year, 2 years, 3 y, 4y, 5years
     * 1 month, 2 months, 3 M, 4M, 5months
     * 1 day, 2 days, 3 d, 4d, 5days
     * 1 hour, 2 hours, 3 h, 4h, 5hours
     * 1 minute, 2 minutes, 3 m, 4m, 5minutes
     * 1 second, 2 seconds, 3 s, 4s, 5seconds
     * </pre>
     * 
     * @param intervalStr The Interval expressed as a String value.
     */
    public void setIntervalAsString(String intervalStr) {
        
        if(intervalStr == null) {
            throw new IllegalArgumentException("Null value passed to TimeInterval constructor");
        }
        intervalStr = intervalStr.trim();
        
        StringBuffer numberPart = new StringBuffer();
        char[] intervalCharArray = intervalStr.toCharArray();
        int idx=0;
        for(idx=0; idx < intervalCharArray.length; ++idx) {
            if(Character.isDigit(intervalCharArray[idx])) {
                numberPart.append(intervalCharArray[idx]);
            }
            else {
                break;
            }
        }
        
        try {
            this.interval = Integer.valueOf(numberPart.toString());
        }
        catch(NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid integer passed to TimeInterval constructor",ex);
        }
        
        if(idx >= intervalStr.length()) {
            // no units where specified, default to days
            this.units = Units.DAY;
        }
        else {
            // lets be robust about how units is specified.
            // These are the valid values
            // (all values are case insesitive except where noted):
            
            // YEAR = "year", "years", "y"
            // MONTH = "month", "months", "M" (case sensitive)
            // DAY = "day", "days", "d"
            // HOUR = "hour", "hours", "h"
            // MINUTE = "minute", "minutes", "m" (case sensive)
            // SECOND = "second", "seconds", "s"
            
            intervalStr = intervalStr.substring(idx).trim();
            
            char firstChar = intervalStr.charAt(0);
            intervalStr = intervalStr.toLowerCase();
            
            if(intervalStr.startsWith("y")) {
                this.units = Units.YEAR;
            }
            else if(intervalStr.startsWith("d")) {
                this.units = Units.DAY;
            }
            else if(intervalStr.startsWith("h")) {
                this.units = Units.HOUR;
            }
            else if(intervalStr.startsWith("s")) {
                this.units = Units.SECOND;
            }
            else if(intervalStr.startsWith("month") ||
                    (!intervalStr.startsWith("minute") && firstChar == 'M')) {
                 this.units = Units.MONTH;
            }
            else if(intervalStr.startsWith("minute") ||
                    (!intervalStr.startsWith("month") && firstChar == 'm')) {
                 this.units = Units.MINUTE;
            }
            else {
                throw new IllegalArgumentException("Invalid interval passed to TimeInterval constructor");
            }
            
        }
    }

    
    /**
     * Computes the difference between two dates expressed
     * in the specified units.
     * <p>
     * e.g. Given the two dates:
     * <pre>
     * date1 = 2005-01-01
     * date2 = 2005-01-05
     * </pre>
     * 
     * This method when called with a Units of "DAY" will return
     * a value of 4.
     * <pre>
     *    TimeInterval.dateDiff(date1, date2, Units.DAY) == 4
     * </pre>
     * If the second date is less than the first date, than a negative
     * nuber will be returned.
     * <pre>
     *    TimeInterval.dateDiff(date2, date1, Units.DAY) == - 4
     * </pre>
     * <p>
     * <b>NOTE:</b> A 1 year diference is defined as two dates with the
     * same month and day, but whose years differ by one. So:
     * <pre>
     *    "March 5th, 2005" - "March 5th, 2004" = 1 year
     *    "March 4th, 2005" - "March 5th, 2004" = 0 years
     * </pre>
     * <b>NOTE:</b> A 1 month diference is defined as two dates with the
     * same day, but whose months differ by one. So:
     * <pre>
     *    "April 5th, 2005" - "March 5th, 2005" = 1 month
     *    "April 4th, 2005" - "March 5th, 2005" = 0 months
     * </pre>
     * <b>NOTE:</b> There is no "last day of month" check, meaning that
     * subtracting the 31st in a 31 day month from the 30th of a 30 day
     * month does not equal 1 month.
     * <pre>
     *    "November 30th, 2005" - "October 31st, 2005" = 0 months!
     *    "December 1st, 2005" - "October 31st, 2005" = 1 month
     * </pre>
     * 
     * <b>NOTE:</b> When getting the difference between two dates using
     * units of YEAR, MONTH, or DAY, the time part of the date is ignored.
     * <p>
     * Generally this method will be used with a TimeInterval object.
     * For example, If I want to make sure that a date is no more
     * than 10 days in the future, I would code the following:
     * <pre>
     * public boolean tenDayCheck(Date futureDate) {
     * 
     *     TimeInterval ti = new TimeInterval(10,Units.DAY);
     *     
     *     return (TimeInterval.dateDiff(new Date(),
     *                                  futureDate,
     *                                  ti.getUnits())
     *             &lt; ti.getInterval());
     *      
     * }
     * </pre>
     * 
     * @param date1 The earlier date.
     * @param date2 The later date.
     * @param units The units to use
     * @return The difference bewteen the two dates expressed in
     *         the specified units (and negative if date 2 was before date 1).
     */
    @SuppressWarnings("incomplete-switch")
	public static long dateDiff(Date date1, Date date2, Units units) {
        
        Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal2.setTime(date2);
        
        switch(units) {
        
            case YEAR:
            case MONTH: 
            case DAY:
                cal1.set(Calendar.HOUR_OF_DAY,0);
                cal2.set(Calendar.HOUR_OF_DAY,0);
                cal1.set(Calendar.MINUTE,0);
                cal2.set(Calendar.MINUTE,0);
                cal1.set(Calendar.SECOND,0);
                cal2.set(Calendar.SECOND,0);
            default:
                cal1.set(Calendar.MILLISECOND,0);
                cal2.set(Calendar.MILLISECOND,0);
        }
        
        long dateDif = 0;
        
        switch(units) {
            case YEAR:
            case MONTH:
                
                dateDif = 0;
                if(!cal2.equals(cal1)) {
                    
                    Calendar calLowDate = cal1;
                    Calendar calHighDate = cal2;
                    long sign = 1;
                
                    if(cal2.before(cal1)) {
                        sign = -1;
                        calLowDate = cal2;
                        calHighDate = cal1;
                    }
                
                    long yearDif = calHighDate.get(Calendar.YEAR) - calLowDate.get(Calendar.YEAR);
                    long monthDif = calHighDate.get(Calendar.MONTH) - calLowDate.get(Calendar.MONTH);
                    long dayDif = calHighDate.get(Calendar.DAY_OF_MONTH) - calLowDate.get(Calendar.DAY_OF_MONTH);
                    
                    switch(units) {
                        case YEAR:
                            dateDif = yearDif;
                            if(dateDif > 0) {
                                if(monthDif >= 0 && dayDif < 0) {
                                     monthDif -= 1;
                                }
                                if(monthDif < 0) {
                                    dateDif -= 1;
                                }
                            }
                            break;
                            
                        case MONTH:
                            dateDif = (yearDif*12) + monthDif;
                            if(dateDif > 0 && dayDif < 0) {
                                dateDif -= 1;
                            }
                            break;
                    }
                    
                    dateDif = sign * (dateDif);
                    
                    
                }
                break;
                
            case DAY:
                dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();
                dateDif = ((dateDif)/86400000); //convert milliseconds to days
                break;
            case HOUR:
                dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();
                dateDif = ((dateDif)/3600000); //convert milliseconds to hours
                break;
            case MINUTE:
                dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();
                dateDif = ((dateDif)/60000); //convert milliseconds to minutes
                break;
            default:
                dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();
                dateDif = ((dateDif)/1000); //convert milliseconds to seconds
                break;
        }
        
        return dateDif;
    }
    
    /**
     * Computes the difference between two dates expressed
     * in the specified units.
     * <p>
     * e.g. Given the two dates:
     * <pre>
     * date1 = 2005-01-01
     * date2 = 2005-01-05
     * </pre>
     * 
     * This method when called with a Units of "DAY" will return
     * a value of 4.
     * <pre>
     *    TimeInterval.dateDiff(date1, date2, Units.DAY) == 4
     * </pre>
     * If the second date is less than the first date, than a negative
     * nuber will be returned.
     * <pre>
     *    TimeInterval.dateDiff(date2, date1, Units.DAY) == - 4
     * </pre>
     * <p>
     * <b>NOTE:</b> A 1 year diference is defined as two dates with the
     * same month and day, but whose years differ by one. So:
     * <pre>
     *    "March 5th, 2005" - "March 5th, 2004" = 1 year
     *    "March 4th, 2005" - "March 5th, 2004" = 0 years
     * </pre>
     * <b>NOTE:</b> A 1 month diference is defined as two dates with the
     * same day, but whose months differ by one. So:
     * <pre>
     *    "April 5th, 2005" - "March 5th, 2005" = 1 month
     *    "April 4th, 2005" - "March 5th, 2005" = 0 months
     * </pre>
     * <b>NOTE:</b> There is no "last day of month" check, meaning that
     * subtracting the 31st in a 31 day month from the 30th of a 30 day
     * month does not equal 1 month.
     * <pre>
     *    "November 30th, 2005" - "October 31st, 2005" = 0 months!
     *    "December 1st, 2005" - "October 31st, 2005" = 1 month
     * </pre>
     * 
     * <b>NOTE:</b> When getting the difference between two dates using
     * units of YEAR, MONTH, or DAY, the time part of the date is ignored.
     * <p>
     * Generally this method will be used with a TimeInterval object.
     * For example, If I want to make sure that a date is no more
     * than 10 days in the future, I would code the following:
     * <pre>
     * public boolean tenDayCheck(Date futureDate) {
     * 
     *     TimeInterval ti = new TimeInterval(10,Units.DAY);
     *     
     *     return (TimeInterval.dateDiff(new Date(),
     *                                  futureDate,
     *                                  ti.getUnits())
     *             &lt; ti.getInterval());
     *      
     * }
     * </pre>
     * 
     * @param date1 The earlier date.
     * @param date2 The later date.
     * @param units The units to use
     * @return The difference bewteen the two dates expressed in
     *         the specified units (and negative if date 2 was before date 1).
     */
    //Criado por Yuri
    @SuppressWarnings("incomplete-switch")
	public static float dateDiffFloat(Date date1, Date date2, Units units) {
        
        Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal2.setTime(date2);
        
        switch(units) {
        
            case YEAR:
            case MONTH: 
            case DAY:
                cal1.set(Calendar.HOUR_OF_DAY,0);
                cal2.set(Calendar.HOUR_OF_DAY,0);
                cal1.set(Calendar.MINUTE,0);
                cal2.set(Calendar.MINUTE,0);
                cal1.set(Calendar.SECOND,0);
                cal2.set(Calendar.SECOND,0);
            default:
                cal1.set(Calendar.MILLISECOND,0);
                cal2.set(Calendar.MILLISECOND,0);
        }
        
        float dateDif = 0;
        
        switch(units) {
            case YEAR:
            case MONTH:
                
                dateDif = 0;
                if(!cal2.equals(cal1)) {
                    
                    Calendar calLowDate = cal1;
                    Calendar calHighDate = cal2;
                    long sign = 1;
                
                    if(cal2.before(cal1)) {
                        sign = -1;
                        calLowDate = cal2;
                        calHighDate = cal1;
                    }
                
                    long yearDif = calHighDate.get(Calendar.YEAR) - calLowDate.get(Calendar.YEAR);
                    long monthDif = calHighDate.get(Calendar.MONTH) - calLowDate.get(Calendar.MONTH);
                    long dayDif = calHighDate.get(Calendar.DAY_OF_MONTH) - calLowDate.get(Calendar.DAY_OF_MONTH);
                    
                    switch(units) {
                        case YEAR:
                            dateDif = yearDif;
                            if(dateDif > 0) {
                                if(monthDif >= 0 && dayDif < 0) {
                                     monthDif -= 1;
                                }
                                if(monthDif < 0) {
                                    dateDif -= 1;
                                }
                            }
                            break;
                            
                        case MONTH:
                            dateDif = (yearDif*12) + monthDif;
                            if(dateDif > 0 && dayDif < 0) {
                                dateDif -= 1;
                            }
                            break;
                    }
                    
                    dateDif = sign * (dateDif);
                    
                    
                }
                break;
                
            case DAY:
                dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();
                dateDif = ((dateDif)/86400000); //convert milliseconds to days
                break;
            case HOUR:
                dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();
                dateDif = ((dateDif)/3600000); //convert milliseconds to hours
                break;
            case MINUTE:
                dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();
                dateDif = ((dateDif)/60000); //convert milliseconds to minutes
                break;
            default:
                dateDif = cal2.getTimeInMillis() - cal1.getTimeInMillis();
                dateDif = ((dateDif)/1000); //convert milliseconds to seconds
                break;
        }
        
        return dateDif;
    }
   
    
    /**
     * Retorna o numero de dias uteis entre duas datas
     * */
    //Criado por Gleidson
    public static int dateDiffUtil(Date date1, Date date2) {
        
        Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal2.setTime(date2);
         
        int dateDif = 0;
       
        
        //while(cal2.com.getTime() - date1.getTime() >=0)
        while(cal2.compareTo(cal1)>0)
        {
        	cal1.add(Calendar.DAY_OF_MONTH, 1);
        	if(cal1.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY && cal1.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY)  
        		dateDif++;  
        }  
        
        return dateDif;
    }
    
    public static boolean dateBetween(Date dateIni, Date dateFim, Date dateTest) {
    	if(dateIni != null && dateFim != null && dateTest != null)	
    		return (dateTest.after(dateIni) || dateTest.equals(dateIni)) && (dateTest.before(dateFim) || dateTest.equals(dateFim));
    	else
    		return false;
	}
}