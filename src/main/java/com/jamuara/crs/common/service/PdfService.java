package com.jamuara.crs.common.service;

import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfService {
    private static final Path PDF_STORAGE = Paths.get("pdf-store");

    public PdfService() throws IOException {
        Files.createDirectories(PDF_STORAGE);
    }

    public void savePdf(String filename, byte[] pdfBytes) throws IOException {
        Path filePath = PDF_STORAGE.resolve(filename);
        Files.write(filePath, pdfBytes);
    }

    public byte[] generatePdf(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }
}
