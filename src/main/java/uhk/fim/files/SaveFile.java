package uhk.fim.files;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFile {
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
