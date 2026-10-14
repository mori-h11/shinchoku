$ErrorActionPreference='Stop'
$projectRoot=$PSScriptRoot
$javaBin='C:\pleiades\2025-12\java\25\bin'
$h2Jar=Join-Path $projectRoot 'src\main\webapp\WEB-INF\lib\h2-2.4.240.jar'
$servletJar='C:\pleiades\2025-12\tomcat\11\lib\servlet-api.jar'
$outputDir=Join-Path $projectRoot 'build\regression'
New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
$javaSources=@(Get-ChildItem (Join-Path $projectRoot 'src\main\java'),(Join-Path $projectRoot 'src\test\java') -Recurse -Filter '*.java' | Select-Object -ExpandProperty FullName)
& (Join-Path $javaBin 'javac.exe') -encoding UTF-8 -cp "$h2Jar;$servletJar" -d $outputDir $javaSources
if ($LASTEXITCODE -ne 0) { throw 'Compile failed' }
& (Join-Path $javaBin 'java.exe') -cp "$outputDir;$h2Jar;$servletJar" dao.RegressionTest
if ($LASTEXITCODE -ne 0) { throw 'Tests failed' }
