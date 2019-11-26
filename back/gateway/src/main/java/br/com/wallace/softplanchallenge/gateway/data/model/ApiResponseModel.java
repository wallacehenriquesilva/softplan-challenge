package br.com.wallace.softplanchallenge.gateway.data.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long timestamp;
    private String status;
    private String message;
}
