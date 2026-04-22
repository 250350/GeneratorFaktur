package pl.comp.generatorfaktur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SeoController {

    @GetMapping("/como-hacer-factura-espana")
    public String page1() {
        return "seo/como-hacer-factura-espana";
    }

    @GetMapping("/crear-factura-online")
    public String page2() {
        return "seo/crear-factura-online";
    }

    @GetMapping("/factura-autonomos-espana")
    public String page3() {
        return "seo/factura-autonomos-espana";
    }

    @GetMapping("/factura-online-rapida")
    public String page4() {
        return "seo/factura-online-rapida";
    }

    @GetMapping("/factura-sin-registro")
    public String page5() {
        return "seo/factura-sin-registro";
    }

    @GetMapping("/facturas-autonomos-sin-software")
    public String page6() {
        return "seo/facturas-autonomos-sin-software";
    }

    @GetMapping("/generador-facturas-gratis")
    public String page7() {
        return "seo/generador-facturas-gratis";
    }

    @GetMapping("/plantilla-factura")
    public String page8() {
        return "seo/plantilla-factura";
    }

    @GetMapping("/programa-facturacion-gratis")
    public String page9() {
        return "seo/programa-facturacion-gratis";
    }

    @GetMapping("/factura-pdf-gratis")
    public String page10() {
        return "seo/factura-pdf-gratis";
    }

}
