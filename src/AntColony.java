import java.util.Random;

/**
 * Created by bluesialia on 27/12/15.
 * Based in <a href="https://github.com/lukedodd/ant-tsp">Lukedodd's work</a>.
 */
public class AntColony {

    // Number of iterations
    private final int _ITERATIONS;

    // Ants in the colony
    private final int _NUMANTS;
    private final Ant[] _ANTS;

    // Initial value of pheromones
    private final double _INITIAL;

    // Contribution of pheromones by each ant
    private final double _CONTRIBUTION;

    // Evaporation of pheromones
    private final double _EVAPORATION;

    // Influence of the pheromones
    private final double _INFLUENCE;

    // Graph
    private final double[][] _GRAPH;

    // Pheromones
    private final double[][] _PHEROMONES;

    // Number of Towns
    private final int _TOWNS;

    // Random Generator
    private final Random _RANDOM = new Random();

    // Best Tour
    private int[] _bestTour;
    private double _bestTourLength;

    /**
     * Instance of a colony of ants to solve a TSP problem.
     * @param numAnts Number of ants in the colony.
     * @param initial Initial amount of pheromones in each path.
     * @param contribution Amount of pheromones an ant will drop throughout its tour.
     * @param evaporation Number between 0 and 1 referencing the amount of pheromones that won't evaorate.
     * @param influence Number between 0 and 1 referencing the influece the pheromones have on the ants.
     * @param graph 2D matrix containing the cost of going from each node to each node.
     * @param iterations Amount of tours each ant will generate.
     */
    public AntColony(int numAnts, double initial, double contribution, double evaporation, double influence, double[][] graph, int iterations) {
        _NUMANTS = numAnts;
        _INITIAL = initial;
        _CONTRIBUTION = contribution;
        _EVAPORATION = evaporation;
        _INFLUENCE = influence;
        _GRAPH = graph;
        _ITERATIONS = iterations;
        _TOWNS = graph.length;
        _ANTS = new Ant[_NUMANTS];

        for (int i = 0; i < _NUMANTS; i++) {
            _ANTS[i] = new Ant();
        }

        _PHEROMONES = new double[_TOWNS][_TOWNS];
        for (int i = 0; i < _PHEROMONES.length; i++) {
            for (int j = 0; j < _PHEROMONES[i].length; j++) {
                _PHEROMONES[i][j] = _INITIAL;
            }
        }

    }

    public int[] solve() {
        for (int iteration = 0; iteration < _ITERATIONS; iteration++) {
            setupAnts();
            moveAnts();
            updateTrails();
            updateBest();
        }

        System.out.println("Best tour length: " + (_bestTourLength));
        System.out.println("Best tour:" + tourToString(_bestTour));
        return _bestTour.clone();
    }

    private void setupAnts() {
        for (int i = 0; i < _NUMANTS; i++) {
            _ANTS[i].reset(_RANDOM.nextInt(_TOWNS));
        }
    }

    private void moveAnts() {
        for (int i = 0; i < _TOWNS - 1; i++) {
            for (int j = 0; j < _NUMANTS; j++) {
                _ANTS[j].move();
            }
        }
    }

    private void updateTrails() {
        // evaporation
        for (int i = 0; i < _TOWNS; i++)
            for (int j = 0; j < _TOWNS; j++)
                _PHEROMONES[i][j] *= _EVAPORATION;

        // each ants contribution
        for (Ant ant : _ANTS) {
            double contribution = _CONTRIBUTION / ant.tourLength();
            for (int i = 0; i < _TOWNS - 1; i++) {
                _PHEROMONES[ant.TOUR[i]][ant.TOUR[i + 1]] += contribution;
            }
            _PHEROMONES[ant.TOUR[_TOWNS - 1]][ant.TOUR[0]] += contribution;
        }
    }

    private void updateBest() {
        if (_bestTour == null) {
            _bestTour = _ANTS[0].TOUR;
            _bestTourLength = _ANTS[0].tourLength();
        }
        for (Ant a : _ANTS) {
            if (a.tourLength() < _bestTourLength) {
                _bestTourLength = a.tourLength();
                _bestTour = a.TOUR.clone();
            }
        }
    }

    private static String tourToString(int tour[]) {
        String t = "";
        for (int i : tour)
            t = t + " " + i;
        return t;
    }

    private class Ant {
        private int currentIndex = 0;
        private final int[] TOUR = new int[_TOWNS];
        private final boolean[] VISITED = new boolean[_TOWNS];

        /**
         * The Ant moves to a random not visited town.
         */
        public void move() {
            if (_RANDOM.nextDouble() > _INFLUENCE) {
                followPheromones();
            } else {
                actRandom();
            }
        }

        /**
         * The Ant moves to a totally random not visited town.
         */
        private void actRandom() {
            int randToTravel = _RANDOM.nextInt(_TOWNS - currentIndex);
            int unvisitedIndex = 0;
            for (int i = 0; i < _TOWNS; i++) {
                if (!visited(i)) {
                    unvisitedIndex++;
                    if (randToTravel < unvisitedIndex) {
                        visitTown(i);
                        break;
                    }
                }
            }
        }

        /**
         * The Ant moves to a random not visited town following the pheromones.
         */
        private void followPheromones() {
            double ac = 0;
            for (int i = 0; i < _TOWNS; i++) {
                if (!visited(i)) {
                    ac += _PHEROMONES[TOUR[currentIndex]][i];
                }
            }

            double randToTravel = _RANDOM.nextDouble() * ac;
            ac = 0;
            for (int i = 0; i < _TOWNS; i++) {
                if (!visited(i)) {
                    ac += _PHEROMONES[TOUR[currentIndex]][i];
                    if (randToTravel < ac) {
                        visitTown(i);
                        break;
                    }
                }
            }
        }

        private void visitTown(int town) {
            currentIndex++;
            TOUR[currentIndex] = town;
            VISITED[town] = true;
        }

        public boolean visited(int i) {
            return VISITED[i];
        }

        public double tourLength() {
            double length = _GRAPH[TOUR[_TOWNS - 1]][TOUR[0]];
            for (int i = 0; i < _TOWNS - 1; i++) {
                length += _GRAPH[TOUR[i]][TOUR[i + 1]];
            }
            return length;
        }

        public void reset(int initialTown) {
            currentIndex = 0;
            for (int i = 0; i < _TOWNS; i++) {
                VISITED[i] = false;
            }
            TOUR[0] = initialTown;
            VISITED[initialTown] = true;
        }
    }
}
