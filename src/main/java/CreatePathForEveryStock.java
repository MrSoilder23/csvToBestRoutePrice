import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


import java.io.*;

import java.util.*;

public class CreatePathForEveryStock {

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


    public static void main(String[] args) {
        //double[] prices = {150,146,152,157,160,163,155,150,147,151}; //Test Value

        csvToList();

        Collection<List<Double>> DoubleCollection = records.values();
        Collection<String> StringCollection = records.keySet();

       Iterator<List<Double>> DoubleIterator = DoubleCollection.iterator();
       Iterator<String> StringIterator = StringCollection.iterator();

       while (DoubleIterator.hasNext()) {
           List<Double> doubleList = DoubleIterator.next();
           double[] prices = doubleList.stream().mapToDouble(d -> d).toArray();
           //System.out.println(Arrays.toString(prices));
           System.out.println(StringIterator.next());

           int i = 0;
           double peak = prices[0];
           double valley = prices[0];
           double maxProfit = 0;
           double profit = 0;
           while (i < prices.length - 1) {
               while (i < prices.length - 1 && prices[i] >= prices[i + 1])
                   i++;
               valley = prices[i];
               int sturn = i + 1;
               System.out.println(valley + " Buy on turn: " + sturn);
               while (i < prices.length - 1 && prices[i + 1] >= prices[i])
                   i++;//post fix use
               peak = prices[i];
               int bturn = i + 1;
               maxProfit += peak - valley;
               profit = peak - valley;
               System.out.println(peak + " Sell on turn: " + bturn + " Profit: " + profit);
           }
           System.out.println(maxProfit);
       }
    }

}