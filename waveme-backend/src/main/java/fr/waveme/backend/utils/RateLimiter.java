package fr.waveme.backend.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
  private static final Map<String, Instant> rateLimiterMap = new ConcurrentHashMap<>();
  private static final long RATE_LIMIT_SECONDS = 5;

  public static void checkRateLimit(String key) {
    Instant now = Instant.now();
    Instant last = rateLimiterMap.getOrDefault(key, Instant.MIN);

    if (now.minusSeconds(RATE_LIMIT_SECONDS).isBefore(last)) {
      throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded");
    }

    rateLimiterMap.put(key, now);
  }
}
