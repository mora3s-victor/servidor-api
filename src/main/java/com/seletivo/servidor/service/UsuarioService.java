package com.seletivo.servidor.service;

import com.seletivo.servidor.dto.CriarUsuarioDTO;
import com.seletivo.servidor.dto.UsuarioDTO;
import com.seletivo.servidor.exception.BusinessException;
import com.seletivo.servidor.exception.ResourceNotFoundException;
import com.seletivo.servidor.model.Usuario;
import com.seletivo.servidor.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDTO criar(CriarUsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole(dto.getRole());
        usuario.setAtivo(true);
        usuario.setUltimoAcesso(LocalDateTime.now());

        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void alterarStatus(Long id, boolean ativo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        usuario.setAtivo(ativo);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(Long id, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        dto.setAtivo(usuario.isAtivo());
        return dto;
    }
} 
