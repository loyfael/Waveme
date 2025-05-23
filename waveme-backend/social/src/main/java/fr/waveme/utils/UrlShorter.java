package fr.waveme.utils;

import java.util.UUID;

public class UrlShorter {
    public String generateShortUrl(String longUrl) {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
