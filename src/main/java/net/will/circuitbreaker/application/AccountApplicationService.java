package net.will.circuitbreaker.application;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import net.will.circuitbreaker.domain.Account;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AccountApplicationService {
    private final AtomicInteger count = new AtomicInteger();

    public Optional<Account> get(String id) {
        if (!StringUtils.hasText(id)) {
            return Optional.empty();
        }

        return Optional.of(Account.getInstance(id, "Alice"));
    }

    public List<Account> list() {
        count.incrementAndGet();
        System.out.println("[" + count.get() + "] Returning a quick list...");

        List<Account> result = new ArrayList<>();
        result.add(Account.getInstance("0", "Bob"));
        result.add(Account.getInstance("1", "Alice"));
        return result;
    }

    @SentinelResource(value = "slowList", fallback = "emptyList")
    public List<Account> slowList() {
        count.incrementAndGet();

        System.out.println("[" + count.get() + "] Doing things slow...");
        doSomethingSlow();

        List<Account> result = new ArrayList<>();
        result.add(Account.getInstance("101", "Chuck"));
        return result;
    }

    private void doSomethingSlow() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @SentinelResource(value = "exceptionList",
            fallback = "emptyList",
            exceptionsToTrace = IllegalStateException.class)
    public List<Account> listWithPossibleException() {
        count.incrementAndGet();
        if (count.get() % 2 == 0) {
            System.out.println("[" + count.get() + "] Throwing an Exception...");
            throw new IllegalStateException("Hit an even number.");
        }

        System.out.println("[" + count.get() + "] Bypassed an Exception and returning a list...");
        List<Account> result = new ArrayList<>();
        result.add(Account.getInstance("102", "Chad"));
        return result;
    }

    public List<Account> emptyList() {
        System.out.println("Returning from fallback function");
        return Collections.emptyList();
    }
}
