package edu.uwm.cs361;

import java.util.Collections;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Course implements Comparable<Course>{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent(mappedBy = "course")
	private List<Section> sections;
	@Persistent
	private String courseid;
	@Persistent
	private String title;
	@Persistent
	private String number;
	
	public Course(String id, String ttl, String num, List<Section> secs){
		courseid = id;
		title = ttl;
		number = num;
		sections = secs;
	}
	public Course(){
		courseid = null;
		title = null;
		number = null;
		sections = null;
	}
	
	/**
	 * 
	 * @return Key for datastore
	 */
	public Key getKey() {
		return key;
	}
	
	/**
	 * 
	 * @return Course title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * 
	 * @param title String to set this courses title to
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<Section> getSections(){
		return this.sections;
	}
	public void setSections(List<Section> secs){
		this.sections = secs;
	}
	/**
	 * 
	 * @return Course id (locale number used for reference in datastore)
	 */
	public String getID() {
		return this.courseid;
	}
	
	/**
	 * 
	 * @param ID Set the course Id to this string
	 */
	public void setID(String ID) {
		this.courseid = ID;
	}
	
	/**
	 * 
	 * @return Courses number (ex CS 361 -- 361)
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * 
	 * @param number Set the courses number to this param
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	public void mergeSections(Course other){
		for(Section sn : this.sections){
			for(Section so : other.sections){
				if(so.edited && sn.equals(so)){
					this.sections.set(this.sections.indexOf(sn), so);
				}
			}
		}
	}
	
	/**
	 * 
	 * @return Returns this course as a string formatted to fit our pages course list
	 */
	public String toHtmlTable() {
		
		DatastoreServ ds = new DatastoreServ();
		
		String str = "";
		str = String.format("<tr><td>%s</td><td>%s</td></tr>\n", number ,title);
		List<Section> sections = ds.getSection(null);
		Collections.sort(sections);
		for(Section s : sections){
			if (s.getCourseid().equals(courseid))
			{
				str += s.toHtmlTR();
			}
		}
		return str;
	}
	
	/**
	 * Compares this course to c. Works like a standard compare, compare key = course number
	 */
	@Override
	public String toString() {
		return "Course [courseid=" + courseid + ", title=" + title
				+ ", sections=" + sections + "]";
	}
	@Override
	public boolean equals(Object o){
		Course c = (Course) o;
		return courseid.equals(c.courseid);
	}

	@Override
	public int compareTo(Course c) {
		
		String lhString = this.number;
		String rhString = c.getNumber();
		
		if(lhString == null || rhString == null) return 0;
		
		lhString = lhString.substring(0, 3);
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