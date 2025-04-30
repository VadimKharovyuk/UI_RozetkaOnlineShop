package com.example.ui_rozetkaonlineshop.Util;

import com.example.ui_rozetkaonlineshop.DTO.Brand.BrandDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BrandCountryHelper {

    public static class CountryCount {
        private String name;
        private int count;

        public CountryCount(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public int getCount() {
            return count;
        }
    }

    public List<CountryCount> getCountriesFromBrands(List<BrandDto.BrandListDTO> brands) {
        if (brands == null || brands.isEmpty()) {
            return Collections.emptyList();
        }

        // Группировка брендов по странам и подсчет количества
        Map<String, Long> countryCounts = brands.stream()
                .map(brand -> brand.getCountry() != null && !brand.getCountry().isEmpty()
                        ? brand.getCountry()
                        : "Не указано")
                .collect(Collectors.groupingBy(country -> country, Collectors.counting()));

        // Преобразование Map в список объектов CountryCount
        List<CountryCount> result = countryCounts.entrySet().stream()
                .map(entry -> new CountryCount(entry.getKey(), entry.getValue().intValue()))
                .sorted(Comparator.comparing(CountryCount::getName))
                .collect(Collectors.toList());

        return result;
    }
}