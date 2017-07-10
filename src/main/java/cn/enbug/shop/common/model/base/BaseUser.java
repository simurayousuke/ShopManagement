package cn.enbug.shop.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public M setUsername(java.lang.String username) {
		set("username", username);
		return (M)this;
	}

	public java.lang.String getUsername() {
		return get("username");
	}

	public M setSalt(java.lang.String salt) {
		set("salt", salt);
		return (M)this;
	}

	public java.lang.String getSalt() {
		return get("salt");
	}

	public M setPwd(java.lang.String pwd) {
		set("pwd", pwd);
		return (M)this;
	}

	public java.lang.String getPwd() {
		return get("pwd");
	}

	public M setUuid(java.lang.String uuid) {
		set("uuid", uuid);
		return (M)this;
	}

	public java.lang.String getUuid() {
		return get("uuid");
	}

	public M setAvator(java.lang.Integer avator) {
		set("avator", avator);
		return (M)this;
	}

	public java.lang.Integer getAvator() {
		return get("avator");
	}

	public M setPhone(java.lang.String phone) {
		set("phone", phone);
		return (M)this;
	}

	public java.lang.String getPhone() {
		return get("phone");
	}

	public M setEmailStatus(java.lang.Integer emailStatus) {
		set("email_status", emailStatus);
		return (M)this;
	}

	public java.lang.Integer getEmailStatus() {
		return get("email_status");
	}

	public M setEmail(java.lang.String email) {
		set("email", email);
		return (M)this;
	}

	public java.lang.String getEmail() {
		return get("email");
	}

}