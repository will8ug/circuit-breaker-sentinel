package net.will.circuitbreaker;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CircuitBreakerApplication {

    public static void main(String[] args) {
        initDegradeRule();

        SpringApplication.run(CircuitBreakerApplication.class, args);
    }

    private static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        rules.add(initRuleForSlowList());
        rules.add(initRuleForExceptionList());

        DegradeRuleManager.loadRules(rules);
        System.out.println("Degrade rule loaded: " + rules);
    }

    private static DegradeRule initRuleForSlowList() {
        return new DegradeRule("slowList")
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                // Max allowed response time
                .setCount(50)
                // Retry timeout (in second)
                .setTimeWindow(5)
                .setSlowRatioThreshold(0.5)
                .setMinRequestAmount(2)
                .setStatIntervalMs(3000);
    }

    private static DegradeRule initRuleForExceptionList() {
        return new DegradeRule("exceptionList")
                .setGrade(CircuitBreakerStrategy.ERROR_RATIO.getType())
                .setCount(0.5)
                .setTimeWindow(5)
                .setMinRequestAmount(2)
                .setStatIntervalMs(3000);
    }
}
