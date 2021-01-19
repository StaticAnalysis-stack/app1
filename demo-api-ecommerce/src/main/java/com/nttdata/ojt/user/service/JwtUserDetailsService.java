package com.nttdata.ojt.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nttdata.ojt.user.entity.DAOUser;
import com.nttdata.ojt.user.entity.UserDTO;
import com.nttdata.ojt.user.entity.UserProduct;
import com.nttdata.ojt.user.exception.EmailidAlreadyPresentException;
import com.nttdata.ojt.user.exception.MobileNumberAlreadyPresentException;
import com.nttdata.ojt.user.exception.UserAlreadyPresentException;
import com.nttdata.ojt.user.exception.UserProductNotPresentException;
import com.nttdata.ojt.user.repositories.UserDao;
import com.nttdata.ojt.user.repositories.UserProductRepository;



@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserProductRepository userProductDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String emailid) throws UsernameNotFoundException {
		/**
		 * load a User details By email id
		*/
		DAOUser user = userDao.findByEmailid(emailid);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + emailid);
		}
		return new org.springframework.security.core.userdetails.User(user.getEmailid(), user.getPassword(),
				new ArrayList<>());
	}
	
	public boolean save(UserDTO user) throws UserAlreadyPresentException, EmailidAlreadyPresentException, MobileNumberAlreadyPresentException {
		/**
		 * save users details
		*/
		DAOUser newUser = new DAOUser();
		newUser.setEmailid(user.getEmailid());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setFirstName(user.getFirstName());
		newUser.setMiddleName(user.getMiddleName());
		newUser.setLastName(user.getLastName());
		newUser.setMobileNumber(user.getMobileNumber());
		if(isEmailidPresent(newUser.getEmailid())) throw new EmailidAlreadyPresentException();
		if(isMobileNoPresent(newUser.getMobileNumber())) throw new MobileNumberAlreadyPresentException();
		if(isPresent(newUser)) throw new UserAlreadyPresentException();
		 userDao.save(newUser);
		 return isPresent(newUser);
	}
	
	public List<DAOUser> getAllUsers(){
		/**
		 * returns a list of all the users
		*/
		List<DAOUser> users = new ArrayList<>();
		this.userDao.findAll().forEach(users::add);
		return users;
	}
	
	public boolean assignProductToUser(UserProduct userProduct) {
		/**
		 * code for checking the user service have recorded 
		 * the same in the respective database will go here
		 * if it have failed to do so we will return false
		 */
		
		if(isEmailidPresent(userProduct.getEmailId())) {
			
			userProductDao.save(userProduct);
			return true;
		}
		else {
			return false;
		}
		
	}
	
	
	public boolean deleteUserProduct(UserProduct userProduct) throws UserProductNotPresentException  {
		/**
		 * delete the user product otherwise throws an exception if it is already absent
		*/
		if(isUserProductPresent(userProduct)!=0) {
			userProduct.setId(isUserProductPresent(userProduct));
			this.userProductDao.delete(userProduct);
			return true;
		}
		else {
			System.out.println("inside else");
			throw new UserProductNotPresentException();
		}
		
	}
	
	public int isUserProductPresent(UserProduct user) {
		/**
		 * checks if the user product is present already
		*/
		int id=0;
		List<UserProduct> users = new ArrayList<>();
		this.userProductDao.findAll().forEach(users::add);
		for (UserProduct us : users) {
			 if(us.getEmailId().equals(user.getEmailId())&&us.getpCode().equals(user.getpCode())&&us.getvCode().equals(user.getvCode())){
				 id=us.getId();
			 }
			 else { 
				 id=0;
			 }
		}
		return id;
	}
	
	/**
	 * find User Details for Login Response
	*/
	public DAOUser findUserDetails(String emailid) {
						
		DAOUser user=userDao.findByEmailid(emailid);
		return user;
	}
	
	public boolean isPresent(DAOUser user) {
		/**
		 * checks if the user is present already
		*/
		List<DAOUser> users=new ArrayList<>();
		this.userDao.findAll().forEach(users::add);
		return users.stream().filter((DAOUser us)->{return us.equals(user);}).toArray().length>0;
		
	}
	
	public boolean isEmailidPresent(String emailid) {
		/**
		 * checks if the Email id is present already
		*/
		List<DAOUser> users = new ArrayList<>();
		this.userDao.findAll().forEach(users::add);
		return users.stream().filter((DAOUser pr)->{
			return pr.getEmailid().equals(emailid);
			}).toArray().length>0;
		
	}//isEmailidPresent
	
	public boolean isMobileNoPresent(String mobileNumber) {
		/**
		 * checks if the Mobile Number is present already
		*/
		List<DAOUser> users = new ArrayList<>();
		this.userDao.findAll().forEach(users::add);
		return users.stream().filter((DAOUser pr)->{
			return pr.getMobileNumber().equals(mobileNumber);
			}).toArray().length>0;
		
	}//isMobileNoPresent
	
	public boolean isValidPassword(String s) 
	{   
		Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$"); 
		 
		Matcher m = p.matcher(s); 
		return (m.find() && m.group().equals(s)); 
	}//isValidPassword
	
	public boolean isValidMail(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    }// isValidMail
	
	public boolean isValidMobileNo(String s) 
	{ 
		Pattern p = Pattern.compile("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$"); 
		 
		Matcher m = p.matcher(s); 
		return (m.find() && m.group().equals(s)); 
	}//isValidMobileNo
	
}