import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args0) {

        double[][] distanceMatrix = new double[42][42];

        try {
            int contFila = 0;
            int contColumna = 0;
            String fileName = "assets/dantzig42.tsp";
            java.util.List<String> lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
            for(int l = 7; l<lines.size();l++){
                String[] s = lines.get(l).split("\\s+");
                for(int i = 1; i<s.length;i++){
                    if(s[i].equals("0")){
                        distanceMatrix[contFila][contColumna] = 0;
                        contFila ++;
                        contColumna = 0;
                    }else{
                        distanceMatrix[contFila][contColumna] = Integer.valueOf(s[i]);
                        contColumna ++;
                    }
                }
            }
            for (int i = 0; i < 42; i++) {
                for (int j = i+1; j < 42; j++) {
                    distanceMatrix[i][j] = distanceMatrix[j][i];
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file.");
            e.printStackTrace();
        }
    }
}
