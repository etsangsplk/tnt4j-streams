//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.12 at 03:47:04 PM EEST 
//

package com.jkool.tnt4j.streams.configure.jaxb;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * Defines Cron scheduler parameters.
 * 
 * 
 * <p>
 * Java class for ScheduleCronType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ScheduleCronType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="expression" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScheduleCronType")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-04-12T03:47:04+03:00", comments = "JAXB RI v2.2.4-2")
public class ScheduleCronType {

	@XmlAttribute(name = "expression", required = true)
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-04-12T03:47:04+03:00", comments = "JAXB RI v2.2.4-2")
	protected String expression;

	public ScheduleCronType() {

	}

	public ScheduleCronType(String expression) {
		this.expression = expression;
	}

	/**
	 * Gets the value of the expression property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-04-12T03:47:04+03:00", comments = "JAXB RI v2.2.4-2")
	public String getExpression() {
		return expression;
	}

	/**
	 * Sets the value of the expression property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-04-12T03:47:04+03:00", comments = "JAXB RI v2.2.4-2")
	public void setExpression(String value) {
		this.expression = value;
	}

}