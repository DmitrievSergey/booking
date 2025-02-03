package com.example.statisticservice.cvs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CsvGeneratorUtil<T> {
    public String generateCsv(List<T> objects) {
        StringBuilder csvContent = new StringBuilder();
        objects.forEach(t -> csvContent.append(t.toString()));

        return csvContent.toString();
    }
}
