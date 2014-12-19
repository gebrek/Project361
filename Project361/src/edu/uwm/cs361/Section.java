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
	private Key instructor;
	@Persistent
	private String fallbackInstructor;
	@Persistent
	private String room;
	@Persistent
	private Course course;
	@Persistent
	public boolean edited;
	
	public Section(String secid, String crsid, String un, String ty, String sec,
			String hrs, String dys, String dts, Staff inst, String fbinst, String rm, Course crs){
		sectionid = secid;
		courseid = crsid;
		units = un;
		type = ty;
		section = sec;
		hours = hrs;
		days = dys;
		dates = dts;
		room = rm;
		course = crs;
		edited = false;
		fallbackInstructor = fbinst;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			instructor = inst.getKey();
		}catch(Exception e){
		}
		pm.close();
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
		course = null;
		edited = false;
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
	public Staff getInstructor() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			return (Staff) pm.getObjectById(Staff.class, instructor);
		} catch(Exception e) {
			return null;
		}
		
	}
	/**
	 * 
	 * @param instructor Instructors full name
	 */
	public void setInstructor(Staff instructor) {
		if(instructor==null){
			this.instructor = null;
			return;
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Staff ins = (Staff) pm.getObjectById(Staff.class, instructor.getKey());
		this.instructor = ins.getKey();
		pm.close();
	}
	public String getInstructorName(){
		if(instructor == null){
			return fallbackInstructor;
		}else{
			try{
				PersistenceManager pm = PMF.get().getPersistenceManager();
				Staff inst = (Staff) pm.getObjectById(Staff.class, instructor);
				return inst.getName();
			}catch(Exception e){
				return fallbackInstructor;
			}
		}
	}
	public void setFallbackInstructor(String fbinst){
		fallbackInstructor = fbinst;
	}
	public String getFallbackInstructor(){
		return fallbackInstructor;
	}
	/**
	 * 
	 * @return meeting room location
	 */
	public String getRoom() {
		return room;
	}
	public Course getCourse(){
		return course;
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
				section, units, hours, days, getInstructorName(), room);
	}
	public String toHtmlTRwithCourse(){
		if (section.length() > 2)
			section = section.substring(0, 3);
		
		return String.format("<tr class='border_bottom'><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>\n",
				courseid.substring(8), section, units, hours, days, getInstructorName(), room);
	}
	@Override 
	public boolean equals(Object o){
		Section s = (Section) o;
		return (courseid.equals(s.courseid) && sectionid.equals(s.sectionid));
	}
	@Override
	public String toString() {
		return "Section [key=" + key + ", sectionid=" + sectionid
				+ ", courseid=" + courseid + ", units=" + units + ", type="
				+ type + ", section=" + section + ", hours=" + hours
				+ ", days=" + days + ", dates=" + dates + ", instructor="
				+ getInstructorName() + ", room=" + room + "]\n";
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
