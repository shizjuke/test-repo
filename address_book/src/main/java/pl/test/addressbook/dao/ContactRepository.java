package pl.test.addressbook.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.test.addressbook.domain.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{

	
}
