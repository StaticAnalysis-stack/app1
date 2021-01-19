package com.nttdata.ojt.user.domain;

import java.util.Date;
import java.util.List;

import com.nttdata.ojt.global.domain.AbstractResponse;
import com.nttdata.ojt.user.entity.DAOUser;

public class UserListRespone extends AbstractResponse {
	List<DAOUser> users = null;
	Date timeRefreshed = null;
	
	public List<DAOUser> getUsers() {
		return users;
	}
	public void setUsers(List<DAOUser> users) {
		this.users = users;
	}
	
	public Date getTimeRefreshed() {
		return timeRefreshed;
	}
	public void setTimeRefreshed(Date timeRefreshed) {
		this.timeRefreshed = timeRefreshed;
	}
	
	

}
