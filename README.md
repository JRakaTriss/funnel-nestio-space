# funnel-nestio-space
 Coding Test Repository for Funnel

This application was coded using intellijIdea for a funnel leasing interview question.

This application can be run in the native intellij Ulitimate server or by running a 'mvn clean compile package' from the root pom directory to produce a .war, this war can be deployed to tomcat 10 running Java 17. 

The simplest way to configure this to the ROOT application context is through renaming the .war to ROOT.war.

<hosturl:port>/<application context>/ provides an online message
 
<hosturl:port>/<application context>/stats provides current min and max stats as a JSON object with the format {"min_altitude":Double, "max_altitude":Double}

<hosturl:port>/<application context>/stats provides the current status message as a singular String.

Unit tests are contained in aurora.triss.work.nestiospace.NestioSpaceApplicationTests.java
