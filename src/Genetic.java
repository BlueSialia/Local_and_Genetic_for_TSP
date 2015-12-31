import java.util.Arrays;
import java.util.Random;

/**
 * Created by bluesialia on 29/12/15.
 * Basic genetic algorithm for TSP.
 */
public class Genetic {

    // Matrix containing the costs of the TSP
    private double[][] _COSTS_;

    // Towns in the TSP
    private final int _TOWNS_;

    // Number of tours
    private final int _POPULATION_;

    // Current population of solutions
    private int[][] _tours_;
    private double[] _toursCost_;

    // Best tour of the previous population
    private int[] _fittest_;
    private double _fittestCost_;

    // Chances of mutating
    private final double _MUTATE_;

    // Max generations
    private final int _DEFAULTMAXGENERATIONS_ = Integer.MAX_VALUE;
    private final int _MAXGENERATIONS_;

    // Random generator
    private final Random _RANDOM_ = new Random();

    public Genetic(double[][] tsp) {
        _COSTS_ = tsp;
        _TOWNS_ = _COSTS_.length;
        _POPULATION_ = _TOWNS_*100;
        _tours_ = new int[_POPULATION_][_TOWNS_];
        _toursCost_ = new double[_POPULATION_];
        _MUTATE_ = 0.5;
        _MAXGENERATIONS_ = _DEFAULTMAXGENERATIONS_;
    }

    public Genetic(double[][] tsp, int population, double mutate) {
        _COSTS_ = tsp;
        _TOWNS_ = _COSTS_.length;
        _POPULATION_ = population;
        _tours_ = new int[_POPULATION_][_TOWNS_];
        _toursCost_ = new double[_POPULATION_];
        _MUTATE_ = mutate;
        _MAXGENERATIONS_ = _DEFAULTMAXGENERATIONS_;
    }

    public Genetic(double[][] tsp, int population, double mutate, int maxGenerations) {
        _COSTS_ = tsp;
        _TOWNS_ = _COSTS_.length;
        _POPULATION_ = population;
        _tours_ = new int[_POPULATION_][_TOWNS_];
        _toursCost_ = new double[_POPULATION_];
        _MUTATE_ = mutate;
        _MAXGENERATIONS_ = maxGenerations;
    }

    public int[] solve() {
        generateInitialTours();
        int numGenerations = 1;
        do {
            generateNewTours();
            numGenerations++;
        } while (_toursCost_[0] < _fittestCost_ && numGenerations < _MAXGENERATIONS_);
        System.out.println(Arrays.toString(_fittest_));
        System.out.println(_fittestCost_);
        return _fittest_;
    }

    public int[] solve(int rep) {
        int[] newSolution,
                solution = solve();
        for (int i = 1; i < rep; i++) {
            newSolution = solve();
            if (tourCost(solution) > tourCost(newSolution)) {
                solution = newSolution;
            }
        }
        return solution;
    }

    private void generateInitialTours() {
        int[] basicTour = new int[_TOWNS_];
        for (int i = 0; i < _TOWNS_; i++) {
            basicTour[i] = i;
        }
        for (int i = 0; i < _POPULATION_; i++) {
            _tours_[i] = shuffleArray(basicTour).clone();
        }
        for (int i = 0; i < _POPULATION_; i++) {
            _toursCost_[i] = tourCost(_tours_[i]);
        }
        orderTours();
        _fittest_ = _tours_[0];
        _fittestCost_ = _toursCost_[0];
    }

    private void generateNewTours() {
        int[][] newTours = new int[_POPULATION_][_TOWNS_];
        double[] newToursCost = new double[_POPULATION_];
        for (int i = 0; i < _POPULATION_/2; i++) {
            int[] firstParent = getOneRandomTour(),
                    secondParent = getOneRandomTour();
            newTours[i*2] = mutateArray(descendant(firstParent, secondParent));
            newTours[i*2 + 1] = mutateArray(descendant(secondParent, firstParent));
            newToursCost[i*2] = tourCost(newTours[i*2]);
            newToursCost[i*2 + 1] = tourCost(newTours[i*2 + 1]);
        }
        _fittest_ = (_toursCost_[0] < _fittestCost_)? _tours_[0] : _fittest_;
        _fittestCost_ = (_toursCost_[0] < _fittestCost_)? _toursCost_[0] : _fittestCost_;
        _tours_ = newTours;
        _toursCost_ = newToursCost;
        orderTours();
    }

    private int[] getOneRandomTour() {
        double random = _RANDOM_.nextDouble()*_toursCost_[_POPULATION_-1];
        for (int i = 0; i < _POPULATION_; i++) {
            if (_toursCost_[i] > random) return _tours_[i];
        }
        return _tours_[0];
    }

    private int[] descendant(int[] firstParent, int[] secondParent) {
        int[] son = new int[_TOWNS_];
        Arrays.fill(son, -1);
        int subSize = _RANDOM_.nextInt(_TOWNS_) + 1,
                startSubSize = (_TOWNS_-subSize == 0)? 0:_RANDOM_.nextInt(_TOWNS_-subSize);
        Integer[] partOfFirstParent = new Integer[subSize];
        for (int i = 0; i < subSize; i++) {
            partOfFirstParent[i] = firstParent[startSubSize + i];
        }
        System.arraycopy(firstParent, startSubSize, son, startSubSize, subSize);
        int indexSecondParent = 0;
        for (int i = 0; i < _TOWNS_; i++) {
            if (son[i]==-1) {
                while (Arrays.asList(partOfFirstParent).contains(secondParent[indexSecondParent])) indexSecondParent++;
                son[i] = secondParent[indexSecondParent];
                indexSecondParent++;
            }
        }
        return son;
    }

    private double tourCost(int[] tour) {
        double length = _COSTS_[tour[_TOWNS_ - 1]][tour[0]];
        for (int i = 0; i < _TOWNS_ - 1; i++) {
            length += _COSTS_[tour[i]][tour[i + 1]];
        }
        return length;
    }

    private int[] shuffleArray(int[] array) {
        int index;
        for (int i = array.length - 1; i > 0; i--)
        {
            index = _RANDOM_.nextInt(i + 1);
            if (index != i)
            {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
        return array;
    }

    private int[] mutateArray(int[] array) {
        double chances = _MUTATE_/array.length;
        int index;
        for (int i = array.length - 1; i > 0; i--)
        {
            index = _RANDOM_.nextInt(i + 1);
            if (index != i && _RANDOM_.nextDouble() < chances)
            {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
        return array;
    }

    private void orderTours() {
        for (int i = 0; i < _POPULATION_ - 1; i++) {
            for (int j = i + 1; j < _POPULATION_; j++) {
                if (_toursCost_[j] < _toursCost_[i]) {
                    double temp1 = _toursCost_[j];
                    int[] temp2 = _tours_[j];
                    _toursCost_[j] = _toursCost_[i];
                    _tours_[j] = _tours_[i];
                    _toursCost_[i] = temp1;
                    _tours_[i] = temp2;
                }
            }
        }
    }
}
