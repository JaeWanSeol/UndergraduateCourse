import java.util.*;
// Bongki Moon (bkmoon@snu.ac.kr)

class dValue implements Comparable<dValue> {
	public int time;
	public Flight start;
	public String end;
	
	//constructor
	public dValue(String end) {
		time = Integer.MAX_VALUE;
		this.start = null;
		this.end = end;
	}
	
	@Override
	public int compareTo(dValue d) {
		return time - d.time;
	}
	
}

public class Planner {
	public ArrayList<Airport> portList;
	public ArrayList<ArrayList<Flight>> fltList;
	public HashMap<String, Integer> portToIdx;
	public HashMap<String, dValue> portTodValue;
	public int numOfAirport;
	
	
	
	// constructor
	public Planner(LinkedList<Airport> portList, LinkedList<Flight> fltList) {
		this.numOfAirport = portList.size();
		this.portList = new ArrayList<Airport>(numOfAirport);
		this.fltList = new ArrayList<ArrayList<Flight>>(numOfAirport);
		this.portToIdx = new HashMap<String, Integer>(numOfAirport);
		
		//make portList
		while(!portList.isEmpty()) {
			Airport temp = portList.poll();
			this.portList.add(temp);
		}
		//make portToIdx
		for(int i = 0 ; i < numOfAirport ; i++) {
			portToIdx.put(this.portList.get(i).port , i);
		}
		//make fltList
		for(int i = 0 ; i < numOfAirport ; i++) {
			this.fltList.add(new ArrayList<Flight>());
		}
		while(!fltList.isEmpty()) {
			Flight temp = fltList.poll();
			this.fltList.get(portToIdx.get(temp.src)).add(temp);
		}
		
	}
	
	public void makeDValue(ArrayList<Flight> fList, String departTime, String dest, dValue d) {
		int result = Integer.MAX_VALUE;
		
		
		for(int i = 0 ; i < fList.size() ; i++) {
			Flight tempflight = fList.get(i);
			if(tempflight.dest.compareTo(dest) == 0) {
				int flighttime;
				int departtime = Integer.parseInt(departTime);
				departtime = (departtime/100)*60 + departtime%100;
				int stime = Integer.parseInt(tempflight.stime);
				stime = (stime/100)*60 + stime%100;
				int dtime = Integer.parseInt(tempflight.dtime);
				dtime = (dtime/100)*60 + dtime%100;
				if(stime < departtime) {
					if(stime > dtime) {
						flighttime = 2880 - departtime + dtime;
					}
					else {
						flighttime = 1440 - departtime + dtime;
					}
				}
				else {
					if(stime > dtime) {
						flighttime = 1440 + dtime - departtime;
					}
					else {
						flighttime = dtime - departtime;
					}
				}
				
				if(flighttime < result) {
					result = flighttime;
					d.start = tempflight;
				}
				//System.out.println(tempflight.src + " " + tempflight.dest + "flighttime, departtime, stime, dtime is " + flighttime + " " + departtime + " " +stime + " " + dtime);
			}
		}
		//System.out.println("final flighttime(result) is " + result);
		if(result != Integer.MAX_VALUE) result = (result/60)*100 + result%60;
		d.time = result;
		return;
	}
	
	public int stringtimeToIntminute (String s) {
		int result = Integer.parseInt(s);
		result = (result/100)*60 + result%100;
		return result;
	}
	
	public String Stringtime(int m) {
		m = (m/60)*100 + m%60;
		return Integer.toString(m);
	}
	
	public Itinerary Schedule(String start, String end, String departure) {
		Itinerary itn = new Itinerary();
		ArrayList<String> setS = new ArrayList<String>(numOfAirport);
		PriorityQueue<dValue> _dValue = new PriorityQueue<dValue>();
		portTodValue = new HashMap<String, dValue>(numOfAirport-1); //except start
		HashMap<String, Boolean> VS = new HashMap<String, Boolean>();
		
		if(portToIdx.get(start) == null || portToIdx.get(end) == null) {
			return itn;
		}
		
		//initialize setS
		setS.add(start);
		VS.put(start, true);
		//initialize _dValue and make portTodValue
		for(int i = 0 ; i < numOfAirport ; i++) {
			String tempstr = portList.get(i).port;
			if(tempstr.compareTo(start) != 0) {
				dValue tempdValue = new dValue(tempstr);
				makeDValue(fltList.get(portToIdx.get(start)), departure, tempstr, tempdValue);
				if(tempdValue.time != Integer.MAX_VALUE) { _dValue.add(tempdValue); }
				portTodValue.put(tempstr, tempdValue);
				
			}
		}
		/*
		System.out.println("before di, dvalue");
		for(dValue e : _dValue) {
			if(e.start != null) System.out.println(e.start.src + e.start.dest + e.start.stime + e.start.dtime + " " +  e.end + " " + e.time);
			else System.out.println(e.end + " " + e.time);
		}
		System.out.println("before di, dvalue end");*/
		//dijkstra while loop
		int currenttime = stringtimeToIntminute(departure);
		while(!_dValue.isEmpty()) {
			
			dValue tempdvalue = _dValue.poll();
			String v = tempdvalue.end;
			if(v.compareTo(end) == 0) break;
			
			//S = S + v
			if(VS.get(v) == null) {
				setS.add(v);
				VS.put(v, true);
			}
			else if(tempdvalue.time == Integer.MAX_VALUE) {continue;}
			else { continue; }
			
			//current time = departure + v's connectTime
			currenttime = stringtimeToIntminute(portList.get(portToIdx.get(v)).connectTime) + stringtimeToIntminute(tempdvalue.start.dtime);
			currenttime = currenttime % 1440;
			//System.out.println("we are " + v + " and currenttime is " + Stringtime(currenttime));
					for(int j = 0 ; j < fltList.get(portToIdx.get(v)).size() ; j++ ) {
						Flight tempflight = fltList.get(portToIdx.get(v)).get(j);
						String u = tempflight.dest;
						if(VS.get(u) == null) {
							//System.out.println(v + " and " + u + " is connected");
							//System.out.println("edge connected " + tempflight.src + " to " + tempflight.dest);
							int dv = portTodValue.get(v).time;
							if(dv != Integer.MAX_VALUE) dv = (dv/100)*60 + dv%100;
							int wvu;
							int du = portTodValue.get(u).time;
							if(du != Integer.MAX_VALUE) du = (du/100)*60 + du%100;
							//currenttime as minute 0~1440
							if(currenttime > stringtimeToIntminute(tempflight.stime)) {
								if(stringtimeToIntminute(tempflight.stime) > stringtimeToIntminute(tempflight.dtime)) {
									wvu = 2880 - currenttime + stringtimeToIntminute(tempflight.dtime);
								}
								else {
									wvu = 1440 - currenttime + stringtimeToIntminute(tempflight.dtime);
								}
							}
							else {
								if(stringtimeToIntminute(tempflight.stime) > stringtimeToIntminute(tempflight.dtime)) {
									wvu = 1440 + stringtimeToIntminute(tempflight.dtime) - currenttime;
								}
								else {
									wvu = stringtimeToIntminute(tempflight.dtime) - currenttime;
								}
							}
							
							//System.out.println("dv wvu du is " + dv + " " + wvu + " " + du);
							if(dv!=Integer.MAX_VALUE && (dv + wvu + stringtimeToIntminute(portList.get(portToIdx.get(v)).connectTime) < du)) {
							
								
							//	System.out.println("dv wvu du is " + dv + " " + wvu + " " + du);
							//	System.out.println(tempflight.src + " " + tempflight.dest + " " + tempflight.stime + " " + tempflight.dtime);

								
								dValue tempdValue = new dValue(u);
								tempdValue.start = tempflight;
								tempdValue.time = dv + wvu + stringtimeToIntminute(portList.get(portToIdx.get(v)).connectTime);
								tempdValue.time = (tempdValue.time/60)*100 + tempdValue.time%60;
								_dValue.add(tempdValue);
								portTodValue.put(u, tempdValue);
							}
						}
						
					}
				}
			
		
		//System.out.println("arriv is " + end);
		dValue tempdValue = portTodValue.get(end);
		while(true) {
			if(tempdValue.start == null) {
				//System.out.println("currend arriv is " + tempdValue.end + " and tempdValue.start is null");
				break;
			}
			//System.out.println("who updated me is " + tempdValue.start.src + " " + tempdValue.start.dest);
			itn.flist.add(tempdValue.start);
			itn.found = true;
			if(tempdValue.start.src.compareTo(start) == 0) {
				//System.out.println("tempdValue's start's src = " + start);
				break;
			}
			tempdValue = portTodValue.get(tempdValue.start.src);
		}
		
		return itn;
	}
	
	
}

