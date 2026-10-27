package br.edu.univille.poo.oficina.control;

import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Ex.: abrir /clientes/999/editar com um id que não existe -> volta para a listagem com a mensagem de erro
@ControllerAdvice
public class ErroController {

    @ExceptionHandler(RegraNegocioException.class)
    public String regraNegocio(RegraNegocioException e, HttpServletRequest request, RedirectAttributes redirect) {
        redirect.addFlashAttribute("erro", e.getMessage());
        String[] partes = request.getRequestURI().split("/");
        return partes.length > 1 ? "redirect:/" + partes[1] : "redirect:/";
    }

}
