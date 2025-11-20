# Get Started

This guide will help you run the Akka HTTP server locally.

## Prerequisites

- **Java Development Kit (JDK)**: Version 11 or higher.
- **sbt**: The interactive build tool for Scala.
- **GraalVM** (Optional, for Native Image):
    - **Install SDKMAN**: `curl -s "https://get.sdkman.io" | bash`
    - **Install GraalVM**: `sdk install java 25-graal`
    - Verify installation with: `native-image --version`

## Running the Server

1.  Open a terminal in the project root directory.
2.  Run the following command to start the server:

    ```bash
    sbt run
    ```

3.  You should see output indicating the server is online:

    ```
    Server online at http://localhost:8081/
    Press RETURN to stop...
    ```

## Running with GraalVM

You can run this project with GraalVM to improve performance and startup time.

### Option 1: Run with GraalVM JDK (JIT)

Simply install a GraalVM JDK and run the project as usual:

```bash
sbt run
```

### Option 2: Build Native Image (AOT)

To build a standalone native executable with instant startup:

1.  Ensure you have GraalVM installed and `native-image` is available.
2.  Run the build command:

    ```bash
    sbt graalvm-native-image:packageBin
    ```

3.  The executable will be generated in `target/graalvm-native-image/`. Run it directly:

    ```bash
    ./target/graalvm-native-image/hello-scala
    ```

## Testing the Server

Once the server is running, you can test the `hello` endpoint using `curl` or your web browser.

### Using curl

```bash
curl http://localhost:8081/hello
```

**Expected Output:**

```html
<h1>Say hello to akka-http</h1>
```

### Using a Browser

Open [http://localhost:8081/hello](http://localhost:8081/hello) in your web browser.

## Stopping the Server

To stop the server, press `RETURN` (Enter) in the terminal where `sbt run` is executing.
