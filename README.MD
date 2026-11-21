# JobTrail

JobTrail is a web application for keeping track of job and internship applications. It stores the companies you are interested in, the people you have talked to, the applications you have sent and the interviews that followed.

The project was built for the course *Java Technologies and Platforms for Distributed Systems*. Its main technical goal is to work with three different database systems at the same time, each one owning a different part of the data:

| Database   | Tables               |
|------------|----------------------|
| PostgreSQL | `companie`, `contact` |
| SQL Server | `aplicare`           |
| Oracle     | `interviu`           |

The user interface and the domain names (entities, fields, URLs) are in Romanian.

## Features

- Full CRUD for companies, contacts, applications and interviews
- Company detail page with its contacts and applications
- Application detail page with its interviews
- Filtering applications by status
- A Kanban board that groups applications by status (Saved, Applied, Interview, Offer, Rejected, Withdrawn). Each card combines data from all three databases: the application (SQL Server), the company name (PostgreSQL) and the next scheduled interview (Oracle)
- Changing an application's status directly from the board
- Follow-up reminder for applications that have been waiting for 14 days or more without an answer
- Validation of all input on the server side, with clear error messages

## Architecture

The application follows a classic three-layer Jakarta EE structure and runs entirely inside WildFly:

1. **Presentation layer**: servlets act as controllers. They read the request parameters, call the business layer and forward the result to a JSP page. JSP pages (with JSTL and EL) only render HTML and live under `WEB-INF/views`, so they cannot be opened directly. Form submissions use the Post/Redirect/Get pattern.
2. **Business layer**: stateless EJBs contain all validation and business rules. Transactions are managed by the container.
3. **Persistence layer**: JPA with Hibernate. There is one persistence unit per database (`pgPU`, `mssqlPU`, `oraclePU`), each bound to a WildFly datasource published in JNDI.

### Integrity across databases

Foreign keys cannot span different database servers, so only `contact.companie_id` is a real foreign key (both tables are in PostgreSQL). The other two relationships are logical links checked in the EJB layer:

- an application can only be created for a company that exists in PostgreSQL;
- an interview can only be created for an application that exists in SQL Server, and not before the application date;
- a company that still has applications cannot be deleted;
- an application that still has interviews cannot be deleted.

The datasources are not XA, so a single JTA transaction can only include one of them. Because of this, every write operation touches exactly one database, and the checks that read from another database run outside the current transaction (`@TransactionAttribute(NOT_SUPPORTED)`). The Kanban board service, which reads from all three databases, also runs without a transaction.

A known limitation is that there is no distributed lock between a check and the following write. For a single-user application this is acceptable. XA datasources with two-phase commit would be the alternative.

## Tech stack

- Java 25
- Jakarta EE 11: Servlet, JSP, JSTL, EJB, JPA
- Hibernate ORM (provided by WildFly)
- WildFly 42
- PostgreSQL 18, SQL Server 2025, Oracle Database 26ai Free
- Maven
- Bootstrap 5 and Bootstrap Icons (loaded from a CDN)

## Project structure

```
src/main/java/ro/jobtrail/
    entity/     JPA entities and enums
    dto/        objects used to build the Kanban board
    service/    stateless EJBs with the business logic
    web/        servlets
src/main/resources/META-INF/
    persistence.xml   the three persistence units
src/main/webapp/
    WEB-INF/views/    JSP pages, grouped by entity
    WEB-INF/web.xml
sql/                  scripts that create the tables and insert test data
```

## Running the project

### Prerequisites

- JDK 25 and Maven
- WildFly 42
- PostgreSQL, SQL Server and Oracle, running locally

### 1. Create the databases

Run the scripts from the `sql/` folder on each server. They create a dedicated user for the application, the tables, the constraints and some test data. Replace the placeholder passwords with your own.

- PostgreSQL: database `jobtrail` on port 5432
- SQL Server: database `jobtrail` on port 1433 (TCP/IP must be enabled, with mixed authentication)
- Oracle: user `JOBTRAIL` in the pluggable database `FREEPDB1` on port 1521

### 2. Configure WildFly

Install the three JDBC drivers as WildFly modules and create the following datasources (for example with `jboss-cli`):

| JNDI name               | Database   |
|-------------------------|------------|
| `java:/jdbc/PostgresDS` | PostgreSQL |
| `java:/jdbc/MssqlDS`    | SQL Server |
| `java:/jdbc/OracleDS`   | Oracle     |

The credentials stay in the WildFly configuration, not in this repository.

### 3. Build and deploy

```
mvn clean package
```

Copy `target/jobtrail.war` into `standalone/deployments` in your WildFly folder and start the server with `standalone.bat` (or `standalone.sh`).

Open `http://localhost:8080/jobtrail/` in the browser. The start page redirects to the Kanban board.

Hibernate is configured with `hbm2ddl.auto=validate`, so it does not create tables. If a table does not match its entity, the deployment fails with a message that names the problem.

## Possible improvements

- User accounts and authentication, with data separated per user
- XA datasources for atomic operations across databases
- Reminders for upcoming interviews
- A REST API for a mobile client

## Author

Andrei Bozac