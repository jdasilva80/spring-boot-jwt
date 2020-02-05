package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MessageSourceAccessor message = getMessageSourceAccessor();
		
		response.setHeader("Content-Disposition", "attachment; filename=\"factura.xlsx\"");
		Factura factura = (Factura) model.get("factura");

		Sheet sheet = workbook.createSheet("factura");

		sheet.createRow(0).createCell(0).setCellValue("Datos del cliente");
		sheet.createRow(1).createCell(0)
				.setCellValue(factura.getCliente().getNombre().concat(" ").concat(factura.getCliente().getApellido()));
		sheet.createRow(2).createCell(0).setCellValue(factura.getCliente().getEmail());

		sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");
		sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());
		sheet.createRow(6).createCell(0).setCellValue("Descripción: " + factura.getDescripcion());
		sheet.createRow(7).createCell(0).setCellValue("Fecha: " + factura.getCreateAt());

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(FillPatternType.DIAMONDS);

		Row headerItems = sheet.createRow(9);

		Cell cell = headerItems.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Producto");
		cell = headerItems.createCell(1);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Precio");
		cell = headerItems.createCell(2);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Cantidad");
		cell = headerItems.createCell(3);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Total");

		int rowNum = 10;
		for (ItemFactura item : factura.getItems()) {

			Row rowItem = sheet.createRow(rowNum);
			rowItem.createCell(0).setCellValue(item.getProducto().getNombre());
			rowItem.createCell(1).setCellValue(item.getProducto().getPrecio());
			rowItem.createCell(2).setCellValue(item.getCantidad());
			rowItem.createCell(3).setCellValue(item.calcularImporte());
			rowNum++;

		}
		Row rowTotal = sheet.createRow(rowNum);
		rowTotal.createCell(2).setCellValue("total->");
		rowTotal.createCell(3).setCellValue(factura.getTotal());

	}

}
