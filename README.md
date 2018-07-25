# spring-data-xml
Provide support when accessing XML databases like eXist with Java and XQuery

The following Spring integration tests demonstrates the simple use-case of an empty XQuery sequence:

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExistConfiguration.class)
class SimpleExistXQJRepositoryTests {

    @Inject
    private SimpleXQJRepository<Integer> existRepository;

    @Test
    void emptySequence() {
        Stream<Integer> result = existRepository.execute("()");

        assertEquals(0, result.count());
    }
}
```

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

> Hint: This repository is a companion of the blog post [Building a Simple eXist Repository on Top of Spring Data](https://devops.datenkollektiv.de/building-a-simple-exist-repository-on-top-of-spring-data.html) which describes the internals of this library in more detail.
