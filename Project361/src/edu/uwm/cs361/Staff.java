package edu.uwm.cs361;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Staff {
	
	@Persistent
	private String email;
	@Persistent
	private String name;
	@Persistent//(mappedBy = "instructor")
	private ArrayList<String> sectionsTaught;
	@Persistent
	private ArrayList<String> officeHours;
	@Persistent
	private String permissions;
	@Persistent
	private String password;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String officeLocation;
	@Persistent
	private String officePhone;
	@Persistent
	private String homeAddress;
	@Persistent
	private String homePhone;
	
	/**
	 * Staff constructor
	 * 
	 * @param email String - staff email address
	 * @param name String - staff full name
	 * @param secs ArrayList<Section> - sections taught
	 * @param staffType String - Instructor / TA
	 * @param pass String - password
	 */
	public Staff(String email, String name, ArrayList<String> secs, String staffType, String pass){	
		this.email = email;
		this.name = name;
		sectionsTaught = secs;
		//officeHours = of;
		permissions = staffType;
		password = pass;
		
		officeLocation = "";
		officePhone = "";
		homeAddress = "";
		homePhone = "";
		
		key = KeyFactory.createKey(Staff.class.getSimpleName(), name);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
		pm.close();
	}
	
	/**
	 * 
	 * @return Key for datastore
	 */
	public String key() {
		return key.toString();
	}
	/**
	 * 
	 * @return string list of office hours
	 */
	public ArrayList<String> getOfficeHours(){
		return officeHours;
	}
	/**
	 * pushes argument to officeHours
	 * @param hours
	 */
	public void addOfficeHours(String hours){
		officeHours.add(hours);
	}
	/**
	 * removes argument from officeHours if it exists
	 * @param hours
	 */
	public void removeOfficeHours(String hours){
		officeHours.remove(hours);
	}
	/**
	 * tries to change an existing element of officeHours
	 * does nothing if oldHours does not exist in officeHours
	 * @param oldHours
	 * @param newHours
	 */
	public void modifyOfficeHours(String oldHours, String newHours){
		if(officeHours.indexOf(oldHours) >= 0)
			officeHours.set(officeHours.indexOf(oldHours), newHours);
	}
	/**
	 * empties officeHours
	 */
	public void removeOfficeHoursAll(){
		officeHours.clear();
	}
	/**
	 * getOfficeLoc
	 * 
	 * @return String containing office location
	 */
	public String getOfficeLoc() {
		return officeLocation;
	}
	
	/**
	 * sets office location to the provided parameter
	 * @param officeLoc New office location
	 */
	public void setOfficeLoc(String officeLoc) {
		this.officeLocation = officeLoc;
	}
	
	/**
	 * getOfficePhone
	 * 
	 * @return String containing office phone
	 */
	public String getOfficePhone() {
		return officePhone;
	}
	
	/**
	 * sets office phone number to phone param
	 * 
	 * @param phone New phone number
	 */
	public void setOfficePhone(String phone) {
		this.officePhone = phone;
	}
	
	/**
	 * getHomeAddress
	 * 
	 * @return String containing home address
	 */
	public String getHomeAddress() {
		return homeAddress;
	}
	
	/**
	 * sets home address to the address param
	 * @param address New address
	 */
	public void setHomeAddress(String address) {
		this.homeAddress = address;
	}
	
	/**
	 * getHomePhone
	 * 
	 * @return String containing home phone
	 */
	public String getHomePhone() {
		return homePhone;
	}
	
	/**
	 * sets home phone number the the provided number
	 * 
	 * @param phone New phone number
	 */
	public void setHomePhone(String phone) {
		this.homePhone = phone;	
	}
	
	/**
	 * getEmail
	 * 
	 * @return String containing email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * setEmail - sets staffs email to the email provided
	 * 
	 * @param email New email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * getName 
	 * 
	 * @return String containing staff's full name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * sets staff name to the provided name
	 * 
	 * @param name New name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * getSectionsTaught
	 * 
	 * @return ArrayList of sections taught by staff
	 */
	public ArrayList<String> getSectionsTaught() {
		return sectionsTaught;
	}
	
	/**
	 * sets the staffs sections taught, to the passed array list
	 * 
	 * @param sectionsTaught Array list with new sections taught
	 */
	public void setSectionsTaught(ArrayList<String> sectionsTaught) {
		this.sectionsTaught = sectionsTaught;
	}
	public void addSectionTaught(Section s){
		sectionsTaught.add(s.getCourseid() + " " + s.getSection());
	}
	/*
	public String getOfficeHours() {
		return officeHours;
	}
	public void setOfficeHours(String officeHours) {
		this.officeHours = officeHours;
	}
	*/
	
	/**
	 * getPermissions
	 * 
	 * @return returns a string indicating staff type
	 */
	public String getPermissions() {
		return permissions;
	}
	
	/**
	 * sets staff type the the permissions parameter
	 * 
	 * @param permissions String containing new staff type
	 */
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * getPassword
	 * 
	 * @return String containing staff's password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * sets staff's password to the newly provided password
	 * 
	 * @param password String containing new password 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getSkills() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
