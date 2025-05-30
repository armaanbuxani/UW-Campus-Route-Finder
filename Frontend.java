import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Frontend extends Application implements FrontendInterface {
    private static Backend back;
    private Label pathLabel;
    private List<String> path;
    private List<Integer> time;
    private String start;
    private String end;
    private boolean showTime = false;
    private double totalTime;

    /**
     * Sets the backend instance for frontend.
     * @param backend backend of this project
     */
    public static void setBackend(Backend backend) {
        Frontend.back = backend; // Creates frontend with backend implementation
    }

    /**
     * Creates stage for frontend
     * @param stage window for frontend
     */
    public void start(Stage stage) {
        Pane root = new Pane();

        createAllControls(root);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Frontend Interface Implementation");
        stage.show();
    }

    /**
     * Creates all frontend control options
     * @param parent the parent pane that contains all controls
     */
    public void createAllControls(Pane parent) {
        createShortestPathControls(parent);
        createPathListDisplay(parent);
        createAdditionalFeatureControls(parent);
        createAboutAndQuitControls(parent);
    }

    public void createShortestPathControls(Pane parent) {
        Label src = new Label("Path Start Selector:");
        src.setLayoutX(32);
        src.setLayoutY(16);
        parent.getChildren().add(src);

        // Allows user to enter path start
        TextField srcText = new TextField();
        srcText.setId("srcTextId");
        srcText.setLayoutX(175);
        srcText.setLayoutY(16);
        parent.getChildren().add(srcText);

        Label dst = new Label("Path End Selector:");
        dst.setLayoutX(32);
        dst.setLayoutY(48);
        parent.getChildren().add(dst);

        // Allows user to enter path start
        TextField dstText = new TextField();
        dstText.setId("dstTextId");
        dstText.setLayoutX(175);
        dstText.setLayoutY(48);
        parent.getChildren().add(dstText);

        Button find = new Button("Submit/Find Button");
        find.setId("findId");
        find.setLayoutX(32);
        find.setLayoutY(80);

        find.setOnAction(e -> {
            start = srcText.getText();
            end = dstText.getText();

            if (!start.isEmpty() && !end.isEmpty()) {
                path = back.findShortestPath(start, end);
                updatePathListDisplay(path);
            }
        });
        parent.getChildren().add(find);
    }

    public void createPathListDisplay(Pane parent) {
        pathLabel = new Label();
        pathLabel.setId("pathId");
        pathLabel.setLayoutX(32);
        pathLabel.setLayoutY(112);
        parent.getChildren().add(pathLabel);
    }

    private void updatePathListDisplay(List<String> path) {
        if (!showTime) {
            String formattedPath = String.join("\n\t", path);
            pathLabel.setText("Results List: \n\t" + formattedPath);
        }
        else {
            StringBuilder sb = new StringBuilder("Results List (with walking times):\n\t");
            for (int i = 0; i < path.size() - 1; i++) {
                sb.append(path.get(i)).append("\n\t-(").append(time.get(i))
                        .append("sec)->");
            }
            sb.append(path.get(path.size() - 1));

            String formattedPath = String.join("\n\t", path);
            pathLabel.setText("Results List: \n\t" + formattedPath + "\n\n" + sb + "\n\tTotal time: " + totalTime +
                    "min");
        }
    }

    public void createAdditionalFeatureControls(Pane parent) {
        this.createTravelTimesBox(parent);
        this.createFurthestDestinationControls(parent);
    }

    public void createTravelTimesBox(Pane parent) {
        CheckBox showTimesBox = new CheckBox("Show Walking Times");
        showTimesBox.setId("showTimesBoxId");
        showTimesBox.setLayoutX(200);
        showTimesBox.setLayoutY(80);

        showTimesBox.setOnAction(e -> {
            if(showTimesBox.isSelected()) {
                List<Double> timeDouble = back.getTravelTimesOnPath(start, end);
                time = new ArrayList<Integer>();
                for (double d : timeDouble) {
                    time.add((int) Math.round(d));
                }
                double totalTimeSec = 0;
                for (double t : time) {
                    totalTimeSec += t;
                }
                totalTime = Math.round(totalTimeSec / 60 * 100.0) / 100.0;
                showTime = true;
            }
        });
        parent.getChildren().add(showTimesBox);
    }

    public void createFurthestDestinationControls(Pane parent) {
        Label locationSelector = new Label("Location Selector:");
        locationSelector.setLayoutX(500);
        locationSelector.setLayoutY(16);
        parent.getChildren().add(locationSelector);

        // Allows user to enter path start
        TextField furthestText = new TextField();
        furthestText.setId("furthestTextId");
        furthestText.setLayoutX(625);
        furthestText.setLayoutY(16);
        parent.getChildren().add(furthestText);

        Button furthestFromButton = new Button("Find Most Distant Location");
        furthestFromButton.setId("furthestFromButtonId");
        furthestFromButton.setLayoutX(500);
        furthestFromButton.setLayoutY(48);
        furthestFromButton.setOnAction(e -> {
            String location = furthestText.getText();
            String furthestLocation = back.getMostDistantLocation(location);

            Label furthestFromLabel = new Label("Most Distance Location:  " + furthestLocation);
            furthestFromLabel.setId("furthestFromLabelId");
            furthestFromLabel.setLayoutX(500);
            furthestFromLabel.setLayoutY(80);
            parent.getChildren().add(furthestFromLabel);
        });
        parent.getChildren().add(furthestFromButton);
    }

    public void createAboutAndQuitControls(Pane parent) {
        Button about = new Button("About");
        about.setId("aboutId");
        about.setLayoutX(32);
        about.setLayoutY(560);
        about.setOnAction(e -> {
            Label aboutLabel = new Label("Frontend made by Armaan Buxani");
            aboutLabel.setId("aboutLabelId");
            aboutLabel.setLayoutX(32);
            aboutLabel.setLayoutY(550);
            parent.getChildren().add(aboutLabel);
        });
        parent.getChildren().add(about);

        Button quit = new Button("Quit");
        quit.setId("quitId");
        quit.setLayoutX(96);
        quit.setLayoutY(560);
        quit.setOnAction(e -> Platform.exit());
        parent.getChildren().add(quit);
    }
}

