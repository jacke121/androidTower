package note.dao;
import java.util.Date;

public class Note {

		public Integer id;
		public Integer parentid;
		public String username;
		public Integer levels;
		public String types;
		public String name;
		public String title;
		public String content;
		public String orders;
		public Date createtime;
		public Date updatetime;
		public Integer lifestatus;
		public Integer upgradeflag;


		public Integer getId() {
			return this.id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getParentid() {
			return this.parentid;
		}

		public void setParentid(Integer parentid) {
			this.parentid = parentid;
		}

		public String getUsername() {
			return this.username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public Integer getLevels() {
			return this.levels;
		}

		public void setLevels(Integer levels) {
			this.levels = levels;
		}

		public String getTypes() {
			return this.types;
		}

		public void setTypes(String types) {
			this.types = types;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return this.content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getOrders() {
			return this.orders;
		}

		public void setOrders(String orders) {
			this.orders = orders;
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

		public Integer getLifestatus() {
			return this.lifestatus;
		}

		public void setLifestatus(Integer lifestatus) {
			this.lifestatus = lifestatus;
		}

		public Integer getUpgradeflag() {
			return this.upgradeflag;
		}

		public void setUpgradeflag(Integer upgradeflag) {
			this.upgradeflag = upgradeflag;
		}

}
