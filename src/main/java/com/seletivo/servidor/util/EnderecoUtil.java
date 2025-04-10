package com.seletivo.servidor.util;

import com.seletivo.servidor.model.Cidade;
import com.seletivo.servidor.model.Endereco;
import com.seletivo.servidor.repository.CidadeRepository;
import com.seletivo.servidor.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnderecoUtil {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco criarOuBuscarEndereco(String tipoLogradouro, String logradouro, Integer numero, String bairro, String cidadeNome, String cidadeUf) {
        // Buscar ou criar cidade
        Cidade cidade = cidadeRepository.findByNomeAndUf(cidadeNome, cidadeUf)
                .orElseGet(() -> {
                    Cidade novaCidade = new Cidade();
                    novaCidade.setNome(cidadeNome);
                    novaCidade.setUf(cidadeUf);
                    return cidadeRepository.save(novaCidade);
                });

        // Criar e salvar o endereço
        Endereco endereco = new Endereco();
        endereco.setTipoLogradouro(tipoLogradouro);
        endereco.setLogradouro(logradouro);
        endereco.setNumero(numero);
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        return enderecoRepository.save(endereco);
    }
} 
