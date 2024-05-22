package com.totvs.contas_pagar.adapter.controllers;

import com.totvs.contas_pagar.adapter.entities.dtos.ContaEntityDTO;
import com.totvs.contas_pagar.domain.Conta;
import com.totvs.contas_pagar.domain.ports.ContaServicePort;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contas")
@AllArgsConstructor
public class ContaController {
    private final ContaServicePort contaServicePort;

    @PostMapping
    public Conta create(@RequestBody @Valid ContaEntityDTO contaEntityDTO) {
        return contaServicePort.saveConta(contaEntityDTO);
    }

    @PutMapping("/{id}")
    public Conta update(@PathVariable Long id, @RequestBody @Valid ContaEntityDTO contaEntityDTO) {
        return contaServicePort.updateConta(id, contaEntityDTO);
    }

    @PatchMapping("/{id}/situacao")
    public Conta updateSituacao(@PathVariable Long id, @RequestBody  ContaEntityDTO contaEntityDTO) {
        return contaServicePort.updateSituacaoConta(id, contaEntityDTO.getSituacao());
    }

    @GetMapping("/filtro")
    public List<Conta> findAll(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal,
                               @RequestParam String descricao, @RequestParam Integer pagina,
                               @RequestParam Integer itens) {
        return contaServicePort.findByDataVencimentoBetweenAndDescricaoContaining(dataInicial, dataFinal, descricao, pagina, itens);
    }

    @GetMapping("/{id}")
    public Optional<Conta> findById(@PathVariable Long id) {
        return contaServicePort.findById(id);
    }

    @GetMapping("/total-valor-pago")
    public ResponseEntity<Object> getTotalValorPagoPeriodo(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal) {
        return  contaServicePort.findTotalValorPagoByPeriodo(dataInicial, dataFinal);
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadContas (@RequestParam("file")MultipartFile file) throws IOException {
        return ResponseEntity.ok(contaServicePort.uploadContas(file));
    }
}
