/**
 * Created by bluesialia on 30/12/15.
 * Bitonic Tour Implementation
 */
public class BitonicTour {

    private int[][] points = {{170, 85}, {166, 88}, {133, 73}, {140, 70}, {142, 55}, {126, 53}, {125, 60}, {119, 68}, {117, 74}, {99, 83}, {73, 79}, {72, 91}, {37, 94}, {6, 106}, {3, 97}, {21, 82}, {33, 67}, {4, 66}, {3, 42}, {27, 33}, {52, 41}, {57, 59}, {58, 66}, {88, 65}, {99, 67}, {95, 55}, {89, 55}, {83, 38}, {85, 25}, {104, 35}, {112, 37}, {112, 24}, {113, 13}, {125, 30}, {135, 32}, {147, 18}, {147, 36}, {154, 45}, {157, 54}, {158, 61}, {172, 82}, {174, 87}};

    public void solve() {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[j][0] < points[i][0]) {
                    int[] temp = points[j];
                    points[j] = points[i];
                    points[i] = temp;
                }
            }
        }
        double[] distanciaHasta = new double[points.length];

        distanciaHasta[1] = 2*straigthLine(points[0], points[1]);
        for (int i = 2; i < points.length; i++) {
            distanciaHasta[i] = Integer.MAX_VALUE;
            double distanciaHastaPuntoIDesdeJ = 0;
            for (int j = i-2; j > -1; j--) {
                distanciaHastaPuntoIDesdeJ += straigthLine(points[j+1], points[j+2]);
                double recorridoBitonico = distanciaHastaPuntoIDesdeJ + straigthLine(points[j], points[i]) + distanciaHasta[j+1] - straigthLine(points[j], points[j+1]);
                if (recorridoBitonico < distanciaHasta[i]) {
                    distanciaHasta[i] = recorridoBitonico;
                }
            }
        }
        System.out.printf("%.2f\n", distanciaHasta[distanciaHasta.length-1]);
    }

    private double straigthLine(int[] point1, int[] point2) {
        return Math.sqrt(Math.pow(point1[0]-point2[0], 2)+Math.pow(point1[1]-point2[1], 2));
    }
}
