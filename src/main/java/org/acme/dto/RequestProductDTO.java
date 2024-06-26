package org.acme.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestProductDTO {

    private String name;

    private String category;

    private int amount;
}