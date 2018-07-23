# spring-data-xml
Provide support when accessing XML databases like eXist with Java and XQuery

## Running the integration tests against eXist-db

**Prerequisites**

* Install and start an [eXist-db](http://exist-db.org/exist/apps/homepage/index.html) locally.

> Tip: Please refer to [eXist - Kick-start Development with Docker](https://devops.datenkollektiv.de/exist-kick-start-development-with-docker.html) for a non-invasive setup with Docker.

* Create the test collection `/data/book` with eXide.
* Download `exist-xqj-api-1.0.1.jar` from [eXist XQJ API](http://xqj.net/exist/) and put the Java library into the `lib` folder.

**Action**

The standard Gradle task `test` will run the integration tests.

```bash
$ ./gradlew test
```
