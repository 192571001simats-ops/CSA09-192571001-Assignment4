# Event Driven Desktop Applications

Athisaya U  
192571001  
CSA09 Programming in Java  
Assignment 4

This is my Java Assignment 4 on Swing and AWT applications. It has 6 tasks covering forms, events, drawing, conversion and database registration.

The task folders contain the Java programs, saved output and screenshots. Input files or generators and timing CSV files are included where needed.

## How to run

Install a full JDK 21, then open a terminal in this repository.

- Windows PowerShell: `./run_all.ps1`
- Linux or macOS: `bash run_all.sh`

The scripts compile and run each task separately. New output and timing summaries go into `runs/TaskNN`, so the saved results in the task folders stay unchanged.

## Database setup

The database setup is in `schema.sql`. The default connection is:

`jdbc:mysql://127.0.0.1:3306/java_assignments?rewriteBatchedStatements=true`

Set `JAVA_ASSIGNMENT_DB_URL`, `JAVA_ASSIGNMENT_DB_USER` and `JAVA_ASSIGNMENT_DB_PASSWORD` for your local database. The code defaults to `root` with an empty password, so use a configured local account instead. The run scripts download MySQL Connector/J from Maven Central and check its SHA-256 hash.

Use a separate assignment database. The tests create and reset the assignment tables. The runtime recheck passed with MariaDB using MySQL Connector/J; Oracle MySQL 8.4 was not tested in that recheck.

## Opening the applications

The GUI programs need a desktop display. After compiling, the interactive entry points are `FeeWindow`, `AppointmentDesk`, `LibrarySearch`, `DrawPad`, `Converter` and `EventRegistration` in their task builds. `ConverterStyleLauncher` accepts `frame`, `named`, `anonymous` or `lambda`.

## Tasks

| Folder | Programs to run |
| --- | --- |
| Task01 | FeeValidation |
| Task02 | AppointmentValidation |
| Task03 | ResizeValidation |
| Task04 | DrawValidation |
| Task05 | ConverterValidation |
| Task06 | RegistrationValidation |

## Timing results

Where timing is needed, the results use two warmup runs and five measured runs. The report tables use milliseconds, while the raw CSV files use nanoseconds. Input generation is outside the timed part.

The heap value is a change in memory usage, not the total bytes allocated. Timing can vary with JVM warmup, garbage collection and other work on the computer, so small differences may not repeat exactly.

## Report

[PDF report](report/Athisaya_U_Java_Assignment4_Completed.pdf)  

## Output and screenshots

Each task has a `real-execution.png` terminal screenshot and a `terminal-run.txt` file with the compiler and program output. The task folders also include screenshots of the running applications. `execution-output.png` shows the saved execution log.
