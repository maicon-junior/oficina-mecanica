package br.edu.univille.poo.oficina.control;

import br.edu.univille.poo.oficina.model.entity.Mecanico;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.service.MecanicoService;
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
@RequestMapping("/mecanicos")
public class MecanicoController {

    private MecanicoService service;

    public MecanicoController(MecanicoService service) {
        this.service = service;
    }

    // Listagem (com pesquisa por nome ou especialidade)
    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String busca) {
        ModelAndView mv = new ModelAndView("mecanico/lista");
        mv.addObject("lista", service.pesquisar(busca));
        mv.addObject("busca", busca);
        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novo() {
        return formulario(new Mecanico(), null);
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        return formulario(service.obterPeloId(id), null);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute Mecanico mecanico, RedirectAttributes redirect) {
        try {
            service.salvar(mecanico);
            redirect.addFlashAttribute("sucesso", "Mecânico salvo com sucesso.");
            return new ModelAndView("redirect:/mecanicos");
        } catch (RegraNegocioException e) {
            return formulario(mecanico, e.getMessage());
        }
    }

    @GetMapping("/{id}/excluir")
    public ModelAndView confirmarExclusao(@PathVariable Long id) {
        Mecanico mecanico = service.obterPeloId(id);
        var detalhes = new LinkedHashMap<String, Object>();
        detalhes.put("Nome", mecanico.getNome());
        detalhes.put("CPF", mecanico.getCpf());
        detalhes.put("Especialidade", mecanico.getEspecialidade());
        return Telas.confirmarExclusao("Excluir mecânico", "/mecanicos/" + id + "/excluir", "/mecanicos", detalhes);
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("sucesso", "Mecânico excluído com sucesso.");
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/mecanicos";
    }

    private ModelAndView formulario(Mecanico mecanico, String erro) {
        ModelAndView mv = new ModelAndView("mecanico/form");
        mv.addObject("mecanico", mecanico);
        mv.addObject("erro", erro);
        return mv;
    }

}
