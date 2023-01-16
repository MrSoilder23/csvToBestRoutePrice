import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;

import java.util.*;
import java.util.stream.Collectors;


public class CreatePathForMultipleStocks {

    public static Map<String, List<Double>> records = new HashMap<>();
    public static double[][] peaks;

    public static void csvToList() {
        try (CSVReader csvReader = new CSVReader(new FileReader("D:\\csvToBestRoutePrice\\src\\dane.csv"))) {
            String[] list;
            while ((list = csvReader.readNext()) != null) {
                double newNum = 0;
                String[] msg = list[1].split(" ");
                String new_str = "";

                for (String words : msg) {
                    if (!words.equals("zł")) {
                        new_str += words + " ";
                        new_str = new_str.replace(",", ".");
                        new_str = new_str.replace(" ", "");
                        try {
                            newNum = Double.parseDouble(new_str);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                if (records.containsKey(list[0])) {
                    if (!new_str.isEmpty() || !new_str.isBlank())
                        records.get(list[0]).add(newNum);
                } else {
                    List<Double> value = new ArrayList<>();

                    value.add(newNum);
                    records.put(list[0], value);
                }
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void peak(double[][] p) {
        peaks = new double[p.length][p[0].length];
        for (int i = 0; i < p.length; i++) {
            for (int j = 1; j < p[0].length-1; j++) {
                if (p[i][j] > p[i][j+1] && p[i][j] > p[i][j-1]) {
                    peaks[i][j] = p[i][j];
                    //System.out.println(p[i][j]);

                } else if (p[i][j+1] > p[i][j]) {

                    if (j+2 != p[0].length) {
                        if (p[i][j + 2] < p[i][j + 1])
                            peaks[i][j + 1] = p[i][j + 1];
                    } else {
                        peaks[i][j + 1] = p[i][j + 1];
                    }

                    //System.out.println(p[i][j+1]);
                }
            }
            //System.out.println("NEW STOCK");
        }
        System.out.println(Arrays.deepToString(peaks));
    }

    public static int[][] turns;
    public static double[][] sellTime;

    public static void closest(double[][] peaks) {

        turns = new int[peaks.length][peaks[0].length];
        sellTime = new double[peaks.length][peaks[0].length];
        for (int i = 0; i < peaks.length; i++) {
            int a = 0;
            for (int j = 0; j < peaks[0].length; j++) {
                if (peaks[i][j] != 0.0) {
                    //System.out.println(peaks[i][j]);
                    //System.out.println(j);

                    turns[i][a] = j;
                    sellTime[i][a] = peaks[i][j];
                    a++;
                }
            }
            //System.out.println("NEW STOCK");
        }
        //System.out.println(Arrays.deepToString(sellTime));
    }

    public static double[][] profitMap;

    public static void ProfitMap(double[][] prices) {
        closest(peaks);

        int next = 0;

        profitMap = new double[prices.length][prices[0].length];
        for (int i = 0; i < prices.length; i++) {
            double sell = sellTime[i][next];
            int turn = turns[i][next];
            for (int j = 0; j < prices[0].length; j++) {
                profitMap[i][j] = sell - prices[i][j];

                if (j == turn) {
                    next++;
                    sell = sellTime[i][next];
                    turn = turns[i][next];
                }
            }
            next = 0;
        }
        System.out.println(Arrays.deepToString(profitMap));
    }

    public static void findPath(double[][] map) {

        double profit = 0;
        int turn;
        for (int i = 0; i < map[0].length; i++) {
            double maxValue = Integer.MIN_VALUE;
            int maxIndex = 0;
            for (int j = 0; j < map.length; j++) {
                if (map[j][i] > maxValue) {
                    maxValue = map[j][i];
                    maxIndex = j;
                }

            }
            profit += maxValue;
            turn = i+1;
            System.out.println("Stock: " + maxIndex + " On Turn: " + turn);
        }
        System.out.println(profit);
    }

    public static void run(double[][] stock) {
        peak(stock);
        ProfitMap(stock);
        findPath(profitMap);
    }

    public static void main(String[] args) {

        csvToList();

        records.remove("");
        records.remove("name");
        records.remove("Obligacje dwuletnie(OK1222)");
        records.remove("Obligacje dwuletnie(OK1223)");
        records.remove("Obligacje dwuletnie(OK1224)");
        records.remove("Obligacje dwuletnie(OK1225)");
        records.remove("Obligacje dwuletnie(OK1226)");


        Collection<List<Double>> collection = records.values();
        Collection<String> names = records.keySet();

        List<List<Double>> lists = new ArrayList<>(collection);

        //System.out.println(lists);

        double[][] arr = lists.stream()
                .map(l -> l.stream().mapToDouble(Double::doubleValue).toArray())
                .toArray(double[][]::new);

        //System.out.println(Arrays.deepToString(arr));

        double[][] a = {{1, 4, 1, 2, 3, 3, 7}, {2, 2, 2, 1, 7, 4, 5}};

        System.out.println(records);
        int nameNum = 0;
        for (String k : names) {
            System.out.println("Name: " + k + " Num: " + nameNum);

            nameNum++;
        }

        //profit(a);

        run(arr);

        //System.out.println(maxProfit(arr, 1));
    }

}