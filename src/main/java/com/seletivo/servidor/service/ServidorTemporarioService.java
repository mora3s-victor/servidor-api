package com.seletivo.servidor.service;

import com.seletivo.servidor.dto.ServidorTemporarioDTO;
import com.seletivo.servidor.dto.ServidorTemporarioUnidadeDTO;
import com.seletivo.servidor.dto.EnderecoDTO;
import com.seletivo.servidor.dto.CidadeDTO;
import com.seletivo.servidor.dto.FotoPessoaDTO;
import com.seletivo.servidor.exception.ResourceNotFoundException;
import com.seletivo.servidor.model.*;
import com.seletivo.servidor.repository.*;
import com.seletivo.servidor.util.EnderecoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServidorTemporarioService {

    private final ServidorTemporarioRepository servidorTemporarioRepository;
    private final UnidadeRepository unidadeRepository;
    private final MinioService minioService;
    private final PessoaEnderecoRepository pessoaEnderecoRepository;
    private final EnderecoUtil enderecoUtil;
    private final LotacaoRepository lotacaoRepository;
    private final CidadeRepository cidadeRepository;
    private final PessoaRepository pessoaRepository;

    @Transactional(readOnly = true)
    public Page<ServidorTemporarioDTO> findAll(Pageable pageable) {
        return servidorTemporarioRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public ServidorTemporarioDTO findById(Long id) {
        return servidorTemporarioRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor temporário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<ServidorTemporarioUnidadeDTO> findByUnidade(Long unidadeId, Pageable pageable) {
        return servidorTemporarioRepository.findByUnidadeId(unidadeId, pageable)
                .map(this::toUnidadeDTO);
    }

    @Transactional(readOnly = true)
    public Page<ServidorTemporarioDTO> findByNome(String nome, Pageable pageable) {
        return servidorTemporarioRepository.findByPessoaNomeContainingIgnoreCase(nome, pageable)
                .map(this::toDTO);
    }

    @Transactional
    public ServidorTemporarioDTO create(ServidorTemporarioDTO dto) {
        // 1. Criar e salvar pessoa
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dto.getNome());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setSexo(dto.getSexo());
        pessoa.setMae(dto.getMae());
        pessoa.setPai(dto.getPai());
        pessoa.setLotacoes(new ArrayList<>());
        pessoa.setEnderecos(new ArrayList<>());
        pessoa.setFotos(new ArrayList<>());
        
        // Salvar pessoa primeiro
        pessoa = pessoaRepository.save(pessoa);

        // 2. Processar e salvar endereços
        for (EnderecoDTO enderecoDTO : dto.getEnderecos()) {
            // 2.1 Buscar ou criar cidade
            Cidade cidade = cidadeRepository.findByNomeAndUf(
                enderecoDTO.getCidade().getNome(),
                enderecoDTO.getCidade().getUf()
            ).orElseGet(() -> {
                Cidade novaCidade = new Cidade();
                novaCidade.setNome(enderecoDTO.getCidade().getNome());
                novaCidade.setUf(enderecoDTO.getCidade().getUf());
                return cidadeRepository.save(novaCidade);
            });

            // 2.2 Criar ou buscar endereço
            Endereco endereco = enderecoUtil.criarOuBuscarEndereco(
                enderecoDTO.getTipoLogradouro(),
                enderecoDTO.getLogradouro(),
                enderecoDTO.getNumero(),
                enderecoDTO.getBairro(),
                cidade.getNome(),
                cidade.getUf()
            );

            // 2.3 Criar e salvar relação pessoa-endereço
            PessoaEndereco pessoaEndereco = new PessoaEndereco();
            pessoaEndereco.setPessoa(pessoa);
            pessoaEndereco.setEndereco(endereco);
            pessoa.getEnderecos().add(pessoaEndereco);
            pessoaEnderecoRepository.save(pessoaEndereco);
        }

        // 3. Criar e salvar servidor temporário
        ServidorTemporario servidor = new ServidorTemporario();
        servidor.setPessoa(pessoa);
        pessoa.setServidorTemporario(servidor);
        servidor.setDataAdmissao(dto.getDataAdmissao());
        servidor.setDataDemissao(dto.getDataDemissao());
        servidor = servidorTemporarioRepository.save(servidor);

        // 4. Criar e salvar lotação
        if (dto.getUnidadeId() != null) {
            Unidade unidade = unidadeRepository.findById(dto.getUnidadeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));

            Lotacao lotacao = new Lotacao();
            lotacao.setPessoa(pessoa);
            lotacao.setUnidade(unidade);
            lotacao.setDataLotacao(dto.getDataAdmissao());
            lotacao.setPortaria(dto.getPortariaLotacao());

            lotacaoRepository.save(lotacao);
            pessoa.getLotacoes().add(lotacao);
        }

        return toDTO(servidor);
    }

    @Transactional
    public ServidorTemporarioDTO update(Long id, ServidorTemporarioDTO dto) {
        ServidorTemporario servidor = servidorTemporarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor temporário não encontrado"));

        Pessoa pessoa = servidor.getPessoa();
        pessoa.setNome(dto.getNome());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setSexo(dto.getSexo());
        pessoa.setMae(dto.getMae());
        pessoa.setPai(dto.getPai());

        // Atualizar endereços
        // Primeiro remove os endereços que não estão mais presentes
        List<Long> novosEnderecoIds = dto.getEnderecos().stream()
            .map(EnderecoDTO::getId)
            .filter(endId -> endId != null)
            .collect(Collectors.toList());
        
        pessoa.getEnderecos().removeIf(pe -> 
            pe.getEndereco().getId() != null && !novosEnderecoIds.contains(pe.getEndereco().getId()));

        // Atualiza ou adiciona novos endereços
        for (EnderecoDTO enderecoDTO : dto.getEnderecos()) {
            if (enderecoDTO.getId() != null) {
                // Atualiza endereço existente
                pessoa.getEnderecos().stream()
                    .filter(pe -> pe.getEndereco().getId().equals(enderecoDTO.getId()))
                    .findFirst()
                    .ifPresent(pe -> {
                        Cidade cidade = cidadeRepository.findByNomeAndUf(
                            enderecoDTO.getCidade().getNome(),
                            enderecoDTO.getCidade().getUf()
                        ).orElseGet(() -> {
                            Cidade novaCidade = new Cidade();
                            novaCidade.setNome(enderecoDTO.getCidade().getNome());
                            novaCidade.setUf(enderecoDTO.getCidade().getUf());
                            return cidadeRepository.save(novaCidade);
                        });
                        
                        Endereco endereco = enderecoUtil.criarOuBuscarEndereco(
                            enderecoDTO.getTipoLogradouro(),
                            enderecoDTO.getLogradouro(),
                            enderecoDTO.getNumero(),
                            enderecoDTO.getBairro(),
                            cidade.getNome(),
                            cidade.getUf()
                        );
                        pe.setEndereco(endereco);
                    });
            } else {
                // Adiciona novo endereço
                Cidade cidade = cidadeRepository.findByNomeAndUf(
                    enderecoDTO.getCidade().getNome(),
                    enderecoDTO.getCidade().getUf()
                ).orElseGet(() -> {
                    Cidade novaCidade = new Cidade();
                    novaCidade.setNome(enderecoDTO.getCidade().getNome());
                    novaCidade.setUf(enderecoDTO.getCidade().getUf());
                    return cidadeRepository.save(novaCidade);
                });
                
                Endereco endereco = enderecoUtil.criarOuBuscarEndereco(
                    enderecoDTO.getTipoLogradouro(),
                    enderecoDTO.getLogradouro(),
                    enderecoDTO.getNumero(),
                    enderecoDTO.getBairro(),
                    cidade.getNome(),
                    cidade.getUf()
                );

                PessoaEndereco pessoaEndereco = new PessoaEndereco();
                pessoaEndereco.setPessoa(pessoa);
                pessoaEndereco.setEndereco(endereco);
                pessoa.getEnderecos().add(pessoaEndereco);
                pessoaEnderecoRepository.save(pessoaEndereco);
            }
        }

        servidor.setDataAdmissao(dto.getDataAdmissao());
        servidor.setDataDemissao(dto.getDataDemissao());

        // Atualiza lotação se necessário
        if (dto.getUnidadeId() != null) {
            Lotacao lotacaoAtual = pessoa.getLotacoes().stream()
                .filter(l -> l.getDataRemocao() == null)
                .findFirst()
                .orElse(null);

            if (lotacaoAtual == null || !lotacaoAtual.getUnidade().getId().equals(dto.getUnidadeId())) {
                // Finaliza lotação atual se existir
                if (lotacaoAtual != null) {
                    lotacaoAtual.setDataRemocao(LocalDate.now());
                }

                // Cria nova lotação
                Unidade unidade = unidadeRepository.findById(dto.getUnidadeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));

                Lotacao novaLotacao = new Lotacao();
                novaLotacao.setPessoa(pessoa);
                novaLotacao.setUnidade(unidade);
                novaLotacao.setDataLotacao(LocalDate.now());
                novaLotacao.setPortaria(dto.getPortariaLotacao());
                lotacaoRepository.save(novaLotacao);
                pessoa.getLotacoes().add(novaLotacao);
            }
        }

        return toDTO(servidorTemporarioRepository.save(servidor));
    }

    @Transactional
    public void delete(Long id) {
        ServidorTemporario servidor = servidorTemporarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor temporário não encontrado"));
        
        // 1. Primeiro remove todas as lotações da pessoa
        Pessoa pessoa = servidor.getPessoa();
        lotacaoRepository.deleteAll(pessoa.getLotacoes());
        pessoa.getLotacoes().clear();
        
        // 2. Remove todos os endereços da pessoa
        pessoaEnderecoRepository.deleteAll(pessoa.getEnderecos());
        pessoa.getEnderecos().clear();
        
        // 3. Remove todas as fotos da pessoa
        pessoa.getFotos().clear();
        
        // 4. Remove o servidor temporário
        servidorTemporarioRepository.delete(servidor);
        
        // 5. Remove a pessoa
        pessoaRepository.delete(pessoa);
    }

    @Transactional
    public void uploadFoto(Long servidorId, MultipartFile foto) throws Exception {
        ServidorTemporario servidor = servidorTemporarioRepository.findById(servidorId)
                .orElseThrow(() -> new ResourceNotFoundException("Servidor temporário não encontrado"));

        String objectName = UUID.randomUUID().toString();
        minioService.uploadFile(objectName, foto);

        FotoPessoa fotoPessoa = new FotoPessoa();
        fotoPessoa.setPessoa(servidor.getPessoa());
        fotoPessoa.setData(LocalDate.now());
        fotoPessoa.setBucket(minioService.getBucketName());
        fotoPessoa.setHash(objectName);
        
        servidor.getPessoa().getFotos().add(fotoPessoa);
        pessoaRepository.save(servidor.getPessoa());
    }

    private ServidorTemporarioDTO toDTO(ServidorTemporario servidor) {
        ServidorTemporarioDTO dto = new ServidorTemporarioDTO();
        dto.setId(servidor.getId());
        dto.setNome(servidor.getPessoa().getNome());
        dto.setDataNascimento(servidor.getPessoa().getDataNascimento());
        dto.setSexo(servidor.getPessoa().getSexo());
        dto.setMae(servidor.getPessoa().getMae());
        dto.setPai(servidor.getPessoa().getPai());
        dto.setDataAdmissao(servidor.getDataAdmissao());
        dto.setDataDemissao(servidor.getDataDemissao());

        // Converter endereços
        List<EnderecoDTO> enderecosDTO = servidor.getPessoa().getEnderecos().stream()
            .map(pe -> {
                EnderecoDTO enderecoDTO = new EnderecoDTO();
                enderecoDTO.setId(pe.getEndereco().getId());
                enderecoDTO.setTipoLogradouro(pe.getEndereco().getTipoLogradouro());
                enderecoDTO.setLogradouro(pe.getEndereco().getLogradouro());
                enderecoDTO.setNumero(pe.getEndereco().getNumero());
                enderecoDTO.setBairro(pe.getEndereco().getBairro());
                
                CidadeDTO cidadeDTO = new CidadeDTO();
                cidadeDTO.setId(pe.getEndereco().getCidade().getId());
                cidadeDTO.setNome(pe.getEndereco().getCidade().getNome());
                cidadeDTO.setUf(pe.getEndereco().getCidade().getUf());
                enderecoDTO.setCidade(cidadeDTO);
                
                return enderecoDTO;
            })
            .collect(Collectors.toList());
        dto.setEnderecos(enderecosDTO);

        // Obtém a unidade atual
        servidor.getPessoa().getLotacoes().stream()
                .filter(l -> l.getDataRemocao() == null)
                .findFirst()
                .ifPresent(l -> {
                    dto.setUnidadeId(l.getUnidade().getId());
                    dto.setPortariaLotacao(l.getPortaria());
                });

        // Obtém as fotos
        List<FotoPessoaDTO> fotosDTO = servidor.getPessoa().getFotos().stream()
            .map(foto -> {
                FotoPessoaDTO fotoDTO = new FotoPessoaDTO();
                fotoDTO.setId(foto.getId());
                fotoDTO.setData(foto.getData());
                fotoDTO.setBucket(foto.getBucket());
                fotoDTO.setHash(foto.getHash());
                try {
                    String url = minioService.getPresignedUrl(foto.getHash());
                    fotoDTO.setUrl(url);
                } catch (Exception e) {
                    log.error("Erro ao gerar URL da foto", e);
                }
                return fotoDTO;
            })
            .collect(Collectors.toList());
        dto.setFotos(fotosDTO);

        return dto;
    }

    private ServidorTemporarioUnidadeDTO toUnidadeDTO(ServidorTemporario servidor) {
        ServidorTemporarioUnidadeDTO dto = new ServidorTemporarioUnidadeDTO();
        dto.setId(servidor.getId());
        dto.setNome(servidor.getPessoa().getNome());
        
        // Calcular idade
        LocalDate dataNascimento = servidor.getPessoa().getDataNascimento();
        LocalDate hoje = LocalDate.now();
        int idade = hoje.getYear() - dataNascimento.getYear();
        if (hoje.getMonthValue() < dataNascimento.getMonthValue() || 
            (hoje.getMonthValue() == dataNascimento.getMonthValue() && 
             hoje.getDayOfMonth() < dataNascimento.getDayOfMonth())) {
            idade--;
        }
        dto.setIdade(idade);

        // Obtém a unidade atual
        servidor.getPessoa().getLotacoes().stream()
                .filter(l -> l.getDataRemocao() == null)
                .findFirst()
                .ifPresent(l -> dto.setUnidadeNome(l.getUnidade().getNome()));

        // Obtém a foto mais recente
        servidor.getPessoa().getFotos().stream()
            .max(Comparator.comparing(FotoPessoa::getData))
            .ifPresent(foto -> {
                try {
                    String url = minioService.getPresignedUrl(foto.getHash());
                    dto.setFotoUrl(url);
                } catch (Exception e) {
                    log.error("Erro ao gerar URL da foto", e);
                }
            });

        return dto;
    }
} 
