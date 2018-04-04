package info.ivicel.github.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AIDLService extends Service {
    private static final String TAG = "AIDLService";

    private List<Book> books = new ArrayList<>();

    // 由 AIDL 文件生成的 BookManager 接口的实现
    // 一般会有多个 client 连接到 server, 所以需要对 server 中的数据处理同步问题
    private final BookManager.Stub bookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                if (books != null) {
                    return books;
                }
            }
            return new ArrayList<>();
        }

        @Override
        public Book getBook() throws RemoteException {
            synchronized (this) {
                if (books == null) {
                    return null;
                }

                Random r = new Random(System.currentTimeMillis());
                int n = r.nextInt(books.size());
                return books.get(n);
            }
        }

        @Override
        public int getBookCount() throws RemoteException {
            synchronized (this) {
                if (books != null) {
                    return books.size();
                }
            }

            return 0;
        }

        @Override
        public Book addBookIn(Book book) throws RemoteException {
            Log.d("OnServer", "addBookIn: " + book);
            book.setName(book.getName() + "_by_server");
            book.setPrice(book.getPrice() + 10);
            books.add(book);
            return book;
        }

        @Override
        public Book addBookOut(Book book) throws RemoteException {
            Log.d("OnServer", "addBookOut: " + book);
            book.setName(book.getName() + "_by_server");
            book.setPrice(book.getPrice() + 10);
            books.add(book);
            return book;
        }

        @Override
        public Book addBookInout(Book book) throws RemoteException {
            Log.d("OnServer", "addBookInOut: " + book);
            book.setName(book.getName() + "_by_server");
            book.setPrice(book.getPrice() + 10);
            books.add(book);
            return book;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        books.add(book);

        book = new Book();
        book.setName("Android编程权威指南");
        book.setPrice(55);
        books.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(getClass().getSimpleName(), String.format("on bind, intent = %s", intent.toString()));
        return bookManager;
    }
}
