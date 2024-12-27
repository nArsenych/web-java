package com.example.spacecatsmarket.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    ASTRO_MOUSE_TOYS("Astro Mouse Toys"),
    MOONLIGHT_SCRATCHERS("Moonlight Scratchers"),
    COMET_COLLAR_BELLS("Comet Collar Bells"),
    QUASAR_CAT_BEDS("Quasar Cat Beds");

    private final String displayName;
}
