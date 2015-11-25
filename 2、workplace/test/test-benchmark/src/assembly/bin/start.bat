set SERVICE_NAME=${project.artifactId}
set VERSION=${project.version}
java -jar -Dbase.dir=.. -Dconf.dir=..\conf ..\lib\%SERVICE_NAME%-%VERSION%.jar