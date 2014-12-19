package edu.uwm.cs361;
//space comment
// cargo cult imports from CreateStaffServlet. clean up later
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.BaseDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import edu.uwm.cs361.ProjectServlet;

@SuppressWarnings("serial")
public class StaffHomeServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DatastoreServ ds = new DatastoreServ();
	 	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		page.banner(req,resp);
		page.courseListLayout(buildPage(),req,resp);
		page.menu(req,resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	/**
	 * Build the main page content 
	 * @return String containing built html code
	 */
	private String buildPage(){
		
		Staff guy = page.getCurrentUser();
		
		int hourPieces = 2;

		String days[] = {"Monday","Tuesday","Wednesday",
						"Thursday","Friday", "Saturday","Sunday"};
		
		int startTime = 4;
		int endTime = 23;
		
		
		ArrayList<Integer> officeForCalendar = new ArrayList<Integer>(); 
		if(guy.getOfficeHours() != null){
			
			
			for(String s : guy.getOfficeHours()) {
				
				int dayBump = -1;
				for(int n = 0; n < days.length; ++n) {
					if(s.contains(days[n].subSequence(0, 3))) dayBump = n;
				}
				
				String guyOfficeTime = s.substring(s.indexOf("> ")+1);
				
				String guyTimes[] = guyOfficeTime.split("--");
				
				String hourMinuteStart[] = guyTimes[0].split(":");
				String hourMinuteEnd[] = guyTimes[1].split(":");
				
				int hourStart = Integer.parseInt(hourMinuteStart[0].trim());
				int minuteStart = Integer.parseInt(hourMinuteStart[1].trim().substring(0, 2));
				int amPmStart = hourMinuteStart[1].contains("a") ? 0 : 1;
				int hourEnd = Integer.parseInt(hourMinuteEnd[0].trim());
				int minuteEnd = Integer.parseInt(hourMinuteEnd[1].trim().substring(0, 2));
				int amPmEnd = hourMinuteEnd[1].contains("a") ? 0 : 1;
				
				minuteStart = minuteStart >= 60/hourPieces ? 1 : 0;
				hourStart =  hourStart == 12 ? hourStart : hourStart + amPmStart * 12;
				
				int cellStart = (hourStart - startTime)*days.length*hourPieces + minuteStart*days.length + dayBump;
				
				minuteEnd = minuteEnd >= 60/hourPieces  ? 1 : 0;
				hourEnd =  hourEnd == 12 ? hourEnd : hourEnd + amPmEnd * 12;
				
				int cellEnd = (hourEnd - startTime)*days.length*hourPieces + minuteEnd*days.length + dayBump - days.length;
				
				for(int m = cellStart; m <= cellEnd; m+=days.length) {
					officeForCalendar.add(m);
				}
			}
		
			Collections.sort(officeForCalendar);
		}
		
		ArrayList<ArrayList<Integer>> sectionsForCalendar = new ArrayList<ArrayList<Integer>>();
		
		if(guy.getSectionsTaught() != null){
			
			for(Section s : guy.getSectionsTaught()) {
				
				ArrayList<Integer> curSectionNums = new ArrayList<Integer>();
				
				String secDays = s.getDays();
				String secTimes = s.getHours();
				
				String days2[] = {"M","T", "W", "R", "F"}; 
					
				if(secDays != null && secTimes != null) {
				
					for(int n = 0; n < days2.length; ++n) {
						
						if(secDays.contains(days2[n])) {
							
							int dayBump = n;
							
							String guyOfficeTime = secTimes;
							
							String guyTimes[] = guyOfficeTime.split("-");
							
							String hourMinuteStart[] = guyTimes[0].split(":");
							String hourMinuteEnd[] = guyTimes[1].split(":");
							
							int hourStart = Integer.parseInt(hourMinuteStart[0].trim());
							int minuteStart = Integer.parseInt(hourMinuteStart[1].trim().substring(0, 2));
							int amPmStart = hourMinuteStart[1].contains("A") ? 0 : 1;
							int hourEnd = Integer.parseInt(hourMinuteEnd[0].trim());
							int minuteEnd = Integer.parseInt(hourMinuteEnd[1].trim().substring(0, 2));
							int amPmEnd = hourMinuteEnd[1].contains("A") ? 0 : 1;
							
							minuteStart = minuteStart >= 60/hourPieces ? 1 : 0;
							hourStart =  hourStart == 12 ? hourStart : hourStart + amPmStart * 12;
							
							int cellStart = (hourStart - startTime)*days.length*hourPieces + minuteStart*days.length + dayBump;
							
							minuteEnd = minuteEnd >= 60/hourPieces  ? 1 : 0;
							hourEnd =  hourEnd == 12 ? hourEnd : hourEnd + amPmEnd * 12;
							
							int cellEnd = (hourEnd - startTime)*days.length*hourPieces + minuteEnd*days.length + dayBump - days.length;
							
							for(int m = cellStart; m <= cellEnd; m+=days.length) {
								curSectionNums.add(m);
							}
						}
					}
				}
				
				Collections.sort(curSectionNums);
				
				sectionsForCalendar.add(curSectionNums);
			}
			
		}
			
		
		
		String http = "";
		http += "<form id=\"ccf\">"
		+			"<div id=\"title-create-staff\">"
		+				"Weekly Calendar "
		+			"</div>"
		+		 	"<div id=\"sub1\">"
		+ 				"<table id=\"month\">"
		+					"<thead>"
		+						"<tr>"
		+							"<th class=\"side_time\">Time</th>";
		
		
		for(int i=0; i<days.length;++i) {
			
			http += "<th class=\"days\">"+days[i]+"</th>";
		}
		
		http +=					"</tr>"
		+					"</thead>"
		+					"<tbody class = \"events\" id=heq_schedule_body\">";
		
		int addedAny = -1;
		
		String timeBreak[] = {"00","30"};
		
		if(!officeForCalendar.isEmpty() || !sectionsForCalendar.isEmpty())
		{ 
			for(int i=startTime;i<endTime;++i)
			{
				for(int dub = 0; dub < hourPieces; ++dub) {
					
					int cur = officeForCalendar.isEmpty() ? -1 : officeForCalendar.get(0);
					
					int sectionLow = 10000;
					
					if(sectionsForCalendar.size() > 0) {
						for(ArrayList<Integer> aL : sectionsForCalendar) {
							if(aL.size() > 0 ) { 
								int myCurNum = aL.get(0);
								if(myCurNum < sectionLow) sectionLow = myCurNum;
							}
						}
					}
					
					int shallowNum = ( (i-startTime)*days.length*hourPieces + dub*days.length);
					
					if( (cur != -1 && cur - days.length < shallowNum) || sectionLow - days.length < shallowNum ) 
					{
						addedAny = 0;
						
						http += "<tr>";
						
						http += "<td class=\"times\">";
						int displayTime = i > 12 ? i - 12 : i;
						String amPm = i >= 12 ? "PM" : "AM";
						
						http += displayTime+":"+timeBreak[dub]+amPm+"</td>";
						
						for(int j = 0; j < days.length; ++j) {
							
							http += "<td class=\"event_details\">";
							
							int num = ( (i-startTime)*days.length*hourPieces + j + dub*days.length);
							
							cur = officeForCalendar.size() > 0 ? officeForCalendar.get(0) : -1;
							
							if(num == cur) {
								
								officeForCalendar.remove(0);
								
								http += "Office hours";
								
								
							} else { 
								
								for(int v = 0; v < sectionsForCalendar.size(); ++v) {
									
									ArrayList<Integer> aL = sectionsForCalendar.get(v);
									
									if(aL.size() > 0 && aL.get(0) == num) {
										
										ArrayList<Section> secs = guy.getSectionsTaught();
										
										Section curSec = secs.get(v);
										
										String curTitle = curSec.getCourseid();
										
										http += curTitle.substring(8);
										
										aL.remove(0);
									}
								}
							}
							
							http+= "</td>";
						}
						
						http += "</tr>";
					} else { 
							if(addedAny != -1 && addedAny < 5) http += "<tr><td class=\"littlecells\"></td> </tr>"; addedAny++; 
					}
				}
			}
		}
		
		http +=					"</tbody>"
		+					"</table>"
		+			"</div>"
		+		"</form>";
		return http;
	}

}
