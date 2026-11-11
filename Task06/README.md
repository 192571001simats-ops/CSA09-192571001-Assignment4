# Task06

Athisaya U  
192571001  
Programming in Java CSA09  
OpenJDK 21.0.13

## Compile and run

From the repository root on Linux or macOS:

```bash
mkdir -p build/Task06
javac -encoding UTF-8 -d build/Task06 $(find shared Task06 -name "*.java")
cd Task06
java -cp ../build/Task06 RegistrationValidation
cd ..
```

Windows PowerShell uses `Get-ChildItem shared,Task06 -Recurse -Filter *.java` to collect sources before calling javac.

## Execution evidence

The captured output is in output.txt. Raw repeated measurements, where required, are in results.csv; summary.csv reports the mean, median, range and standard deviation. Heap deltas reflect heap occupancy and are not allocated-byte counts.

Implementation source and recorded output appear in implementation-screenshot.png. Full execution and compilation records are retained in implementation-run.txt and implementation-compile.txt.
