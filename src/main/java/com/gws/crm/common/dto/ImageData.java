package com.gws.crm.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImageData {
    private final String contentType;
    private final byte[] data;

}
