package edu.uwm.cs361;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Course {
	@Persistent
	private String designation;
	@Persistent
	private String title;
	@Persistent(mappedBy = "course")
	private ArrayList<Section> sections;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
//	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private Key key;
	
	public Course(String des, String titl, ArrayList<Section> secs){
		designation = des;
		title = titl;
		sections = secs;
		
		key = KeyFactory.createKey(Course.class.getSimpleName(), designation);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
		pm.close();
	}

	// deprecated
	public Course(Entity e) {
		DemeritDatastoreService ds = new DemeritDatastoreService();
		String myKey = ds.getOurKey(e.getKey());
		designation = "COMPSCI-" +myKey; 
		title = e.getProperty(ds.COURSE_TITLE).toString();
//		keyfield = designation;
		
	}
	
	public Key key(){
		return key;
	}
	public String getDesignation() {
		return designation;
	}
	public String getNumber() {
		// gotta comply with DemeritDatastoreService.java
		return designation.replaceFirst(".*?(?=\\d)", "");
	}
	public String getTitle() {
		return title;
	}
	public ArrayList<Section> getSections() {
		return sections;
	}
	public void setSections(ArrayList<Section> secs){
		sections = secs;
	}
	@Override
	public String toString() {
		return "Course [designation=" + designation + ", title=" + title
				+ ", sections=" + sections + "]";
	}
	public String toHtmlTable() {
		String str = "";
		str = String.format("<tr><td>%s</td><td>%s</td></tr>\n",designation,title);
		for(Section s : sections){
			str += s.toHtmlTR();
		}
		return str;
	}
}
