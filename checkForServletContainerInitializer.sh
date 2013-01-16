#
# Finds ServletContainerInitializers in the webapp
# You may want to add any jars found by this script
# to conf/catalina.properties -> tomcat.util.scan.DefaultJarScanner.jarsToSkip
# otherwise tomcat startup may slow down significantly.
#
# For best results call this script after a "ant clean tomcat"
#
for i in web/WEB-INF/lib/*.jar
do
	echo ---------- $i
	unzip -t $i \
		| grep META-INF/services/javax.servlet.ServletContainerInitializer
done
