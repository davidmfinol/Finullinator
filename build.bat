cd src
javac *.java
jar cfm Finullinator.jar Manifest.txt *.class
del ..\classes\Finullinator.jar
move Finullinator.jar ..
del *.class