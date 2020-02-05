package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		MessageSourceAccessor message = getMessageSourceAccessor();
		
		Factura factura = (Factura) model.get("factura");

		PdfPCell cell = null;
		PdfPTable tabla1 = new PdfPTable(1);
		tabla1.setSpacingAfter(20);

		cell = new PdfPCell(new Phrase("Datos del cliente"));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);

		tabla1.addCell(cell);
		tabla1.addCell(factura.getCliente().getNombre() + "" + factura.getCliente().getApellido());
		tabla1.addCell(factura.getCliente().getEmail());

		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);

		cell = new PdfPCell(new Phrase(message.getMessage("texto.factura.ver.detalle.factura")));
		cell.setBackgroundColor(new Color(195, 230, 203));
		cell.setPadding(8f);

		tabla2.addCell(cell);
		tabla2.addCell("folio: " + factura.getId());
		tabla2.addCell("descripción: " + factura.getDescripcion());
		tabla2.addCell("fecha: " + factura.getCreateAt());

		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.setWidths(new float[] { 3.5f, 1, 1, 1 });
		tabla3.setSpacingAfter(20);
		tabla3.addCell("Producto");
		tabla3.addCell("Precio");

		tabla3.addCell("Cantidad ");
		tabla3.addCell("Total");

		for (ItemFactura itemFactura : factura.getItems()) {

			tabla3.addCell(itemFactura.getProducto().getNombre());
			tabla3.addCell(itemFactura.getProducto().getPrecio().toString());
			cell = new PdfPCell(new Phrase(itemFactura.getCantidad().toString()));
			cell.setHorizontalAlignment(Phrase.ALIGN_RIGHT);
			tabla3.addCell(cell);
			tabla3.addCell(itemFactura.calcularImporte().toString());
		}

		cell = new PdfPCell(new Phrase("TOTAL: "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Phrase.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(factura.getTotal().toString());

		document.add(tabla1);
		document.add(tabla2);
		document.add(tabla3);
	}
}
