package uhk.fim.files;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import java.io.*;
import java.util.Iterator;

public class FileWorks {
    public ShoppingCart loadFileCsv(String fileName) {
        String row;
        ShoppingCart shoppingCart = new ShoppingCart();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(";");
                shoppingCart.addItem(new ShoppingCartItem(data[0], Double.parseDouble(data[1]), Integer.parseInt(data[2])));
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shoppingCart;
    }

    public ShoppingCart loadFileXML(String fileName) {
        DocumentFactory df = DocumentFactory.getInstance();
        SAXReader reader = new SAXReader(df);
        ShoppingCart shoppingCart = new ShoppingCart();
        try {
            org.dom4j.Document doc = reader.read(new File(fileName));
            Element rootElement = doc.getRootElement();
            Iterator<Element> items = rootElement.elementIterator();
            while (items.hasNext()) {
                Element item = items.next();
                String name = item.attributeValue("Name");
                double price = Double.parseDouble(item.attributeValue("PricePerPiece"));
                int pieces = Integer.parseInt(item.attributeValue("Pieces"));
                shoppingCart.addItem(new ShoppingCartItem(name, price, pieces));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return shoppingCart;
    }

    public void saveFileCsv(String fileName, ShoppingCart shoppingCart) {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(fileName, false))) {
            for (ShoppingCartItem item : shoppingCart.getItems()) {
                bfw.write(item.getName() + ";" + item.getPricePerPiece() + ";" + item.getPieces());
                bfw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFileXML(String fileName, ShoppingCart shoppingCart) {
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("root");
            for (ShoppingCartItem item : shoppingCart.getItems()) {
                root.addElement("Item")
                        .addAttribute("Name", item.getName())
                        .addAttribute("PricePerPiece", String.valueOf(item.getPricePerPiece()))
                        .addAttribute("Pieces", String.valueOf(item.getPieces()));
            }
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                XMLWriter writer = new XMLWriter(fileWriter);
                writer.write( document );
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
