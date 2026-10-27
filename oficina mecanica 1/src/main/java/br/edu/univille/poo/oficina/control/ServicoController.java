package br.edu.univille.poo.oficina.control;

import br.edu.univille.poo.oficina.model.entity.Servico;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.service.ServicoService;
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
@RequestMapping("/servicos")
public class ServicoController {

    private ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    // Listagem (com pesquisa pela descrição)
    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String busca) {
        ModelAndView mv = new ModelAndView("servico/lista");
        mv.addObject("lista", service.pesquisar(busca));
        mv.addObject("busca", busca);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novo() {
        return formulario(new Servico(), null);
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        return formulario(service.obterPeloId(id), null);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute Servico servico, RedirectAttributes redirect) {
        try {
            service.salvar(servico);
            redirect.addFlashAttribute("sucesso", "Serviço salvo com sucesso.");
            return new ModelAndView("redirect:/servicos");
        } catch (RegraNegocioException e) {
            return formulario(servico, e.getMessage());
        }
    }

    @GetMapping("/{id}/excluir")
    public ModelAndView confirmarExclusao(@PathVariable Long id) {
        Servico servico = service.obterPeloId(id);
        var detalhes = new LinkedHashMap<String, Object>();
        detalhes.put("Descrição", servico.getDescricao());
        detalhes.put("Valor", Telas.moeda(servico.getValor()));
        return Telas.confirmarExclusao("Excluir serviço", "/servicos/" + id + "/excluir", "/servicos", detalhes);
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("sucesso", "Serviço excluído com sucesso.");
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/servicos";
    }

    private ModelAndView formulario(Servico servico, String erro) {
        ModelAndView mv = new ModelAndView("servico/form");
        mv.addObject("servico", servico);
        mv.addObject("erro", erro);
        return mv;
    }

}
