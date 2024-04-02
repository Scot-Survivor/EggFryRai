echo "Building Java Project will create a FatJar called "PDMS.jar" on the same level this is ran"
CALL gradlew.bat shadowJar
echo "Java Project Built"
echo "Copying FatJar to the root of the project"
copy build\libs\pdms-1.0.0-all.jar PDMS.jar
echo "FatJar Copied"