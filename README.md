
### Test Urls
* Ping url: http://localhost:8080/scraping/analyse?url=http://google.com

#### Installations
* Install JDK 8
* Gradle

#### How to run
* Run **/.gradlew build** It should produce executable jar under ${SOURCE}/build/libs

##### Run a jar
* java -jar build/libs/web-page-analysis-0.1.jar 
* Application will use 8080 as default port

#### Tech stack used 
* It is Java 8 / Spring boot application with embedded tomcat
* Used JSoup for web scraping
* Gradle
* ReactJS for web client

#### Assumptions/understandings
* Assumed Anchor tags, source attribute tags and link tags as hypermedia links in the implementation
* Internal link
** sub domain is internal link. For example submitted url is google.com, then **support.google.com**, **docs.google.com** will be internal links
** Empty href value is treated as internal link
* External link
** .com and .de is treated as different domain
* If page is login form if it has element <input type=password>

#### High level design/solution approach
* Built the single component using Spring boot and it is responsible for both web and REST service
* Defined the domain model to represent the analysis report result. Refer {@link com.webscraping.analyse.model}
* Used Jsoup for traversing the HTML DOM tree

##### Backend component
Backend exposed single endpoint to get the user submitted url and sends the response back as application/json. 
For this, {@link PageAnalysisController} is responsible for getting the user submitted url and after that main job done by the {@link WebPageAnalyser}.
Ultimately, {@link AnalysisResult} model object holds the analysis result and sends back to user!

##### Web client
Web code is part of deployed Spring boot component. It is a simple React code to do the job.
Implemented few simple react components. They are App, AnalysisResult, HeadingLevel, Link. Refer {@link app.js}

##### Execution Flow
* When the user enters the url then request comes to {@link PageAnalysisController} @http://localhost:8080/scraping/analyse?url=
* Url will be delegated to {@link WebPageAnalyser}

#### Local Test url:
* http://localhost:8080/scraping/analyse?url=http://google.com

#### Testing
* Some url used to test 
** https://www.spiegel.de/meinspiegel/login.html
** http://github.com
** http://google.com
** http://ycombinator.com/
** http://we.com
** http://spring.io


#### Helpful notes
* A URI just identifies a resource - not necessarily a location of that resource.
* 