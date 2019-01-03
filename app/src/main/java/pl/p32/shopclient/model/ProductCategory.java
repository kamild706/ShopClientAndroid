package pl.p32.shopclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "product_category", primaryKeys = {"productId", "categoryId"},
        foreignKeys = {
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId"),
                @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "categoryId")
        })
public class ProductCategory {

    @Expose
    @SerializedName("productId")
    @NonNull
    private String productId;

    @Expose
    @SerializedName("categoryId")
    @NonNull
    private String categoryId;

    @NonNull
    public String getProductId() {
        return productId;
    }

    public void setProductId(@NonNull String productId) {
        this.productId = productId;
    }

    @NonNull
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "ProductCategoryDao{" +
                "productId='" + productId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
