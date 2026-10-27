package br.edu.univille.poo.oficina.control;

import br.edu.univille.poo.oficina.model.entity.OrdemServico;
import br.edu.univille.poo.oficina.model.entity.StatusOrdem;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.service.MecanicoService;
import br.edu.univille.poo.oficina.model.service.OrdemServicoService;
import br.edu.univille.poo.oficina.model.service.ServicoService;
import br.edu.univille.poo.oficina.model.service.VeiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/ordens")
public class OrdemServicoController {

    private OrdemServicoService service;
    private VeiculoService veiculoService;
    private MecanicoService mecanicoService;
    private ServicoService servicoService;

    public OrdemServicoController(OrdemServicoService service, VeiculoService veiculoService,
                                  MecanicoService mecanicoService, ServicoService servicoService) {
        this.service = service;
        this.veiculoService = veiculoService;
        this.mecanicoService = mecanicoService;
        this.servicoService = servicoService;
    }

    // Listagem com filtro por status e por placa
    @GetMapping
    public ModelAndView index(@RequestParam(required = false) StatusOrdem status,
                              @RequestParam(required = false) String placa) {
        ModelAndView mv = new ModelAndView("ordem/lista");
        mv.addObject("lista", service.pesquisar(status, placa));
        mv.addObject("statusSelecionado", status);
        mv.addObject("placa", placa);
        mv.addObject("statusLista", StatusOrdem.values());
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novo() {
        return formulario(service.nova(), null);
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        return formulario(service.obterPeloId(id), null);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute OrdemServico ordem, RedirectAttributes redirect) {
        try {
            OrdemServico salva = service.salvar(ordem);
            redirect.addFlashAttribute("sucesso", "Ordem de serviço nº " + salva.getId() + " salva com sucesso.");
            return new ModelAndView("redirect:/ordens");
        } catch (RegraNegocioException e) {
            return formulario(ordem, e.getMessage());
        }
    }

    @GetMapping("/{id}/excluir")
    public ModelAndView confirmarExclusao(@PathVariable Long id) {
        OrdemServico ordem = service.obterPeloId(id);
        var detalhes = new LinkedHashMap<String, Object>();
        detalhes.put("Nº da OS", ordem.getId());
        detalhes.put("Abertura", ordem.getDataAbertura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        detalhes.put("Veículo", ordem.getVeiculo().getDescricao());
        detalhes.put("Cliente", ordem.getVeiculo().getCliente().getNome());
        detalhes.put("Status", ordem.getStatus().getDescricao());
        detalhes.put("Valor total", Telas.moeda(ordem.getValorTotal()));
        return Telas.confirmarExclusao("Excluir ordem de serviço", "/ordens/" + id + "/excluir", "/ordens", detalhes);
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("sucesso", "Ordem de serviço excluída com sucesso.");
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/ordens";
    }

    private ModelAndView formulario(OrdemServico ordem, String erro) {
        ModelAndView mv = new ModelAndView("ordem/form");
        mv.addObject("ordem", ordem);
        mv.addObject("veiculos", veiculoService.obterTodos());
        mv.addObject("mecanicos", mecanicoService.obterTodos());
        mv.addObject("servicos", servicoService.obterTodos());
        mv.addObject("statusLista", StatusOrdem.values());
        mv.addObject("erro", erro);
        return mv;
    }

}
