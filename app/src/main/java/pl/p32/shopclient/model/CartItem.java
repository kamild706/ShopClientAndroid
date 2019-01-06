package pl.p32.shopclient.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items",
        foreignKeys = {
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId"),
        })
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String productId;

    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
