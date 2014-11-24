package edu.uwm.cs361;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class DatastoreServ {
	
	private DatastoreService ds = null;
	private String adminPassword = "admin";
	
	private PersistenceManager _pm = PMF.get().getPersistenceManager();
	
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String password) {
		adminPassword = password;
	}
	
	public DatastoreServ() {
		ds = DatastoreServiceFactory.getDatastoreService();
	}
	public DatastoreService getDatastore(){
		return ds;
	}
	
	public void createStaff(Staff s){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(s);
		}finally{
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Course> getCourse(String query){
		
		Query q = _pm.newQuery(Course.class);
		
		if(query != null) {
			
			q.setFilter(query);
		}
		
		List<Course> courseList = (List<Course>)q.execute();
		Collections.sort(courseList);
		return courseList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Section> getSection(String query){
		
		Query q = _pm.newQuery(Section.class);
		
		if(query != null) {
			
			q.setFilter(query);
		}
		
		return (List<Section>) q.execute() ;
	}
	
	@SuppressWarnings("unchecked")
	public List<Course> getAllCourses(){
		
		return getCourse(null) ;
	}
	public ArrayList<Staff> getAllStaff(){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Extent<Staff> e = pm.getExtent(Staff.class);
		ArrayList<Staff> ss = new ArrayList<Staff>();
		for(Staff s : e){
			ss.add(s);
		}
		return ss;
	}
	
	public void addCourse(String ID, String title, String number) {
		
		Course course = new Course();
		
		course.setID(ID);
		course.setTitle(title);
		course.setNumber(number);
		
		_pm.makePersistent(course);
	}
	
	public void addSection(String sectionid, String courseid, String units,
			String designation, String hours, String days,
			String dates, String slurpInstructor, String room) {
		
		String[] temp = designation.split(" ");
		
		Section section = new Section();
		section.setID(sectionid);
		section.setCourseid(courseid);
		section.setDates(dates);
		section.setDays(days);
		section.setHours(hours);
		section.setInstructor("");
		section.setRoom(room);
		section.setType(temp.length == 2 ? temp[0] : "");
		section.setSection(temp.length == 2 ? temp[1] : "");
		section.setUnits(units);
		
		_pm.makePersistent(section);
	}
	
	public void deleteCourses() {
		
		List<Course> courses = getCourse(null);
		List<Section> sections = getSection(null);
		
		_pm.deletePersistentAll(courses);
		_pm.deletePersistentAll(sections);
	}
	
	public void editSection(String sectionid, String staff) {
		
		Section section = getSection("sectionid=='"+sectionid+"'").get(0);
		
		section.setInstructor(staff);
		
		_pm.makePersistent(section);
	}
	
}
