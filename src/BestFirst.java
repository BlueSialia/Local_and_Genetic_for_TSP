import java.util.Random;

/**
 * Created by bluesialia on 31/12/15.
 */
public class BestFirst {

    private static int[][] distanceMatrix = new int[42][42];
    private static final int NUM_CALCULATIONS = 1000000;

    /**
     * Local search using Best First algorithm. This algorithm explore the neighbors and takes the first one.
     * @return
     */
    private void localSearchBF(){
        int[] bestRoute = new int[42];
        int bestCost = Integer.MAX_VALUE;
        int totalCost = 0;
        System.out.println("*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+");
        System.out.println("*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+ BEST FIRST *+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+");
        System.out.println("*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+");
        for (int j = 0; j < 10; j++) {
            //We initialize a route with random cities
            int[] route = randomRoute();
            int cost = calculateCost(route, 1);
            int calculations = 1;
            int i = 0;
            while(calculations < NUM_CALCULATIONS && i < route.length - 1){
                int[] routeAux = swap(route, i, i+1);
                i++;
                /*System.out.println("======= RANDOM ROUTE SWAP ========");
                    for (int j = 0; j < routeAux.length; j++) {
                System.out.print(routeAux[j] + "\t");
                    }
                    System.out.println();*/
                int costAux = calculateCost(routeAux, 1);
                calculations++;
                if(costAux<cost){
                    route = routeAux;
                    cost = costAux;
                    i = 0;
                }
            }
            totalCost += cost;
            if(cost < bestCost){
                bestRoute = route.clone();
                bestCost = cost;
            }
            System.out.println("===========================" + "Iteraction " + j + "===========================");
            System.out.println("******** ROUTE *********");
            for (int k = 0; k < route.length; k++) {
                System.out.print(route[k] + "\t");
            }
            System.out.println("\nCoste: " + cost);
            /*System.out.println("======= BEST FIRST ROUTE ========");
            for (int j = 0; j < route.length; j++) {
            System.out.print(route[j] + "\t");
            }
            System.out.println();
            System.out.println("======= BEST FIRST COST " + j + " ========");
            System.out.println(cost);
            System.out.println("======= BEST FIRST CALCULATIONS " + j + " ========");
            System.out.println(calculations);*/
            //System.out.println("(" + j + "," + cost + ")");
        }
        System.out.println("===============================================================");
        System.out.println("===================== Best Solution ===========================");
        System.out.println("===============================================================");
        for (int l = 0; l < bestRoute.length; l++) {
            System.out.print(bestRoute[l] + "\t");
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
     * @param
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
