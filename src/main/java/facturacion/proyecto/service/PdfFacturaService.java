
package facturacion.proyecto.service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.model.FacturaDetalle;
import facturacion.proyecto.model.FacturaResumen;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PdfFacturaService {

    public String generarPdfFactura(FacturaResumen factura, Cliente cliente, List<FacturaDetalle> detalle) throws Exception {
        File carpeta = new File("facturas_pdf");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String nombreArchivo = factura.getNumeroFactura() + ".pdf";
        String ruta = new File(carpeta, nombreArchivo).getAbsolutePath();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(ruta));
        document.open();

        document.add(new Paragraph("FACTURA ELECTRÓNICA"));
        document.add(new Paragraph("Número: " + factura.getNumeroFactura()));
        document.add(new Paragraph("Fecha: " + String.valueOf(factura.getFechaEmision())));
        document.add(new Paragraph("Fecha/Hora emisión: " + String.valueOf(factura.getFechaHoraEmision())));
        document.add(new Paragraph("Cliente: " + cliente.getNombreCompleto()));
        document.add(new Paragraph("Identificación: " + cliente.getIdentificacion()));
        document.add(new Paragraph("Correo: " + cliente.getCorreo()));
        document.add(new Paragraph("Teléfono: " + cliente.getTelefono()));
        document.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);

        tabla.addCell(new PdfPCell(new Phrase("Código")));
        tabla.addCell(new PdfPCell(new Phrase("Producto")));
        tabla.addCell(new PdfPCell(new Phrase("Descripción")));
        tabla.addCell(new PdfPCell(new Phrase("Cantidad")));
        tabla.addCell(new PdfPCell(new Phrase("IVA %")));
        tabla.addCell(new PdfPCell(new Phrase("Subtotal")));
        tabla.addCell(new PdfPCell(new Phrase("Total")));

        double subtotal = 0;
        double impuesto = 0;
        double total = 0;

        for (FacturaDetalle d : detalle) {
            tabla.addCell(d.getCodigoProducto());
            tabla.addCell(d.getNombreProducto());
            tabla.addCell(d.getDescripcionEditable());
            tabla.addCell(String.valueOf(d.getCantidad()));
            tabla.addCell(String.format("%.2f", d.getPorcentajeIva()));
            tabla.addCell(String.format("%.2f", d.getSubtotal()));
            tabla.addCell(String.format("%.2f", d.getTotal()));

            subtotal += d.getSubtotal();
            impuesto += d.getImpuesto();
            total += d.getTotal();
        }

        document.add(tabla);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Subtotal: " + String.format("%.2f", subtotal)));
        document.add(new Paragraph("Impuesto: " + String.format("%.2f", impuesto)));
        document.add(new Paragraph("Total: " + String.format("%.2f", total)));
        document.close();

        return ruta;
    }
}
