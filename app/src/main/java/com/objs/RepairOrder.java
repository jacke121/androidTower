package com.objs;

import java.util.ArrayList;
import java.util.List;

public class RepairOrder {
	
	public 	Integer	total	;

	public 	List<Rows> 	rows	;
	public	RepairOrder(){
		rows=new ArrayList<Rows>();
		}
	public class Rows{
		public String maintainNum;
		public String assetCode;
		public String assetName;
		public String assetSize;
		public String yt;
		public String department;
		public String person;
		public Integer maintainType;
		public String content;
		public String applyDate;
		public String issuer;
		public String maintainPerson;
		public String maintainDate;
		public String orderState;
	}

}
