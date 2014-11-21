package edu.uwm.cs361;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Staff {
	// email name sectionsTaught officeHours permissions password
	@Persistent
	private String email;
	@Persistent
	private String name;
	@Persistent
	private ArrayList<Section> sectionsTaught;
	@Persistent
	private String officeHours;
	@Persistent
	private String permissions;
	@Persistent
	private String password;
	
	
}
