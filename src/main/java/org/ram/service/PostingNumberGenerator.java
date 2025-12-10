package org.ram.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class PostingNumberGenerator {

    private final AtomicInteger counter = new AtomicInteger();

    public String generate() {
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        int num = counter.getAndIncrement();
        return "P" + today + "-" + String.format("%05d", num);
    }
}
