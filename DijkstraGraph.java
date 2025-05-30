// === CS400 Spring 2024 File Header Information ===
// Name: Armaan Buxani
// Email: buxani@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Test;

import javax.naming.directory.SearchControls;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new PlaceholderMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // implement in step 5.3

        // Creates priority queue
        PriorityQueue<SearchNode> pq = new PriorityQueue<SearchNode>();
        // Creates map to keep track of visited nodes and their minimum cost
        PlaceholderMap<NodeType, SearchNode> visited = new PlaceholderMap<>();

        // Checks if the start node exists in the graph
        if (!nodes.containsKey(start)) {
            throw new NoSuchElementException("Start does not exist");
        }

        // Checks if the end node exists in the graph
        if (!nodes.containsKey(end)) {
            throw new NoSuchElementException("End does not exist");
        }

        // Inserts the start node into the priority queue with cost 0
        SearchNode startNode = new SearchNode(nodes.get(start), 0, null);
        pq.add(startNode);

        // Explores the graph
        while (!pq.isEmpty()) {
            SearchNode current = pq.poll();
            Node currentNode = current.node;
            NodeType currentData = currentNode.data;

            // Ends if end node is reached
            if (currentData.equals(end)) {
                return current;
            }

            // Checks if we have not visited the node yet to continue
            if (!visited.containsKey(currentData)) {
                visited.put(currentData, current); // Marks as visited

                for (Edge edge : currentNode.edgesLeaving) {
                    Node neighbor = edge.successor; // Finds successor
                    NodeType neighborData = neighbor.data; // Finds data of neighbor
                    double edgeWeight = edge.data.doubleValue(); // Gets cost
                    double updatedCost = current.cost + edgeWeight; // Updates total cost from start to end

                    // If the neighbor has not been visited or a shorter path to it is found
                    if (!visited.containsKey(neighborData) || updatedCost < visited.get(neighborData).cost) {
                        // Creates a new SearchNode for the neighbor with updated cost and path
                        SearchNode neighborSearchNode = new SearchNode(neighbor, updatedCost, current);
                        pq.add(neighborSearchNode); // Re-iterates to find new short path
                    }
                }
            }
        }
        // Throws exception if path is not found
        throw new NoSuchElementException("Path does not exist");
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // Computes shortest Path
        SearchNode endNode = computeShortestPath(start, end);
        // Creates Linked List to store path
        LinkedList<NodeType> path = new LinkedList<>();

        // Enters path in List
        for(SearchNode node = endNode; node != null; node = node.predecessor) {
            path.addFirst((NodeType)node.node.data);
        }

        return path;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        try {
            SearchNode endNode = computeShortestPath(start, end);
            return endNode.cost;
        }
        catch (NoSuchElementException e) {
            return Double.NaN;
        }
    }

    // TODO: implement 3+ tests in step 4.1

    @Test
    public void testShortPathOne() {
        // Creating graph
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        // Creating nodes
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        // Creating edges
        graph.insertEdge("A", "C", 1);
        graph.insertEdge("A", "D", 4);
        graph.insertEdge("C", "E", 10);
        graph.insertEdge("D", "B", 2);
        graph.insertEdge("D", "E", 10);
        graph.insertEdge("B", "E", 1);

        // Tests if shortest path is correct
        assertEquals(Arrays.asList("A","D","B","E"), graph.shortestPathData("A", "E"));

        // Tests if shortest path cost is correct
        assertEquals(7, graph.shortestPathCost("A", "E"));
    }

    @Test
    public void testShortPathTwo() {
        // Creating graph
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        // Creating nodes
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        // Creating edges
        graph.insertEdge("A", "C", 1);
        graph.insertEdge("C", "A", 1);
        graph.insertEdge("A", "D", 4);
        graph.insertEdge("D", "A", 4);
        graph.insertEdge("C", "E", 10);
        graph.insertEdge("E", "C", 10);
        graph.insertEdge("D", "B", 2);
        graph.insertEdge("B", "D", 2);
        graph.insertEdge("D", "E", 10);
        graph.insertEdge("E", "D", 10);
        graph.insertEdge("B", "E", 1);
        graph.insertEdge("E", "B", 1);

        // Tests if shortest path is correct
        assertEquals(Arrays.asList("C","A","D","B"), graph.shortestPathData("C", "B"));

        // Tests if shortest path cost is correct
        assertEquals(7, graph.shortestPathCost("C", "B"));
    }

    @Test
    public void testNoPath() {
        // Creating graph
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
        // Creating nodes
        graph.insertNode("A");
        graph.insertNode("B");
        // Creating edge that doesn't connect A and B
        graph.insertEdge("A", "A", 1);

        // Tests if NoSuchElementException is thrown when searching for a non-existent path
        assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathData("A", "B");
        }, "NoSuchElementException was expected when no path exists from A to B");

        // Tests if NoSuchElementException is thrown when searching for a non-existent path
        assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathCost("A", "B");
        }, "NoSuchElementException was expected when no path exists from A to B for cost computation");
    }
}
