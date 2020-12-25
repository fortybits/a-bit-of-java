package edu.bit.concurrency;

import java.time.LocalDate;

public class News {

    private final String headline;
    private final LocalDate date;

    private News(String headline, LocalDate date) {
        this.headline = headline;
        this.date = date;
    }

    public static News create(String headline) {
        return new News(headline, LocalDate.now());
    }

    public String getHeadline() {
        return headline;
    }

    public LocalDate getDate() {
        return date;
    }

}