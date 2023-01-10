import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;

import java.util.*;
import java.util.stream.Collectors;


public class CreatePathForMultipleStocks {

    public static Map<String, List<Double>> records = new HashMap<>();

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

    public static double maxProfit(double[][] prices, int k) {
        int n = prices[0].length;
        int m = prices.length;
        double[][] dp = new double[k+1][n];
        int[][] buy = new int[k+1][n];
        int[][] sell = new int[k+1][n];
        for (int i = 1; i <= k; i++) {
            double maxDiff = -prices[0][0];
            for (int j = 1; j < n; j++) {
                for (int p = 0; p < m; p++) {
                    maxDiff = Math.max(maxDiff, dp[i-1][j] - prices[p][j]);
                    if (prices[p][j] + maxDiff > dp[i][j-1]) {
                        dp[i][j] = prices[p][j] + maxDiff;
                        buy[i][j] = p;
                        sell[i][j] = j;
                    } else {
                        dp[i][j] = dp[i][j-1];
                        buy[i][j] = buy[i][j-1];
                        sell[i][j] = sell[i][j-1];
                    }
                }
            }
        }
        int i = k;
        int j = n-1;
        while (i > 0 && j > 0) {
            if (buy[i][j] != buy[i][j-1]) {
                System.out.println("Buy Stock " + (buy[i][j] + 1) + " at day " + sell[i][j]);
                i--;
            }
            j--;
        }
        j = n-1;
        while (i > 0 && j > 0) {
            if (sell[i][j] != sell[i][j-1]) {
                System.out.println("Sell Stock " + (buy[i][j] + 1) + " at day " + sell[i][j]);
                i--;
            }
            j--;
        }
        return dp[k][n-1];
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

        List<List<Double>> lists = new ArrayList<>(collection);

        System.out.println(lists);

        double[][] arr = lists.stream()
                .map(l -> l.stream().mapToDouble(Double::doubleValue).toArray())
                .toArray(double[][]::new);

        System.out.println(Arrays.deepToString(arr));

        System.out.println(records);

        double[][] answer = {{1.5,1.1,1.2,5.5,1.6,4.1}, {1.2,2.5,3.2,6.4,6.5,7.1}};

        System.out.println(maxProfit(answer, 2));
    }

}