package pl.test.addressbook.service.test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import pl.test.addressbook.AddressBookApplication;
import pl.test.addressbook.dao.ContactRepository;
import pl.test.addressbook.domain.Contact;
import pl.test.addressbook.service.test.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = AddressBookApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application-integrationtest.properties")
public class AddressBookApplicationIntegrationTests {

	@Autowired
    private MockMvc mvc;
    
	@Autowired
    private ContactRepository repo;
	
	private Contact storedContact;
	
	@Before
	public void setUp(){
		
		Contact obj = new Contact();
    	obj.setFirstname("First");
    	obj.setLastname("Last");
    	obj.setEmail("db@wp.pl");
    	
    	storedContact = repo.save(obj);
		
	}
	
	@After
	public void cleanUp(){
		    	
    	repo.delete(storedContact);
		
	}
	
	@Test
    public void givenContact_whenAddContact_thenReturnAddedContact() throws Exception{
    	
    	Contact obj = new Contact();
    	obj.setFirstname("Arek");
    	obj.setLastname("Www");
    	obj.setEmail("test@wp.pl");
    	    	
        mvc.perform(put("/api/contacts/")
        		  .content(TestUtil.convertObjectToJsonBytes(obj))
        	      .contentType(MediaType.APPLICATION_JSON))
        	      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.firstname", is(obj.getFirstname())))
			      .andExpect(jsonPath("$.lastname", is(obj.getLastname())))
			      .andExpect(jsonPath("$.email", is(obj.getEmail())));
                
    }
	
	@Test
    public void givenContactId_whenGetContact_thenReturnContact() throws Exception{
    	
    	mvc.perform(get("/api/contacts/" + storedContact.getContactId())
        	      .contentType(MediaType.APPLICATION_JSON))
        	      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.firstname", is(storedContact.getFirstname())))
			      .andExpect(jsonPath("$.lastname", is(storedContact.getLastname())))
			      .andExpect(jsonPath("$.email", is(storedContact.getEmail())));
                
    }
	
	@Test
    public void givenNotExistingContactId_whenGetContact_thenReturn4xxError() throws Exception{
    	
    	mvc.perform(get("/api/contacts/0")
        	      .contentType(MediaType.APPLICATION_JSON))
        	      .andExpect(status().is4xxClientError());
                
    }
	
	@Test
    public void givenContact_whenUpdateContact_thenReturnUpdatedContact() throws Exception{
    	
    	Contact obj = new Contact();
    	obj.setFirstname("First");
    	obj.setLastname("Last");
    	obj.setEmail("test@wp.pl");
    	    	
        mvc.perform(post("/api/contacts/" + storedContact.getContactId())
        		  .content(TestUtil.convertObjectToJsonBytes(obj))
        	      .contentType(MediaType.APPLICATION_JSON))
        	      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.firstname", is(obj.getFirstname())))
			      .andExpect(jsonPath("$.lastname", is(obj.getLastname())))
			      .andExpect(jsonPath("$.email", is(obj.getEmail())));
                
    }
	
	 @Test
	 public void givenInvalidApiVersion_whenGetContacts_thenNotAcceptableStatus() throws Exception {
		 mvc.perform(get("/api/contacts/").accept("application/vnd.company.app-v1.0+json")) //
	               .andExpect(status().isNotAcceptable());
	}
	 
	 @Test
	 public void givenContactIdAndAcceptHeader_whenGetContact_thenReturnContact() throws Exception{
	    	
	   mvc.perform(get("/api/contacts/" + storedContact.getContactId())
			   		  .accept("application/vnd.company.app-v2.0+json"))
	        	      .andExpect(status().isOk())
	        	      .andExpect(content().contentType("application/vnd.company.app-v2.0+json;charset=UTF-8")) 
				      .andExpect(jsonPath("$.firstname", is(storedContact.getFirstname())))
				      .andExpect(jsonPath("$.lastname", is(storedContact.getLastname())))
				      .andExpect(jsonPath("$.email", is(storedContact.getEmail())));
	                
	 }

}
