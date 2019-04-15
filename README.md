## Diary for travellers version 1.0
This is a Java web application which is deployed inside a Tomcat server container.

### Used technologies:
* Spring MVC 5.x and Spring Security 4.2.0
* JPA 2.1 and Hibernate 5.1
* Postgresql  9.6
* WebSocket protocol (by using STOMP as a sub-protocol)
* Jasypt ver. 1.9.2 
* Apache Tomcat 8.x
* Apache Maven 3.6.x

This project is available on Heroku: https://travel-diary.herokuapp.com/

If you want to get sandbox account, please contact me.

If you are developer and wish to deploy this project on your local machine, read next section

### How to deploy
1.  Clone or download [Travel diary web application](https://github.com/kverchi/Diary.git). 
     * Use your favorite IDE for development: [Spring Tool Suite for Eclipse](https://spring.io/tools), [IntelliJ IDEA](https://www.jetbrains.com/idea/), etc.
2.  Prepeare you local database:
     * Download and install [PostgreSQL](https://www.postgresql.org/download/). Optionally, you can download and install [pgAdmin 4](https://www.pgadmin.org/download/) which is an administration and development platform for PostgreSQL.
     * Clone or download application database backup file form here https://github.com/kverchi/diary-db-backup.git
     * Create the database with name *diary*
     * Restore postgresql backup file *diary-v1.0.tar* on your local Postgresql server for newly created database *diary*. [How to restore backup](https://www.postgresql.org/docs/9.6/backup-dump.html#BACKUP-DUMP-RESTORE) 
     * Set database credentials for connection to your database. 
     
     There are 2 ways to set database credentials into application's properties file: as a *plain text* or as an *encrypted values*
     
     Set credentials as a *plain text*:
     * Open *<path-to-app>/src/main/resources/properties/local/app.properties* and set your database username and password to JDBC_DATABASE_USERNAME and JDBC_DATABASE_PASSWORD properties.
     
     Set credentials as an *encrypted values* with Jasypt encryption tool:
     * Download and install [Jasypt](http://www.jasypt.org/download.html)
     * Set Jasypt enctiption key as local environment variable DIARY_PASS_VAR. [How to set environment variable in Windows](https://www.computerhope.com/issues/ch000549.htm) and [how to set environment variable in Linux](https://www.tecmint.com/set-path-variable-linux-permanently/).
     * Encrypt your database username and password with your environment variable. [How to encrypt with Jasypt](https://apereo.atlassian.net/wiki/spaces/CASUM/pages/103261428/HOWTO+Use+Jasypt+to+encrypt+passwords+in+configuration+files)
     * Open *<path-to-app>/src/main/resources/properties/local/app.properties* and set your encrypted username and encrypted password to JDBC_DATABASE_USERNAME and JDBC_DATABASE_PASSWORD properties
     
3. Build war file with Maven: open a terminal, go to the root of your local project and execute `mvn package`. [How to install Maven](https://maven.apache.org/install.html).
4. Deploy war file at your local **Tomcat Root**. [How to deploy war file](http://www.jguru.com/faq/view.jsp?EID=123229)
    * To deploy war file at Tomcat Root:
       Set the context path of the application in the server.xml (which is located at $CATALINA_HOME\conf)
```
<Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true">
        <Context path="" docBase="Diary" reloadable="true"></Context>
 ...
</Host>
```

