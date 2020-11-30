package uhk.fim.model;

public class ShoppingCartItem {
    private String name;
    private double pricePerPiece;
    private int pieces;
    private boolean purchased;

    public ShoppingCartItem(String name, double pricePerPiece, int pieces, boolean purchased) {
        this.name = name;
        this.pricePerPiece = pricePerPiece;
        this.pieces = pieces;
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public double getTotalPrice() {
        return pricePerPiece * pieces;
    }
}
