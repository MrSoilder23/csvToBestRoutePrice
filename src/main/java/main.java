import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


import java.io.*;

import java.util.*;

public class main {

    public static Map<String, List<Double>> records = new HashMap<String, List<Double >>();

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
                        new_str = new_str.replace(",",".");
                        new_str = new_str.replace(" ","");
                        try {
                            newNum = Double.parseDouble(new_str);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                if (records.containsKey(list[0])) {

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
        //int[] prices = {150,146,152,157,160,163,155,150,147,151};

        csvToList();

        double[] prices = records.get("Bank Północny").stream().mapToDouble(Double::doubleValue).toArray();

        int i=0;
        double peak=prices[0];
        double valley=prices[0];
        double maxProfit=0;
        while(i<prices.length-1)
        {
            while(i<prices.length-1 && prices[i]>=prices[i+1])
                i++;
            valley=prices[i];
            int sturn = i+1;
            System.out.println(valley + " Buy on turn: " + sturn);
            while(i<prices.length-1 && prices[i+1]>=prices[i])
                i++;//post fix use
            peak=prices[i];
            int bturn = i+1;
            System.out.println(peak + " Sell on turn: " + bturn);
            maxProfit+=peak-valley;
        }

        System.out.println(maxProfit);
    }

}