package info.ivicel.github.aidl_simulator;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.List;


public class Proxy implements IBookManager {
    private IBinder mBinder;

    public Proxy(IBinder binder) {
        mBinder = binder;
    }

    @Override
    public List<Book> getBooks() {
        List<Book> books = null;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(Stub.DESCRIPTION);
            mBinder.transact(TRANSACT_GET_BOOKS, data, reply, 0);
            reply.readException();
            if (0 != reply.readInt()) {
                books = reply.createTypedArrayList(Book.CREATOR);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }



        data.recycle();
        reply.recycle();

        return books;
    }

    @Override
    public void addBookIn(Book book) {
        // 用来保存 client 传向 server 的数据
        Parcel data = Parcel.obtain();
        // 用来保存 server 返回/修改后的数据
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(Stub.DESCRIPTION);
            data.writeInt(1);
            book.writeToParcel(data, 0);
            mBinder.transact(TRANSACT_ADD_BOOK_IN, data, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public void addBookOut(Book book) {
        // 用来保存 client 传向 server 的数据
        Parcel data = Parcel.obtain();
        // 用来保存 server 返回/修改后的数据
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(Stub.DESCRIPTION);
        try {
            mBinder.transact(TRANSACT_ADD_BOOK_OUT, data, reply, 0);
            reply.readException();
            if (0 != reply.readInt()) {
                book.readFromParcel(reply);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public void addBookInOut(Book book) {
        // 用来保存 client 传向 server 的数据
        Parcel data = Parcel.obtain();
        // 用来保存 server 返回/修改后的数据
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(Stub.DESCRIPTION);
            data.writeInt(1);
            book.writeToParcel(data, 0);

            mBinder.transact(IBookManager.TRANSACT_ADD_BOOK_INOUT, data, reply, 0);

            reply.readException();
            if (0 != reply.readInt()) {
                book.readFromParcel(reply);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public IBinder asBinder() {
        return mBinder;
    }


}
