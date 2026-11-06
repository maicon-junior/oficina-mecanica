package br.edu.univille.poo.oficina.control;

import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

final class Telas {

    private Telas() {}

    static ModelAndView confirmarExclusao(String titulo, String acao, String voltar, Map<String, Object> detalhes) {
        ModelAndView mv = new ModelAndView("confirmar-exclusao");
        mv.addObject("titulo", titulo);
        mv.addObject("acao", acao);
        mv.addObject("voltar", voltar);
        mv.addObject("detalhes", detalhes);
        return mv;
    }

    static String moeda(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(valor);
    }

}
