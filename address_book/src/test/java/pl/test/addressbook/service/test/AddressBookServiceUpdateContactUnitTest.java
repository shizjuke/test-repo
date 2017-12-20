package pl.test.addressbook.service.test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.test.addressbook.dao.ContactRepository;
import pl.test.addressbook.domain.Contact;
import pl.test.addressbook.service.AddressBookService;
import pl.test.addressbook.service.test.util.TestUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(AddressBookService.class)
public class AddressBookServiceUpdateContactUnitTest {
 
    @Autowired
    private MockMvc mvc;
 
    @MockBean
    private ContactRepository contactRepository;
	
    @Test
    public void givenInvalidContact_whenUpdateContact_thenReturn4xxError() throws Exception{
    	
    	Contact obj = new Contact();
    	obj.setContactId(1l);
    	obj.setFirstname("Arek");
    	obj.setLastname("Www");

        mvc.perform(post("/api/contacts/" + obj.getContactId())
        		.content(TestUtil.convertObjectToJsonBytes(obj))
        	    .contentType(MediaType.APPLICATION_JSON))
        	    .andExpect(status().is4xxClientError());
                
    }
    
    @Test
    public void givenNotExistingContact_whenUpdateContact_thenReturn4xxError() throws Exception{
    	
    	Contact obj = new Contact();
    	obj.setContactId(0l);
    	obj.setFirstname("Arek");
    	obj.setLastname("Www");

    	when(contactRepository.findOne(obj.getContactId())).thenReturn(null);
    	
        mvc.perform(post("/api/contacts/" + obj.getContactId())
        		  .content(TestUtil.convertObjectToJsonBytes(obj))
        	      .contentType(MediaType.APPLICATION_JSON))
        	      .andExpect(status().is4xxClientError());
                
    }
    
    @Test
    public void givenContact_whenUpdateContact_thenReturnUpdatedContact() throws Exception{
    	
    	Contact oldEmail = new Contact();
    	oldEmail.setContactId(1l);
    	oldEmail.setFirstname("Arek");
    	oldEmail.setLastname("Www");
    	oldEmail.setEmail("old@wp.pl");
        
    	Contact newEmail = new Contact();
    	newEmail.setContactId(1l);
    	newEmail.setFirstname("Arek");
    	newEmail.setLastname("Www");
    	newEmail.setEmail("new@wp.pl");
    	
    	when(contactRepository.findOne(newEmail.getContactId())).thenReturn(oldEmail);
    	oldEmail.setEmail(newEmail.getEmail());
    	when(contactRepository.save(oldEmail)).thenReturn(newEmail);
    	
        mvc.perform(post("/api/contacts/" + newEmail.getContactId())
        	  .content(TestUtil.convertObjectToJsonBytes(newEmail))
      	      .contentType(MediaType.APPLICATION_JSON))
      	      .andExpect(status().isOk())
      	      .andExpect(jsonPath("$.firstname", is(newEmail.getFirstname())));
    }
    
}