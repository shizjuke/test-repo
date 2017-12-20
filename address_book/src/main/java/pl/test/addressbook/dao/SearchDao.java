package pl.test.addressbook.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pl.test.addressbook.domain.Contact;

public interface SearchDao extends JpaRepository<Contact, Long>,  
JpaSpecificationExecutor<Contact>, QueryByExampleExecutor<Contact> {

}
