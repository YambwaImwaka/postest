package com.example.Spring.boot.POS.System.controllers;

import com.example.Spring.boot.POS.System.dto.SaleDTO;
import com.example.Spring.boot.POS.System.model.Product;
import com.example.Spring.boot.POS.System.model.Sale;
import com.example.Spring.boot.POS.System.repository.ProductRepository;
import com.example.Spring.boot.POS.System.service.SalesService;
//import com.stripe.exception.StripeException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.sun.enterprise.module.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//import javax.naming.Context;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.example.Spring.boot.POS.System.repository.SaleRepository;

import javax.naming.Context;

/*
@RestController
@Controller
//@RequestMapping("/sales")
@RequestMapping("/api/sales")
public class SalesController {
    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;    }

    @GetMapping
    public String salesPage(Model model) {
        model.addAttribute("sale", new SaleDTO());
        return "sales";
    }

    @PostMapping("/process")
    public String processSale(@ModelAttribute("sale") SaleDTO saleDTO,
                              Authentication authentication) throws StripeException {
        salesService.processSale(saleDTO, authentication.getName());
        return "redirect:/sales?success";
    }*?

    /*public ResponseEntity<?> processSale(@RequestBody SaleRequest request,
                                         Authentication authentication) {
        try {
            Sale sale = salesService.processSale(request, authentication.getName());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "saleId", sale.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }*/

  /*  @GetMapping("/search")
    @ResponseBody
    public List<Product> searchProducts(@RequestParam String query) {
        return salesService.searchProducts(query);
    }



}*/
  @Controller
  @RequestMapping("/sales")
  public class SalesController {
      private final SalesService salesService;
      private final SaleRepository saleRepository;

      public SalesController(SalesService salesService, ProductRepository productRepository, SaleRepository saleRepository) {
          this.salesService = salesService;

          this.saleRepository = saleRepository;
      }

      @GetMapping
      public String salesPage(Model model) {
          model.addAttribute("sale", new SaleDTO());
          return "sales";
      }

      @PostMapping("/process")
      public String processSale(@ModelAttribute("sale") SaleDTO saleDTO,
                                Authentication authentication) {
          salesService.processSale(saleDTO, authentication.getName());
          return "redirect:/sales?success";
      }
      /*@PostMapping("/process")
      public String processSale(@ModelAttribute Sale sale) {
          salesService.processSale(sale);
          return "redirect:/sales";
      }*/

      @GetMapping("/search")
      @ResponseBody
      public List<Product> searchProducts(@RequestParam String query) {
          return salesService.searchProducts(query);
      }

      // Add to SalesController.java
     /* @GetMapping("/receipt/{transactionCode}")
      public String viewReceipt(@PathVariable String transactionCode, Model model) {
          Sale sale = saleRepository.findByTransactionCode(transactionCode)
                  .orElseThrow(() -> new RuntimeException("Sale not found"));
          model.addAttribute("sale", sale);
          return "receipt";
      }

      @GetMapping("/receipt/pdf/{transactionCode}")
      public ResponseEntity<byte[]> generatePdfReceipt(@PathVariable String transactionCode) throws Exception {
          Sale sale = saleRepository.findByTransactionCode(transactionCode)
                  .orElseThrow(() -> new RuntimeException("Sale not found"));

          Context context = new Context();
          context.setVariable("sale", sale);

          String html = templateEngine.process("receipt", context);

          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          PdfRendererBuilder builder = new PdfRendererBuilder();
          builder.withHtmlContent(html, "/");
          builder.toStream(outputStream);
          builder.run();

          return ResponseEntity.ok()
                  .contentType(MediaType.APPLICATION_PDF)
                  .header("Content-Disposition", "attachment; filename=receipt.pdf")
                  .body(outputStream.toByteArray());
      }*/


      @GetMapping("/history")
      public String transactionHistory(
              @RequestParam(required = false) String query,
              @RequestParam(defaultValue = "0") int page,
              Model model) {

          Pageable pageable = PageRequest.of(page, 20);
          Page<Sale> sales = saleRepository.findAll(PageRequest.of(page, 20));

          model.addAttribute("sales", sales);
          model.addAttribute("currentPage", page);
          return "transaction-history";
      }

  }