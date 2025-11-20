# GraalVM Native Image Guide

This guide details how to build a Native Image for this Akka HTTP project, specifically addressing how to handle runtime reflection using the Native Image Agent.

## Prerequisites

Ensure you have GraalVM installed (see [Get Started](get-started.md)).

## Building the Native Image

To build the native image, run:

```bash
sbt graalvm-native-image:packageBin
```

The executable is generated at `target/graalvm-native-image/hello-scala`.

## Handling Reflection with Akka

Akka heavily relies on Java reflection, which GraalVM Native Image does not support by default (it requires all reflection targets to be known at build time). If you try to run the native image without configuration, you will likely see errors like:

```text
java.lang.IllegalArgumentException: no matching constructor found on class akka.actor.Props$EmptyActor
```

### The Solution: Native Image Agent

To fix this, we use the **GraalVM Native Image Agent**. The agent runs with your application on the standard JVM, traces all reflection usage, and generates the necessary configuration files.

### Step-by-Step Configuration

1.  **Create the configuration directory:**

    ```bash
    mkdir -p src/main/resources/META-INF/native-image
    ```

2.  **Run the application with the Agent:**

    Run the following command to start your app with the agent attached. It will write configuration files to the directory created above.

    ```bash
    sbt 'set fork := true' 'set javaOptions += "-agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image"' run
    ```

3.  **Exercise the Application:**
    While the server is running, make requests to it (e.g., `curl http://localhost:8081/hello`) to ensure the agent captures the code paths used by your application.

4.  **Stop the Server:**
    Press `RETURN` or `Ctrl+C` to stop the server. The agent will write the final configuration files (e.g., `reflect-config.json`, `jni-config.json`) to `src/main/resources/META-INF/native-image`.

5.  **Rebuild the Native Image:**

    Now that the configuration files are present, rebuild the image:

    ```bash
    sbt graalvm-native-image:packageBin
    ```

6.  **Run the Native Image:**

    ```bash
    ./target/graalvm-native-image/hello-scala
    ```

    It should now start instantly and function correctly.

## Understanding `reachability-metadata.json`

You might notice a file named `reachability-metadata.json` (or separate files like `reflect-config.json`, `resource-config.json`) generated in `src/main/resources/META-INF/native-image`.

**Why do we need this?**

GraalVM Native Image operates under a **Closed World Assumption**. This means it assumes it knows about all reachable code and data at build time. It performs static analysis to remove unused classes and methods to keep the binary small.

However, libraries like **Akka** use dynamic features like **Java Reflection** to instantiate classes (e.g., Actors) based on configuration or runtime data. The static analysis cannot "see" these usages.

The `reachability-metadata.json` file bridges this gap. It explicitly tells the Native Image builder:
*   "Keep these classes available for reflection."
*   "Include these resource files in the binary."
*   "Allow these JNI calls."

Without this file, the builder would aggressively strip out the "unused" Akka classes, causing your application to crash at runtime with `ClassNotFoundException` or `IllegalArgumentException`.
