# Vanguard Test 20241209
### Technologies Used
- Database version: MySQL 8.4
- Spring Boot Version: 3.4
- Java Version: 17
### How to setup the database
1. Download and install the MySQL Community Server
2. Setup a basic root
3. After setup is completed, create a database called `vg_test`
4. Run the `full_setup_sql.sql` file found in the repository (under folder `1) sql-setup-tests`)
### How to build this project
#### Intellij with Maven
1. Open the project using Intellij (version used for development is Community Edition 2024.3)
2. Ensure your JAVA_HOME is pointed to jdk 17
3. Run Maven Clean and Maven Package
4. A plugin for Spring Boot Run has been provided with this project, run the goal `spring-boot:run`
5. Test using the Postman in the folder `4) Postman`

#### mvn Command line
1. Ensure your JAVA_HOME is pointed to jdk 17
2. Ensure your environment variables have the mvnw executable path
3. Open Windows Powershell.
4. cd to the repository root folder (`vanguard-test-20241209`)
6. run `.\mvnw.cmd clean` (clear once before starting)
7. run `.\mvnw.cmd package` (just to ensure that the application is built correctly)
8. run `.\mvnw.cmd spring-boot:run` (to start the application)
9. Test using the Postman in the folder `4) Postman`