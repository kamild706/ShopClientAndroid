package pl.p32.shopclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "rates")
public class Rates {

    @PrimaryKey
    private int id = 1;

    @SerializedName("USD")
    @Expose
    private double usd;

    @SerializedName("GBP")
    @Expose
    private double gbp;

    @SerializedName("EUR")
    @Expose
    private double eur;

    public int getId() {
        return id;
    }

    public void setId(int id) {
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getGbp() {
        return gbp;
    }

    public void setGbp(double gbp) {
        this.gbp = gbp;
    }

    public double getEur() {
        return eur;
    }

    public void setEur(double eur) {
        this.eur = eur;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "id=" + id +
                ", usd=" + usd +
                ", gbp=" + gbp +
                ", eur=" + eur +
                '}';
    }
}