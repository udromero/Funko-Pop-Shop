package com.dannydevs.funkopopshop.ShoppingCartRecyclerView;

public class ShoppingCartModel {
    String itemPictureUrl = "";
    String itemName = "";
    String itemPrice = "";

    public ShoppingCartModel(String itemPictureUrl, String itemName, String itemPrice) {
        this.itemPictureUrl = itemPictureUrl;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemPictureUrl() {
        return itemPictureUrl;
    }

    public void setItemPictureUrl(String itemPictureUrl) {
        this.itemPictureUrl = itemPictureUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}
