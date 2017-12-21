# Simple Address Book application

### Functionalities
- CRUD on address data
- full text search (spaces and + can be used between words) and structured search
- API versioning using accept header with vendor and version strategy
- Data model
```
firstname
lastname
home address
phone number
email
```
- validation on fields:
   - firstname: required, min. length: 2
   - lastname: required, min. length:2
   - email: format
   - phone number: min. 8 digits
  
### Architecture
- Web: HTML, JQuery
- Business Logic/API: Spring
- Data  Access: Hibernate
- Database: H2

### Build 
Java8
Maven

### Build 
 - path to jar in this repository: release/
 - java -jar addressbook-0.0.1-SNAPSHOT
 - url: localhost:8080

### To improve 
- single add/update/search form
- use jquery-validation
- add second level cache
- add security
