package com.seletivo.servidor.service;

import com.seletivo.servidor.dto.LotacaoDTO;
import com.seletivo.servidor.dto.LotacaoUpdateDTO;
import com.seletivo.servidor.exception.ResourceNotFoundException;
import com.seletivo.servidor.model.Lotacao;
import com.seletivo.servidor.model.Pessoa;
import com.seletivo.servidor.model.Unidade;
import com.seletivo.servidor.repository.LotacaoRepository;
import com.seletivo.servidor.repository.PessoaRepository;
import com.seletivo.servidor.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LotacaoService {

	@Autowired
    private LotacaoRepository lotacaoRepository;
    
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
    private UnidadeRepository unidadeRepository;

    @Transactional(readOnly = true)
    public Page<LotacaoDTO> findAll(Pageable pageable) {
        return lotacaoRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Transactional
    public LotacaoDTO create(LotacaoDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));

        Unidade unidade = unidadeRepository.findById(dto.getUnidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));

        Lotacao lotacao = new Lotacao();
        lotacao.setPessoa(pessoa);
        lotacao.setUnidade(unidade);
        lotacao.setDataLotacao(dto.getDataLotacao());
        lotacao.setDataRemocao(dto.getDataRemocao());
        lotacao.setPortaria(dto.getPortaria());

        return toDTO(lotacaoRepository.save(lotacao));
    }

    @Transactional
    public LotacaoDTO update(Long id, LotacaoUpdateDTO dto) {
        Lotacao lotacao = lotacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lotação não encontrada"));

        if (dto.getPessoaId() != null) {
            Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));
            lotacao.setPessoa(pessoa);
        }

        if (dto.getUnidadeId() != null) {
            Unidade unidade = unidadeRepository.findById(dto.getUnidadeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
            lotacao.setUnidade(unidade);
        }

        lotacao.setDataLotacao(dto.getDataLotacao());
        lotacao.setDataRemocao(dto.getDataRemocao());
        lotacao.setPortaria(dto.getPortaria());

        return toDTO(lotacaoRepository.save(lotacao));
    }

    @Transactional
    public void delete(Long id) {
        Lotacao lotacao = lotacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lotação não encontrada"));
        lotacaoRepository.delete(lotacao);
    }

    private LotacaoDTO toDTO(Lotacao lotacao) {
        LotacaoDTO dto = new LotacaoDTO();
        dto.setId(lotacao.getId());
        dto.setPessoaId(lotacao.getPessoa().getId());
        dto.setPessoaNome(lotacao.getPessoa().getNome());
        dto.setUnidadeId(lotacao.getUnidade().getId());
        dto.setUnidadeNome(lotacao.getUnidade().getNome());
        dto.setDataLotacao(lotacao.getDataLotacao());
        dto.setDataRemocao(lotacao.getDataRemocao());
        dto.setPortaria(lotacao.getPortaria());
        return dto;
    }
} 
