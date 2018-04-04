package info.ivicel.github.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import info.ivicel.github.aidldemo.Book;
import info.ivicel.github.aidldemo.BookManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private BookManager bookManager;
    private boolean bound = false;

    private TextView logTextView;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            bookManager = BookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            bookManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logTextView = findViewById(R.id.log_text);
        if (!bound) {
            attempToBindService();
        }

        findViewById(R.id.get_books_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBooks();
            }
        });

        findViewById(R.id.add_book_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookIn();
            }
        });

        findViewById(R.id.add_book_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookOut();
            }
        });

        findViewById(R.id.add_book_inout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookInout();
            }
        });
    }

    private void addBookIn() {
        if (checkServerService()) {
            return;
        }

        Book book = new Book();
        book.setName("new_book_in");
        book.setPrice(20);
        try {
            Log.d("OnClient", "before addBookIn client book = " + book);
            Book b2 = bookManager.addBookIn(book);
            Log.d("OnClient", "addBookIn return from server: " + b2);
            Log.d("OnClient", "after addBookIn client book = " + book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void addBookOut() {
        if (checkServerService()) {
            return;
        }

        Book book = new Book();
        book.setName("new_book_out");
        book.setPrice(21);
        try {
            Log.d("OnClient", "before addBookOut client book = " + book);
            Book b2 = bookManager.addBookOut(book);
            Log.d("OnClient", "addBookOut return from server: " + b2);
            Log.d("OnClient", "after addBookOut client book = " + book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void addBookInout() {
        if (checkServerService()) {
            return;
        }

        Book book = new Book();
        book.setName("new_book_inout");
        book.setPrice(22);
        try {
            Log.d("OnClient", "before addBookInOut client book = " + book);
            Book b2 = bookManager.addBookInout(book);
            Log.d("OnClient", "addBookInOut return from server: " + b2);
            Log.d("OnClient", "after addBookInOut client book = " + book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void attempToBindService() {
        Intent intent = new Intent();
        // service 定义的 action
        intent.setAction("info.ivicel.github.aidl");
        // server 端的包名
        intent.setPackage("info.ivicel.github.aidldemo");
        bound = bindService(intent, connection, BIND_AUTO_CREATE);
        Log.d(TAG, "attempToBindService: " + bound);
    }

    private boolean checkServerService() {
        if (!bound) {
            attempToBindService();
        }

        return bookManager == null;
    }

    private void getBooks() {
        if (checkServerService()) {
            return;
        }
        try {
            List<Book> books = bookManager.getBooks();
            StringBuilder sb = new StringBuilder();
            for (Book book : books) {
                Log.d(TAG, "getBooks: " + book);
                sb.append(book);
                sb.append("\n");
            }
            logTextView.setText(sb.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bound) {
            unbindService(connection);
        }
    }
}
