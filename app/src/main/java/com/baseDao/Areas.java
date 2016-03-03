package com.baseDao;
import java.util.Date;

public class Areas {

		public Integer id;
		public String city;
		public String area;
		public Integer areastatus;
		public Integer count;
		public Integer okcount;
		public Date createtime;
		public Date updatetime;
		public Integer lifeStatus;
		public Integer upgradeFlag;


		public Integer getId() {
			return this.id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getCity() {
			return this.city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getArea() {
			return this.area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public Integer getAreastatus() {
			return this.areastatus;
		}

		public void setAreastatus(Integer areastatus) {
			this.areastatus = areastatus;
		}

		public Integer getCount() {
			return this.count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public Integer getOkcount() {
			return this.okcount;
		}

		public void setOkcount(Integer okcount) {
			this.okcount = okcount;
		}

		public Date getCreatetime() {
			return this.createtime;
		}

		public void setCreatetime(Date createtime) {
			this.createtime = createtime;
		}

		public Date getUpdatetime() {
			return this.updatetime;
		}

		public void setUpdatetime(Date updatetime) {
			this.updatetime = updatetime;
		}

		public Integer getLifeStatus() {
			return this.lifeStatus;
		}

		public void setLifeStatus(Integer lifeStatus) {
			this.lifeStatus = lifeStatus;
		}

		public Integer getUpgradeFlag() {
			return this.upgradeFlag;
		}

		public void setUpgradeFlag(Integer upgradeFlag) {
			this.upgradeFlag = upgradeFlag;
		}

}
