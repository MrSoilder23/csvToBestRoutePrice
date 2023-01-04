import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


import java.io.*;

import java.util.*;

public class main {

    public static Map<String, List<String >> records = new HashMap<String, List<String >>();

    public static void csvToList() {
        try (CSVReader csvReader = new CSVReader(new FileReader("D:\\csvToBestRoutePrice\\src\\dane.csv"));) {
            String[] list = null;
            while ((list = csvReader.readNext()) != null) {
                String msg[] = list[1].split(" ");
                String new_str = "";

                for (String words : msg) {
                    if (!words.equals("zł")) {
                        new_str += words + " ";
                        new_str = new_str.replace(",",".");
                        new_str = new_str.replace(" ","");
                        try {
                            double d = Double.parseDouble(new_str);
                        } catch (NumberFormatException e) {
                            System.out.println(e);
                        }

                    }
                }
                if (records.containsKey(list[0])) {

                    records.get(list[0]).add(new_str);
                } else {
                    List<String > value = new ArrayList<>();

                    value.add(new_str);
                    records.put(list[0], value);
                    }
                }

        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }


        public static void main(String[] args) {
        int[] prices = {150,146,152,157,160,163,155,150,147,151};

        csvToList();

        Object[] bankPolnocny = records.get("Bank Północny").toArray();

        //Object[] bankPolnocny = records.get("Bank Północny").toArray();
        System.out.println(records);

        int i=0;
        int peak=prices[0];
        int valley=prices[0];
        int maxProfit=0;
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