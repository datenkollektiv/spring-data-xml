package de.datenkollektiv.sandbox.data.exist.book;

import de.datenkollektiv.sandbox.data.xml.XMLEntityInformation;

class BookEntityInformation extends XMLEntityInformation<Book> {

    public Class<Book> getJavaType() {
        return Book.class;
    }

    public boolean isNew(Book entity) {
        return false;
    }

    // WARNING: This ID will be used within the URI to identify the XML document inside the database.
    // Only valid URI characters are allowed.
    public String getId(Book entity) {
        return asId(entity);
    }

    public static String asId(Book book) {
        return book.getIsbn13();
    }

}
