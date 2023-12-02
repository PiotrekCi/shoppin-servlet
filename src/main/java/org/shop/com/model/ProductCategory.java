package org.shop.com.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductCategory {
    EXAMPLE("example");

    private final String value;
}
