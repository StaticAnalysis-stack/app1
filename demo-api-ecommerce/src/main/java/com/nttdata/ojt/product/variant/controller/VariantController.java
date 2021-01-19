package com.nttdata.ojt.product.variant.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.nttdata.ojt.global.MessageKey;
import com.nttdata.ojt.global.domain.AbstractResponse;
import com.nttdata.ojt.global.domain.ResponseError;
import com.nttdata.ojt.product.ProductApiEndpoint;
import com.nttdata.ojt.product.domain.request.ProductUserRequest;
import com.nttdata.ojt.product.domain.request.ProductVariantRequest;
import com.nttdata.ojt.product.domain.response.ProductBoughtResponse;
import com.nttdata.ojt.product.domain.response.ProductListResponse;
import com.nttdata.ojt.product.domain.response.UserProductResponse;
import com.nttdata.ojt.product.entity.ProductUser;
import com.nttdata.ojt.product.exceptions.ProductAlreadyPresentException;
import com.nttdata.ojt.product.variant.domain.request.VariantProductRequest;
import com.nttdata.ojt.product.variant.domain.request.VariantRequest;
import com.nttdata.ojt.product.variant.domain.response.VariantAddResponse;
import com.nttdata.ojt.product.variant.domain.response.VariantAssignResponse;
import com.nttdata.ojt.product.variant.domain.response.VariantListResponse;
import com.nttdata.ojt.product.variant.domain.response.VariantStockGetResponse;
import com.nttdata.ojt.product.variant.domain.response.VariantStockUpdateResponse;
import com.nttdata.ojt.product.variant.entity.Variant;
import com.nttdata.ojt.product.variant.entity.VariantProduct;
import com.nttdata.ojt.product.variant.service.VariantService;
import com.nttdata.ojt.product.variant.endpoint.VariantEndPoint;
/**
 * @author Sushant Patil
 */
@RestController()
public class VariantController {
	
	
	
	@Autowired 
	private VariantService variantService;

	@Autowired
	private MessageSource msgSrc;
	
	@Autowired
	private RestTemplate restTemplate;
	private static final String MISSING_ATTR_MSG = "Required attributes missing";
	private static final Logger logger = LoggerFactory.getLogger(VariantController.class);

	
	/**
	 * api endpoint for creating the variant,
	 * assigning it to the product
	 * @param token for authentication
	 * @param variant 
	 * @param builder
	 * @return whether the variant has been created or not
	 */
	@PostMapping(VariantEndPoint.CREATE_VARIANT)
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<VariantAssignResponse> createVariant(@RequestHeader(value = "Authorization") String token,@RequestBody VariantRequest variant,
			UriComponentsBuilder builder) {
		VariantAssignResponse response = new VariantAssignResponse();
		ProductVariantRequest req = new ProductVariantRequest(variant.getpCode(), variant.getvCode());
        /**
         * Before inserting the variant it must first be inserted  in
         * the product variant table 
         */
		ResponseEntity<?> responseUser =  requestProductApiForAssignment(req,token);
		if(
			!((AbstractResponse)responseUser.getBody()).isCompletelyServed()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError("Failed to assign variant"));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		if (!variant.isNotEmpty()) {
			response.setCompletelyServed(false);
			response.setHasError(true);
			response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,variant.getLocale())));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		boolean	flag;
		boolean flag1;
		try {	
			
			
		Variant v = new Variant();
		v.setvCode(variant.getvCode());
		v.setvDesc(variant.getvDesc());
		v.setvColor(variant.getvColor());
		v.setvSize(variant.getvSize());
		v.setvStock(variant.getvStock());
			
		VariantProduct vp=new VariantProduct();
		vp.setpCode(variant.getpCode());
		vp.setvCode(variant.getvCode());

		
		 // Adding variant to variant and variantProduct table 
     	flag = variantService.addVariant(v);
		flag1=variantService.addVariantProduct(vp);

	} catch (ProductAlreadyPresentException e) {
		flag = false;
		response.addError(new ResponseError("variant already present"));
		logger.debug("Variant already present exception occured");
	}
		if (flag) {
			response.setCompletelyServed(true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		/**
		 * If the variant is not added in variant table
		 * or variantProduct table it will be deleted 
		 * from product variant table.
		 */
		response.setHasError(true);
		ResponseError responseError = new ResponseError();
		responseError.setErrorMessage("failed to assign variant");
		response.addError(responseError);
		requestProductApiForDeletion(req,token);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	
	/**
	 * api endpoint to retreive all the variants present in the database
	 * @return all the variants present in the database
	 */
	@GetMapping(VariantEndPoint.GET_VARIANTS)
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<VariantListResponse> getAllVariants() {
		
		List<Variant> list = variantService.getAllVariants();
		VariantListResponse response = new VariantListResponse();
       	if(!list.isEmpty()) {
       		response.setCompletelyServed(true);
       		response.setHasError(false);
       		response.setVariants(list);
       		response.setTimeRefreshed(new Date());
       	}else {
       		response.setCompletelyServed(false);
       		response.setHasError(true);
       		response.addError(new ResponseError("No variants available"));
    		logger.debug("Variant not available exception occured");

    		return new ResponseEntity<VariantListResponse>(response, HttpStatus.BAD_REQUEST);

       	}
		return new ResponseEntity<VariantListResponse>(response, HttpStatus.OK);
	}
	
	
	/**
	 *  api endpoint for retrieving all the variants
	 *  assigned to the given product
     *  @param pCode product code to retrieve all the variants
	 *  @return the variants assigned to the given product
	*/
	@GetMapping(VariantEndPoint.GET_PRODUCT_VARIANTS)
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<VariantListResponse> getProductVariant(@RequestParam(value = "pCode") String pCode)
			{
		String locale = "en";
		VariantListResponse response = new VariantListResponse();

		if(pCode==null || pCode=="")
		{
			response.setCompletelyServed(false);
       		response.setHasError(true);
       		response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,new Locale(locale))));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		List<Variant> list = variantService.getProductVariant(pCode);
		
       	if(!list.isEmpty()) {

       		response.setCompletelyServed(true);
       		response.setHasError(false);
       		response.setTimeRefreshed(new Date());
       		response.setVariants(list);
       	}else {
       		response.setCompletelyServed(false);
       		response.setHasError(true);
       		response.addError(new ResponseError("No variant available"));
    		logger.debug("Variant not available exception occured");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       	}
		return new ResponseEntity<VariantListResponse>(response, HttpStatus.OK);
	}

	@GetMapping(VariantEndPoint.DELETE_STOCK_VARIANT)
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<VariantStockUpdateResponse> updateVariantStock(@RequestParam String vCode,int quantity)
			{
		String locale = "en";
		VariantStockUpdateResponse response = new VariantStockUpdateResponse();
		if(vCode==null || vCode=="" || quantity==0)
		{
			response.setCompletelyServed(false);
       		response.setHasError(true);
       		response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,new Locale(locale))));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		boolean flag = variantService.updateVariantStock(vCode,quantity);
		
       	if(flag) {

       		response.setCompletelyServed(true);
       		response.setHasError(false);
       		
       	}else {
       		response.setCompletelyServed(false);
       		response.setHasError(true);
       		response.addError(new ResponseError("No variant available"));
    		logger.debug("Variant not available exception occured");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       	}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(VariantEndPoint.GET_VARIANT_STOCK)
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<VariantStockGetResponse> getVariantStock(@RequestParam(value = "vCode") String vCode)
			{
		String locale = "en";
		VariantStockGetResponse response = new VariantStockGetResponse();
		if(vCode==null || vCode=="")
		{
			response.setCompletelyServed(false);
       		response.setHasError(true);
       		response.addError(new ResponseError(msgSrc.getMessage(MessageKey.MISSING_ATTR_MSG, null,new Locale(locale))));
       		response.setvStock(-1);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		int vStock = variantService.getVariantStock(vCode);
       		response.setCompletelyServed(true);
       		response.setHasError(false);

       		response.setvStock(vStock);
       	
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	
	/**
	 * @param variant 
	 * @param token for authentication
	 * @return whether variant has been assigned or not 
	 */
	private ResponseEntity<?> requestProductApiForAssignment(ProductVariantRequest variant,String token) {
		try {
			MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
			headers.add("Authorization", token);
			RequestEntity<?> req = new RequestEntity<>(variant,headers,HttpMethod.POST,new URI(this.baseUrl()+"/products/assignVariant"));
			return restTemplate.exchange(req, AbstractResponse.class);
		}catch (URISyntaxException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
    /**
     * 
     * @param variant 
     * @param token for authentication
     * @return whether variant has been deleted or not
     */
	private ResponseEntity<?> requestProductApiForDeletion(ProductVariantRequest variant,String token) {
		try {
			MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
			headers.add("Authorization", token);
			RequestEntity<?> req = new RequestEntity<>(variant,headers,HttpMethod.POST,new URI(this.baseUrl()+"/products/deleteProductVariant"));
			
			return restTemplate.exchange(req, AbstractResponse.class);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	private String baseUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
	}

}
	
	
	
	


	
