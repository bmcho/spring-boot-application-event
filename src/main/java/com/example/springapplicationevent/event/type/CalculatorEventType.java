package com.example.springapplicationevent.event.type;

import com.example.springapplicationevent.enums.ElementaryArithmeticType;
import lombok.Getter;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class CalculatorEventType {

    @Getter
    private final Map.Entry<ElementaryArithmeticType, List<Integer>> value;

    public CalculatorEventType(AbstractMap.SimpleEntry value) {
        this.value = value;
    }
}
