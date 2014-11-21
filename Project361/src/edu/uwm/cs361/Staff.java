package edu.uwm.cs361;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Staff {
	// email name sectionsTaught officeHours permissions password
	@Persistent
	private String email;
	@Persistent
	private String name;
	@Persistent(mappedBy = "instructor")
	private ArrayList<Section> sectionsTaught;
	@Persistent
	private String officeHours;
	@Persistent
	private String permissions;
	@Persistent
	private String password;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public Staff(String em, String na, ArrayList<Section> secs, String of, String perm, String pass){
		email = em;
		name = na;
		sectionsTaught = secs;
		officeHours = of;
		permissions = perm;
		password = pass;
		
		key = KeyFactory.createKey(Staff.class.getSimpleName(), name);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
		pm.close();
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Section> getSectionsTaught() {
		return sectionsTaught;
	}
	public void setSectionsTaught(ArrayList<Section> sectionsTaught) {
		this.sectionsTaught = sectionsTaught;
	}
	public String getOfficeHours() {
		return officeHours;
	}
	public void setOfficeHours(String officeHours) {
		this.officeHours = officeHours;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
