package info.ivicel.github.aidl_simulator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class IRemoteService extends Service {
    private static final String TAG = "IRemoteService";
    private List<Book> books = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("android_book_server_0");
        book.setPrice(0);
        books.add(book);
        book = new Book();
        book.setName("android_book_server_1");
        book.setPrice(1);
        books.add(book);
    }

    private Stub stub = new Stub() {
        @Override
        public List<Book> getBooks() {
            Log.d(TAG, "getBooks: ");
            return books;
        }

        @Override
        public void addBookIn(Book book) {
            Log.d("addBookInOut", "from client: " + book);
            book.setName(book.getName() + "_in_by_server");
            Log.d("addBookInOut", "after server change book's name: " + book);
            books.add(book);
        }

        @Override
        public void addBookOut(Book book) {
            Log.d("addBookInOut", "from client: " + book);
            book.setName(book.getName() + "_out_by_server");
            Log.d("addBookInOut", "after server change book's name: " + book);
            books.add(book);
        }

        @Override
        public void addBookInOut(Book book) {
            Log.d("addBookInOut", "from client: " + book);
            book.setName(book.getName() + "_inout_by_server");
            Log.d("addBookInOut", "after server change book's name: " + book);
            books.add(book);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub.asBinder();
    }
}
