package com.objs;

import java.util.ArrayList;
import java.util.List;

public class AreaInfo {
	public 	Integer	total	;
	public 	List<Rows> 	rows	;
	public AreaInfo(){
		rows=new ArrayList<Rows>();
		}
	public class Rows{
		public String cityname;
		public String areaname;
		public String assetSize;
		public String department;
		public String content;
		public String orderState;
	}

}
