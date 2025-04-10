package com.seletivo.servidor.dto;

import com.seletivo.servidor.model.Role;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class UsuarioDTO {
    private Long id;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    
    @NotNull(message = "Perfil é obrigatório")
    private Role role;
    
    private boolean ativo;
} 
