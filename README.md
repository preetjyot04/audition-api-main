# Audition API

The purpose of this Spring Boot application is to test general knowledge of SpringBoot, Java, Gradle etc. It is created
for hiring needs of our company but can be used for other purposes.

## Getting Started

### Prerequisite tooling

- Any Springboot/Java IDE. Ideally IntelliJIdea.
- Java 17
- Gradle 8

### Prerequisite knowledge

- Java
- SpringBoot
- Gradle
- Junit

### Importing Google Java codestyle into INtelliJ

```
- Go to IntelliJ Settings
- Search for "Code Style"
- Click on the "Settings" icon next to the Scheme dropdown
- Choose "Import -> IntelliJ Idea code style XML
- Pick the file "google_java_code_style.xml" from root directory of the application
__Optional__
- Search for "Actions on Save"
    - Check "Reformat Code" and "Organise Imports"
```

## How to Start the application.

- Import application to your favourite IDE.
- Open Terminal and type below cmd to compile the code.

````
./gradlew clean build
````

- After successfull compilation, run the applicaiton with below cmd.

````
./gradlew bootRun
````

## After application successfully runs, Test the below APIs.

-
    1. Get the list of Posts
       http://localhost:8080/posts
-
    2. Get List of Posts with UserId filter
       http://localhost:8080/posts?userId=2
-
    3. Get any particular post with comments
       http://localhost:8080/posts/2
-
    4. Get only comments for any post
       http://localhost:8080/posts/2/comments

---

## Additional Details

- For test coverage I have used Jacoco. Same can be seen under below path post the build is successfull.
    - build>JacocoHtml>index.html
- For Robustness of system, I have used circuit breaker patters (retry and fallback).
- For input validations, I have used regex and matches().
- Error handling is done with individual class level and Global exceptional handling.
- Rest template was created with LoggingInterceptor to logs request/response.
- Related to obserability
    - Logging is done with slf4j and logback and current level of logs is set to WARN and same can be updated in
      logback-spring.xml under the console appender.
    - Metrics instrumentation is done with Actuators with Prometheus, Currently /health and /info endpoints are enabled as mentioned in
      the TODO and other endpoints can be enabled by adding in application.yml file along with info.(we can add prometheus and run docker image of Prometheus and grafana to see the metrics UI dashboards, I have added the configs file under Monitoring folder)
    - For tracing, spanId and traceId is added to header as directed in TODO item.