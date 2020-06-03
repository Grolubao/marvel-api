
# Marvel Yapily API  
  
The Marvel Yapily API extends the current Marvel API bringing support for fetching all Marvel characters and additional translation of the description of a single Marvel character.  
  
## Installation Instructions  
  
1. Install Maven which can be found here:  
  
   [Maven installation instructions](https://maven.apache.org/install.html)  
  
2. Sign up for a free Marvel developer API key:   
  
   [Marvel developer API](http://developer.marvel.com)  
     
3. Sign up for a free Yandex developer API key:  
  
   [Yandex Translate API](https://translate.yandex.com/developers/keys)  
  
4. Create a file in your local file structure named: **api_key**  
  
5. Open the **api_key** file and add the following properties:
```bash  
 marvel.key.public = this_is_the_value_of_your_marvel_public_key
 marvel.key.private = this_is_the_value_of_your_marvel_private_key
 yandex.key = this_is_the_value_of_your_yandex_public_key
```
6. Change the 3 properties above to match your keys.  
  
7. Edit the **application.properties** file inside the project and change the **api.keys.location** property to match the location of your api_key file   
     
  
## Build and Run  
  
In order to build and run the project, navigate to its root folder and execute the following command:  
  
```bash  
mvn spring-boot:run 
```  
  
## Tests  
  
If you wish to run the test suite, navigate to its root folder and execute the following command:  
  
```bash  
mvn test 
```  
  
## Author  
Rafael Franco  
  
https://www.linkedin.com/in/rafaelsoaresfranco/