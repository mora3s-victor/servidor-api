package com.seletivo.servidor.service;

import com.seletivo.servidor.dto.UnidadeDTO;
import com.seletivo.servidor.dto.EnderecoDTO;
import com.seletivo.servidor.dto.CidadeDTO;
import com.seletivo.servidor.exception.ResourceNotFoundException;
import com.seletivo.servidor.model.*;
import com.seletivo.servidor.repository.UnidadeRepository;
import com.seletivo.servidor.repository.UnidadeEnderecoRepository;
import com.seletivo.servidor.repository.CidadeRepository;
import com.seletivo.servidor.util.EnderecoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final UnidadeEnderecoRepository unidadeEnderecoRepository;
    private final EnderecoUtil enderecoUtil;
    private final CidadeRepository cidadeRepository;

    @Transactional(readOnly = true)
    public Page<UnidadeDTO> findAll(Pageable pageable) {
        log.debug("Buscando todas as unidades com paginação: {}", pageable);
        return unidadeRepository.findAllWithEnderecos(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public UnidadeDTO findById(Long id) {
        log.debug("Buscando unidade por ID: {}", id);
        return unidadeRepository.findByIdWithEnderecos(id)
                .map(this::toDTO)
                .orElseThrow(() -> {
                    log.error("Unidade não encontrada com ID: {}", id);
                    return new ResourceNotFoundException("Unidade não encontrada");
                });
    }

    @Transactional
    public UnidadeDTO create(UnidadeDTO dto) {
        log.debug("Criando nova unidade: {}", dto);
        
        // Criar e salvar a unidade
        Unidade unidade = new Unidade();
        unidade.setNome(dto.getNome());
        unidade.setSigla(dto.getSigla());
        unidade.setEnderecosUnidade(new ArrayList<>());
        unidade = unidadeRepository.save(unidade);

        if (dto.getEndereco() != null) {
            // Buscar ou criar cidade
            CidadeDTO cidadeDTO = dto.getEndereco().getCidade();
            Cidade cidade = cidadeRepository.findByNomeAndUf(
                cidadeDTO.getNome(),
                cidadeDTO.getUf()
            ).orElseGet(() -> {
                Cidade novaCidade = new Cidade();
                novaCidade.setNome(cidadeDTO.getNome());
                novaCidade.setUf(cidadeDTO.getUf());
                return cidadeRepository.save(novaCidade);
            });

            Endereco endereco = enderecoUtil.criarOuBuscarEndereco(
                dto.getEndereco().getTipoLogradouro(),
                dto.getEndereco().getLogradouro(),
                dto.getEndereco().getNumero(),
                dto.getEndereco().getBairro(),
                cidade.getNome(),
                cidade.getUf()
            );

            UnidadeEndereco unidadeEndereco = new UnidadeEndereco();
            unidadeEndereco.setUnidade(unidade);
            unidadeEndereco.setEndereco(endereco);
            unidadeEndereco = unidadeEnderecoRepository.save(unidadeEndereco);

            unidade.getEnderecosUnidade().add(unidadeEndereco);
            unidade = unidadeRepository.save(unidade);
        }

        return toDTO(unidade);
    }

    @Transactional
    public UnidadeDTO update(Long id, UnidadeDTO dto) {
        log.debug("Atualizando unidade com ID: {}. Dados: {}", id, dto);
        
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Unidade não encontrada para atualização. ID: {}", id);
                    return new ResourceNotFoundException("Unidade não encontrada");
                });

        unidade.setNome(dto.getNome());
        unidade.setSigla(dto.getSigla());

        if (dto.getEndereco() != null) {
            // Buscar ou criar cidade
            CidadeDTO cidadeDTO = dto.getEndereco().getCidade();
            Cidade cidade = cidadeRepository.findByNomeAndUf(
                cidadeDTO.getNome(),
                cidadeDTO.getUf()
            ).orElseGet(() -> {
                Cidade novaCidade = new Cidade();
                novaCidade.setNome(cidadeDTO.getNome());
                novaCidade.setUf(cidadeDTO.getUf());
                return cidadeRepository.save(novaCidade);
            });

            Endereco endereco = enderecoUtil.criarOuBuscarEndereco(
                dto.getEndereco().getTipoLogradouro(),
                dto.getEndereco().getLogradouro(),
                dto.getEndereco().getNumero(),
                dto.getEndereco().getBairro(),
                cidade.getNome(),
                cidade.getUf()
            );

            UnidadeEndereco unidadeEndereco;

            if (!unidade.getEnderecosUnidade().isEmpty()) {
                // Atualizar endereço existente
                unidadeEndereco = unidade.getEnderecosUnidade().get(0);
                unidadeEndereco.setEndereco(endereco);
                unidadeEndereco = unidadeEnderecoRepository.save(unidadeEndereco);
            } else {
                // Criar novo relacionamento
                unidadeEndereco = new UnidadeEndereco();
                unidadeEndereco.setUnidade(unidade);
                unidadeEndereco.setEndereco(endereco);
                unidadeEndereco = unidadeEnderecoRepository.save(unidadeEndereco);
                
                unidade.getEnderecosUnidade().add(unidadeEndereco);
            }
        }

        unidade = unidadeRepository.save(unidade);
        return toDTO(unidade);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deletando unidade com ID: {}", id);
        if (!unidadeRepository.existsById(id)) {
            log.error("Unidade não encontrada para deleção. ID: {}", id);
            throw new ResourceNotFoundException("Unidade não encontrada");
        }
        unidadeRepository.deleteById(id);
        log.info("Unidade deletada com sucesso. ID: {}", id);
    }

    private UnidadeDTO toDTO(Unidade unidade) {
        UnidadeDTO dto = new UnidadeDTO();
        dto.setId(unidade.getId());
        dto.setNome(unidade.getNome());
        dto.setSigla(unidade.getSigla());

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

        return dto;
    }
} 
