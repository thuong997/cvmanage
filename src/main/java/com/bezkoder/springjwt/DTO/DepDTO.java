package com.bezkoder.springjwt.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class DepDTO implements Serializable {

    private int depId;

    private String depName;
}
