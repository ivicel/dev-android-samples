package info.ivicel.github.ipcwithmessenger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ivicel on 2018/4/5.
 */
public class MyData implements Parcelable {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MyData(String data) {
        this.data = data;
    }

    protected MyData(Parcel in) {
        data = in.readString();
    }

    public static final Creator<MyData> CREATOR = new Creator<MyData>() {
        @Override
        public MyData createFromParcel(Parcel in) {
            return new MyData(in);
        }

        @Override
        public MyData[] newArray(int size) {
            return new MyData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
    }
}
