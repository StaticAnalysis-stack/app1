package medicinal;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.example.medicinal.model.Medicine;


public class ProfilesTestCase extends AbstractTest {
   @Override
   @Before
   public void setUp() {
      super.setUp();
   }
   @Test
   public void getMedicinesList() throws Exception {
	   
	   String uri = "/view";
      
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
         .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
     
      int status = mvcResult.getResponse().getStatus();
      assertEquals(200, status);
   
  	
   }
   @Test
   public void createProduct() throws Exception {
      String uri = "/add";
      Medicine med = new Medicine();
      med.setNo(4);
      med.setBrand_name("abc");
      med.setCompany_name("xyz");
	  med.setExpiry("2020-12-25");
	  med.setBatch_no("lmn777");
	  med.setProduct_type("English");
	  med.setAmount(200.00);
      String inputJson = super.mapToJson(med);
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
         .contentType(MediaType.APPLICATION_JSON_VALUE)
         .content(inputJson)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      assertEquals(200, status);
   }
   @Test
   public void updateProduct() throws Exception {
      String uri = "/update";
      Medicine med = new Medicine();
      med.setBatch_no("lmn777");
      String inputJson = super.mapToJson(med);
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
         .contentType(MediaType.APPLICATION_JSON_VALUE)
         .content(inputJson)).andReturn();
      
      int status = mvcResult.getResponse().getStatus();
      assertEquals(200, status);
   }
   @Test
   public void deleteProduct() throws Exception {
      String uri = "/delete";
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
      int status = mvcResult.getResponse().getStatus();
      assertEquals(200, status); 
   }
}
