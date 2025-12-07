package com.jamuara.crs.enums;

import lombok.Getter;

@Getter
public enum HotelRateCode {
    RAC("Rack"),

    BAR("Best Available Rate"),

    PRO("Promotional"),

    COR("Corporate"),

    GOV("Government (qualified)"),

    AAA("AAA (qualified)"),

    BNB("Bed and Breakfast"),

    PKG("Package"),

    TVL("Travel Industry"),

    SPC("Special Promo Rate"),

    WKD("Weekend"),

    CON("Convention"),

    SNR("Senior (Europe) (qualified)"),

    ARP("AARP - American Association of Retired People (50+) (qualified)"),

    SRS("Senior (qualified)"),

    ROR("Room Only Rate (no breakfast)"),

    FAM("Family"),

    DAY("Day rate");

    private final String value;

    HotelRateCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name() + ": " + value;
    }
}
