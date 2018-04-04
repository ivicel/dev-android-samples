package info.ivicel.github.aidl_simulator;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private int num = 0;

    private IBookManager manager;
    private TextView textView;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            manager = Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            manager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.add_book_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBookIn();
            }
        });

        findViewById(R.id.add_book_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBookOut();
            }
        });

        findViewById(R.id.add_book_inout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBookInOut();
            }
        });

        findViewById(R.id.get_books).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAllBook();
            }
        });

        textView = findViewById(R.id.text_view);

        Intent intent = new Intent(this, IRemoteService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void addNewBookIn() {
        Book book = new Book();
        book.setName("book name-" + num);
        book.setPrice(num);
        manager.addBookInOut(book);
        num++;
    }

    private void addNewBookOut() {
        Book book = new Book();
        book.setName("book name-" + num);
        book.setPrice(num);
        manager.addBookOut(book);
        num++;
    }

    private void addNewBookInOut() {
        Book book = new Book();
        book.setName("book name-" + num);
        book.setPrice(num);
        manager.addBookInOut(book);
        num++;
    }

    private void listAllBook() {
        List<Book> books = manager.getBooks();
        if (books != null) {
            StringBuilder sb = new StringBuilder();
            for (Book book : books) {
                sb.append(book);
                sb.append("\n");
                Log.d(TAG, "listAllBook: " + book);
            }
            textView.setText(sb.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
