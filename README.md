
#### Assumptions/understandings
* Assumed Anchor tags, source attribute tags and link tags as hypermedia links in the implementation
* Internal link
** sub domain is internal link. For example submitted url is google.com, then **support.google.com**, **docs.google.com** will be internal links
** Empty href value is treated as internal link
** link starts with **mailto:** is external link
* External link
** .com and .de is treated as different domain
* If page is login form if it has input type=password.

#### Solution 
* It is Java 8 / Spring boot application
* Used JSoup for web scraping
* Gradle
* 

#### Local Test url:
* http://localhost:8080/scraping/analyse?url=http://google.com

#### Testing notes
* some url used to test 
** http://github.com
** http://google.com
** http://ycombinator.com/


#### Some notes
* A URI just identifies a resource - not necessarily a location of that resource.
* 