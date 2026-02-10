package com.chitkara.diya.controller;

import com.chitkara.diya.service.AiService;
import com.chitkara.diya.util.MathUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class BfhlController {

    private static final String EMAIL = "diya0075.be23@chitkara.edu.in";

    private final AiService aiService;

    public BfhlController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "is_success", true,
                "official_email", EMAIL
        ));
    }

    @PostMapping("/bfhl")
    public ResponseEntity<?> bfhl(@RequestBody Map<String, Object> body) {

        if (body.size() != 1) {
            return ResponseEntity.badRequest().body(Map.of(
                    "is_success", false
            ));
        }

        String key = body.keySet().iterator().next();
        Object value = body.get(key);
        Object result;

        try {
            switch (key) {

                case "fibonacci":
                    int n = (int) value;
                    result = MathUtil.fibonacci(n);
                    break;

                case "prime":
                    List<Integer> primes = ((List<?>) value).stream()
                            .map(v -> (int) v)
                            .filter(MathUtil::isPrime)
                            .toList();
                    result = primes;
                    break;

                case "lcm":
                    result = MathUtil.lcmList((List<?>) value);
                    break;

                case "hcf":
                    result = MathUtil.hcfList((List<?>) value);
                    break;

                case "AI":
                    try {
                        result = aiService.ask(value.toString());
                    } catch (Exception e) {
                        return ResponseEntity.status(502).body(Map.of(
                                "is_success", false
                        ));
                    }
                    break;

                default:
                    return ResponseEntity.badRequest().body(Map.of(
                            "is_success", false
                    ));
            }

            return ResponseEntity.ok(Map.of(
                    "is_success", true,
                    "official_email", EMAIL,
                    "data", result
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "is_success", false
            ));
        }
    }
}

