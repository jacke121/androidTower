package note.dao;
import java.util.Date;

public class Users {

		public Integer id;
		public String username;
		public String password;
		public String repassword;
		public Date createtime;
		public String autologin;
		public Date updatetime;
		public Integer countlogin;
		public Integer lifestatus;
		public Integer upgradeflag;


		public Integer getId() {
			return this.id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getUsername() {
			return this.username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return this.password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRepassword() {
			return this.repassword;
		}

		public void setRepassword(String repassword) {
			this.repassword = repassword;
		}

		public Date getCreatetime() {
			return this.createtime;
		}

		public void setCreatetime(Date createtime) {
			this.createtime = createtime;
		}

		public String getAutologin() {
			return this.autologin;
		}

		public void setAutologin(String autologin) {
			this.autologin = autologin;
		}

		public Date getUpdatetime() {
			return this.updatetime;
		}

		public void setUpdatetime(Date updatetime) {
			this.updatetime = updatetime;
		}

		public Integer getCountlogin() {
			return this.countlogin;
		}

		public void setCountlogin(Integer countlogin) {
			this.countlogin = countlogin;
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
