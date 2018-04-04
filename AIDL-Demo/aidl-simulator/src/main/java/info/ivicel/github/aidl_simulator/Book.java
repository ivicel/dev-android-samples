package info.ivicel.github.aidl_simulator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ivicel on 2018/4/3.
 */
public class Book implements Parcelable {
    private String name;
    private int price;

    public Book() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    protected Book(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
    }

    public void readFromParcel(Parcel src) {
        name = src.readString();
        price = src.readInt();
    }

    @Override
    public String toString() {
        return "Book{name = " + name + ", price = " + price + "}";
    }
}
