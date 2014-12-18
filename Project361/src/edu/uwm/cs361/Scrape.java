package edu.uwm.cs361;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class Scrape {

	private static DatastoreServ _ds = new DatastoreServ();
	
	private static int _sectionID = 1;
	
	/**
	 * Processes all courses at a hard-coded hyperlink
	 * @throws IOException
	 */
	public static void getCourseListandStore() throws IOException{
		_ds.deleteCourses();
		getURL("http://www4.uwm.edu/schedule/index.cfm?a1=subject_details&subject=COMPSCI&strm=2149");
	}
	
	/**
	 * Gets a buffer containing all the html form an url
	 * @param url Url to get content from
	 * @throws IOException
	 */
	private static void getURL(String url) throws IOException {
		URL place = new URL(url);
		String buf = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(place.openStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			if(inputLine.trim().isEmpty()) {
				continue;
			}
			buf += inputLine;
		}
		in.close();
		ArrayList<Course> crs = getAllCourses(buf);
//		List<Course> old = _ds.getAllCourses();
//		for(Course cn : crs){
//			for(Course co : old){
//				if(cn.equals(co)){
//					cn.mergeSections(co);
//				}
//			}
//		}
//		_ds.deleteCourses();
		_ds.addCourseAll(crs);
	}
	
	/*
	 * scraping repeatedly narrows the scope of text being processed until it reaches 
	 * a portion which may easily be read in as fields for creating objects. the
	 * overall structure of the page is as follows
	 * * (head
	 * *   ((course1 ID title
	 * *      ((section1 <fields>)
	 * *       (section2 <fields>)
	 * *       ...))
	 * *    (course2 ID title
	 * *      ((section1 <fields>)
	 * *       ...))
	 * *    ...)
	 * *   tail)
	 * 
	 * the process is relatively simple. 
	 * getAllCourses discards head and tail and calls getCourseAttributes on everything that looks like a course
	 * getCourseAttributes gathers information about the course including its sections via getCourseSections
	 * getCourseSections is similar to getAllCourses; calling getSectionAttributes on everything that looks like a section
	 * by the time getSectionAttributes is called, the text being parsed is regular and small enough that we can simply
	 * 		construct a Section based on the content of the td tags 
	 * that Section is then returned and the next one processed until no more remain
	 * then the Course including those Sections is returned and the next one processed until no more remain
	 */
	
	/**
	 * @param text
	 * @return list of all courses scraped
	 */
	private static ArrayList<Course> getAllCourses(String text){
		Pattern pattern = Pattern.compile("<td class=\"body copy\">\\s*<span class=\"subhead\">.*?</div>\\s*</td>\\s*</tr>\\s*<tr>");
		Matcher matcher = pattern.matcher(text);
		ArrayList<String> rawCourses = new ArrayList<String>();
		while(matcher.find()){
			rawCourses.add(matcher.group());
		}
		ArrayList<Course> courses = new ArrayList<Course>();
		for(String s : rawCourses){
			courses.add(courses.size(),getCourseAttributes(s));
		}
		return courses;
	}
	/**
	 * @param course
	 * @return
	 */
	private static Course getCourseAttributes(String course){
		Pattern pattern = Pattern.compile("<span class=\"subhead\">.*?</span>");
		Matcher matcher = pattern.matcher(course);
		matcher.find();
		String[] temp = removeTags(matcher.group()).split(":");
		String[] attr = new String[2];
		attr[0] = temp[0].trim();
		attr[1] = temp[1].trim();
		//COMPSCI 101(111)
		//COMPSCI-201
		Course c = new Course(attr[0],attr[1],attr[0].substring(8, 11),null);
		c.setSections(getCourseSections(course,c));
		return c;
	}
	/**
	 * @param course
	 * @param crs
	 * @return
	 */
	private static ArrayList<Section> getCourseSections(String course, Course crs){
		Pattern pattern = Pattern.compile("<tr class=\"body copy\".*?>.*?</tr>");
		Matcher matcher = pattern.matcher(course);
		ArrayList<String> rawSections = new ArrayList<String>();
		while(matcher.find()){
			rawSections.add(matcher.group());
		}
		ArrayList<Section> secs = new ArrayList<Section>();
		for(String s : rawSections){
			if(getSectionAttributes(s,crs)==null) continue;
			secs.add(secs.size(),getSectionAttributes(s,crs));
		}
		return secs;
	}
	/**
	 * @param section
	 * @param crs
	 * @return
	 */
	private static Section getSectionAttributes(String section, Course crs) {
		Pattern pattern = Pattern.compile("<td style.*?>.*?</td>");
		Matcher matcher = pattern.matcher(section);
		String[] attributes = new String[11]; 
		// all sections have 11 <td> tags though not all are needed
		for(int i=0; i<11 ; i++){
			matcher.find();
			attributes[i] = removeTags(matcher.group()).trim();
		}
		// a couple sections are stubs that only contain less than minimum information
		if(attributes[3].equals("&nbsp;")){
			return null;
		}
		return new Section(attributes[3],crs.getID(),attributes[2],attributes[3].substring(0,3),
				attributes[3].substring(attributes[3].length()-3, attributes[3].length()),
				attributes[5],attributes[6],attributes[7],null,attributes[8],attributes[9],crs);
	}
	
	/**
	 * Removes unnecessary tags from line
	 * @param line
	 */
	private static String removeTags(String line){
		// who can see anything with all those angle brackets everywhere?
		return line.replaceAll("<[^<>]*>", ""); // regex a neat
	}
}
