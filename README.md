# SpringForum Installation Guide

## Install Required Tools

1. *Install JDK* (Recommended: JDK 11, JDK 17, or JDK 21)
    - Download from the official Oracle website: [Oracle JDK Downloads](https://git-scm.com/downloads)

2. *Install Maven*
    - Download from the Apache Maven official website: [Apache Maven Downloads](https://maven.apache.org/download.cgi)

3. *Install Git*
    - Download from the official Git website: [Git Downloads](https://git-scm.com/downloads)

4. *Install JDBC Driver*
    - Download the Microsoft JDBC driver for SQL Server: [Microsoft JDBC Driver](https://learn.microsoft.com/vi-vn/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server?view=sql-server-ver16)

## Install an IDE (Choose one of the three popular IDEs)

1. *VS Code*
    - Download and install from the official website: [VS Code Download](https://code.visualstudio.com/)
    - Install extensions: Java Extension Pack and Spring Boot Extension Pack from the Marketplace

2. *Eclipse*
    - Download and install from the official website: [Eclipse Downloads](https://www.eclipse.org/downloads/)
    - Install "Eclipse IDE for Java Developers" or "Eclipse IDE for Enterprise Java and Web Developers" for Java and Spring Boot development tools

3. *IntelliJ IDEA*
    - Download and install from the official website: [IntelliJ IDEA Downloads](https://www.jetbrains.com/idea/)
    - Install either the Community or Ultimate edition

## Configure Environment Variables

1. *JAVA_HOME*
    - Set the JAVA_HOME environment variable to point to your JDK installation directory.

2. *M2_HOME*
    - Set the M2_HOME environment variable to point to your Maven installation directory.

## Clone the Project

1. Open the terminal and run the following command:
   ```sh
   git clone https://github.com/leequanno1/BlogForum.git

## Connect IDE to Database

First run the **database.sql** in MSSQL Server to create a database
- *For IntelliJ IDEA*: [IntelliJ Database Connection](https://youtu.be/RF-_vchtV58?si=2gM46UIoVOocl2PI)
- *For VS Code*: [VS Code Database Connection](https://youtu.be/AvNVxRIMvco?si=EEw6C_lM7E1iABGm)
- *For Eclipse*: [Eclipse Database Connection](https://youtu.be/w7jPOwe_ue8?si=rh0tVACl3IqcE6aO)

## Build and Run the Project

### Open the Project in IDE

Open the cloned project in your chosen IDE (VS Code, Eclipse, IntelliJ IDEA).

### Build the Project

Use the command `mvn clean install` or `gradle build` in the terminal to download dependencies and build the project.

### Run the Spring Boot Application

Run the application by clicking the Run button in the IDE or using the command `mvn spring-boot:run` or `gradle bootRun` in the terminal.
