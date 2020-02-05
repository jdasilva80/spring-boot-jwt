package com.bolsadeideas.springboot.app.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("clientes.csv")
public class ClienteCsvView extends AbstractView {

	public ClienteCsvView() {
		setContentType("txt/csv");
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setHeader("Content-Dispositon", "attachment; filename=\"clientes.csv\"");
		response.setContentType(getContentType());
		
		@SuppressWarnings({ "unchecked" })
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");

		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

			// the header elements are used to map the bean values to each column (names
			// must match)
			final String[] header = new String[] { "id", "nombre", "apellido", "email", "createAt" };

			// write the header
			beanWriter.writeHeader(header);

			// write the beans
			for (final Cliente cliente : clientes) {
				beanWriter.write(cliente, header);
			}

		} finally {
			if (beanWriter != null) {
				beanWriter.close();
			}
		}

	}

}
