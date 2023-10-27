package com.example.springapplicationevent.event.listener;

import com.example.springapplicationevent.enums.ElementaryArithmeticType;
import com.example.springapplicationevent.event.type.CalculatorEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculatorEventListener {

    @EventListener
    public Integer elementaryArithmeticHandler(CalculatorEventType calculatorEventType) throws Exception {
        Map.Entry<ElementaryArithmeticType, List<Integer>> value = calculatorEventType.getValue();
        final ElementaryArithmeticType eventType = value.getKey();
        final List<Integer> integers = value.getValue();

        Integer retVal;

        if (eventType == ElementaryArithmeticType.PLUS) {
            retVal = integers.stream().reduce(Integer::sum).orElse(0);
        } else if (eventType == ElementaryArithmeticType.MINUS) {
            retVal = integers.stream().reduce((x, y) -> x - y).orElse(0);
        } else if (eventType == ElementaryArithmeticType.MULTIPLICATION) {
            retVal = integers.stream().reduce((x, y) -> x * y).orElse(0);
        } else {
            try {
                retVal = integers.stream().reduce((x, y) -> x / y).orElse(0);
            } catch (Exception e) {
                throw new Exception(new Throwable("not allowed integer data value for division"));
            }
        }

        return retVal;
    }

}


