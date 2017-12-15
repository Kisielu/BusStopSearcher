# Bus Stop Searcher

This simple app downloads all bus stops in Poznan from dedicated URL (set in properties
file) and can determine the closest one basing on latitude and longitude set by user.

### Running the application 

To run the app, you need to download MySQL server from 

`https://dev.mysql.com/downloads/mysql/`

After installing the server, create a schema called `busStop` and a user `busstop:busstop`
(or another one, to your discretion) and put username and password in the properties file.

To run the application, write `mvn clean spring-boot:run` in the terminal

To run all the tests application needs to be running since some of them are 
integration tests.

To populate DB with bus stops call 

`GET /stops/populate`

To find the closest bus stop call 

`GET /stops/closest?latitude=&longitude=`