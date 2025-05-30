import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Backend implements BackendInterface {
    private GraphADT<String, Double> graph;
    private List<String> listOfAllLocations;

    public Backend(GraphADT<String, Double> graph) {
        this.graph = graph;
        this.listOfAllLocations = new ArrayList<>();
    }

    @Override
    public void loadGraphData(String filename) throws IOException {
        File file = new File(filename);

        if (!file.exists() || file.length() == 0) {
            throw new IOException("File does not exist or is empty!");
        }

        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()) {
            String line = scan.nextLine().trim();
            if (line.startsWith("\"")) {
                String[] parts = line.split("\"");
                if (parts.length >= 4) {
                    String start = parts[1];
                    String finish = parts[3];
                    String[] edgeDetails = parts[4].trim().split("\\[");
                    if (edgeDetails.length >= 2) {
                        String[] secondsPart = edgeDetails[1].split("=");
                        if (secondsPart.length >= 2) {
                            double time = Double.parseDouble(secondsPart[1].replaceAll("[^\\d.]", ""));
                            if (!graph.containsNode(start)) {
                                graph.insertNode(start);
                                listOfAllLocations.add(start);
                            }
                            if (!graph.containsNode(finish)) {
                                graph.insertNode(finish);
                                listOfAllLocations.add(finish);
                            }
                            if (!graph.containsEdge(start, finish)) {
                                graph.insertEdge(start, finish, time);
                            }
                        }
                    }
                }
            }
        }
        scan.close();
    }

    @Override
    public List<String> getListOfAllLocations() {
        return listOfAllLocations;
    }

    @Override
    public List<String> findShortestPath(String startLocation, String endLocation) {
        return graph.shortestPathData(startLocation, endLocation);
    }

    @Override
    public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
        List<String> path = findShortestPath(startLocation, endLocation);
        List<Double> travelTimes = new ArrayList<>();

        if (path.size() <= 1) {
            return travelTimes; // Return empty list if no path or only one node is found
        }

        for (int i = 0; i < path.size() - 1; i++) {
            travelTimes.add(graph.getEdge(path.get(i), path.get(i + 1)));
        }

        return travelTimes;
    }

    @Override
    public String getMostDistantLocation(String location) throws NoSuchElementException {
        List<String> allLocations = getListOfAllLocations();
        String mostDistantLocation = null;
        double maxCost = 0.0;

        for (String loc : allLocations) {
            if (!loc.equals(location)) {
                double cost = graph.shortestPathCost(location, loc);
                if (cost > maxCost) {
                    maxCost = cost;
                    mostDistantLocation = loc;
                }
            }
        }

        if (mostDistantLocation == null) {
            throw new NoSuchElementException("Location not found in the graph: " + location);
        }

        return mostDistantLocation;
    }
}


