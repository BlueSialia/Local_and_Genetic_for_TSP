import java.util.Random;

public class Greedy {

    private double[][] distanceMatrix = new double[42][42];
    private final int NUM_CALCULATIONS = 1000000;

    public Greedy(double[][] matrix) {
        distanceMatrix = matrix;
        localSearchG();
    }

    private void localSearchG() {
        int[] bestRoute = new int[42];
        int bestCost = Integer.MAX_VALUE;
        int totalCost = 0;
        System.out.println("*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+");
        System.out.println("*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+ GREEDY *+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+");
        System.out.println("*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+");
        for (int j = 0; j < 10; j++) {
            int[] route = randomRoute();
            int cost = calculateCost(route, 1);
            int calculations = 1;
            int i = 0;
            int[] routeAux = route;
            int costAux = cost;
            boolean stop = false;
            while(calculations < NUM_CALCULATIONS && !stop){
                int[] routeAux2 = swap(route, i, i+1);
                i++;
                int costAux2 = calculateCost(routeAux2, 1);
                calculations++;
                if(costAux2<costAux){
                    routeAux = routeAux2;
                    costAux = costAux2;
                }
                if(i == route.length - 1){
                    if(cost == costAux){
                        stop = true;
                    }
                    else{
                        route = routeAux;
                        cost = costAux;
                        i = 0;
                    }
                }
            }
            totalCost += cost;
            if(costAux < bestCost){
                bestRoute = route.clone();
                bestCost = cost;
            }
            System.out.println("===========================" + "Iteraction " + j + "===========================");
            System.out.println("******** ROUTE *********");
            for (int aRoute : route) {
                System.out.print(aRoute + "\t");
            }
            System.out.println("\nCoste: " + cost);

            //System.out.println("======= GREEDY CALCULATIONS " + j + " ========");
            //System.out.println(calculations);
            //System.out.println("(" + j + "," + cost + ")");
        }
        System.out.println("===============================================================");
        System.out.println("===================== Best Solution ===========================");
        System.out.println("===============================================================");
        for (int aBestRoute : bestRoute) {
            System.out.print(aBestRoute + "\t");
        }
        System.out.println("\nCoste: " + bestCost);
        System.out.println("===============================================================");
        System.out.println("Average fitness: " + totalCost/10.0);
    }

    /**
     * Calculates the cost of the array route. It takes the distances values from the matrix distanceMatrix.
     * @param route
     * @param mode
     * @return int cost
     */
    private int calculateCost(int[] route, int mode) {
        int cost = 0;
        for (int i = 0; i < route.length - mode; i++) {
            if(route[i] > route[i+1]){
                cost += distanceMatrix[route[i]][route[i+1]];
            }
            else{
                cost += distanceMatrix[route[i+1]][route[i]];
            }
        }
        if(route[0] > route[route.length - mode]){
            cost += distanceMatrix[route[0]][route[route.length - mode]];
        }
        else{
            cost += distanceMatrix[route[route.length - mode]][route[0]];
        }
        return cost;
    }

    /**
     * Swap the position i and the position j on the array route and return the result on the array routeAux.
     * @param route
     * @param i
     * @param j
     * @return int[] routeAux
     */
    private int[] swap(int[] route, int i, int j) {
        int[] routeAux = route.clone();
        int positionI = routeAux[i];
        routeAux[i] = routeAux[j];
        routeAux[j] = positionI;
        return routeAux;
    }

    /**
     * It calculates a random route taken all the cities
     * @return int route
     */
    private int[] randomRoute(){
        int[] route = new int[42];
        Random r = new Random();
        boolean addZero = true;
        for (int i = 0; i < route.length; i++) {
            int newCity = r.nextInt(route.length);
            if(!contains(route, newCity)){
                if(newCity == 0 && addZero){
                    route[i] = newCity;
                    addZero = false;
                }
                else if(newCity == 0 && !addZero){
                    i--;
                }
                else {
                    route[i] = newCity;
                }
            }
            else{
                i--;
            }
        }
        return route;
    }

    /**
     * It look for the newCuty inside the route. If it founds it return TRUE, else return FALSE
     * @param route
     * @param newCity
     * @return boolean found
     */
    private boolean contains(int[] route, int newCity) {
        boolean finded = false;
        int i = 0;
        while(!finded && i<route.length){
            if(route[i] == newCity && newCity != 0){
                finded = true;
            }
            i++;
        }
        return finded;
    }

}
