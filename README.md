# multi-module-demo

A small Gradle multi-module Java project with three modules: `core`, `service`, and `app`.

Structure

- core: utility class
- service: depends on `core`
- app: depends on `service` and `core`, contains `Main` to run

Build and run (requires Gradle installed):

```bash
gradle build
gradle :app:run
```

Or run the `app` module directly after building:

```bash
java -cp service/build/libs/service-0.1.0.jar;core/build/libs/core-0.1.0.jar app.Main
```
