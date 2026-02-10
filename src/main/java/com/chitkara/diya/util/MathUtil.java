package com.chitkara.diya.util;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {

    public static List<Integer> fibonacci(int n) {
        List<Integer> list = new ArrayList<>();
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            list.add(a);
            int temp = a + b;
            a = b;
            b = temp;
        }
        return list;
    }

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    public static int lcmList(List<?> list) {
        return list.stream()
                .map(v -> (int) v)
                .reduce(1, MathUtil::lcm);
    }

    public static int hcfList(List<?> list) {
        return list.stream()
                .map(v -> (int) v)
                .reduce(MathUtil::gcd)
                .orElse(0);
    }
}

