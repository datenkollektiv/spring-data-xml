package de.datenkollektiv.sandbox.data.exist.book;

import javax.xml.bind.annotation.*;

/*
<book category="COOKING" isbn-13="9780062678416">
  <title lang="en">Everyday Italian</title>
  <author>Giada De Laurentiis</author>
  <year>2005</year>
  <price>30.00</price>
</book>
 */
@XmlRootElement(name = "book")
@XmlType(propOrder = {"title", "author", "year", "price"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Book {

    @XmlAttribute(name = "isbn-13")
    private String isbn13;

    @XmlAttribute
    private String category;

    private String title;
    private String author;
    private int year;
    private Double price;

    public Book() {

    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
