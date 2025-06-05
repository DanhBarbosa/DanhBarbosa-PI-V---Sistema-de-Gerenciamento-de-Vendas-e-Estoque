package br.senac.tads.pi.PI_IV.venda.controller;

import br.senac.tads.pi.PI_IV.venda.dto.ItemVendaDto;
import br.senac.tads.pi.PI_IV.venda.dto.VendaRequestDto;
import br.senac.tads.pi.PI_IV.venda.dto.VendaResponseDto;
import br.senac.tads.pi.PI_IV.venda.mapper.VendaMapper;
import br.senac.tads.pi.PI_IV.venda.model.ItemVenda;
import br.senac.tads.pi.PI_IV.venda.model.Venda;
import br.senac.tads.pi.PI_IV.venda.service.VendaService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }


    @PostMapping
    public ResponseEntity salvar(@AuthenticationPrincipal UserDetails usuario, @Valid @RequestBody VendaRequestDto vendaRequestDto) {
        Venda venda = VendaMapper.toModel(vendaRequestDto);
        vendaService.salvarVenda(venda, usuario.getUsername());
        return ResponseEntity.ok().build();

    }

    @GetMapping("/exportar")
    public void exportarVendas(@RequestParam(value = "inicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                               @RequestParam(value = "fim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                               @RequestParam(value = "produto", required = false) String produto,
                               @RequestParam(value = "funcionario", required = false) String funcionario,

                               HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=vendas.xlsx");
        vendaService.exportar(response.getOutputStream(), inicio, fim, funcionario, produto);
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDto>> listarVendas() {
        List<VendaResponseDto> vendaResponseDtos = vendaService.listarVendas();
        return ResponseEntity.ok().body(vendaResponseDtos);
    }

    @PostMapping("/produtos")
    public ResponseEntity<?> validarProduto(@Valid @RequestBody List<ItemVendaDto> produtos) {

        List<ItemVenda> itensVendas = VendaMapper.toListItemVenda(produtos);
        vendaService.validarVenda(itensVendas);
        return ResponseEntity.ok().build();
    }

}
