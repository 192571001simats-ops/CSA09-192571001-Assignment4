set -euo pipefail
cd "$(dirname "$0")"
ROOT="$PWD"
mkdir -p build
mkdir -p lib
if [ ! -f lib/mysql-connector-j-9.2.0.jar ]; then
  curl --fail --location https://repo.maven.apache.org/maven2/com/mysql/mysql-connector-j/9.2.0/mysql-connector-j-9.2.0.jar --output lib/mysql-connector-j-9.2.0.jar
fi
printf "%s  %s\n" "7e9941bbdcca244d878ea95bfff788fd9ba6a65af757f24be6c632930d61c7ed" "lib/mysql-connector-j-9.2.0.jar" | sha256sum --check

mkdir -p "build/Task01"
javac -g -encoding UTF-8 -cp "$ROOT/lib/mysql-connector-j-9.2.0.jar" -d "build/Task01" $(find shared Task01 -name "*.java")
TASK_BUILD="Task01"
mkdir -p "runs/Task01"
cd "runs/Task01"
rm -f results.csv
: > output.txt
printf "FeeValidation\n" | tee -a output.txt
java -Xms128m -Xmx512m -cp "$ROOT/build/Task01:$ROOT/lib/mysql-connector-j-9.2.0.jar" FeeValidation 2>&1 | tee -a output.txt
java -cp "$ROOT/build/$TASK_BUILD" SummarizeResults
cd "$ROOT"

mkdir -p "build/Task02"
javac -g -encoding UTF-8 -cp "$ROOT/lib/mysql-connector-j-9.2.0.jar" -d "build/Task02" $(find shared Task02 -name "*.java")
TASK_BUILD="Task02"
mkdir -p "runs/Task02"
cd "runs/Task02"
rm -f results.csv
: > output.txt
printf "AppointmentValidation\n" | tee -a output.txt
java -Xms128m -Xmx512m -cp "$ROOT/build/Task02:$ROOT/lib/mysql-connector-j-9.2.0.jar" AppointmentValidation 2>&1 | tee -a output.txt
java -cp "$ROOT/build/$TASK_BUILD" SummarizeResults
cd "$ROOT"

mkdir -p "build/Task03"
javac -g -encoding UTF-8 -cp "$ROOT/lib/mysql-connector-j-9.2.0.jar" -d "build/Task03" $(find shared Task03 -name "*.java")
TASK_BUILD="Task03"
mkdir -p "runs/Task03"
cd "runs/Task03"
rm -f results.csv
: > output.txt
printf "ResizeValidation\n" | tee -a output.txt
java -Xms128m -Xmx512m -cp "$ROOT/build/Task03:$ROOT/lib/mysql-connector-j-9.2.0.jar" ResizeValidation 2>&1 | tee -a output.txt
java -cp "$ROOT/build/$TASK_BUILD" SummarizeResults
cd "$ROOT"

mkdir -p "build/Task04"
javac -g -encoding UTF-8 -cp "$ROOT/lib/mysql-connector-j-9.2.0.jar" -d "build/Task04" $(find shared Task04 -name "*.java")
TASK_BUILD="Task04"
mkdir -p "runs/Task04"
cd "runs/Task04"
rm -f results.csv
: > output.txt
printf "DrawValidation\n" | tee -a output.txt
java -Xms128m -Xmx512m -cp "$ROOT/build/Task04:$ROOT/lib/mysql-connector-j-9.2.0.jar" DrawValidation 2>&1 | tee -a output.txt
java -cp "$ROOT/build/$TASK_BUILD" SummarizeResults
cd "$ROOT"

mkdir -p "build/Task05"
javac -g -encoding UTF-8 -cp "$ROOT/lib/mysql-connector-j-9.2.0.jar" -d "build/Task05" $(find shared Task05 -name "*.java")
TASK_BUILD="Task05"
mkdir -p "runs/Task05"
cd "runs/Task05"
rm -f results.csv
: > output.txt
printf "ConverterValidation\n" | tee -a output.txt
java -Xms128m -Xmx512m -cp "$ROOT/build/Task05:$ROOT/lib/mysql-connector-j-9.2.0.jar" ConverterValidation 2>&1 | tee -a output.txt
java -cp "$ROOT/build/$TASK_BUILD" SummarizeResults
cd "$ROOT"

mkdir -p "build/Task06"
javac -g -encoding UTF-8 -cp "$ROOT/lib/mysql-connector-j-9.2.0.jar" -d "build/Task06" $(find shared Task06 -name "*.java")
TASK_BUILD="Task06"
mkdir -p "runs/Task06"
cd "runs/Task06"
rm -f results.csv
: > output.txt
printf "RegistrationValidation\n" | tee -a output.txt
java -Xms128m -Xmx512m -cp "$ROOT/build/Task06:$ROOT/lib/mysql-connector-j-9.2.0.jar" RegistrationValidation 2>&1 | tee -a output.txt
java -cp "$ROOT/build/$TASK_BUILD" SummarizeResults
cd "$ROOT"
