package org.acme.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPayloadDTO {

    private String name;

    private String category;

    private int amount;
}