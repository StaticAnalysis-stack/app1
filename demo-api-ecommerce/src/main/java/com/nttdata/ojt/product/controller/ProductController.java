package com.nttdata.ojt.product.controller;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.nttdata.ojt.global.MessageKey;
import com.nttdata.ojt.global.domain.ResponseError;
import com.nttdata.ojt.product.domain.request.ProductRequest;
import com.nttdata.ojt.product.domain.request.ProductUserRequest;
import com.nttdata.ojt.product.domain.request.ProductVariantRequest;
import com.nttdata.ojt.product.domain.response.ProductAddResponse;
import com.nttdata.ojt.product.domain.response.ProductBoughtResponse;
import com.nttdata.ojt.product.domain.response.ProductListResponse;
import com.nttdata.ojt.product.domain.response.ProductVariantAddResponse;
import com.nttdata.ojt.product.domain.response.ProductVariantDeleteResponse;
import com.nttdata.ojt.product.domain.response.UserProductResponse;
import com.nttdata.ojt.product.entity.Product;
import com.nttdata.ojt.product.entity.ProductUser;
import com.nttdata.ojt.product.entity.ProductVariant;
import com.nttdata.ojt.product.exceptions.ProductAlreadyPresentException;
import com.nttdata.ojt.product.exceptions.ProductVariantAlreadyPresentException;
import com.nttdata.ojt.product.exceptions.ProductVariantNotPresentException;
import com.nttdata.ojt.product.service.ProductService;
import com.nttdata.ojt.product.variant.domain.response.VariantListResponse;
import com.nttdata.ojt.product.variant.domain.response.VariantStockGetResponse;
import com.nttdata.ojt.product.variant.domain.response.VariantStockUpdateResponse;
import com.nttdata.ojt.product.variant.endpoint.VariantEndPoint;
import com.nttdata.ojt.product.variant.entity.Variant;
import com.nttdata.ojt.product.ProductApiEndpoint;

/**
 * @author Mustafa Malayawala
 */
@RestController
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductService productService;
	
	@Autowired
	private MessageSource msgSrc;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	 /**
     * api endpoint for creating a product
     * @param product - contains product details
     * @param builder - provides flexibility of using URI template variables 
     * and injecting it directly into Spring Controller method
     * @return whether the product has been added or not
     */
	@PostMapping(ProductApiEndpoint.CREATE_PRODUCT)	
	public ResponseEntity<ProductAddResponse> addProductRequest(@RequestBody ProductRequest product,
			UriComponentsBuilder builder) {

	
		
		ProductAddResponse response = new ProductAddResponse();
		if (!product.isNotEmpty()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,product.getLocale())));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		boolean flag;
		try {

			Product pr = new Product();
			if(product.getpCode()!=null && !product.getpCode().isEmpty()) {
				pr.setpCode(product.getpCode());
			}else {
				String id = UUID.randomUUID().toString();
				pr.setpCode(id);
				product.setpCode(id);
			}
			pr.setpDesc(product.getpDesc());
			pr.setpName(product.getpName());
			pr.setImageUrl(product.getImageUrl());
			flag = productService.addProduct(pr);
		} catch (ProductAlreadyPresentException e) {
			flag = false;
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.PRODUCT_ALREADY_PRESENT, null,product.getLocale())));
			logger.debug("product alread present exception occured");
		}

		if (flag) {
			response.setCompletelyServed(true);
			response.setpCode(product.getpCode());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setHasError(true);
		ResponseError responseError = new ResponseError();
		responseError.setErrorMessage(msgSrc.getMessage(MessageKey.FAILED_TO_ADD,null,product.getLocale()));
		response.addError(responseError);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	
	/**
	 * api endpoint for listing all available products
	 * @return the list of products
	 */
	@GetMapping(ProductApiEndpoint.LIST_PRODUCTS)	
	public ResponseEntity<ProductListResponse> getAllProducts() {
		
		
		List<Product> productList = productService.getAllProducts();
		ProductListResponse response = new ProductListResponse();
		if (!productList.isEmpty()) {
			response.setCompletelyServed(true);
			response.setHasError(false);
			response.setProducts(productList);
			response.setTimeRefreshed(new Date());
		} else {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.NO_PRODUCT_AVAILABLE, null,new Locale("en"))));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * api endpoint for assigning the product to use
	 * @param token for authentication
	 * @param productUserReq - parameters to be added in the productUser table
	 * @param builder - provides flexibility of using URI template variables 
	 * and injecting it directly into Spring Controller method
	 * @return whether the product has been added or not
	 */
	@PostMapping(ProductApiEndpoint.ASSIGN_PRODUCT)
	public ResponseEntity<ProductBoughtResponse> buyProduct(@RequestHeader(value = "Authorization") String token,@RequestBody ProductUserRequest productUserReq,
			UriComponentsBuilder builder) {
		
		ProductBoughtResponse response = new ProductBoughtResponse();
		ResponseEntity<?> responseUser =  requestUserApiForAssignment(productUserReq,token);
		if(responseUser.getStatusCodeValue()!=HttpStatus.OK.value()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError("Failed to assign product"));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		//now registering the data for this service
		
		
		if (!productUserReq.isNotEmpty()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,productUserReq.getLocale())));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		int vStock = requestVariantApiForStock(productUserReq.getvCode(), token);
		if(!validateVariantCodeWithVariantApi(productUserReq.getvCode(),token) || vStock<=0) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.VARIANT_NOT_VALID, null,productUserReq.getLocale())));
			requestUserApiForDeletion(productUserReq,token);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(!requestVariantApiForStockDecrement(productUserReq.getvCode(),productUserReq.getQuantity(), token)) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError("Failed to buy : stock error"));
			requestUserApiForDeletion(productUserReq,token);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		ProductUser productUser = new ProductUser();
		productUser.setpCode(productUserReq.getpCode());
		productUser.setEmailId(productUserReq.getEmailId());
		productUser.setQuantity(productUserReq.getQuantity());
		productUser.setvCode(productUserReq.getvCode());
		
		boolean flag = productService.assignProductToUser(productUser);

		if (flag) {
			response.setCompletelyServed(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setHasError(true);
		ResponseError responseError = new ResponseError();
		responseError.setErrorMessage("failed to buy product");
		response.addError(responseError);
		requestUserApiForDeletion(productUserReq,token);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}


	
	/**
	 * api endpoint for getting all the assignments of the product to the use
	 * @param emailId to retreive the products
	 * @param builder provides flexibility of using URI template variables 
	 * and injecting it directly into Spring Controller method
	 * @return all the products assigned to the user
	 */
	@GetMapping(ProductApiEndpoint.GET_ORDERS)
	
	public ResponseEntity<ProductListResponse> getOrders(@RequestParam String emailId, UriComponentsBuilder builder) {
	
		String locale = "en";
		ProductListResponse response = new ProductListResponse();
		if (emailId == null || emailId.isEmpty()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,new Locale(locale))));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		List<Product> productList = productService.getProductsByEmailId(emailId);
		if (!productList.isEmpty()) {
			response.setCompletelyServed(true);
			response.setHasError(false);
			response.setProducts(productList);
			response.setTimeRefreshed(new Date());
		} else {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError("No product available"));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	
	/**
	 * api endpoint for assigning a Variant to the product
	 * to be used by variant service
	 * @param productVariantReq - parameters to be inserted in productVariant table
	 * @param builder provides flexibility of using URI template variables
	 * and injecting it directly into Spring Controller method
	 * @return whether product is assigned or not
	 */
	@PostMapping(ProductApiEndpoint.ASSIGN_VARIANT)
	
	public ResponseEntity<ProductVariantAddResponse> assignVariant(@RequestBody ProductVariantRequest productVariantReq,
			UriComponentsBuilder builder) {
	
		ProductVariantAddResponse response = new ProductVariantAddResponse();
		if(!productVariantReq.isNotEmpty()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,productVariantReq.getLocale())));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		try {

			ProductVariant productVariant = new ProductVariant();
			productVariant.setpCode(productVariantReq.getpCode());
			productVariant.setvCode(productVariantReq.getvCode());

			boolean status = productService.saveProductVariant(productVariant);
			if (status) {
				response.setCompletelyServed(true);
				response.setHasError(false);
			} else {
				response.setCompletelyServed(false);
				response.setHasError(true);
				response.addError(new ResponseError("Failed to save"));
			}

		} catch (ProductVariantAlreadyPresentException e) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError("Already present "));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	
	/**
	    * api endpoint for deleting a Variant
		* to be used by variant service
	    * @param productVariantReq 
	    * @param builder provides flexibility of using URI template variables 
	    * and injecting it directly into Spring Controller method
	    * @return whether variant is deleted or not
	    */
	@PostMapping(ProductApiEndpoint.DELETE_VARIANT)
	
	public ResponseEntity<ProductVariantDeleteResponse> deleteVariant(
			@RequestBody ProductVariantRequest productVariantReq, UriComponentsBuilder builder) {
		
		ProductVariantDeleteResponse response = new ProductVariantDeleteResponse();
		if(!productVariantReq.isNotEmpty()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,productVariantReq.getLocale())));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			ProductVariant productVariant = new ProductVariant();
			productVariant.setpCode(productVariantReq.getpCode());
			productVariant.setvCode(productVariantReq.getvCode());
			boolean status = productService.deleteProductVariant(productVariant);
			if (status) {
				response.setCompletelyServed(true);
				response.setHasError(false);
			} else {
				response.setCompletelyServed(false);
				response.setHasError(true);
				response.addError(new ResponseError("Failed to delete"));
			}

		} catch (ProductVariantNotPresentException e) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError("Not present"));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	/**
	 * this function will call rest api of the variant service
	 * and will check if the variant code supplied is correct
	 * @param variantCode for retrieving the variants
	 * @param token for authentication
	 * @return list of variants
	 */
	private boolean validateVariantCodeWithVariantApi(String variantCode,String token) {
		
		MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", token);
		RequestEntity<?> req;
		try {
			req = new RequestEntity<>(headers,HttpMethod.GET,new URI(this.baseUrl()+"/"+VariantEndPoint.GET_VARIANTS));
		} catch (URISyntaxException e) {
			logger.warn("uri syntax error");
			return false;
		}
		ResponseEntity<VariantListResponse> json =  restTemplate.exchange(req,new ParameterizedTypeReference<VariantListResponse>() {
		});
		
		
		List<Variant> var = json.getBody().getVariants();
		return var.stream().filter(i->i.getvCode().equals(variantCode)).count()>0;

	}
	
	
	/**
     * @param ProductUserReq
     * @param token for authentication
     * @return whether product is assigned or not
     */
	private ResponseEntity<?> requestUserApiForAssignment(ProductUserRequest productUserReq,String token) {
		try {
			MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
			headers.add("Authorization", token);
			
			RequestEntity<?> req = new RequestEntity<>(productUserReq,headers,HttpMethod.POST,new URI(this.baseUrl()+"/user/assignProduct"));
			logger.warn("inside firstt");
			return restTemplate.exchange(req, UserProductResponse.class);
		}catch (URISyntaxException e) {
			logger.warn("uri syntax error");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			logger.warn("inside secondD"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/**
	 * @param productUserReq 
	 * @param token for authentication
	 * @return whether product is deleted or not
	 */
	private ResponseEntity<?> requestUserApiForDeletion(ProductUserRequest productUserReq,String token) {
		try {
			MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
			headers.add("Authorization", token);
			RequestEntity<?> req = new RequestEntity<>(productUserReq,headers,HttpMethod.POST,new URI(this.baseUrl()+"/user/deleteAssignedProduct"));
			
			return restTemplate.exchange(req, UserProductResponse.class);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * @param vCode
	 * @param quantity 
	 * @param token for authentication
	 * @return whether stock is decremented or not
	 */
	private boolean requestVariantApiForStockDecrement(String vCode,int quantity,String token){
		
		MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", token);
		try {
			RequestEntity<?> req = new RequestEntity<>(headers,HttpMethod.GET,new URI(this.baseUrl()+"/"+VariantEndPoint.DELETE_STOCK_VARIANT+"?vCode="+vCode+"&quantity="+quantity));
			ResponseEntity<VariantStockUpdateResponse> json =  restTemplate.exchange(req,new ParameterizedTypeReference<VariantStockUpdateResponse>() {
			});
			
			return json.getBody().isCompletelyServed();
		} catch (URISyntaxException | HttpClientErrorException e) {
			return false;
		}
		
	}
	
	/**
	 * @param vCode
	 * @param token for authentication
	 * @return stock
	 */
	private int requestVariantApiForStock(String vCode,String token) {
		String api = "";
		MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", token);
		try {
			RequestEntity<?> req = new RequestEntity<>(headers,HttpMethod.GET,new URI(this.baseUrl()+"/"+VariantEndPoint.GET_VARIANT_STOCK+"?vCode="+vCode));
			ResponseEntity<VariantStockGetResponse> json =  restTemplate.exchange(req,new ParameterizedTypeReference<VariantStockGetResponse>() {
			});
			
			return json.getBody().getvStock();
		} catch (URISyntaxException | HttpClientErrorException e) {
			return -1;
		}
	}
	
	/**
	 * @return baseUrl of the application
	 */
	private String baseUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
	}


}
