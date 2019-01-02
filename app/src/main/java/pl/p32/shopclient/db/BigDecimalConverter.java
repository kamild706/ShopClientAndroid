package pl.p32.shopclient.db;

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.room.TypeConverter;

public class BigDecimalConverter {

    @TypeConverter
    public static Integer fromBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal.multiply(BigDecimal.valueOf(100)).intValue();
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Integer integer) {
        return BigDecimal.valueOf(integer).divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY);
    }
}
