package com.seletivo.servidor.service;

import com.seletivo.servidor.dto.UnidadeEnderecoDTO;
import com.seletivo.servidor.dto.EnderecoDTO;
import com.seletivo.servidor.dto.CidadeDTO;
import com.seletivo.servidor.exception.ResourceNotFoundException;
import com.seletivo.servidor.model.*;
import com.seletivo.servidor.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnidadeEnderecoService {

    private final ServidorEfetivoRepository servidorEfetivoRepository;
    private final ServidorTemporarioRepository servidorTemporarioRepository;

    @Transactional(readOnly = true)
    public List<UnidadeEnderecoDTO> findByServidorNome(String nome) {
        log.debug("Buscando endereço da unidade pelo nome do servidor: {}", nome);
        
        // Buscar servidores efetivos
        List<ServidorEfetivo> servidoresEfetivos = servidorEfetivoRepository.findByPessoaNomeContainingIgnoreCase(nome, Pageable.unpaged()).getContent();
        
        // Buscar servidores temporários
        List<ServidorTemporario> servidoresTemporarios = servidorTemporarioRepository.findByPessoaNomeContainingIgnoreCase(nome, Pageable.unpaged()).getContent();
        
        // Converter servidores efetivos para DTO
        List<UnidadeEnderecoDTO> result = servidoresEfetivos.stream()
                .filter(servidor -> servidor.getPessoa().getLotacoes() != null && !servidor.getPessoa().getLotacoes().isEmpty())
                .map(this::toUnidadeEnderecoDTO)
                .collect(Collectors.toList());
        
        // Adicionar servidores temporários ao resultado
        result.addAll(servidoresTemporarios.stream()
                .filter(servidor -> servidor.getPessoa().getLotacoes() != null && !servidor.getPessoa().getLotacoes().isEmpty())
                .map(this::toUnidadeEnderecoDTO)
                .collect(Collectors.toList()));
        
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum servidor encontrado com o nome: " + nome);
        }
        
        return result;
    }
    
    private UnidadeEnderecoDTO toUnidadeEnderecoDTO(ServidorEfetivo servidor) {
        UnidadeEnderecoDTO dto = new UnidadeEnderecoDTO();
        
        // Obter a lotação atual
        Lotacao lotacaoAtual = servidor.getPessoa().getLotacoes().stream()
                .filter(l -> l.getDataRemocao() == null)
                .findFirst()
                .orElse(null);
        
        if (lotacaoAtual != null && lotacaoAtual.getUnidade() != null) {
            Unidade unidade = lotacaoAtual.getUnidade();
            dto.setUnidadeId(unidade.getId());
            dto.setUnidadeNome(unidade.getNome());
            dto.setUnidadeSigla(unidade.getSigla());
            
            // Obter o endereço da unidade
            if (unidade.getEnderecosUnidade() != null && !unidade.getEnderecosUnidade().isEmpty()) {
                UnidadeEndereco unidadeEndereco = unidade.getEnderecosUnidade().get(0);
                if (unidadeEndereco != null && unidadeEndereco.getEndereco() != null) {
                    Endereco endereco = unidadeEndereco.getEndereco();
                    
                    EnderecoDTO enderecoDTO = new EnderecoDTO();
                    enderecoDTO.setId(endereco.getId());
                    enderecoDTO.setTipoLogradouro(endereco.getTipoLogradouro());
                    enderecoDTO.setLogradouro(endereco.getLogradouro());
                    enderecoDTO.setNumero(endereco.getNumero());
                    enderecoDTO.setBairro(endereco.getBairro());
                    
                    if (endereco.getCidade() != null) {
                        CidadeDTO cidadeDTO = new CidadeDTO();
                        cidadeDTO.setId(endereco.getCidade().getId());
                        cidadeDTO.setNome(endereco.getCidade().getNome());
                        cidadeDTO.setUf(endereco.getCidade().getUf());
                        enderecoDTO.setCidade(cidadeDTO);
                    }
                    
                    dto.setEndereco(enderecoDTO);
                }
            }
        }
        
        return dto;
    }
    
    private UnidadeEnderecoDTO toUnidadeEnderecoDTO(ServidorTemporario servidor) {
        UnidadeEnderecoDTO dto = new UnidadeEnderecoDTO();
        
        // Obter a lotação atual
        Lotacao lotacaoAtual = servidor.getPessoa().getLotacoes().stream()
                .filter(l -> l.getDataRemocao() == null)
                .findFirst()
                .orElse(null);
        
        if (lotacaoAtual != null && lotacaoAtual.getUnidade() != null) {
            Unidade unidade = lotacaoAtual.getUnidade();
            dto.setUnidadeId(unidade.getId());
            dto.setUnidadeNome(unidade.getNome());
            dto.setUnidadeSigla(unidade.getSigla());
            
            // Obter o endereço da unidade
            if (unidade.getEnderecosUnidade() != null && !unidade.getEnderecosUnidade().isEmpty()) {
                UnidadeEndereco unidadeEndereco = unidade.getEnderecosUnidade().get(0);
                if (unidadeEndereco != null && unidadeEndereco.getEndereco() != null) {
                    Endereco endereco = unidadeEndereco.getEndereco();
                    
                    EnderecoDTO enderecoDTO = new EnderecoDTO();
                    enderecoDTO.setId(endereco.getId());
                    enderecoDTO.setTipoLogradouro(endereco.getTipoLogradouro());
                    enderecoDTO.setLogradouro(endereco.getLogradouro());
                    enderecoDTO.setNumero(endereco.getNumero());
                    enderecoDTO.setBairro(endereco.getBairro());
                    
                    if (endereco.getCidade() != null) {
                        CidadeDTO cidadeDTO = new CidadeDTO();
                        cidadeDTO.setId(endereco.getCidade().getId());
                        cidadeDTO.setNome(endereco.getCidade().getNome());
                        cidadeDTO.setUf(endereco.getCidade().getUf());
                        enderecoDTO.setCidade(cidadeDTO);
                    }
                    
                    dto.setEndereco(enderecoDTO);
                }
            }
        }
        
        return dto;
    }
} 
