package uhk.fim.model;

import java.text.spi.BreakIteratorProvider;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<ShoppingCartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<ShoppingCartItem>();
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void addItem(ShoppingCartItem newItem) {
        int a = 5;
        int b = 10;

        boolean isIntList = false;
        for (ShoppingCartItem item : items) {
            if(item.getName().equals(newItem.getName()) && item.getPricePerPiece() == newItem.getPricePerPiece()) {
                isIntList = true;
                item.setPieces(item.getPieces() + newItem.getPieces());
                break;
            }
        }

        if(!isIntList)
            items.add(newItem);
    }

    public double getTotalPrice() {
        double sum = 0;
        for (ShoppingCartItem item : items) {
            sum += item.getTotalPrice();
        }
        return sum;
    }
}
