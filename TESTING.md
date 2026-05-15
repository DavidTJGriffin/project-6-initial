# API Testing with Bruno

Bruno requests will not work unless both Spring Boot services are running first. Start there.

---

## Prerequisites

You need **Java** and **Maven** installed before anything else will work.

### Check if you already have them

Open a terminal and run:

```
java -version
mvn -version
```

If either command says "not recognized" or "command not found", install the missing tool first:

- **Java**: Download from [adoptium.net](https://adoptium.net/) (Java 21 or newer recommended). On Mac you can also use `brew install temurin`.
- **Maven**: Download from [maven.apache.org](https://maven.apache.org/download.cgi) and follow the install guide, or on Mac run `brew install maven`.

> **Note:** This project does NOT include a Maven wrapper (`mvnw` / `mvnw.cmd`). You must use the system `mvn` command directly.

---

## Step 1 — Start the Services

You need **two terminals open at the same time**. In each one, navigate to the project root first, then run the commands below.

### Windows (PowerShell or Command Prompt)

**Terminal 1 — Dictionary service (port 9091)**
```powershell
cd dictionary
mvn spring-boot:run
```

**Terminal 2 — Aggregator service (port 9090)**
```powershell
cd aggregator
mvn spring-boot:run
```

### Mac / Linux (Terminal)

**Terminal 1 — Dictionary service (port 9091)**

```bash
cd dictionary
mvn spring-boot:run
```

**Terminal 2 — Aggregator service (port 9090)**

```bash
cd aggregator
mvn spring-boot:run
```

---

Wait until both terminals show a line like:

```
Started DictionaryApplication in X.X seconds
Started AggregatorApplication in X.X seconds
```

The services are now live on `localhost:9091` and `localhost:9090`. **Leave both terminals running while you test.**

To stop a service, press `Ctrl+C` in its terminal.

---

## Step 2 — Install Bruno

Choose one:

### Option A: VS Code Extension
1. Open VS Code
2. Press `Ctrl+Shift+X` to open Extensions
3. Search **Bruno**
4. Install the one published by **Bruno API Client**
5. You will now see `.bru` files rendered as requests in the editor

### Option B: Desktop App
1. Go to [usebruno.com](https://www.usebruno.com/) and download the app
2. Install and open it

---

## Step 3 — Open the Collection

### VS Code Extension
1. In the Explorer panel, navigate to the `bruno/` folder in this project
2. Click any `.bru` file to open it as a request
3. Look for the **environment selector** at the top of the request panel and select **local**
4. Click **Send Request**

### Desktop App
1. Click **Open Collection**
2. Browse to and select the `bruno/` folder at the root of this project
3. The collection will load with two folders: **Dictionary** and **Aggregator**
4. In the top-right corner, open the environment dropdown and select **local**
5. Click any request in the sidebar, then click **Send**

---

## Step 4 — Send Requests

The collection is split into two folders matching the two services:

| Folder | Service | Port |
|---|---|---|
| Dictionary | Direct requests to the dictionary | 9091 |
| Aggregator | Requests to the aggregator (which calls the dictionary internally) | 9090 |

You can edit the value at the end of any URL before sending. For example, change `/getWordsEndingWith/ing` to `/getWordsEndingWith/ed` to test a different suffix.

> **Note:** `Get All Palindromes` will take several seconds to respond — it queries every letter of the alphabet internally before returning results.

---

## Troubleshooting

**`mvn` not recognized / command not found**

- Maven is not installed or not on your PATH. See the Prerequisites section above.
- On Windows, restart your terminal after installing Maven so the PATH update takes effect.

**`java` not recognized / command not found**

- Java is not installed. See the Prerequisites section above.

**"Could not send request" / connection refused**

- The service is not running. Go back to Step 1 and make sure both terminals are active and show the "Started" message.

**Port already in use**

- Something else is already using port 9090 or 9091. Stop the other process or restart your terminal and try again.
- On Windows: open Task Manager, find the Java process using that port, and end it.
- On Mac/Linux: `lsof -i :9090` to find and kill the process.

**Wrong environment selected**

- Make sure **local** is selected, not "No Environment". Without it, `{{aggregator_url}}` and `{{dictionary_url}}` won't resolve to actual addresses.

**First run takes a long time**

- Maven downloads dependencies on the first run. This is normal — subsequent starts will be much faster.
