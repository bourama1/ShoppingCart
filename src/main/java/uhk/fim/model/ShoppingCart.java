package uhk.fim.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<ShoppingCartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<ShoppingCartItem>();
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void addItem(ShoppingCartItem newItem) {
        boolean isIntList = false;
        for (ShoppingCartItem item : items) {
            if(item.getName().equals(newItem.getName()) && item.getPricePerPiece() == newItem.getPricePerPiece() && item.isPurchased() == newItem.isPurchased()) {
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

    public double getPurchasedPrice() {
        double sum = 0;
        for (ShoppingCartItem item : items) {
            if (item.isPurchased())
                sum += item.getTotalPrice();
        }
        return sum;
    }

    public double getUnpurchasedPrice() {
        return getTotalPrice() - getPurchasedPrice();
    }

    public void updateCart() {
        boolean isIntList = false;
        for (ShoppingCartItem item : items) {
            for (int i = 0; i < items.size(); i++){
                if (i == items.indexOf(item))
                    continue;
                if (item.getName().equals(items.get(i).getName())
                        && item.getPricePerPiece() == items.get(i).getPricePerPiece()
                        && item.isPurchased() == items.get(i).isPurchased()){
                    item.setPieces(item.getPieces() + items.get(i).getPieces());
                    items.remove(i);
                }
            }
        }
    }
}
