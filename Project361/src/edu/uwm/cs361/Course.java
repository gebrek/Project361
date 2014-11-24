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
	
	@Persistent
	private String courseid;
	
	@Persistent
	private String title;
	
	@Persistent
	private String number;
	
	public Key getKey() {
		return key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getID() {
		return this.courseid;
	}
	public void setID(String ID) {
		this.courseid = ID;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
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
	
	@Override
	public int compareTo(Course c) {
		
		String lhString = this.number;
		String rhString = c.getNumber();
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