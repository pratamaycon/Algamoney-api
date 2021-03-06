package com.example.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.reposiory.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa salvar(Pessoa pessoa) {
		pessoa.getContatos().forEach(c -> c.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Optional<Pessoa> optionalPessoa = buscarPessoaPeloId(codigo);
		
		Pessoa pessoaSalva = optionalPessoa.get();
		pessoaSalva.getContatos().clear();
		pessoaSalva.getContatos().addAll(pessoa.getContatos());
		pessoaSalva.getContatos().forEach(c -> c.setPessoa(pessoaSalva));
		
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo", "contatos");
		return pessoaRepository.save(pessoaSalva);
	}
	
	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Optional<Pessoa> optionalPessoa = buscarPessoaPeloId(codigo);
		Pessoa pessoaSalva = optionalPessoa.get();
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}

	private Optional<Pessoa> buscarPessoaPeloId(Long codigo) {
		Optional<Pessoa> optionalPessoa = pessoaRepository.findById(codigo);
		
		if (!optionalPessoa.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return optionalPessoa;
	}

}
