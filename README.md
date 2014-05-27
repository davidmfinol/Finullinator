Finullinator
============

The spiritual successor of LIBnation. 
This project was my final project for my CS3 class, with my partner Christiaan Cleary. 
Christiaan acquired and set up the art assets, while I did all of the programming.
Our original goals for the project can be found in Finullinator.doc.
Though a lot of those goals were not met, the project itself served as a very good introduction to building a game engine.


Build
-------------
Windows: 
Running build.bat should work with a correctly set java classpath.

Otherwise: 
1. Run "javac *.java" in the src/ folder to compile all the java files.
2. Run "jar cfm Finullinator.jar Manifest.txt *.class" in the src/ folder, and move the resulting Finullinator.jar file to the root folder.
3. You may want to clean-up/remove the now unneeded .class files.


Run
-------------
Either double-click on Finullinator.jar or run "java -jar Finullinator.jar" to run the game with default settings.
Add the -h flag ("java -jar Finullinator.jar -h") to see the settings that can be changed. Parameters changed through command line will override the settings.dat file.