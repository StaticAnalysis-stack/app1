package com.nttdata.ojt.user.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.nttdata.ojt.global.domain.ResponseError;
import com.nttdata.ojt.product.controller.ProductController;
import com.nttdata.ojt.user.UserApiEndpoint;
import com.nttdata.ojt.user.config.JwtTokenUtil;
import com.nttdata.ojt.user.domain.JwtRequest;
import com.nttdata.ojt.user.domain.JwtResponse;
import com.nttdata.ojt.user.domain.UserAddResponse;
import com.nttdata.ojt.user.domain.UserListRespone;
import com.nttdata.ojt.user.domain.UserProductRequest;
import com.nttdata.ojt.user.domain.UserProductResponse;
import com.nttdata.ojt.user.entity.DAOUser;
import com.nttdata.ojt.user.entity.UserDTO;
import com.nttdata.ojt.user.entity.UserProduct;
import com.nttdata.ojt.user.exception.EmailidAlreadyPresentException;
import com.nttdata.ojt.user.exception.MobileNumberAlreadyPresentException;
import com.nttdata.ojt.user.exception.NotValidMailException;
import com.nttdata.ojt.user.exception.NotValidMobileNumber;
import com.nttdata.ojt.user.exception.UserAlreadyPresentException;
import com.nttdata.ojt.user.exception.UserProductNotPresentException;
import com.nttdata.ojt.user.exception.WeakPasswordException;
import com.nttdata.ojt.user.service.JwtUserDetailsService;

/**
 * @author Sagar Deshmukh
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;  

	private static final String MISSING_ATTR_MSG = "Required attributes missing";
	 
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	/**
	 * api endpoint for Authenticate (Login)
	 * @param authenticationRequest
	 * @return whether user is able to log in or not
	 * @throws Exception
	 */
	@RequestMapping(value = UserApiEndpoint.LOGIN, method = RequestMethod.POST)
	public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		
		authenticate(authenticationRequest.getEmailid(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getEmailid());

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		DAOUser user=userDetailsService.findUserDetails(authenticationRequest.getEmailid());

		return ResponseEntity.ok(new JwtResponse(token,user.getFirstName(),user.getMiddleName(),user.getLastName()));
	}
	
	
	/**
	 * api endpoint for creating a users
	 * @param user
	 * @return whether the user has been added or not
	 */
	@RequestMapping(value = UserApiEndpoint.ADDUSER, method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
		
		UserAddResponse response=new UserAddResponse();
		boolean flag;
		if (!user.isNotEmpty()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(MISSING_ATTR_MSG));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			if(!userDetailsService.isValidMail(user.getEmailid())) {
				throw new NotValidMailException();
			}
			else if(!userDetailsService.isValidMobileNo(user.getMobileNumber())) {
				throw new NotValidMobileNumber();
			}
			else if(!userDetailsService.isValidPassword(user.getPassword())) {
				throw new WeakPasswordException();
			}
			flag = userDetailsService.save(user);
		}
		catch (UserAlreadyPresentException e) {
			flag = false;
			response.addError(new ResponseError("User already registerd"));
		} catch (EmailidAlreadyPresentException e) {
			flag = false;
			response.addError(new ResponseError("Emailid already registerd"));
		} catch (NotValidMailException e) {
			flag = false;
			response.addError(new ResponseError("Not a Valid Emailid"));
		} catch (NotValidMobileNumber e) {
			flag = false;
			response.addError(new ResponseError("Not a Valid Mobile Number"));
		} catch (WeakPasswordException e) {
			flag = false;
			response.addError(new ResponseError("Enter Password is weak!! Please choose a strong Password!"));
			
		} catch (MobileNumberAlreadyPresentException e) {
			flag = false;
			response.addError(new ResponseError("MobileNumber already registerd"));
		} 
		 if (flag) {
         	response.setCompletelyServed(true);
         	response.setEmailid(user.getEmailid());
         	response.setFirstName(user.getFirstName());
         	return new ResponseEntity<UserAddResponse>(response,HttpStatus.ACCEPTED);
         }
		 response.setHasError(true);
         ResponseError responseError = new ResponseError();
         responseError.setErrorMessage("failed to register");
         response.addError(responseError);
         return new ResponseEntity<UserAddResponse>(response,HttpStatus.BAD_REQUEST);

	}
	
	/**
	 * api endpoint for listing all available Users
	 * @return all the users present
	 */
	@GetMapping(UserApiEndpoint.GETALLUSER)
	public ResponseEntity<UserListRespone> getAllUsers() {
		List<DAOUser> userList = userDetailsService.getAllUsers();
		UserListRespone response = new UserListRespone();
		if (!userList.isEmpty()) {
			response.setCompletelyServed(true);
			response.setHasError(false);
			response.setUsers(userList);
			response.setTimeRefreshed(new Date());
		} else {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError("No Users available"));
			logger.debug("No Users available exception occured");

		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * api endpoint for assigning the product to user
	 * @param userProductReq
	 * @param builder provides flexibility of using URI template variables 
	 * and injecting it directly into Spring Controller method
	 * @return whether the product has been assigned or not
	 */
	@PostMapping(UserApiEndpoint.ASSIGNPRODUCT)
	public ResponseEntity<UserProductResponse> buyProduct(@RequestBody UserProductRequest userProductReq,
			UriComponentsBuilder builder) {

		UserProductResponse response = new UserProductResponse();
		if (!userProductReq.isNotEmpty()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(MISSING_ATTR_MSG));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		UserProduct userProduct = new UserProduct();
		userProduct.setpCode(userProductReq.getpCode());
		userProduct.setEmailId(userProductReq.getEmailId());
		userProduct.setQuantity(userProductReq.getQuantity());
		userProduct.setvCode(userProductReq.getvCode());

		boolean flag = userDetailsService.assignProductToUser(userProduct);

		if (flag) {
			response.setCompletelyServed(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setHasError(true);
		ResponseError responseError = new ResponseError();
		responseError.setErrorMessage("user is is not registered");
		response.addError(responseError);
		logger.debug("User not registered exception occured");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	
	
	/**
	 * api endpoint for deleting a UserProduct
	 * to be used by product service
	 * @param userProductReq
	 * @param builder provides flexibility of using URI template variables 
	 * and injecting it directly into Spring Controller method
	 * @return whether the product has been deleted or not
	 */
		@PostMapping(UserApiEndpoint.DELETEASSIGNPRODUCT)
		public ResponseEntity<UserProductResponse> deleteUserProduct(
				@RequestBody UserProductRequest userProductReq, UriComponentsBuilder builder) {
			
			UserProductResponse response = new UserProductResponse();
			if(!userProductReq.isNotEmpty()) {
				response.setCompletelyServed(false);
				response.setHasError(true);
				response.addError(new ResponseError(MISSING_ATTR_MSG));
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			try {
				UserProduct userProduct = new UserProduct();
				
				userProduct.setpCode(userProductReq.getpCode());
				userProduct.setEmailId(userProductReq.getEmailId());
				userProduct.setQuantity(userProductReq.getQuantity());
				userProduct.setvCode(userProductReq.getvCode());
				boolean status = userDetailsService.deleteUserProduct(userProduct);
				if (status) {
					response.setCompletelyServed(true);
					response.setHasError(false);
				} else {
					response.setCompletelyServed(false);
					response.setHasError(true);
					response.addError(new ResponseError("Failed to delete"));
					logger.debug("Failed to delete product exception occured");

				}

			} catch (UserProductNotPresentException e) {
				response.setCompletelyServed(false);
				response.setHasError(true);
				response.addError(new ResponseError("Not present"));
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	/**
	 * 
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}