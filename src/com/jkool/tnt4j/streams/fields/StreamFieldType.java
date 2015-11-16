/*
 * Copyright (c) 2015 jKool, LLC. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * jKool, LLC. ("Confidential Information").  You shall not disclose
 * such Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with jKool, LLC.
 *
 * JKOOL MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JKOOL SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * CopyrightVersion 1.0
 *
 */

package com.jkool.tnt4j.streams.fields;

import com.jkool.tnt4j.streams.utils.StreamTimestamp;
import com.nastel.jkool.tnt4j.core.OpCompCode;
import com.nastel.jkool.tnt4j.core.OpLevel;
import com.nastel.jkool.tnt4j.core.OpType;

/**
 * <p>
 * Defines the set of item fields supported by jKool Database Access API.
 * </p>
 * <p>
 * Fields should be specified using the defined label instead of the enumeration
 * name.
 * </p>
 *
 * @version $Revision: $
 */
public enum StreamFieldType {
	/**
	 * Name of application associated with the activity.
	 */
	ApplName(String.class),

	/**
	 * Host name of server to associate with activity.
	 */
	ServerName(String.class),

	/**
	 * IP Address of server to associate with activity.
	 */
	ServerIp(String.class),

	/**
	 * Name to assign to activity entry. Examples are operation, method, API
	 * call, event, etc.
	 */
	EventName(String.class),

	/**
	 * Type of activity - Value must match values in
	 * {@link com.nastel.jkool.tnt4j.core.OpType} enumeration.
	 */
	EventType(Enum.class),

	/**
	 * Time action associated with activity started.
	 */
	StartTime(StreamTimestamp.class),

	/**
	 * Time action associated with activity ended.
	 */
	EndTime(StreamTimestamp.class),

	/**
	 * Elapsed time of the activity.
	 */
	ElapsedTime(Long.class),

	/**
	 * Identifier of process where activity event has occurred.
	 */
	ProcessID(Integer.class),

	/**
	 * Identifier of thread where activity event has occurred.
	 */
	ThreadID(Integer.class),

	/**
	 * Indicates completion status of the activity - Value must match values in
	 * {@link com.nastel.jkool.tnt4j.core.OpCompCode} enumeration.
	 */
	CompCode(Enum.class),

	/**
	 * Numeric reason/error code associated with the activity.
	 */
	ReasonCode(Integer.class),

	/**
	 * Error/exception message associated with the activity.
	 */
	Exception(String.class),

	/**
	 * Indicates completion status of the activity - Value can either be label
	 * from {@link com.nastel.jkool.tnt4j.core.OpLevel} enumeration or a numeric
	 * value.
	 */
	Severity(Enum.class),

	/**
	 * Location that activity occurred at.
	 */
	Location(String.class),

	/**
	 * Identifier used to correlate/relate activity entries to group them into
	 * logical entities.
	 */
	Correlator(String.class),

	/**
	 * User-defined label to associate with the activity, generally for locating
	 * activity.
	 */
	Tag(String.class),

	/**
	 * Name of user associated with the activity.
	 */
	UserName(String.class),

	/**
	 * Name of resource associated with the activity.
	 */
	ResourceName(String.class),

	/**
	 * User data to associate with the activity.
	 */
	Message(String.class),

	/**
	 * Identifier used to uniquely identify the data associated with this
	 * activity.
	 */
	TrackingId(String.class),

	/**
	 * Length of activity event message data.
	 */
	MsgLength(Integer.class),

	/**
	 * MIME type of activity event message data.
	 */
	MsgMimeType(String.class),

	/**
	 * Encoding of activity event message data.
	 */
	MsgEncoding(String.class),

	/**
	 * CharSet of activity event message data.
	 */
	MsgCharSet(String.class),

	/**
	 * Activity event category name.
	 */
	Category(String.class),

	/**
	 * User-defined value associated with the activity (e.g. monetary value).
	 */
	Value(String.class);

	private Class dataType;

	private StreamFieldType(Class type) {
		this.dataType = type;
	}

	/**
	 * Gets the data type that this field's values are represented in.
	 *
	 * @return field data type
	 */
	public Class getDataType() {
		return dataType;
	}

	/**
	 * For fields that are {@link Enum}s, gets the enumeration class defining
	 * the set of possible values for the field.
	 *
	 * @return enumeration class for field, or {@code null} if this field is not
	 *         an enumeration
	 */
	public Class<? extends Enum<?>> getEnumerationClass() {
		if (dataType != Enum.class) {
			return null;
		}

		switch (this) {
		case Severity:
			return OpLevel.class;
		case EventType:
			return OpType.class;
		case CompCode:
			return OpCompCode.class;

		default:
			throw new UnsupportedOperationException(this + " does not have a defined enumeration set");
		}
	}

	/**
	 * For fields that are {@link Enum}s, gets the numeric value for the
	 * enumeration constant with the specified name.
	 *
	 * @param enumLabel
	 *            name of enumeration constant
	 *
	 * @return ordinal value for enumeration with specified name, or {@code -1}
	 *         if this field is not an enumeration
	 *
	 * @throws UnsupportedOperationException
	 *             if this field is not an enumeration
	 * @throws IllegalArgumentException
	 *             if enumLabel is not a valid enumeration label
	 */
	public int getEnumValue(String enumLabel) {
		if (dataType != Enum.class) {
			return -1;
		}

		switch (this) {
		case Severity:
			return OpLevel.valueOf(enumLabel).ordinal();

		case EventType:
			return OpType.valueOf(enumLabel).ordinal();

		case CompCode:
			return OpCompCode.valueOf(enumLabel).ordinal();

		default:
			throw new UnsupportedOperationException(this + " does not have a defined enumeration set");
		}
	}

	/**
	 * For fields that are {@link Enum}s, gets the enumeration with the
	 * specified name.
	 *
	 * @param enumLabel
	 *            name of enumeration constant
	 *
	 * @return enumeration constant, or {@code null} if this field is not an
	 *         enumeration
	 *
	 * @throws IllegalArgumentException
	 *             if ordinal is not a valid enumeration value
	 */
	public Enum<?> getEnum(String enumLabel) {
		if (dataType != Enum.class) {
			return null;
		}

		switch (this) {
		case Severity:
			return OpLevel.valueOf(enumLabel.toUpperCase());

		case EventType:
			return OpType.valueOf(enumLabel.toUpperCase());

		case CompCode:
			return OpCompCode.valueOf(enumLabel.toUpperCase());

		default:
			throw new UnsupportedOperationException(this + " does not have a defined enumeration set");
		}
	}

	/**
	 * For fields that are {@link Enum}s, gets the name of the enumeration
	 * constant with the specified ordinal value.
	 *
	 * @param value
	 *            value for enumeration
	 *
	 * @return enumLabel name of enumeration constant, or {@code null} if this
	 *         field is not an enumeration
	 *
	 * @throws IllegalArgumentException
	 *             if ordinal is not a valid enumeration value
	 */
	public String getEnumLabel(int value) {
		if (dataType != Enum.class) {
			return null;
		}

		switch (this) {
		case Severity:
			return OpLevel.valueOf(value).toString();
		case EventType:
			return OpType.valueOf(value).toString();
		case CompCode:
			return OpCompCode.valueOf(value).toString();

		default:
			throw new UnsupportedOperationException(this + " does not have a defined enumeration set");
		}
	}

	/**
	 * For fields that are {@link Enum}s, gets the enumeration with the
	 * specified ordinal value.
	 *
	 * @param value
	 *            value for enumeration
	 *
	 * @return enumeration constant, or {@code null} if this field is not an
	 *         enumeration
	 *
	 * @throws IllegalArgumentException
	 *             if ordinal is not a valid enumeration value
	 */
	public Enum<?> getEnum(int value) {
		if (dataType != Enum.class) {
			return null;
		}

		switch (this) {
		case Severity:
			return OpLevel.valueOf(value);
		case EventType:
			return OpType.valueOf(value);
		case CompCode:
			return OpCompCode.valueOf(value);

		default:
			throw new UnsupportedOperationException(this + " does not have a defined enumeration set");
		}
	}

	/**
	 * Gets the field enumeration object based on the enumeration's ordinal
	 * value.
	 *
	 * @param ordinal
	 *            enumeration ordinal value
	 *
	 * @return field type enumeration object
	 *
	 * @throws IndexOutOfBoundsException
	 *             if ordinal value is outside the range of enumeration ordinal
	 *             values
	 */
	public static StreamFieldType getType(int ordinal) {
		StreamFieldType[] enums = StreamFieldType.values();
		if (ordinal < 0 || ordinal >= enums.length) {
			throw new IndexOutOfBoundsException(
					"Invalid StreamFieldType ordinal value '" + ordinal + "' (range: 0-" + (enums.length - 1) + ")");
		}
		return enums[ordinal];
	}
}
