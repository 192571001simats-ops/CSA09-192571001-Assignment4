$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
$repoRoot = $PSScriptRoot
New-Item -ItemType Directory -Path build -Force | Out-Null
New-Item -ItemType Directory -Path lib -Force | Out-Null
if (!(Test-Path lib/mysql-connector-j-9.2.0.jar)) {
  Invoke-WebRequest 'https://repo.maven.apache.org/maven2/com/mysql/mysql-connector-j/9.2.0/mysql-connector-j-9.2.0.jar' -OutFile lib/mysql-connector-j-9.2.0.jar
}
if ((Get-FileHash 'lib/mysql-connector-j-9.2.0.jar' -Algorithm SHA256).Hash.ToLower() -ne '7e9941bbdcca244d878ea95bfff788fd9ba6a65af757f24be6c632930d61c7ed') { throw 'JDBC driver checksum mismatch' }

$task = 'Task01'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "lib/mysql-connector-j-9.2.0.jar" -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath + ";" + (Join-Path $repoRoot "lib/mysql-connector-j-9.2.0.jar")
& java -Xms128m -Xmx512m -cp $classPath FeeValidation 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task02'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "lib/mysql-connector-j-9.2.0.jar" -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath + ";" + (Join-Path $repoRoot "lib/mysql-connector-j-9.2.0.jar")
& java -Xms128m -Xmx512m -cp $classPath AppointmentValidation 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task03'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "lib/mysql-connector-j-9.2.0.jar" -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath + ";" + (Join-Path $repoRoot "lib/mysql-connector-j-9.2.0.jar")
& java -Xms128m -Xmx512m -cp $classPath ResizeValidation 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task04'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "lib/mysql-connector-j-9.2.0.jar" -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath + ";" + (Join-Path $repoRoot "lib/mysql-connector-j-9.2.0.jar")
& java -Xms128m -Xmx512m -cp $classPath DrawValidation 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task05'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "lib/mysql-connector-j-9.2.0.jar" -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath + ";" + (Join-Path $repoRoot "lib/mysql-connector-j-9.2.0.jar")
& java -Xms128m -Xmx512m -cp $classPath ConverterValidation 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task06'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "lib/mysql-connector-j-9.2.0.jar" -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath + ";" + (Join-Path $repoRoot "lib/mysql-connector-j-9.2.0.jar")
& java -Xms128m -Xmx512m -cp $classPath RegistrationValidation 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location
