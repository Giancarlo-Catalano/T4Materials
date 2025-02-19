package Ex2;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;

public class Unit2Parser {

    private static DocumentBuilder builder = null;
    private static XPath xPath = null;

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory bldFactory = DocumentBuilderFactory.newInstance();
        builder = bldFactory.newDocumentBuilder();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        xPath = xPathFactory.newXPath();

        Document doc = builder.parse(new File(args[0]));

        question_1(doc);
        question_2(doc);
        question_3(doc);

        question_4(doc);
        question_5(doc);
        question_6(doc);

        question_7(doc);
        question_8(doc);
        question_9(doc);

    }

    public static NodeList getFromXPath(String xpathString, Document doc) throws Exception {
        return (NodeList) xPath.evaluate(xpathString, doc, XPathConstants.NODESET);
    }

    public static String getTextInTagWithin(Node node, String tagName) throws Exception {
        return xPath.evaluate(tagName, node);
    }

    public static void question_1(Document doc) {
        NodeList receiptNodes = doc.getElementsByTagName("receipt");
        System.out.println("Total number of receipts = " + receiptNodes.getLength());
    }


    public static void question_2(Document doc) throws Exception{
        NodeList janeReceipts = getFromXPath("//receipt[cashier='Jane Smith']/total", doc);
        float total = 0;
        for (int i=0;i<janeReceipts.getLength();i++) {
            Node node = janeReceipts.item(i);
            float value = Float.parseFloat(node.getTextContent());
            total += value;
        }

        System.out.println("The total for Jane is "+total);
    }

    public static void question_3(Document doc) throws Exception {
        NodeList paymentTypesNodes = getFromXPath("//receipt/payment/type", doc);

        Set<String> paymentTypes = new HashSet<>();
        for (int i = 0; i < paymentTypesNodes.getLength(); i++) {
            paymentTypes.add(paymentTypesNodes.item(i).getTextContent());
        }
        System.out.println("There are "+paymentTypes.size()+" different payment types");
    }

    public static void question_4(Document doc) throws Exception {
        NodeList items = getFromXPath("//item/name", doc);
        HashMap<String, Integer> frequencyMap = new HashMap<>();

        for (int i=0;i<items.getLength();i++) {
            String itemName = items.item(i).getTextContent();
            Integer prevCount = frequencyMap.getOrDefault(itemName, 0);
            frequencyMap.put(itemName, prevCount + 1);
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

    public static void question_5(Document doc) throws Exception {
        NodeList hardDriveNodes = getFromXPath("//item[name='External Hard Drive']/subtotal", doc);

        float total = 0;
        for (int i=0;i<hardDriveNodes.getLength();i++) {
            Node node = hardDriveNodes.item(i);
            float value = Float.parseFloat(node.getTextContent());
            total += value;
        }

        System.out.println("The total for hard drives is "+total);
    }

    public static void question_6(Document doc) throws Exception {
        NodeList items = getFromXPath("//receipt", doc);
        HashMap<String, Float> totalMap = new HashMap<>();

        for (int i=0;i<items.getLength();i++) {
            Node node = items.item(i);
            String cashierName = getTextInTagWithin(node, "cashier");
            Float itemCost = Float.parseFloat(getTextInTagWithin(node, "total"));
            Float prevTotal = totalMap.getOrDefault(cashierName, (float)0);
            totalMap.put(cashierName, prevTotal + itemCost);
        }

        String mostExpensiveItem = null;
        float maxTotal = 0;
        for (Map.Entry<String, Float> entry : totalMap.entrySet()) {
            if (entry.getValue() > maxTotal) {
                mostExpensiveItem = entry.getKey();
                maxTotal = entry.getValue();
            }
        }
        System.out.println("The most productive cashier is "+mostExpensiveItem+", since it processed "+maxTotal+"£");
    }

    public static void question_7(Document doc) throws Exception{
        NodeList transactions = getFromXPath("//receipt", doc);

        Node mostExpensiveNode = null;
        float mostExpensiveTotal = (float)(-1);
        for (int i = 0; i< transactions.getLength();i++) {
            Node node = transactions.item(i);
            float newCost = Float.parseFloat(getTextInTagWithin(node, "total"));
            if (newCost > mostExpensiveTotal) {
                mostExpensiveNode = node;
                mostExpensiveTotal = newCost;
            }
        }

        String cashier = getTextInTagWithin(mostExpensiveNode, "cashier");
        System.out.println("The most expensive transaction was "+mostExpensiveTotal+"£ by "+cashier);
    }

    public static ArrayList<String> getItemNames(Node transaction) {
        ArrayList<String> itemNames = new ArrayList<>();
        NodeList itemNodes = ((Element) transaction).getElementsByTagName("name");
        for (int i = 0; i < itemNodes.getLength(); i++) {
            itemNames.add(itemNodes.item(i).getTextContent());
        }

        return itemNames;
    }

    public static void question_8(Document doc) throws Exception {
        HashMap<Set<String>, Integer> combinationCounter = new HashMap<>();

        NodeList nodes = getFromXPath("//receipt", doc);

        for (int i=0;i<nodes.getLength();i++) {
            ArrayList<String> itemNames = getItemNames(nodes.item(i));
            if (itemNames.size() < 2) {
                continue;
            }
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


    public static void question_9(Document doc) throws Exception {
        NodeList transactions = getFromXPath("//receipt[count(payment[type != 'Credit Card']) = 0][items/item/name = 'USB Cable']/total", doc);
        float total = 0;

        for (int i=0;i<transactions.getLength();i++) {
            float totalForReceipt = Float.parseFloat(transactions.item(i).getTextContent());
            total += totalForReceipt;
        }

        System.out.println("The total amount spent for credit card transactions containing a USB cable is "+total);
    }


}
