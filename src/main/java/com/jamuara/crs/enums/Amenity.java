package com.jamuara.crs.enums;

import lombok.Getter;

@Getter
public enum Amenity {
    SWIMMING_POOL("SWIMMING_POOL"),
    SPA("SPA"),
    FITNESS_CENTER("FITNESS_CENTER"),
    AIR_CONDITIONING("AIR_CONDITIONING"),
    RESTAURANT("RESTAURANT"),
    PARKING("PARKING"),
    PETS_ALLOWED("PETS_ALLOWED"),
    AIRPORT_SHUTTLE("AIRPORT_SHUTTLE"),
    BUSINESS_CENTER("BUSINESS_CENTER"),
    DISABLED_FACILITIES("DISABLED_FACILITIES"),
    WIFI("WIFI"),
    MEETING_ROOMS("MEETING_ROOMS"),
    NO_KID_ALLOWED("NO_KID_ALLOWED"),
    TENNIS("TENNIS"),
    GOLF("GOLF"),
    KITCHEN("KITCHEN"),
    ANIMAL_WATCHING("ANIMAL_WATCHING"),
    BABY_SITTING("BABY-SITTING"),
    BEACH("BEACH"),
    CASINO("CASINO"),
    JACUZZI("JACUZZI"),
    SAUNA("SAUNA"),
    SOLARIUM("SOLARIUM"),
    MASSAGE("MASSAGE"),
    VALET_PARKING("VALET_PARKING"),
    BAR_OR_LOUNGE("BAR or LOUNGE"),
    KIDS_WELCOME("KIDS_WELCOME"),
    NO_PORN_FILMS("NO_PORN_FILMS"),
    MINIBAR("MINIBAR"),
    TELEVISION("TELEVISION"),
    WIFI_IN_ROOM("WI-FI_IN_ROOM"),
    ROOM_SERVICE("ROOM_SERVICE"),
    GUARDED_PARKING("GUARDED_PARKING"),
    SERV_SPEC_MENU("SERV_SPEC_MENU");

    private final String value;

    // Constructor
    Amenity(String value) {
        this.value = value;
    }

    // Getter method
    public String getValue() {
        return value;
    }

    public static Amenity fromKey(String key) {
        for(Amenity amenity: Amenity.values()) {
            if(amenity.name().equals(key)) {
                return amenity;
            }
        }
        throw new IllegalArgumentException("Unknown amenity enum key: " + key);
    }

    public static Amenity fromValue(String value) {
        for(Amenity amenity: Amenity.values()) {
            if(amenity.getValue().equals(value)) {
                return amenity;
            }
        }
        throw new IllegalArgumentException("Unknown amenity enum: " + value);
    }

    @Override
    public String toString() {
        return name() + ": " + value;
    }
}
