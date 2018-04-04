package info.ivicel.github.aidl_simulator;

import android.os.IBinder;
import android.os.IInterface;

import java.util.List;

/**
 * Created by Ivicel on 2018/4/3.
 */
public interface IBookManager extends IInterface {
    List<Book> getBooks();

    // 模拟 aidl 传入 in 标志
    void addBookIn(Book book);
    // 模拟 aidl 传入 out 标志
    void addBookOut(Book book);
    // 模拟 aidl 传入 inout 标志
    void addBookInOut(Book book);


    int TRANSACT_GET_BOOKS = IBinder.FIRST_CALL_TRANSACTION + 1;
    int TRANSACT_ADD_BOOK_IN = IBinder.FIRST_CALL_TRANSACTION + 2;
    int TRANSACT_ADD_BOOK_OUT = IBinder.FIRST_CALL_TRANSACTION + 3;
    int TRANSACT_ADD_BOOK_INOUT= IBinder.FIRST_CALL_TRANSACTION + 4;

}
