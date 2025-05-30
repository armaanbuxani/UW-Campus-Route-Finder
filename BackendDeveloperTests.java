import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class BackendDeveloperTests {
    private BackendInterface backend;
    private GraphADT<String, Double> graph;

    @BeforeEach
    void setUp() {
        graph = new GraphPlaceholder();
        backend = new Backend();
    }

    /**
     * Test that the list of all locations is correctly retrieved from the backend.
     */
    @Test
    void testGetListOfAllLocations() {
        List<String> locations = backend.getListOfAllLocations();
        assertEquals(3, locations.size());
        assertTrue(locations.contains("Union South"));
        assertTrue(locations.contains("Computer Sciences and Statistics"));
        assertTrue(locations.contains("Atmospheric, Oceanic and Space Sciences"));
    }

    /**
     * Test that the shortest path between two locations is correctly found by the backend.
     */
    @Test
    void testFindShortestPath() {
        List<String> path = backend.findShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        assertEquals(3, path.size());
        assertEquals("Union South", path.get(0));
        assertEquals("Computer Sciences and Statistics", path.get(1));
        assertEquals("Atmospheric, Oceanic and Space Sciences", path.get(2));
    }

    /**
     * Test that the travel times on the shortest path are correctly retrieved by the backend.
     */
    @Test
    void testGetTravelTimesOnPath() {
        List<Double> travelTimes = backend.getTravelTimesOnPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        assertEquals(2, travelTimes.size());
        assertEquals(176.0, travelTimes.get(0));
        assertEquals(127.2, travelTimes.get(1));
    }

    /**
     * Test that the most distant location from a given location is correctly found by the backend.
     */
    @Test
    void testGetMostDistantLocation() {
        String mostDistant = backend.getMostDistantLocation("Union South");
        assertEquals("Computer Sciences and Statistics", mostDistant);
    }
}
