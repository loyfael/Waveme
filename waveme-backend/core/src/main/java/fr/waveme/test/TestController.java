package fr.waveme.test;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/ping")
    public String ping() {
        System.out.println("✅ /test/ping hit");
        return "pong";
    }
}
