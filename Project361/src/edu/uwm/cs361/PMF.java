package edu.uwm.cs361;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;



public class PMF {
	
	private static PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private PMF() {}
	
	/** 
	 * @return Persistence manager factory for datastore methods
	 */
	public static PersistenceManagerFactory get(){
		return PMF;
	}
}
