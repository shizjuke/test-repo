package pl.test.addressbook.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.test.addressbook.dao.ContactRepository;
import pl.test.addressbook.dao.SearchDao;
import pl.test.addressbook.dao.SearchSpecification;
import pl.test.addressbook.domain.Contact;

@RestController
@RequestMapping("/api")
public class AddressBookSearchService {

	private static final Logger logger = Logger.getLogger(AddressBookSearchService.class);

	@Autowired
	SearchDao searchDao;
	
	/**
	 * Full text search for contacts
	 * @param text - search phrase
	 * @return
	 */
	@GetMapping("/search/{phrase}")
	public List<Contact> fulltextSearchContacts(
			@PathVariable(value = "phrase") String phrase) {
		return searchDao.findAll(SearchSpecification.simpleSearch(phrase));
	}
	
	/**
	 * Search for contacts
	 * @param details - search details
	 * @return
	 */
	@PostMapping("/search/")
	public List<Contact> structuredSearchContacts(
		   @RequestBody Contact filter) {
		
		//Filter - ignore case search and contains 
        ExampleMatcher matcher = ExampleMatcher.matching()     
                  .withStringMatcher(StringMatcher.CONTAINING)   // Match string containing pattern   
                  .withIgnoreCase();                 // ignore case sensitivity 
         
        Example<Contact> example = Example.of(filter, matcher); 
        
		return searchDao.findAll(example);
	}
	
}
