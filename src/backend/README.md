### SPRING BACKEND

install maven on your computer and then launch

```bash
mvn install
```

To build project

```bash
mvn package -DskipTests
```

It will create `StraySafe.jar` in `target` directory. This is the file that is launched when we want to run the backend

Run it with java 21

```bash
java -jar target/StraySafe.jar
```

