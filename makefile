make:
	#dependencies/javafx-sdk-11.0.2/lib
	javac --module-path dependencies/openjfx-11.0.2_linux-x64_bin-sdk/lib --add-modules javafx.controls -classpath bin -sourcepath src -d bin @sources

run:
	java --module-path dependencies/openjfx-11.0.2_osx-x64_bin-sdk/lib --add-modules javafx.controls -cp bin LepinskiEngine.GameEngine

runLinux:
	java --module-path dependencies/openjfx-11.0.2_linux-x64_bin-sdk/lib --add-modules javafx.controls -cp bin LepinskiEngine.GameEngine

runWin:
	java --module-path dependencies/openjfx-11.0.2_windows-x64_bin-sdk/lib --add-modules javafx.controls -cp bin LepinskiEngine.GameEngine

runJar:
	java -jar bin/Maze.jar

doc:
	javadoc --module-path dependencies/javafx-sdk-11.0.2/lib --add-modules javafx.controls -classpath bin -sourcepath src -d docs @sources

clean:
	rm bin/*.class
	clear
