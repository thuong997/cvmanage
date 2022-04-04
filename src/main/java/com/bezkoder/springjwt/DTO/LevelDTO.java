package com.bezkoder.springjwt.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class LevelDTO implements Serializable {
    private int levelId;
    private String levelName;
}
