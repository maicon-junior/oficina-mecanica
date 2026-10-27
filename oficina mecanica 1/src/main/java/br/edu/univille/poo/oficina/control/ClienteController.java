package br.edu.univille.poo.oficina.control;

import br.edu.univille.poo.oficina.model.entity.Cliente;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.service.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteController {

    private ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    // Listagem (com pesquisa por nome ou CPF)
    @GetMapping
    public ModelAndView index(@RequestParam(required = false) String busca) {
        ModelAndView mv = new ModelAndView("cliente/lista");
        mv.addObject("lista", service.pesquisar(busca));
        mv.addObject("busca", busca);
        return mv;
    }

    // Tela de cadastro
    @GetMapping("/novo")
    public ModelAndView novo() {
        return formulario(new Cliente(), null);
    }

    // Tela de edição
    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        return formulario(service.obterPeloId(id), null);
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute Cliente cliente, RedirectAttributes redirect) {
        try {
            service.salvar(cliente);
            redirect.addFlashAttribute("sucesso", "Cliente salvo com sucesso.");
            return new ModelAndView("redirect:/clientes");
        } catch (RegraNegocioException e) {
            return formulario(cliente, e.getMessage());
        }
    }

    // Tela de exclusão (confirmação)
    @GetMapping("/{id}/excluir")
    public ModelAndView confirmarExclusao(@PathVariable Long id) {
        Cliente cliente = service.obterPeloId(id);
        var detalhes = new LinkedHashMap<String, Object>();
        detalhes.put("Nome", cliente.getNome());
        detalhes.put("CPF", cliente.getCpf());
        detalhes.put("Telefone", cliente.getTelefone());
        detalhes.put("E-mail", cliente.getEmail());
        return Telas.confirmarExclusao("Excluir cliente", "/clientes/" + id + "/excluir", "/clientes", detalhes);
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("sucesso", "Cliente excluído com sucesso.");
        } catch (RegraNegocioException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/clientes";
    }

    private ModelAndView formulario(Cliente cliente, String erro) {
        ModelAndView mv = new ModelAndView("cliente/form");
        mv.addObject("cliente", cliente);
        mv.addObject("erro", erro);
        return mv;
    }

}
