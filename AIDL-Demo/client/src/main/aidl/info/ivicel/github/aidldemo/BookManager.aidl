package info.ivicel.github.aidldemo;

// 非基本数据类型需要导入
import info.ivicel.github.aidldemo.Book;

interface BookManager {
    List<Book> getBooks();
    Book getBook();
    int getBookCount();

    // 测试不同的 tag 标志的影响
    Book addBookIn(in Book book);
    Book addBookOut(out Book book);
    Book addBookInout(inout Book book);
}
