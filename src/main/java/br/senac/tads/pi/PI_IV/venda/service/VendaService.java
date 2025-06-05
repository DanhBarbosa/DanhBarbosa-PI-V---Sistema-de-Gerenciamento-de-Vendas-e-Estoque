package br.senac.tads.pi.PI_IV.venda.service;


import br.senac.tads.pi.PI_IV.produto.model.Produto;
import br.senac.tads.pi.PI_IV.produto.service.ProdutoService;
import br.senac.tads.pi.PI_IV.usuario.model.Usuario;
import br.senac.tads.pi.PI_IV.usuario.service.UsuarioService;
import br.senac.tads.pi.PI_IV.venda.dto.VendaResponseDto;
import br.senac.tads.pi.PI_IV.venda.model.ItemVenda;
import br.senac.tads.pi.PI_IV.venda.model.Venda;
import br.senac.tads.pi.PI_IV.venda.repository.ItemVendaRepository;
import br.senac.tads.pi.PI_IV.venda.repository.VendaRepository;
import br.senac.tads.pi.PI_IV.venda.repository.projection.VendaProjection;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;
    private final ItemVendaRepository itemVendaRepository;


    public VendaService(VendaRepository vendaRepository, UsuarioService usuarioService, ProdutoService produtoService, ItemVendaRepository itemVendaRepository) {
        this.vendaRepository = vendaRepository;
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
        this.itemVendaRepository = itemVendaRepository;
    }

    public Venda salvarVenda(Venda venda, String login) {
        Usuario usuario = usuarioService.buscarUsuarioPorLogin(login);
        venda.setNomeVendedor(usuario.getNome());
        List<ItemVenda> produtos = venda.getItens();

        validarVenda(produtos);

        produtos.forEach(item -> {
            item.setVenda(venda);
            produtoService.atualizarQuantidadeDoProduto(item.getIdProduto(), item.getQuantidade());
        });
        return vendaRepository.save(venda);
    }

    public List<VendaResponseDto> listarVendas() {

        List<VendaProjection> allVendas = itemVendaRepository.findAllVendas();

        return allVendas.stream()
                .map(item -> {
                    String nomeProduto = produtoService.buscarProdutoPorId(item.getIdProduto())
                            .map(Produto::getNome)
                            .orElse(null);
                    return new VendaResponseDto(item.getIdVenda(), item.getNomeFuncionario(), item.getDataVenda(), item.getQuantidade(), item.getValorVenda(), item.getMetodoPagamento(), nomeProduto);
                })
                .collect(Collectors.toList());

    }


    public void exportar(OutputStream outputStream, LocalDate inicio, LocalDate fim, String funcionario, String nomeProduto) throws IOException {

        LocalDateTime inicioDoDia = inicio == null ? null : inicio.atStartOfDay();
        LocalDateTime fimDoDia = fim == null ? null : fim.atTime(LocalTime.MAX);


        List<Venda> vendas = vendaRepository.buscarVendas(funcionario, inicioDoDia, fimDoDia);


        List<Venda> vendasFiltradas = vendas.stream()
                .map(venda -> {
                    List<ItemVenda> itensFiltrados = venda.getItens().stream()
                            .filter(item -> {
                                if (nomeProduto == null) {
                                    return true;
                                }
                                Produto produto = produtoService.buscarProdutoPorId(item.getIdProduto()).orElse(null);
                                return produto != null && produto.getNome().equalsIgnoreCase(nomeProduto);
                            })
                            .collect(Collectors.toList());

                    if (itensFiltrados.isEmpty()) {
                        return null;
                    }

                    venda.setItens(itensFiltrados);
                    return venda;
                })
                .filter(Objects::nonNull)
                .toList();


        List<Venda> vendasExcel = vendasFiltradas.isEmpty() ? vendas : vendasFiltradas;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Vendas");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Vendedor");
        header.createCell(2).setCellValue("Total da Venda");

        header.createCell(3).setCellValue("Nome do Produto");
        header.createCell(4).setCellValue("Quantidade");
        header.createCell(5).setCellValue("Valor");
        header.createCell(6).setCellValue("Metodo de Pagamento");
        header.createCell(7).setCellValue("Data da Venda");

        int rowIdx = 1;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


        for (Venda venda : vendasExcel) {
            for (ItemVenda item : venda.getItens()) {
                Row row = sheet.createRow(rowIdx++);
                Produto produto = produtoService.buscarProdutoPorId(item.getIdProduto()).get();
                row.createCell(0).setCellValue(venda.getId());
                row.createCell(1).setCellValue(venda.getNomeVendedor());
                row.createCell(2).setCellValue(venda.getTotalVenda());
                row.createCell(3).setCellValue(produto.getNome());
                row.createCell(4).setCellValue(item.getQuantidade());
                row.createCell(5).setCellValue(item.getPreco());
                row.createCell(6).setCellValue(venda.getMetodoPagamento());
                row.createCell(7).setCellValue(venda.getDataVenda().format(formatter));
            }
        }

        workbook.write(outputStream);
        workbook.close();
    }

    public void validarVenda(List<ItemVenda> produtos) {

        produtos.forEach(item -> {
            produtoService.validarQuantidadeProduto(item.getIdProduto(), item.getQuantidade());
        });

    }
}
