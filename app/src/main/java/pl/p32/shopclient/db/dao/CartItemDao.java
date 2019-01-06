package pl.p32.shopclient.db.dao;

import java.math.BigDecimal;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import pl.p32.shopclient.model.CartItem;

@Dao
public interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartItem cartItem);

    @Query("SELECT p.id, p.imageUrl, p.name, p.price, c.quantity, p.cardinality, c.id AS orderId " +
            "FROM cart_items c JOIN products p ON c.productId = p.id")
    LiveData<List<OrderedProduct>> loadAll();

    @Query("DELETE FROM cart_items WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM cart_items")
    void deleteAll();

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    CartItem getCartItemByProductId(String productId);

    class OrderedProduct {
        public String id;
        public String imageUrl;
        public String name;
        public BigDecimal price;
        public int quantity;
        public int cardinality;
        public int orderId;
    }
}
