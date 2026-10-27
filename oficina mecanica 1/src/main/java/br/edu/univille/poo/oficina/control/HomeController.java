package br.edu.univille.poo.oficina.control;

import br.edu.univille.poo.oficina.model.service.OrdemServicoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

// Painel principal: totais e últimas ordens de serviço
@Controller
@RequestMapping("/")
public class HomeController {

    private OrdemServicoService ordemServicoService;

    public HomeController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;
    }

    @GetMapping
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("home");
        mv.addObject("resumo", ordemServicoService.obterResumo());
        mv.addObject("ultimasOrdens", ordemServicoService.obterUltimas());
        return mv;
    }

}
