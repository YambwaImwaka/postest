package com.example.Spring.boot.POS.System.controllers;

import com.example.Spring.boot.POS.System.model.Sale;
import com.example.Spring.boot.POS.System.repository.SaleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final SaleRepository saleRepository;
    private final TemplateEngine templateEngine;

    public ReceiptController(SaleRepository saleRepository,
                             TemplateEngine templateEngine) {
        this.saleRepository = saleRepository;
        this.templateEngine = templateEngine;
    }

    @GetMapping(value = "/{transactionCode}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<ByteArrayResource> generatePdfReceipt(
            @PathVariable String transactionCode) throws Exception {

        Sale sale = saleRepository.findByTransactionCode(transactionCode)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Context context = new Context();
        context.setVariable("sale", sale);

        String html = templateEngine.process("receipt-template", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=receipt.pdf")
                .body(new ByteArrayResource(outputStream.toByteArray()));
    }
}
