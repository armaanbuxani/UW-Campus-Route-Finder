import javafx.application.Application;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        System.out.println("v0.1");
        Backend back = new Backend(new DijkstraGraph<>());
        Frontend.setBackend(back);
        Frontend frontend = new Frontend();
        try {
            back.loadGraphData("campus.dot");
        } catch (IOException e) {
            System.out.println("Error loading data");
            System.exit(1);
        }
        Application.launch(frontend.getClass(), args);
    }
}
