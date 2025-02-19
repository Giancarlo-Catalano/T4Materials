package Ex3;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.*;

public class Unit3Parser {

    public class Item {
        String name;
        double price;
        int quantity;
        double subtotal;
    }

    class Payment {
        String type;
        double amount;
    }

    public class Receipt {
        String id;
        String cashier;
        String timestamp;
        Item[] items;
        double total;
        Payment[] payments;



        public ArrayList<String> getItemNames() {
            ArrayList<String> itemNames = new ArrayList<>();
            for (Item item : this.items) {
                itemNames.add(item.name);
            }
            return itemNames;
        }

        public HashSet<String> getPaymentTypes() {
            HashSet<String> paymentTypes = new HashSet<>();
            for (Payment payment : this.payments) {
                paymentTypes.add(payment.type);
            }

            return paymentTypes;
        }

    }

    public class Sales {
        String date;
        Receipt[] receipts;
    }

    public class Contents {
        Sales sales;
    }


    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        Contents contents = gson.fromJson(new FileReader(args[0]), Contents.class);

        Sales sales = contents.sales;
        question_1(sales);
        question_2(sales);
        question_3(sales);
        question_4(sales);
        question_5(sales);
        question_6(sales);
        question_7(sales);
        question_8(sales);
        question_9(sales);


    }

    public static void question_1(Sales sales){
        System.out.println("The quantity of receipts is "+ sales.receipts.length);
    }

    public static void question_2(Sales sales){

        double total = 0;

        for (Receipt receipt : sales.receipts) {
            if (receipt.cashier.equals("Jane Smith")) {
                total += receipt.total;
            }
        }
        System.out.println("The total of Jane smith is "+total);
    }

    public static void question_3(Sales sales){

        Set<String> paymentTypes = new HashSet<>();
        for (Receipt receipt : sales.receipts) {
            for (Payment payment : receipt.payments) {
                paymentTypes.add(payment.type);
            }
        }
        System.out.println("There are "+paymentTypes.size()+" different payment types");
    }

    public static void question_4(Sales sales){
        HashMap<String, Integer> frequencyMap = new HashMap<>();

        for (Receipt receipt : sales.receipts) {
            for (Item item : receipt.items) {
                String itemName = item.name;
                Integer prevCount = frequencyMap.getOrDefault(itemName, 0);
                frequencyMap.put(itemName, prevCount + 1);
            }

        }

        String mostCommonItem = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonItem = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        System.out.println("The most common item is "+mostCommonItem+", since it appears "+maxCount+" times");
    }

    public static void question_5(Sales sales){

        double total = 0;
        for (Receipt receipt : sales.receipts) {
            boolean contains_hard_drive = false;
            for (Item item : receipt.items) {
                if (item.name.equals("External Hard Drive")) {
                    contains_hard_drive = true;
                    break;
                }
            }
            if (contains_hard_drive) {
                total += receipt.total;
            }

        }

        System.out.println("The total for hard drives is "+total);
    }

    public static void question_6(Sales sales){

        HashMap<String, Double> totalMap = new HashMap<>();

        for (Receipt receipt : sales.receipts) {
            String cashierName = receipt.cashier;
            double itemCost = receipt.total;
            Double prevTotal = totalMap.getOrDefault(cashierName, (double)0);
            totalMap.put(cashierName, prevTotal + itemCost);
        }

        String mostExpensiveItem = null;
        Double maxTotal = (double)0;
        for (Map.Entry<String, Double> entry : totalMap.entrySet()) {
            if (entry.getValue() > maxTotal) {
                mostExpensiveItem = entry.getKey();
                maxTotal = entry.getValue();
            }
        }
        System.out.println("The most productive cashier is "+mostExpensiveItem+", since it processed "+maxTotal+"£");
    }

    public static void question_7(Sales sales){

        Receipt mostExpensiveReceipt = null;
        double mostExpensiveTotal = (double)(-1);
        for (Receipt receipt : sales.receipts) {
            double newCost = receipt.total;
            if (newCost > mostExpensiveTotal) {
                mostExpensiveReceipt = receipt;
                mostExpensiveTotal = newCost;
            }
        }

        System.out.println("The most expensive transaction was "+mostExpensiveTotal+"£ by "+mostExpensiveReceipt.cashier);
    }

    public static void question_8(Sales sales){

        HashMap<Set<String>, Integer> combinationCounter = new HashMap<>();

        for (Receipt receipt : sales.receipts) {

            if (receipt.items.length < 2)
                continue;
            ArrayList<String> itemNames = receipt.getItemNames();
            for (int itemIndexA = 0; itemIndexA < itemNames.size() - 1; itemIndexA++) {
                for (int itemIndexB = itemIndexA + 1; itemIndexB < itemNames.size(); itemIndexB++) {
                    Set<String> pair = new HashSet<>();
                    pair.add(itemNames.get(itemIndexA));
                    pair.add(itemNames.get(itemIndexB));
                    int prevCount = combinationCounter.getOrDefault(pair, 0);
                    combinationCounter.put(pair, prevCount + 1);
                }
            }

        }

        Set<String> winningPair = null;
        int winningFreq = 0;

        for (Map.Entry<Set<String>, Integer> entry : combinationCounter.entrySet()) {
            if (entry.getValue() > winningFreq) {
                winningPair = entry.getKey();
                winningFreq = entry.getValue();
            }
        }

        System.out.println("The most common combination is "+winningPair.toArray()[0] + " & "+winningPair.toArray()[1] + ", which appears "+winningFreq + " times");

    }

    public static void question_9(Sales sales){

        double total = 0;

        HashSet<String> expectedPaymentTypes = new HashSet<>(Arrays.asList("Credit Card"));
        for (Receipt receipt: sales.receipts) {
            if (!expectedPaymentTypes.equals(receipt.getPaymentTypes()))
                continue;
            if (!receipt.getItemNames().contains("USB Cable"))
                continue;

            total += receipt.total;
        }

        System.out.println("The total amount spent for credit card transactions containing a USB cable is "+total);
    }


}
