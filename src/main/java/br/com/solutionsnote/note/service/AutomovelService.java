package br.com.solutionsnote.note.service;

import br.com.solutionsnote.note.dto.AutomovelCreateDTO;
import br.com.solutionsnote.note.dto.AutomovelDTO;
import br.com.solutionsnote.note.mapper.AutomovelMapper;
import br.com.solutionsnote.note.model.Automovel;
import br.com.solutionsnote.note.model.Patio;
import br.com.solutionsnote.note.repository.AutomovelRepository;
import br.com.solutionsnote.note.repository.PatioRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class AutomovelService {

    @Autowired
    private AutomovelRepository repository;

    @Autowired
    private PatioRepository patioRepository;

    public AutomovelDTO salvar(AutomovelCreateDTO dto) {
        Patio patio = patioRepository.findById(dto.patioId())
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));

        Automovel a = new Automovel();
        a.setPlaca(dto.placa());
        a.setChassi(dto.chassi());
        a.setTipo(dto.tipo());
        a.setCor(dto.cor());
        a.setLocalizacaoNoPatio(dto.localizacaoNoPatio());
        a.setComentarios(dto.comentarios());
        a.setPatio(patio);

        return AutomovelMapper.toDTO(repository.save(a));
    }

    public Page<AutomovelDTO> listar(String tipo, Pageable pageable) {
        Page<Automovel> page = tipo == null ?
                repository.findAll(pageable) :
                repository.findByTipoContainingIgnoreCase(tipo, pageable);

        return page.map(AutomovelMapper::toDTO);
    }
}