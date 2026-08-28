package com.neosoft.order_service.service;

import com.neosoft.order_service.entity.Order;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
public class InvoiceGenerator {

    public byte[] generateInvoicePdf(Order order) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                float y = 750;
                float margin = 50;

                content.beginText();
                content.setFont(boldFont, 18);
                content.newLineAtOffset(margin, y);
                content.showText("INVOICE");
                content.endText();

                y -= 40;
                content.beginText();
                content.setFont(regularFont, 11);
                content.newLineAtOffset(margin, y);
                content.showText("Order ID: " + order.getId());
                content.endText();

                y -= 20;
                content.beginText();
                content.setFont(regularFont, 11);
                content.newLineAtOffset(margin, y);
                content.showText("Date: " + order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")));
                content.endText();

                y -= 30;
                content.beginText();
                content.setFont(boldFont, 12);
                content.newLineAtOffset(margin, y);
                content.showText("Customer Details");
                content.endText();

                y -= 18;
                content.beginText();
                content.setFont(regularFont, 11);
                content.newLineAtOffset(margin, y);
                content.showText("Name: " + order.getCustomerName());
                content.endText();

                y -= 30;
                content.beginText();
                content.setFont(boldFont, 12);
                content.newLineAtOffset(margin, y);
                content.showText("Order Details");
                content.endText();

                y -= 18;
                content.beginText();
                content.setFont(regularFont, 11);
                content.newLineAtOffset(margin, y);
                content.showText("Product: " + order.getProductName());
                content.endText();

                y -= 18;
                content.beginText();
                content.setFont(regularFont, 11);
                content.newLineAtOffset(margin, y);
                content.showText("Quantity: " + order.getQuantity());
                content.endText();

                y -= 18;
                content.beginText();
                content.setFont(regularFont, 11);
                content.newLineAtOffset(margin, y);
                content.showText("Total Amount: Rs. " + order.getTotalAmount());
                content.endText();

                y -= 40;
                content.beginText();
                content.setFont(regularFont, 9);
                content.newLineAtOffset(margin, y);
                content.showText("This is a system-generated invoice for POC purposes.");
                content.endText();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate invoice PDF for order " + order.getId(), e);
        }
    }
}