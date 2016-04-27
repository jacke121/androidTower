package com.baseDao;
import java.io.Serializable;
import java.util.Date;

public class Areas implements Serializable {

		public Integer id;
		public String area;
		public Integer areastatus;
		public Integer count;
		public Integer okcount;
		public Date createtime;
		public Date updatetime;
		public Integer lifeStatus;
		public Long upgradeFlag;
		public String gongbian;
		public String quxian;
		public String qubian;
		public String danwei;


		public Integer getId() {
			return this.id;
		}

		public void setId(Integer id) {
			this.id = id;
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

		public Long getUpgradeFlag() {
			return this.upgradeFlag;
		}

		public void setUpgradeFlag(Long upgradeFlag) {
			this.upgradeFlag = upgradeFlag;
		}

		public String getGongbian() {
			return this.gongbian;
		}

		public void setGongbian(String gongbian) {
			this.gongbian = gongbian;
		}

		public String getQuxian() {
			return this.quxian;
		}

		public void setQuxian(String quxian) {
			this.quxian = quxian;
		}

		public String getQubian() {
			return this.qubian;
		}

		public void setQubian(String qubian) {
			this.qubian = qubian;
		}

		public String getDanwei() {
			return this.danwei;
		}

		public void setDanwei(String danwei) {
			this.danwei = danwei;
		}

}
