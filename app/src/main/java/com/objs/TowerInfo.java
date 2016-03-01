package com.objs;

import java.util.ArrayList;
import java.util.List;

public class TowerInfo {
	public 	Integer	total	;
	public 	List<Rows> 	rows	;
	public TowerInfo(){
		rows=new ArrayList<Rows>();
		}
	public class Rows{
		public String cityname;
		public String assetCode;
		public String assetName;
		public String assetSize;
		public String department;
		public String person;
		public String content;
		public String orderState;
		public Integer maintainType;
		public String maintainPerson;
		public String maintainDate;
		public String issuer;
		public String maintainNum;
	}

}
