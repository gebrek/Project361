package edu.uwm.cs361;


import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
//my new
@PersistenceCapable
public class Section implements Comparable<Section>{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String sectionid;
	@Persistent
	private String courseid;
	@Persistent
	private String units;
	@Persistent
	private String type;
	@Persistent
	private String section;
	@Persistent
	private String hours;
	@Persistent
	private String days;
	@Persistent
	private String dates;
	@Persistent
	private String instructor;
	@Persistent
	private String room;
	
	public Section(String secid, String crsid, String un, String ty, String sec,
			String hrs, String dys, String dts, String inst, String rm){
		sectionid = secid;
		courseid = crsid;
		units = un;
		type = ty;
		section = sec;
		hours = hrs;
		days = dys;
		dates = dts;
		instructor = inst;
		room = rm;
	}
	public Section(){
		sectionid = null;
		courseid = null;
		units = null;
		type = null;
		section = null;
		hours = null;
		days = null;
		dates = null;
		instructor = null;
		room = null;
	}
	
	/**
	 * @return key for datastore
	 */
	public Key getKey() {
		return key;
	}
	
	/**
	 * 
	 * @param key Key to set for datastore
	 */
	public void setKey(Key key) {
		this.key = key;
	}
	
	/**
	 * 
	 * @return Local only section ID
	 */
	public String getID() {
		return sectionid;
	}
	
	/**
	 * 
	 * @param sectionid Local section id to set
	 */
	public void setID(String sectionid) {
		this.sectionid = sectionid;
	}
	
	/**
	 * 
	 * @return Courses id (non local)
	 */
	public String getCourseid() {
		return courseid;
	}
	/**
	 * 
	 * @param courseid Non-local course id to set
	 */
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}
	
	/**
	 * 
	 * @return Number credits
	 */
	public String getUnits() {
		return units;
	}
	
	/**
	 * 
	 * @param units Number of credits
	 */
	public void setUnits(String units) {
		this.units = units;
	}
	
	/**
	 * 
	 * @return lec, lab or dis
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 
	 * @param type set with lec, lab, dis
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return returns this section number
	 */
	public String getSection() {
		return section;
	}
	
	/**
	 * 
	 * @param section number of section
	 */
	public void setSection(String section) {
		this.section = section;
	}
	
	/**
	 * 
	 * @return Hours sections meets
	 */
	public String getHours() {
		return hours;
	}
	
	/**
	 * 
	 * @param hours Sets sections meeting hours
	 */
	public void setHours(String hours) {
		this.hours = hours;
	}
	
	/**
	 * 
	 * @return Meeting days
	 */
	public String getDays() {
		return days;
	}
	
	/**
	 * 
	 * @param days Set meeting days
	 */
	public void setDays(String days) {
		this.days = days;
	}
	
	/**
	 * 
	 * @return Meeting dates
	 */
	public String getDates() {
		return dates;
	}
	/**
	 * 
	 * @param dates Set meeting dates
	 */
	public void setDates(String dates) {
		this.dates = dates;
	}
	/**
	 * 
	 * @return Assigned instructors name
	 */
	public String getInstructor() {
		return instructor;
	}
	/**
	 * 
	 * @param instructor Instructors full name
	 */
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	
	/**
	 * 
	 * @return meeting room location
	 */
	public String getRoom() {
		return room;
	}
	
	/**
	 * 
	 * @param room Set meeting room
	 */
	public void setRoom(String room) {
		this.room = room;
	}
	
	/**
	 * 
	 * @return Returns this sections as a formatted html line to fit our course list html
	 */
	public String toHtmlTR() {
		if (section.length() > 2)
			section = section.substring(0, 3);
		
		return String.format("<tr class='border_bottom'><td></td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>\n",
				section, units, hours, days, instructor, room);
	}

	/**
	 * Compares this section to s. Works like a standard compare, compare key = section number
	 */
	@Override
	public int compareTo(Section s) {
		
		String lhString = this.section;
		String rhString = s.getSection();
		
		if (lhString.length() < 2)
			lhString = "000";
		else
			lhString = lhString.substring(0, 3);

		if (rhString.length() < 2)
			rhString = "000";
		else
			rhString = rhString.substring(0, 3);
		
		int lhs = Integer.parseInt(lhString);
		int rhs = Integer.parseInt(rhString);
		
		if (lhs < rhs)
			return -1;
		else if (lhs > rhs)
			return 1;
		else
			return 0;
	}
}
