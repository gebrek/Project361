package edu.uwm.cs361;


import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Section {
	// applying the same annotation to multiple values seems to only now have been 
	// supported in java 1.8, which is not what we're using
	// lol seriously. 
	
	// removed deprecated constructors
	
	@Persistent
	private Course course;
	@Persistent
	private String units;
	@Persistent
	private String designation;
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

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	/*public void setKey(Key key) {
		this.key = key;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
		pm.close();
	}
	*/
	
	public void setInstructor(String instructor) {
		this.instructor = instructor;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
		pm.close();
	}
	
	public Section(Course c, String un, String des, 
			String hr, String dy, String dts, String ins, String rm){
		course = c;
		units = un;
		designation = des;
		hours = hr;
		days = dy;
		dates = dts;
		instructor = ins;
		room = rm;
		
		key = KeyFactory.createKey(Section.class.getSimpleName(), designation);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
		pm.close();
	}

	// CLOS does OO better
	public Key key() {
		return key;
	}
	public String getUnits() {
		return units;
	}
	public String getDesignation() {
		return designation;
	}
	public String getType(){
		return designation.substring(0, 4);
	}
	public String getNumber() {
		return designation.replaceFirst(".*?(?=\\d)", "");
	}
	public String getHours() {
		return hours;
	}
	public String getDays() {
		return days;
	}
	public String getDates() {
		return dates;
	}
	public String getInstructor() {
		return instructor;
	}
	public String getRoom() {
		return room;
	}
	@Override
	public String toString() {
		return "Section [units=" + units + ", designation=" + designation
				+ ", hours=" + hours + ", days=" + days + ", dates=" + dates
				+ ", instructor=" + instructor + ", room=" + room + "]";
	}
	public String toHtmlTR() {
		return String.format("<tr class='border_bottom'><td></td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>\n",
				designation, units, hours, days, instructor, room);
	}
	
	//HQ Methods
	public String prettyKey() {
		String workingKey = this.key.toString();
		int quoteIndex = workingKey.indexOf("\"");
		int secondQuote = workingKey.indexOf("\"", quoteIndex);
		
		return "";
		
	}
	public Key convertPrettyKey() {
		
		return this.key;
	}
}
