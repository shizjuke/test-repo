package pl.test.addressbook.exception;

public class ContactValidationFailed extends RuntimeException {

	public ContactValidationFailed(String msg) {
		super(msg);
	}

}
