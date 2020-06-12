# nhsbsa-test

Contains some very basic CRUD endpoints for 'Person' which contains a list of skillEntities.

Spring multi-module application uses JPA/H2, has a universal identifier, and some basic handling for threads.

Endpoints can be tested via swagger by running the application in the parent directory using:
mvn spring-boot:run and directing your browser to http://localhost:8080/swagger-ui.html
(providing you have the correct firewall/port forwarding settings) or by running the application and then executing the tests in the component test directory.
