# COMP47660-Secure Software Engineering - Assignment 1
**Team:**    
[Ciaran Flanagan](https://github.com/ciaranpflanagan) (18336646)    
[Brian Byrne](https://github.com/brianbyrne99) (18391933)

# Installation
1. Download code
2. Run `mvn clean install` to install the required packages
3. Configure database connection inside of `src/main/resources/application.properties`.
4. Start the application by running `mvn spring-boot:run`.
5. Visit the application at [localhost:8080](http://localhost:8080/).

You can login to the application via [localhost:8080/login](http://localhost:8080/login).
   
## Database Setup
You can register as a normal user in the application via [localhost:8080/users/register](http://localhost:8080/users/register). However to set an account as a HSE admin (this is needed to access certain HSE admin only features), you must manually alter the database.
### Setting Up Roles
Two roles need to be created in the database. Run the following commands to create the `USERS` and `HSE` roles.
```SQL
INSERT INTO roles (id, role) VALUES (1, "USERS");
```
```SQL
INSERT INTO roles (id, role) VALUES (2, "HSE");
```
### Assigning Roles
Once you have the roles created you can assign your account as a HSE admin by updating the `user_role` table with the following command (Where `$user_id` is your own users ID from the `users` table).
```SQL
INSERT INTO user_role (user_id, role_id) VALUES ($user_id, 2);
```