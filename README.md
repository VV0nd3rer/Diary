## Diary for travellers version 1.0
This is a Java web application which is deployed inside a Tomcat server container.

### Used technologies:
* Spring MVC  5.x and Spring Security 4.2.0
* JPA 2.1 and Hibernate 5.1
* Postgresql  9.6
* WebSocket protocol (by using STOMP as a sub-protocol)
* Jasypt ver. 1.9.2 
* Apache Tomcat 8.x

This project is available on Heroku: https://travel-diary.herokuapp.com/

If you want to get sandbox account, please contact me.

If you are developer and wish to deploy this project on your local machine, read next section

### How to deploy
1.  Prepeare you local database:
     * Clone or download application database backup file form here https://github.com/kverchi/diary-db-backup.git
     * Create the database with name *diary*
     * Restore postgresql backup file *diary-v1.0.tar* on your local Postgresql server for newly created database *diary*. [How to restore backup](https://www.postgresql.org/docs/9.6/backup-dump.html#BACKUP-DUMP-RESTORE) 
     * Set Jasypt enctiption key as local environment variable DIARY_PASS_VAR
     * Encrypt JDBC_DATABASE_USERNAME and JDBC_DATABASE_PASSWORD credentials with your environment variable. [How to encrypt with Jasypt](https://apereo.atlassian.net/wiki/spaces/CASUM/pages/103261428/HOWTO+Use+Jasypt+to+encrypt+passwords+in+configuration+files)
     * Set encrypted values into application's */resources/properties/local/app.properties* file 
2. Build war file with Maven: `mvn package`
3. Deploy war file at your local **Tomcat Root**. [How to deploy war file](https://tomcat.apache.org/tomcat-8.0-doc/deployer-howto.html#Deployment_on_Tomcat_startup)
    * To deploy war file at Tomcat Root:
       Set the context path of the application in the server.xml (which is located at $CATALINA_HOME\conf)
```
<Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true">
        <Context path="" docBase="Diary" reloadable="true"></Context>
 ...
</Host>
```

