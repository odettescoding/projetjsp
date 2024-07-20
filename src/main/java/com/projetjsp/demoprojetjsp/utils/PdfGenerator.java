package com.projetjsp.demoprojetjsp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import com.projetjsp.demoprojetjsp.models.Transfert;

@Component
public class PdfGenerator {

    public byte[] generateTransfertListPdf(List<Transfert> transferts, String numtel) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PDDocument document = new PDDocument();

        try {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

            float margin = 50;
            float yStart = 700;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float rowHeight = 20;
            int numberOfColumns = 6; // Nombre de colonnes dans le tableau des résultats

            // Dessiner les informations sur l'envoyeur au-dessus du tableau des résultats
            drawSenderInfo(contentStream, margin, yPosition, tableWidth, numtel);
            yPosition -= 2 * rowHeight; // Espacement avant le tableau des résultats

            // Dessiner le tableau des résultats
            drawResultTableHeader(contentStream, margin, yPosition, tableWidth, rowHeight, numberOfColumns);
            yPosition -= rowHeight; // Espacement avant les lignes de données

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (Transfert transfert : transferts) {
                drawResultTableRow(contentStream, margin, yPosition, tableWidth, rowHeight,
                        dateFormat.format(transfert.getDate()),
                        transfert.getRaison(),
                        transfert.getRecepteur().getNom(),
                        transfert.getMontant().toString(),
                        transfert.getFraisDeTransfert().toString());
                yPosition -= rowHeight;
                if (yPosition < 50) {
                    contentStream.close();
                    PDPage newPage = new PDPage();
                    document.addPage(newPage);
                    contentStream = new PDPageContentStream(document, newPage);
                    contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                    yPosition = yStart;
                    drawResultTableHeader(contentStream, margin, yPosition, tableWidth, rowHeight, numberOfColumns);
                    yPosition -= rowHeight;
                }
            }

            contentStream.close();
            document.save(outputStream);
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    private void drawSenderInfo(PDPageContentStream contentStream, float margin, float yPosition, float tableWidth,
            String numtel)
            throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Informations de l'envoyeur:");
        contentStream.newLine();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.showText("Numéro de téléphone: " + numtel);
        // Ajouter d'autres informations de l'envoyeur si nécessaire
        contentStream.endText();
    }

    private void drawResultTableHeader(PDPageContentStream contentStream, float margin, float yPosition,
            float tableWidth,
            float rowHeight, int numberOfColumns) throws IOException {
        float startX = margin;
        float startY = yPosition;
        float cellWidth = tableWidth / (float) numberOfColumns;
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

        // Dessiner les titres des colonnes
        for (int i = 0; i < numberOfColumns; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);
            contentStream.showText(getHeaderTitle(i));
            contentStream.endText();
            startX += cellWidth;
        }
    }

    private void drawResultTableRow(PDPageContentStream contentStream, float margin, float yPosition, float tableWidth,
            float rowHeight, String... cellValues) throws IOException {
        float startX = margin;
        float startY = yPosition;
        float cellWidth = tableWidth / 6; // Nombre de colonnes

        // Dessiner une ligne de tableau avec les valeurs de cellule fournies
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        for (String cellValue : cellValues) {
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);
            contentStream.showText(cellValue);
            contentStream.endText();
            startX += cellWidth;
        }
    }

    private String getHeaderTitle(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Date d'envoi";
            case 1:
                return "Raison du transfert";
            case 2:
                return "Nom du récepteur";
            case 3:
                return "Montant";
            case 4:
                return "Frais de transfert";
            default:
                return "";
        }
    }
}
