package info.ivicel.github.aidl_simulator;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;


public abstract class Stub extends Binder implements IBookManager {
    private static final String TAG = "Stub";
    public static final String DESCRIPTION = "info.github.manual_aidl.IBookManager";

    // get client proxy
    public static IBookManager asInterface(IBinder binder) {
        if (binder == null) {
            return null;
        }
        return new Proxy(binder);
    }

    public Stub() {
        this.attachInterface(this, DESCRIPTION);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        Log.d(TAG, "onTransact: " + code);
        switch (code) {
            case IBookManager.TRANSACT_GET_BOOKS: {
                data.enforceInterface(Stub.DESCRIPTION);
                List<Book> books = getBooks();
                if (reply != null) {
                    reply.writeNoException();
                    if (books != null) {
                        reply.writeInt(1);
                        reply.writeTypedList(books);
                    } else {
                        reply.writeInt(0);
                    }
                }
                break;
            }

            case IBookManager.TRANSACT_ADD_BOOK_IN: {
                Book book;
                data.enforceInterface(DESCRIPTION);
                if (0 != data.readInt()) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                addBookIn(book);
                break;
            }

            case IBookManager.TRANSACT_ADD_BOOK_OUT: {
                data.enforceInterface(Stub.DESCRIPTION);
                Book book = new Book();
                addBookOut(book);
                if (reply != null) {
                    reply.writeNoException();
                    reply.writeInt(1);
                    book.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                }
                break;
            }

            case IBookManager.TRANSACT_ADD_BOOK_INOUT: {
                Book book;
                data.enforceInterface(Stub.DESCRIPTION);
                if (0 != data.readInt()) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                addBookInOut(book);
                if (reply != null) {
                    reply.writeNoException();
                    if (book != null) {
                        reply.writeInt(1);
                        book.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                }
                break;
            }

            default:
                return super.onTransact(code, data, reply, flags);
        }

        return true;
    }

    @Nullable
    @Override
    public String getInterfaceDescriptor() {
        return DESCRIPTION;
    }
}
