package com.baseDao;
import java.io.Serializable;
import java.util.Date;

public class Biao implements Serializable {

		public Integer id;
		public String name;
		public String code;
		public Integer gantaid;
		public Integer taiquid;
		public String yunxing;
		public String zuobiao;
		public Integer level;
		public Date createtime;
		public Date updatetime;
		public Integer lifeStatus;
		public Long upgradeFlag;
		public String areaname;
		public String danwei;


		public Integer getId() {
			return this.id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return this.code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Integer getGantaid() {
			return this.gantaid;
		}

		public void setGantaid(Integer gantaid) {
			this.gantaid = gantaid;
		}

		public Integer getTaiquid() {
			return this.taiquid;
		}

		public void setTaiquid(Integer taiquid) {
			this.taiquid = taiquid;
		}

		public String getYunxing() {
			return this.yunxing;
		}

		public void setYunxing(String yunxing) {
			this.yunxing = yunxing;
		}

		public String getZuobiao() {
			return this.zuobiao;
		}

		public void setZuobiao(String zuobiao) {
			this.zuobiao = zuobiao;
		}

		public Integer getLevel() {
			return this.level;
		}

		public void setLevel(Integer level) {
			this.level = level;
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

		public String getAreaname() {
			return this.areaname;
		}

		public void setAreaname(String areaname) {
			this.areaname = areaname;
		}

		public String getDanwei() {
			return this.danwei;
		}

		public void setDanwei(String danwei) {
			this.danwei = danwei;
		}

}
