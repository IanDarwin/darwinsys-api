package com.darwinsys.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Utilities for using choice items like "Modified in last..." and a list of { day, week, month...}.
 * Typical use is getDateRangeValues() to get the values, and getDateRangeLabels for the labels, when
 * displaying the choice item to the user, and pass the chosen value to getDateFromRange().
 * Alternately, if your UI allows components to be displayed directly (e.g., Swing), use the
 * public field dateRanges. For example, in Struts you might use this in a JSP:
 * <pre>
 *  &lt;jsp:useBean name="my.dates" class="...DateRangeUIHelper"/&gt;
 * 	&lt;html:select ...&gt;
 *  &lt;html:options name="my.date" property="dateRangeValues"
 *       labelName="my.date" labelProperty="dateRangeLabels"
 *       /&gt;
 *
 *
 *   // Swing Example:
 *   // Constructor:
 *   jComboBox.setListData(DateUtils.dateRanges);
 *   // Action Handler
 *   DateRangeUIHelper.Range r = (DateRangeUIHelpers.Range)jComboBox.getSelectedItem();
 *   Date startDate = DateRangeUIHelper.getDateFromRange(r.getChoiceValue());
 * </pre>
 * <p>For a runnable example, see test c.d.util.DateRangeUIHelper in the the darwinsys-api project.
 */
public class DateRangeUIHelper {

	/**
	 * Tiny data holder for a range like "7d" and its numeric equivalent.
	 */
	public static class Range {
		final String choiceValue;
		final String choiceLabel;
		final int days;
		public Range(String val, int days, String lab) {
			super();
			this.choiceValue = val;
			this.choiceLabel = lab;
			this.days = days;
		}
		public String getChoiceLabel() {
			return choiceLabel;
		}
		public String getChoiceValue() {
			return choiceValue;
		}
		public int getDays() {
			return days;
		}
		@Override
		public String toString() {
			return choiceLabel;
		}
	}
	/**
	 * The list of Range values that this class uses.
	 */
	static final Range[] dateRanges = {
		new Range("1d",   1, "day"),
		new Range("7d",   7, "week"),
		new Range("1m",  31, "month"),
		new Range("3m",  90, "three months"),
		new Range("6m", 183, "six months"),
		new Range("1y", 365, "year"),
	};

	public static Range[] getDateRanges() {
		return dateRanges.clone();
	}

	/** Return the &lt;choice&gt; values for use in a DropDown and in getDateFromRange(). */
	public static String[] getDateRangeValues() {
		String[] choices = new String[dateRanges.length];
		for (int i = 0; i < dateRanges.length; i++) {
			choices[i] = dateRanges[i].choiceValue;
		}
		return choices;
	}

	/** Return the &lt;choice&gt; labels for use in a DropDown and in getDateFromRange(). */
	public static String[] getDateRangeLabels() {
		String[] choices = new String[dateRanges.length];
		for (int i = 0; i < dateRanges.length; i++) {
			choices[i] = dateRanges[i].choiceLabel;
		}
		return choices;
	}

	public static Date getDateFromRange(String dropdownValue) {
		if (dropdownValue == null) {
			throw new IllegalArgumentException("Logic Error: input argument may not be null");
		}
		int nDays = -1;
		for (Range d : dateRanges) {
			if (dropdownValue.equals(d.choiceValue)) {
				nDays = d.days;
				break;
			}
		}
		if (nDays == -1) {
			throw new IllegalArgumentException(
				String.format("Logic Error: Days dropdown value %s invalid (not one we provided)", dropdownValue));
		}

		/** Today's date */
		Calendar now = Calendar.getInstance();

		// Add a "# of days" increment (really a decrement) to the existing Calendar object
		now.add(Calendar.DATE, -nDays);

		Date dateStart = now.getTime();
		// System.out.println("New date is " + dateStart);
		return dateStart;
	}
}
