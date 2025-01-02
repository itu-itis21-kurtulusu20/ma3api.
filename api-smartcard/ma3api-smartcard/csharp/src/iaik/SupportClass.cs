//
// In order to convert some functionality to Visual C#, the Java Language Conversion Assistant
// creates "support classes" that duplicate the original functionality.  
//
// Support classes replicate the functionality of the original code, but in some cases they are 
// substantially different architecturally. Although every effort is made to preserve the 
// original architecture of the application in the converted project, the user should be aware that 
// the primary goal of these support classes is to replicate functionality, and that at times 
// the architecture of the resulting solution may differ somewhat.
//

using System;

/// <summary>
/// Contains conversion support elements such as classes, interfaces and static methods.
/// </summary>
public class SupportClass
{
	/// <summary>
	/// Converts an array of bytes to an array of bytes
	/// </summary>
	/// <param name="byteArray">The array of bytes to be converted</param>
	/// <returns>The new array of bytes</returns>
	public static byte[] ToByteArray(byte[] byteArray)
	{
		//byte[] byteArray = null;

		if (byteArray != null)
		{
			byteArray = new byte[byteArray.Length];
			for(int index=0; index < byteArray.Length; index++)
				byteArray[index] = (byte) byteArray[index];
		}
		return byteArray;
	}

	/// <summary>
	/// Converts a string to an array of bytes
	/// </summary>
	/// <param name="sourceString">The string to be converted</param>
	/// <returns>The new array of bytes</returns>
	public static byte[] ToByteArray(System.String sourceString)
	{
		return System.Text.UTF8Encoding.UTF8.GetBytes(sourceString);
	}

	/// <summary>
	/// Converts a array of object-type instances to a byte-type array.
	/// </summary>
	/// <param name="tempObjectArray">Array to convert.</param>
	/// <returns>An array of byte type elements.</returns>
	public static byte[] ToByteArray(System.Object[] tempObjectArray)
	{
		byte[] byteArray = null;
		if (tempObjectArray != null)
		{
			byteArray = new byte[tempObjectArray.Length];
			for (int index = 0; index < tempObjectArray.Length; index++)
				byteArray[index] = (byte)tempObjectArray[index];
		}
		return byteArray;
	}

	/*******************************/
	/// <summary>
	/// Converts an array of bytes to an array of chars
	/// </summary>
	/// <param name="byteArray">The array of bytes to convert</param>
	/// <returns>The new array of chars</returns>
	/*public static char[] ToCharArray(byte[] byteArray) 
	{
		return System.Text.UTF8Encoding.UTF8.GetChars(ToByteArray(byteArray));
	}*/

	/// <summary>
	/// Converts an array of bytes to an array of chars
	/// </summary>
	/// <param name="byteArray">The array of bytes to convert</param>
	/// <returns>The new array of chars</returns>
	public static char[] ToCharArray(byte[] byteArray) 
	{
		return System.Text.UTF8Encoding.UTF8.GetChars(byteArray);
	}

	/*******************************/
	/// <summary>
	/// Represents a collection ob objects that contains no duplicate elements.
	/// </summary>	
	public interface SetSupport : System.Collections.ICollection, System.Collections.IList
	{
		/// <summary>
		/// Adds a new element to the Collection if it is not already present.
		/// </summary>
		/// <param name="obj">The object to add to the collection.</param>
		/// <returns>Returns true if the object was added to the collection, otherwise false.</returns>
		new bool Add(System.Object obj);

		/// <summary>
		/// Adds all the elements of the specified collection to the Set.
		/// </summary>
		/// <param name="c">Collection of objects to add.</param>
		/// <returns>true</returns>
		bool AddAll(System.Collections.ICollection c);
	}


	/*******************************/
	/// <summary>
	/// Summary description for EqualsSupport.
	/// </summary>
	public class EqualsSupport
	{
		/// <summary>
		/// Determines whether two Collections instances are equal.
		/// </summary>
		/// <param name="source">The first Collections to compare. </param>
		/// <param name="target">The second Collections to compare. </param>
		/// <returns>Return true if the first collection is the same instance as the second collection, otherwise returns false.</returns>
		public static bool Equals(System.Collections.ICollection source, System.Collections.ICollection target )
		{
			bool equal = true;

			System.Collections.ArrayList sourceInterfaces = new System.Collections.ArrayList(source.GetType().GetInterfaces());
			System.Collections.ArrayList targetInterfaces = new System.Collections.ArrayList(target.GetType().GetInterfaces());

			if (sourceInterfaces.Contains(System.Type.GetType("SupportClass+SetSupport")) && 
				!targetInterfaces.Contains(System.Type.GetType("SupportClass+SetSupport")))
				equal = false;
			else if (targetInterfaces.Contains(System.Type.GetType("SupportClass+SetSupport")) && 
				!sourceInterfaces.Contains(System.Type.GetType("SupportClass+SetSupport")))
				equal = false;

			if (equal)
			{
				System.Collections.IEnumerator sourceEnumerator = ReverseStack(source);
				System.Collections.IEnumerator targetEnumerator = ReverseStack(target);

				if (source.Count != target.Count)
					equal = false;

				while(sourceEnumerator.MoveNext() && targetEnumerator.MoveNext())
					if (!sourceEnumerator.Current.Equals(targetEnumerator.Current))
						equal = false;
			}

			return equal;
		}

		/// <summary>
		/// Determines if a Collection is equal to the Object.
		/// </summary>
		/// <param name="source">The first Collections to compare.</param>
		/// <param name="target">The Object to compare.</param>
		/// <returns>Return true if the first collection contains the same values of the second Object, otherwise returns false.</returns>
		public static bool Equals(System.Collections.ICollection source, System.Object target)
		{
			return (target is System.Collections.ICollection) ? Equals(source, (System.Collections.ICollection) target) : false;
		}

		/// <summary>
		/// Determines if a IDictionaryEnumerator is equal to the Object.
		/// </summary>
		/// <param name="source">The first IDictionaryEnumerator to compare.</param>
		/// <param name="target">The second Object to compare.</param>
		/// <returns>Return true if the first IDictionaryEnumerator contains the same values of the second Object, otherwise returns false.</returns>
		public static bool Equals(System.Collections.IDictionaryEnumerator source, System.Object target)
		{
			return (target is System.Collections.IDictionaryEnumerator) ? Equals(source, (System.Collections.IDictionaryEnumerator) target) : false;
		}

		/// <summary>
		/// Determines if a IDictionary is equal to the Object.
		/// </summary>
		/// <param name="source">The first IDictionary to compare.</param>
		/// <param name="target">The second Object to compare.</param>
		/// <returns>Return true if the first IDictionary contains the same values of the second Object, otherwise returns false.</returns>
		public static bool Equals(System.Collections.IDictionary source, System.Object target)
		{
			return (target is System.Collections.IDictionary)? Equals(source, (System.Collections.IDictionary) target) : false;
		}

		/// <summary>
		/// Determines whether two IDictionaryEnumerator instances are equals.
		/// </summary>
		/// <param name="source">The first IDictionaryEnumerator to compare.</param>
		/// <param name="target">The second IDictionaryEnumerator to compare.</param>
		/// <returns>Return true if the first IDictionaryEnumerator contains the same values as the second IDictionaryEnumerator, otherwise return false.</returns>
		public static bool Equals(System.Collections.IDictionaryEnumerator source, System.Collections.IDictionaryEnumerator target )
		{
			while (source.MoveNext() && target.MoveNext())
				if (source.Key.Equals(target.Key))
					if (source.Value.Equals(target.Value))
						return true;
			return false;
		}

		/// <summary>
		/// Reverses the Stack Collection received.
		/// </summary>
		/// <param name="collection">The collection to reverse.</param>
		/// <returns>The collection received in reverse order if it was a System.Collections.Stack type, otherwise it does 
		/// nothing to the collection.</returns>
		public static System.Collections.IEnumerator ReverseStack(System.Collections.ICollection collection)
		{
			if ((collection.GetType()) == (typeof(System.Collections.Stack)))
			{
				System.Collections.ArrayList collectionStack = new System.Collections.ArrayList(collection);
				collectionStack.Reverse();
				return collectionStack.GetEnumerator();
			}
			else
				return collection.GetEnumerator();
		}

		/// <summary>
		/// Determines whether two IDictionary instances are equal.
		/// </summary>
		/// <param name="source">The first Collection to compare.</param>
		/// <param name="target">The second Collection to compare.</param>
		/// <returns>Return true if the first collection is the same instance as the second collection, otherwise return false.</returns>
		public static bool Equals(System.Collections.IDictionary source, System.Collections.IDictionary target)
		{
			System.Collections.Hashtable targetAux = new System.Collections.Hashtable(target);

			if (source.Count == targetAux.Count)
			{
				System.Collections.IEnumerator sourceEnum = source.Keys.GetEnumerator();
				while (sourceEnum.MoveNext())
					if (targetAux.Contains(sourceEnum.Current))
						targetAux.Remove(sourceEnum.Current);
					else
						return false;
			}
			else
				return false;
			if (targetAux.Count == 0)
				return true;
			else
				return false;
		}
	}


	/*******************************/
	/// <summary>
	/// SupportClass for the HashSet class.
	/// </summary>
	[Serializable]
	public class HashSetSupport : System.Collections.ArrayList, SetSupport
	{
		public HashSetSupport() : base()
		{	
		}

		public HashSetSupport(System.Collections.ICollection c) 
		{
			this.AddAll(c);
		}

		public HashSetSupport(int capacity) : base(capacity)
		{
		}

		/// <summary>
		/// Adds a new element to the ArrayList if it is not already present.
		/// </summary>		
		/// <param name="obj">Element to insert to the ArrayList.</param>
		/// <returns>Returns true if the new element was inserted, false otherwise.</returns>
		new public virtual bool Add(System.Object obj)
		{
			bool inserted;

			if ((inserted = this.Contains(obj)) == false)
			{
				base.Add(obj);
			}

			return !inserted;
		}

		/// <summary>
		/// Adds all the elements of the specified collection that are not present to the list.
		/// </summary>
		/// <param name="c">Collection where the new elements will be added</param>
		/// <returns>Returns true if at least one element was added, false otherwise.</returns>
		public bool AddAll(System.Collections.ICollection c)
		{
			System.Collections.IEnumerator e = new System.Collections.ArrayList(c).GetEnumerator();
			bool added = false;

			while (e.MoveNext() == true)
			{
				if (this.Add(e.Current) == true)
					added = true;
			}

			return added;
		}
		
		/// <summary>
		/// Returns a copy of the HashSet instance.
		/// </summary>		
		/// <returns>Returns a shallow copy of the current HashSet.</returns>
		public override System.Object Clone()
		{
			return base.MemberwiseClone();
		}
	}


	/*******************************/
	/// <summary>
	/// This class manages different features for calendars.
	/// The different calendars are internally managed using a hashtable structure.
	/// </summary>
	public class CalendarManager
	{
		/// <summary>
		/// Field used to get or set the year.
		/// </summary>
		public const int YEAR = 1;

		/// <summary>
		/// Field used to get or set the month.
		/// </summary>
		public const int MONTH = 2;
		
		/// <summary>
		/// Field used to get or set the day of the month.
		/// </summary>
		public const int DATE = 5;
		
		/// <summary>
		/// Field used to get or set the hour of the morning or afternoon.
		/// </summary>
		public const int HOUR = 10;
		
		/// <summary>
		/// Field used to get or set the minute within the hour.
		/// </summary>
		public const int MINUTE = 12;
		
		/// <summary>
		/// Field used to get or set the second within the minute.
		/// </summary>
		public const int SECOND = 13;
		
		/// <summary>
		/// Field used to get or set the millisecond within the second.
		/// </summary>
		public const int MILLISECOND = 14;
		
		/// <summary>
		/// Field used to get or set the day of the year.
		/// </summary>
		public const int DAY_OF_YEAR = 4;
		
		/// <summary>
		/// Field used to get or set the day of the month.
		/// </summary>
		public const int DAY_OF_MONTH = 6;
		
		/// <summary>
		/// Field used to get or set the day of the week.
		/// </summary>
		public const int DAY_OF_WEEK = 7;
		
		/// <summary>
		/// Field used to get or set the hour of the day.
		/// </summary>
		public const int HOUR_OF_DAY = 11;
		
		/// <summary>
		/// Field used to get or set whether the HOUR is before or after noon.
		/// </summary>
		public const int AM_PM = 9;
		
		/// <summary>
		/// Field used to get or set the value of the AM_PM field which indicates the period of the day from midnight to just before noon.
		/// </summary>
		public const int AM = 0;
		
		/// <summary>
		/// Field used to get or set the value of the AM_PM field which indicates the period of the day from noon to just before midnight.
		/// </summary>
		public const int PM = 1;
		
		/// <summary>
		/// The hashtable that contains the calendars and its properties.
		/// </summary>
		static public CalendarHashTable manager = new CalendarHashTable();

		/// <summary>
		/// Internal class that inherits from HashTable to manage the different calendars.
		/// This structure will contain an instance of System.Globalization.Calendar that represents 
		/// a type of calendar and its properties (represented by an instance of CalendarProperties 
		/// class).
		/// </summary>
		public class CalendarHashTable:System.Collections.Hashtable 
		{
			/// <summary>
			/// Gets the calendar current date and time.
			/// </summary>
			/// <param name="calendar">The calendar to get its current date and time.</param>
			/// <returns>A System.DateTime value that indicates the current date and time for the 
			/// calendar given.</returns>
			public System.DateTime GetDateTime(System.Globalization.Calendar calendar)
			{
				if (this[calendar] != null)
					return ((CalendarProperties) this[calendar]).dateTime;
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = System.DateTime.Now;
					this.Add(calendar, tempProps);
					return this.GetDateTime(calendar);
				}
			}

			/// <summary>
			/// Sets the specified System.DateTime value to the specified calendar.
			/// </summary>
			/// <param name="calendar">The calendar to set its date.</param>
			/// <param name="date">The System.DateTime value to set to the calendar.</param>
			public void SetDateTime(System.Globalization.Calendar calendar, System.DateTime? date)
			{
				if (this[calendar] != null)
				{
                    ((CalendarProperties)this[calendar]).dateTime = (System.DateTime)date;
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
                    tempProps.dateTime = (System.DateTime)date;
					this.Add(calendar, tempProps);
				}
			}

			/// <summary>
			/// Sets the corresponding field in an specified calendar with the value given.
			/// If the specified calendar does not have exist in the hash table, it creates a 
			/// new instance of the calendar with the current date and time and then assings it 
			/// the new specified value.
			/// </summary>
			/// <param name="calendar">The calendar to set its date or time.</param>
			/// <param name="field">One of the fields that composes a date/time.</param>
			/// <param name="fieldValue">The value to be set.</param>
			public void Set(System.Globalization.Calendar calendar, int field, int fieldValue)
			{
				if (this[calendar] != null)
				{
					System.DateTime tempDate = ((CalendarProperties) this[calendar]).dateTime;
					switch (field)
					{
						case CalendarManager.DATE:
							tempDate = tempDate.AddDays(fieldValue - tempDate.Day);
							break;
						case CalendarManager.HOUR:
							tempDate = tempDate.AddHours(fieldValue - tempDate.Hour);
							break;
						case CalendarManager.MILLISECOND:
							tempDate = tempDate.AddMilliseconds(fieldValue - tempDate.Millisecond);
							break;
						case CalendarManager.MINUTE:
							tempDate = tempDate.AddMinutes(fieldValue - tempDate.Minute);
							break;
						case CalendarManager.MONTH:
							//Month value is 0-based. e.g., 0 for January
							tempDate = tempDate.AddMonths((fieldValue + 1) - tempDate.Month);
							break;
						case CalendarManager.SECOND:
							tempDate = tempDate.AddSeconds(fieldValue - tempDate.Second);
							break;
						case CalendarManager.YEAR:
							tempDate = tempDate.AddYears(fieldValue - tempDate.Year);
							break;
						case CalendarManager.DAY_OF_MONTH:
							tempDate = tempDate.AddDays(fieldValue - tempDate.Day);
							break;
						case CalendarManager.DAY_OF_WEEK:
							tempDate = tempDate.AddDays((fieldValue - 1) - (int)tempDate.DayOfWeek);
							break;
						case CalendarManager.DAY_OF_YEAR:
							tempDate = tempDate.AddDays(fieldValue - tempDate.DayOfYear);
							break;
						case CalendarManager.HOUR_OF_DAY:
							tempDate = tempDate.AddHours(fieldValue - tempDate.Hour);
							break;

						default:
							break;
					}
					((CalendarProperties) this[calendar]).dateTime = tempDate;
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = System.DateTime.Now;
					this.Add(calendar, tempProps);
					this.Set(calendar, field, fieldValue);
				}
			}

			/// <summary>
			/// Sets the corresponding date (day, month and year) to the calendar specified.
			/// If the calendar does not exist in the hash table, it creates a new instance and sets 
			/// its values.
			/// </summary>
			/// <param name="calendar">The calendar to set its date.</param>
			/// <param name="year">Integer value that represent the year.</param>
			/// <param name="month">Integer value that represent the month.</param>
			/// <param name="day">Integer value that represent the day.</param>
			public void Set(System.Globalization.Calendar calendar, int year, int month, int day)
			{
				if (this[calendar] != null)
				{
					this.Set(calendar, CalendarManager.YEAR, year);
					this.Set(calendar, CalendarManager.MONTH, month);
					this.Set(calendar, CalendarManager.DATE, day);
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = System.DateTime.Now;
					this.Add(calendar, tempProps);
					this.Set(calendar, year, month, day);
				}
			}

			/// <summary>
			/// Sets the corresponding date (day, month and year) and hour (hour and minute) 
			/// to the calendar specified.
			/// If the calendar does not exist in the hash table, it creates a new instance and sets 
			/// its values.
			/// </summary>
			/// <param name="calendar">The calendar to set its date and time.</param>
			/// <param name="year">Integer value that represent the year.</param>
			/// <param name="month">Integer value that represent the month.</param>
			/// <param name="day">Integer value that represent the day.</param>
			/// <param name="hour">Integer value that represent the hour.</param>
			/// <param name="minute">Integer value that represent the minutes.</param>
			public void Set(System.Globalization.Calendar calendar, int year, int month, int day, int hour, int minute)
			{
				if (this[calendar] != null)
				{
					this.Set(calendar, CalendarManager.YEAR, year);
					this.Set(calendar, CalendarManager.MONTH, month);
					this.Set(calendar, CalendarManager.DATE, day);
					this.Set(calendar, CalendarManager.HOUR, hour);
					this.Set(calendar, CalendarManager.MINUTE, minute);
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = System.DateTime.Now;
					this.Add(calendar, tempProps);
					this.Set(calendar, year, month, day, hour, minute);
				}
			}

			/// <summary>
			/// Sets the corresponding date (day, month and year) and hour (hour, minute and second) 
			/// to the calendar specified.
			/// If the calendar does not exist in the hash table, it creates a new instance and sets 
			/// its values.
			/// </summary>
			/// <param name="calendar">The calendar to set its date and time.</param>
			/// <param name="year">Integer value that represent the year.</param>
			/// <param name="month">Integer value that represent the month.</param>
			/// <param name="day">Integer value that represent the day.</param>
			/// <param name="hour">Integer value that represent the hour.</param>
			/// <param name="minute">Integer value that represent the minutes.</param>
			/// <param name="second">Integer value that represent the seconds.</param>
			public void Set(System.Globalization.Calendar calendar, int year, int month, int day, int hour, int minute, int second)
			{
				if (this[calendar] != null)
				{
					this.Set(calendar, CalendarManager.YEAR, year);
					this.Set(calendar, CalendarManager.MONTH, month);
					this.Set(calendar, CalendarManager.DATE, day);
					this.Set(calendar, CalendarManager.HOUR, hour);
					this.Set(calendar, CalendarManager.MINUTE, minute);
					this.Set(calendar, CalendarManager.SECOND, second);
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = System.DateTime.Now;
					this.Add(calendar, tempProps);
					this.Set(calendar, year, month, day, hour, minute, second);
				}
			}

			/// <summary>
			/// Gets the value represented by the field specified.
			/// </summary>
			/// <param name="calendar">The calendar to get its date or time.</param>
			/// <param name="field">One of the field that composes a date/time.</param>
			/// <returns>The integer value for the field given.</returns>
			public int Get(System.Globalization.Calendar calendar, int field)
			{
				if (this[calendar] != null)
				{
					int tempHour;
					switch (field)
					{
						case CalendarManager.DATE:
							return ((CalendarProperties) this[calendar]).dateTime.Day;
						case CalendarManager.HOUR:
							tempHour = ((CalendarProperties) this[calendar]).dateTime.Hour;
							return tempHour > 12 ? tempHour - 12 : tempHour;
						case CalendarManager.MILLISECOND:
							return ((CalendarProperties) this[calendar]).dateTime.Millisecond;
						case CalendarManager.MINUTE:
							return ((CalendarProperties) this[calendar]).dateTime.Minute;
						case CalendarManager.MONTH:
							//Month value is 0-based. e.g., 0 for January
							return ((CalendarProperties) this[calendar]).dateTime.Month - 1;
						case CalendarManager.SECOND:
							return ((CalendarProperties) this[calendar]).dateTime.Second;
						case CalendarManager.YEAR:
							return ((CalendarProperties) this[calendar]).dateTime.Year;
						case CalendarManager.DAY_OF_MONTH:
							return ((CalendarProperties) this[calendar]).dateTime.Day;
						case CalendarManager.DAY_OF_YEAR:							
							return (int)(((CalendarProperties) this[calendar]).dateTime.DayOfYear);
						case CalendarManager.DAY_OF_WEEK:
							return (int)(((CalendarProperties) this[calendar]).dateTime.DayOfWeek) + 1;
						case CalendarManager.HOUR_OF_DAY:
							return ((CalendarProperties) this[calendar]).dateTime.Hour;
						case CalendarManager.AM_PM:
							tempHour = ((CalendarProperties) this[calendar]).dateTime.Hour;
							return tempHour > 12 ? CalendarManager.PM : CalendarManager.AM;

						default:
							return 0;
					}
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = System.DateTime.Now;
					this.Add(calendar, tempProps);
					return this.Get(calendar, field);
				}
			}

			/// <summary>
			/// Sets the time in the specified calendar with the long value.
			/// </summary>
			/// <param name="calendar">The calendar to set its date and time.</param>
			/// <param name="milliseconds">A long value that indicates the milliseconds to be set to 
			/// the hour for the calendar.</param>
			public void SetTimeInMilliseconds(System.Globalization.Calendar calendar, long milliseconds)
			{
				if (this[calendar] != null)
				{
					((CalendarProperties) this[calendar]).dateTime = new System.DateTime(milliseconds);
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = new System.DateTime(System.TimeSpan.TicksPerMillisecond * milliseconds);
					this.Add(calendar, tempProps);
				}
			}
				
			/// <summary>
			/// Gets what the first day of the week is; e.g., Sunday in US, Monday in France.
			/// </summary>
			/// <param name="calendar">The calendar to get its first day of the week.</param>
			/// <returns>A System.DayOfWeek value indicating the first day of the week.</returns>
			public System.DayOfWeek GetFirstDayOfWeek(System.Globalization.Calendar calendar)
			{
				if (this[calendar] != null)
				{
					if (((CalendarProperties)this[calendar]).dateTimeFormat == null)
					{
						((CalendarProperties)this[calendar]).dateTimeFormat = new System.Globalization.DateTimeFormatInfo();
						((CalendarProperties)this[calendar]).dateTimeFormat.FirstDayOfWeek = System.DayOfWeek.Sunday;
					}
					return ((CalendarProperties) this[calendar]).dateTimeFormat.FirstDayOfWeek;
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = System.DateTime.Now;
					tempProps.dateTimeFormat = new System.Globalization.DateTimeFormatInfo();
					tempProps.dateTimeFormat.FirstDayOfWeek = System.DayOfWeek.Sunday;
					this.Add(calendar, tempProps);
					return this.GetFirstDayOfWeek(calendar);
				}
			}

			/// <summary>
			/// Sets what the first day of the week is; e.g., Sunday in US, Monday in France.
			/// </summary>
			/// <param name="calendar">The calendar to set its first day of the week.</param>
			/// <param name="firstDayOfWeek">A System.DayOfWeek value indicating the first day of the week
			/// to be set.</param>
			public void SetFirstDayOfWeek(System.Globalization.Calendar calendar, System.DayOfWeek  firstDayOfWeek)
			{
				if (this[calendar] != null)
				{
					if (((CalendarProperties)this[calendar]).dateTimeFormat == null)
						((CalendarProperties)this[calendar]).dateTimeFormat = new System.Globalization.DateTimeFormatInfo();

					((CalendarProperties) this[calendar]).dateTimeFormat.FirstDayOfWeek = firstDayOfWeek;
				}
				else
				{
					CalendarProperties tempProps = new CalendarProperties();
					tempProps.dateTime = System.DateTime.Now;
					tempProps.dateTimeFormat = new System.Globalization.DateTimeFormatInfo();
					this.Add(calendar, tempProps);
					this.SetFirstDayOfWeek(calendar, firstDayOfWeek);
				}
			}

			/// <summary>
			/// Removes the specified calendar from the hash table.
			/// </summary>
			/// <param name="calendar">The calendar to be removed.</param>
			public void Clear(System.Globalization.Calendar calendar)
			{
				if (this[calendar] != null)
					this.Remove(calendar);
			}

			/// <summary>
			/// Removes the specified field from the calendar given.
			/// If the field does not exists in the calendar, the calendar is removed from the table.
			/// </summary>
			/// <param name="calendar">The calendar to remove the value from.</param>
			/// <param name="field">The field to be removed from the calendar.</param>
			public void Clear(System.Globalization.Calendar calendar, int field)
			{
				if (this[calendar] != null)
					this.Set(calendar, field, 0);
			}

			/// <summary>
			/// Internal class that represents the properties of a calendar instance.
			/// </summary>
			class CalendarProperties
			{
				/// <summary>
				/// The date and time of a calendar.
				/// </summary>
				public System.DateTime dateTime;
				
				/// <summary>
				/// The format for the date and time in a calendar.
				/// </summary>
				public System.Globalization.DateTimeFormatInfo dateTimeFormat;
			}
		}
	}
	/*******************************/
	/// <summary>
	/// Copies an array of chars obtained from a String into a specified array of chars
	/// </summary>
	/// <param name="sourceString">The String to get the chars from</param>
	/// <param name="sourceStart">Position of the String to start getting the chars</param>
	/// <param name="sourceEnd">Position of the String to end getting the chars</param>
	/// <param name="destinationArray">Array to return the chars</param>
	/// <param name="destinationStart">Position of the destination array of chars to start storing the chars</param>
	/// <returns>An array of chars</returns>
	public static void GetCharsFromString(System.String sourceString, int sourceStart, int sourceEnd, char[] destinationArray, int destinationStart)
	{	
		int sourceCounter;
		int destinationCounter;
		sourceCounter = sourceStart;
		destinationCounter = destinationStart;
		while (sourceCounter < sourceEnd)
		{
			destinationArray[destinationCounter] = (char) sourceString[sourceCounter];
			sourceCounter++;
			destinationCounter++;
		}
	}

	/*******************************/
	/// <summary>
	/// Performs an unsigned bitwise right shift with the specified number
	/// </summary>
	/// <param name="number">Number to operate on</param>
	/// <param name="bits">Ammount of bits to shift</param>
	/// <returns>The resulting number from the shift operation</returns>
	public static int URShift(int number, int bits)
	{
		if ( number >= 0)
			return number >> bits;
		else
			return (number >> bits) + (2 << ~bits);
	}

	/// <summary>
	/// Performs an unsigned bitwise right shift with the specified number
	/// </summary>
	/// <param name="number">Number to operate on</param>
	/// <param name="bits">Ammount of bits to shift</param>
	/// <returns>The resulting number from the shift operation</returns>
	public static int URShift(int number, long bits)
	{
		return URShift(number, (int)bits);
	}

	/// <summary>
	/// Performs an unsigned bitwise right shift with the specified number
	/// </summary>
	/// <param name="number">Number to operate on</param>
	/// <param name="bits">Ammount of bits to shift</param>
	/// <returns>The resulting number from the shift operation</returns>
	public static long URShift(long number, int bits)
	{
		if ( number >= 0)
			return number >> bits;
		else
			return (number >> bits) + (2L << ~bits);
	}

	/// <summary>
	/// Performs an unsigned bitwise right shift with the specified number
	/// </summary>
	/// <param name="number">Number to operate on</param>
	/// <param name="bits">Ammount of bits to shift</param>
	/// <returns>The resulting number from the shift operation</returns>
	public static long URShift(long number, long bits)
	{
		return URShift(number, (int)bits);
	}

	/*******************************/
	/// <summary>
	/// Reverses string values.
	/// </summary>
	/// <param name="text">The StringBuilder object containing the string to be reversed.</param>
	/// <returns>The reversed string contained in a StringBuilder object.</returns>
	public static System.Text.StringBuilder ReverseString(System.Text.StringBuilder text)
	{
		char[] tmpChar = text.ToString().ToCharArray();
		System.Array.Reverse(tmpChar);
		return new System.Text.StringBuilder(new System.String(tmpChar));
	}


	/*******************************/
	/// <summary>
	/// This method returns the literal value received
	/// </summary>
	/// <param name="literal">The literal to return</param>
	/// <returns>The received value</returns>
	public static long Identity(long literal)
	{
		return literal;
	}

	/// <summary>
	/// This method returns the literal value received
	/// </summary>
	/// <param name="literal">The literal to return</param>
	/// <returns>The received value</returns>
	public static ulong Identity(ulong literal)
	{
		return literal;
	}

	/// <summary>
	/// This method returns the literal value received
	/// </summary>
	/// <param name="literal">The literal to return</param>
	/// <returns>The received value</returns>
	public static float Identity(float literal)
	{
		return literal;
	}

	/// <summary>
	/// This method returns the literal value received
	/// </summary>
	/// <param name="literal">The literal to return</param>
	/// <returns>The received value</returns>
	public static double Identity(double literal)
	{
		return literal;
	}

	/*******************************/
	/// <summary>
	/// Writes the exception stack trace to the received stream
	/// </summary>
	/// <param name="throwable">Exception to obtain information from</param>
	/// <param name="stream">Output sream used to write to</param>
	public static void WriteStackTrace(System.Exception throwable, System.IO.TextWriter stream)
	{
		stream.Write(throwable.StackTrace);
		stream.Flush();
	}

}
