package uhk.fim.files;

import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class LoadFile {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
            for (Iterator<Element> it = items; it.hasNext(); ) {
                Element item = it.next();
                String name = item.attributeValue("Name");
                Double price = Double.parseDouble(item.attributeValue("PricePerPiece"));
                int pieces = Integer.parseInt(item.attributeValue("Pieces"));
                shoppingCart.addItem(new ShoppingCartItem(name, price, pieces));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return shoppingCart;
    }
}
