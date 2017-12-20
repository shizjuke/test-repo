package pl.test.addressbook.service;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.test.addressbook.dao.ContactRepository;
import pl.test.addressbook.domain.Contact;
import pl.test.addressbook.exception.ContactValidationFailed;

/**
 * REST api for CRUD operations on Contact resource
 *
 */
@RestController
@RequestMapping(path = {"/api"}, 
				headers = "Accept=application/vnd.company.app-v2.0+json",
			    produces = "application/vnd.company.app-v2.0+json")
public class AddressBookService {

	@Autowired
	ContactRepository contactsRepository;

	/**
	 * Add new contact
	 * 
	 * @param contact
	 *            - new contact details
	 */
	@PutMapping("/contacts")
	public Contact addContact(@Valid @RequestBody Contact contact) {
		validateContactFields(contact);
		return contactsRepository.save(contact);
	}

	/**
	 * Get all contacts
	 * 
	 * @return
	 */
	@GetMapping("/contacts")
	public List<Contact> getAllContacts() {
		return contactsRepository.findAll();
	}

	/**
	 *  Update a contact
	 * @param contactId - contact to update identifier
	 * @param details - updated contact
	 * @return
	 */
	@PostMapping("/contacts/{id}")
	public ResponseEntity<Contact> updateContact(
			@PathVariable(value = "id") Long contactId,
			@Valid @RequestBody Contact details) {

		validateContactFields(details);
		
		Contact contact = contactsRepository.findOne(contactId);
		if (contact == null) {
			return ResponseEntity.notFound().build();
		}
		
		contact.setEmail(details.getEmail());
		contact.setHomeAddress(details.getHomeAddress());
		contact.setPhoneNumber(details.getPhoneNumber());
		contact.setNote(details.getNote());

		Contact updatedContact = contactsRepository.save(contact);
		return ResponseEntity.ok(updatedContact);
	}

	/**
	 * Validate if any of: email, home address or phone is provided
	 * @param details
	 */
	private void validateContactFields(Contact details) {
		if(StringUtils.isEmpty(details.getEmail())
				&& StringUtils.isEmpty(details.getHomeAddress())
				&& StringUtils.isEmpty(details.getPhoneNumber())){
			throw new ContactValidationFailed("Email, phone or home address is required");
		}
	}

	/**
	 * Get a single contact
	 * 
	 * @param contactId
	 *            - contact identifier
	 * @return
	 */
	@GetMapping("/contacts/{id}")
	public ResponseEntity<Contact> getContactById(@PathVariable(value = "id") long contactId) {

		Contact contact = contactsRepository.findOne(contactId);
		if (contact == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(contact);

	}

	/**
	 * Delete a contact
	 * 
	 * @param contactId
	 *            - contact identifier
	 * @return
	 */
	@DeleteMapping("/contacts/{id}")
	public ResponseEntity<Contact> deleteContact(@PathVariable(value = "id") Long contactId) {

		Contact contact = contactsRepository.findOne(contactId);
		if (contact == null) {
			return ResponseEntity.notFound().build();
		}

		contactsRepository.delete(contact);
		return ResponseEntity.ok().build();
	}
	
}
