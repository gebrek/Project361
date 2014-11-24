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
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public String getID() {
		return sectionid;
	}
	public void setID(String sectionid) {
		this.sectionid = sectionid;
	}
	public String getCourseid() {
		return courseid;
	}
	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	
	public String toHtmlTR() {
		return String.format("<tr class='border_bottom'><td></td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>\n",
				section, units, hours, days, instructor, room);
	}

	@Override
	public int compareTo(Section s) {
		
		String lhString = this.section;
		String rhString = s.getSection();
		
		if (lhString.length() < 3)
			lhString = "000";
		if (rhString.length() < 3)
			rhString = "000";
		
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
