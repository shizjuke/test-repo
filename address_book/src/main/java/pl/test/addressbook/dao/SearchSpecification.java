package pl.test.addressbook.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import pl.test.addressbook.domain.Contact;

public class SearchSpecification {

	public static Specification<Contact> simpleSearch(final String phrase) {
	    return new Specification<Contact>() {
	      public Predicate toPredicate(Root<Contact> root, CriteriaQuery<?> query,
	            CriteriaBuilder builder) {
	         
	    	  String words [] = phrase.split("[\\s,\\+]");
	    	  Predicate [] likePredicate = new Predicate [words.length];
	    	  int idx = 0;
	    	  for(String word : words) {
	    		  likePredicate[idx] = builder.like(root.<String>get("searchString"), "%" + word + "%");
	    		  idx++;
	    	  }
	    	  
	    	  return builder.or(likePredicate);
	      }
	    };
	  }
	
}
