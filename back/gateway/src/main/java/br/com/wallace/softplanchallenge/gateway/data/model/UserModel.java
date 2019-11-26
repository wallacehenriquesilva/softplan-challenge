package br.com.wallace.softplanchallenge.gateway.data.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
