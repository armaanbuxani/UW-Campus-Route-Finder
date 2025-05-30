# University of Wisconsin Campus Route Finder

A JavaFX-based application that helps users find the shortest route and time between various locations on the UW-Madison campus. The tool also supports identifying the furthest reachable location from a given point using graph algorithms.

---

## Features

- Developed a JavaFX application to compute optimal routes and travel times between campus locations.
- Applied **Dijkstra's Algorithm** to calculate shortest paths with high accuracy.
- Designed a modular and scalable backend using **Java generics** and well-structured interfaces.
- Wrote **JUnit test cases** and implemented exception handling to validate user inputs and improve reliability.
- Built an intuitive **graphical user interface (GUI)** using JavaFX to enhance the overall user experience.

---

## Technologies Used

- **Java**
- **JavaFX**
- **JUnit**
- **Graph Algorithms (Dijkstra's)**
- **Custom Interfaces & Generics**

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/uw-campus-route-finder.git
2. Navigate into the project folder:
   ```bash
   cd uw-campus-route-finder
3. Ensure junit5.jar is available in the parent directory.
4. Use the provided Makefile commands:
- To compile and run the application:
  ```bash
  make runApp
- To run backend developer tests:
  ```bash
  make runBDTests
- To run frontend developer tests:
  ```bash
  make runFDTests
- To clean compiled .class files:
  ```bash
  make clean
