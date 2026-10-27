package br.edu.univille.poo.oficina.control;

import br.edu.univille.poo.oficina.model.entity.Veiculo;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.service.ClienteService;
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

import java.util.LinkedHashMap;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    private VeiculoService service;
    private ClienteService clienteService;

    public VeiculoController(VeiculoService service, ClienteService clienteService) {
        this.service = service;
        this.clienteService = clienteService;
    }

    // Listagem (com pesquisa por placa, modelo ou cliente)
    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String busca) {
        ModelAndView mv = new ModelAndView("veiculo/lista");
        mv.addObject("lista", service.pesquisar(busca));
        mv.addObject("busca", busca);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novo() {
        return formulario(new Veiculo(), null);
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        return formulario(service.obterPeloId(id), null);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute Veiculo veiculo, RedirectAttributes redirect) {
        try {
            service.salvar(veiculo);
            redirect.addFlashAttribute("sucesso", "Veículo salvo com sucesso.");
            return new ModelAndView("redirect:/veiculos");
        } catch (RegraNegocioException e) {
            return formulario(veiculo, e.getMessage());
        }
    }

    @GetMapping("/{id}/excluir")
    public ModelAndView confirmarExclusao(@PathVariable Long id) {
        Veiculo veiculo = service.obterPeloId(id);
        var detalhes = new LinkedHashMap<String, Object>();
        detalhes.put("Placa", veiculo.getPlaca());
        detalhes.put("Veículo", veiculo.getMarca() + " " + veiculo.getModelo());
        detalhes.put("Ano", veiculo.getAno());
        detalhes.put("Cliente", veiculo.getCliente().getNome());
        return Telas.confirmarExclusao("Excluir veículo", "/veiculos/" + id + "/excluir", "/veiculos", detalhes);
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("sucesso", "Veículo excluído com sucesso.");
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/veiculos";
    }

    private ModelAndView formulario(Veiculo veiculo, String erro) {
        ModelAndView mv = new ModelAndView("veiculo/form");
        mv.addObject("veiculo", veiculo);
        mv.addObject("clientes", clienteService.obterTodos());
        mv.addObject("erro", erro);
        return mv;
    }

}
