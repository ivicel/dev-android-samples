package info.ivicel.github.aidldemo;


import android.os.Parcel;
import android.os.Parcelable;

// 如果 java 的实现类是放在和 aidl 同一个包内
// 一定不能忘了把 java 源码的路径加入到 build.gradle 中, 否则会找不到文件

public class Book implements Parcelable {
    private String name;
    private int price;

    public Book(){}

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
        dest.writeInt(price);
        dest.writeString(name);
    }

    // 默认的 Parcelable 是没有规定要实现 readFromParcel 方法
    // 但如果不实现这个方法, Book 的 tag 只能为 in

    public void readFromParcel(Parcel dest) {
        price = dest.readInt();
        name = dest.readString();
    }

    // 方便打印数据格式

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
