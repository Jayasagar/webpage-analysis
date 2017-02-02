
### Test Urls
* Ping url: http://localhost:8902/scraping/analyse?url=http://google.com

#### Installations
* Install JDK 8
* Gradle

#### How to run
* Run **/.gradlew build** It should produce executable jar under ${SOURCE}/build/libs

##### Run a jar
* java -jar build/libs/web-page-analysis-0.1.jar 
* Application will use 8902 as default port. Can change it in /resources/application.properties
* If you wish to change the default port, please dont forget to change it in webclient code which is in app.js.

#### Tech stack used 
* It is Java 8 / Spring boot application with embedded tomcat
* Used JSoup for web scraping
* Gradle
* ReactJS for web client

#### Assumptions/understandings
* Acceptable url http connection timeout is 5 seconds.
* Ignored the possible duplicate links.
* Assumed Anchor tags, source attribute tags and link tags as hypermedia links in the implementation.
* Internal link assumption
** sub domain is internal link. For example submitted url is google.com, then **support.google.com**, **docs.google.com** will be internal links
** Empty href value in the <a> element is treated as internal link. Ignored URI creation problem with it as it is not external one, should be internal.
** 
* External link assumption
** .com and .de is treated as different domain
* If page is login form if it has element "input type=password"

#### Limitations/TODOs
* Server side caching
* Not implemented optional requirement. Validate all the links whether they are reachable from server in Javascript. However given quick thoughts on it under section 'Thoughts on validate each HTTP url requirement'
* Web client basic validations
* Hardcoded the page analysis endpoint url in web client code.

#### High level design/solution approach
* Built single component using Spring boot and it is responsible for both web and REST service
* Defined the domain model to represent the analysis report result. Refer {@link com.webscraping.analyse.model}
* Used Jsoup for traversing the HTML DOM tree

#### Backend component
Backend exposed single endpoint to get the user submitted url and sends the response back as application/json. 
For this, {@link PageAnalysisController} is responsible for getting the user submitted url and after that main job done by the {@link WebPageAnalyser}.
Ultimately, {@link AnalysisResult} model object holds the analysis result and sends back to user!

#### Web client
Web code is part of deployed Spring boot component. It is a simple React code.
Implemented few simple react components. They are App, AnalysisResult, HeadingLevel, Link. Refer {@link app.js}

#### Execution flow
* When the user enters the url then request comes to {@link PageAnalysisController} @http://localhost:8080/scraping/analyse?url=
* Url will be delegated to {@link WebPageAnalyser}. This is the primary logic where we do page analysis.

#### Thoughts on validate each HTTP url requirement
* In order to validate all the links whether reachable or not, 
primary problem would be performance hit as every http connection we establish might take considerable amount time or we wait untill connection timeout.
Even though we set connection timeout, but as might have too many links to validate so overall time to process single user request might take lot of time!
##### Solution approach 1:
* Web client could do this from Javascript so that 2 benefits 1) server never have too much load and 2) we can do asynchronous calls from Javascript
##### Solution approach 2:
* We can use threading to process the multiple HTTP links so that we can improve the overall performance. 

#### Local Test url:
* http://localhost:8902/scraping/analyse?url=http://google.com

#### Testing
* Some url used to test 
** https://www.immobilienscout24.de/
** https://www.spiegel.de/meinspiegel/login.html
** http://github.com
** http://google.com
** http://ycombinator.com/
** http://we.com
** http://spring.io

#### Helpful notes
* A URI just identifies a resource - not necessarily a location of that resource.