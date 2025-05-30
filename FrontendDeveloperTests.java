import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrontendDeveloperTests extends ApplicationTest {
    private Backend backend;
    private Frontend frontend;
    private GraphADT<String, Double> graph;

    @BeforeEach
    void setup() {
        graph = new DijkstraGraph<>();
        backend = new Backend(graph);
        try {
            backend.loadGraphData("campus.dot");
        }
        catch (IOException e) {
            fail("Error loading data" + e.getMessage());
        }
        Frontend.setBackend(backend);
    }

    @Start
    public void start(Stage stage) {
        Pane root = new Pane();
        Frontend.setBackend(backend);
        frontend = new Frontend();
        frontend.createAllControls(root);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Tests if when Submit button is pressed, it displays the path
     */
    @Test
    public void testShortestPathButton() {
        // Tests if button is present and correct
        Button button = lookup("#findId").query();
        assertEquals("Submit/Find Button", button.getText());

        // User input
        clickOn("#srcTextId");
        write("Union South");
        clickOn("#dstTextId");
        write("Atmospheric, Oceanic and Space Sciences");

        // Tests if path is displayed when button is clicked
        clickOn("#findId");
        Label label = lookup("#pathId").query();
        assertEquals("Results List: \n\tUnion South\n\tAtmospheric, Oceanic and Space Sciences", label.getText());
    }

    /**
     * Tests if when Travel Times Box is pressed, it displays the path with walking time
     */
    @Test
    public void testTravelTimesBox() throws Exception {
        Button button = lookup("#findId").query();

        // User input
        clickOn("#srcTextId");
        write("Union South");
        clickOn("#dstTextId");
        write("Atmospheric, Oceanic and Space Sciences");

        //Tests if path timing is displayed when checkbox and button is clicked
        clickOn("#findId");
        clickOn("#showTimesBoxId");
        clickOn("#findId");
        Label label = lookup("#pathId").query();
        assertEquals("Results List: \n\tUnion South\n\tAtmospheric, " +
                "Oceanic and Space Sciences\n\nResults List (with walking times):\n\tUnion South\n\t" +
                "-(182sec)->Atmospheric, Oceanic and Space " +
                "Sciences\n\tTotal time: 3.03min", label.getText());
    }

    /**
     * Tests if when Furthest Destination Button is pressed, the most distant location is displayed
     */
    @Test
    public void testFurthestDestinationButton() {
        // Tests if button is present and correct
        Button button = lookup("#furthestFromButtonId").query();
        assertEquals("Find Most Distant Location", button.getText());

        // Simulates user input
        clickOn("#furthestTextId");
        write("Union South");

        // Tests if most distant location is displayed when button is clicked
        clickOn("#furthestFromButtonId");
        Label label = lookup("#furthestFromLabelId").query();
        assertEquals("Most Distance Location:  Smith Residence Hall", label.getText());
    }

    /**
     * Tests if when About button is pressed, details about the project is displayed. Also checks if
     * Quit button is present
     */
    @Test
    public void testQuitAndAboutControls() {
        // Tests if button is present and correct
        Button aboutButton = lookup("#aboutId").query();
        assertEquals("About", aboutButton.getText());

        clickOn("#aboutId");
        Label label = lookup("#aboutLabelId").query();
        assertEquals("Frontend made by Armaan Buxani", label.getText());

        // Tests if button is present and correct
        Button quitButton = lookup("#quitId").query();
        assertEquals("Quit", quitButton.getText());
    }

    /**
     * Tests if frontend and backend accurately work to show correct shortest path
     */
    @Test
    void testIntegrationShortestPath() {
        // Simulates user input
        clickOn("#srcTextId");
        write("Union South");
        clickOn("#dstTextId");
        write("Atmospheric, Oceanic and Space Sciences");
        clickOn("#findId");

        Label label = lookup("#pathId").query();

        // Gets expected path from backend
        List<String> expectedPathList = backend.findShortestPath("Union South", "Atmospheric, Oceanic and Space " +
                "Sciences");
        System.out.println(expectedPathList);
        String expectedPath = "Results List: \n\t" + expectedPathList.get(0) + "\n\t" + expectedPathList.get(1);

        // Tests if frontend response matches backend
        assertTrue(label.getText().equals(expectedPath), "Shortest path is incorrect");
    }

    /**
     * Tests if backend and frontend accurately show furthest location
     */
    @Test
    void testIntegrationFurthestLocation() {
        //Simulates user input
        clickOn("#furthestTextId");
        write("Union South");
        clickOn("#furthestFromButtonId");

        Label label = lookup("#furthestFromLabelId").query();
        String expectedLocation = backend.getMostDistantLocation("Union South");

        // Tests if frontend response matches backend
        assertTrue(label.getText().contains(expectedLocation));
    }

    /**
     * Tests if backend correctly creates a list for shortest path
     */
    @Test
    void testPartnerFindShortestPath() {
        // Creates backend implementation
        List<String> path = backend.findShortestPath("Memorial Union", "Bascom Hall");

        // Tests if right location is at right index
        assertEquals("Memorial Union", path.get(0));
        assertEquals("Radio Hall", path.get(1));
        assertEquals("Education Building", path.get(2));
        assertEquals("North Hall", path.get(3));
        assertEquals("Bascom Hall", path.get(4));
        // Tests if size is correct
        assertEquals(5, path.size());
    }

    /**
     * Tests if backend correctly returns most distant location
     */
    @Test
    void testPartnerGetMostDistantLocation() {
        // Creates backend implementation
        String furthestLocation = backend.getMostDistantLocation("Memorial Union");

        // Tests if most distant is correct
        assertEquals("Smith Residence Hall", furthestLocation);
    }


}


