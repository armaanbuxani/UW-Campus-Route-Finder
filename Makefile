runTests: FrontendDeveloperTests.java Frontend.java 
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar Frontend.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests

runApp: Frontend.java Backend.java DijkstraGraph.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar App.java
	java --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar App

clean:
	rm -f *.class

runBDTests: BackendDeveloperTests.java
	javac -cp ../junit5.jar:.  BackendDeveloperTests.java 
	javac Backend.java
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests

