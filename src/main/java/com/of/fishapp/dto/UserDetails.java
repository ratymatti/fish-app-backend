package com.of.fishapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDetails {

    private final String googleId;
    private final String name;
    private final String email;

}
