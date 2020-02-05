package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;

	private Page<T> page;

	private int totalPaginas;

	private int numElementosPorPagina;

	private int paginaActual;

	private List<PageItem> pageItems;

	public PageRender(String url, Page<T> page) {

		this.url = url;
		this.page = page;
		this.pageItems = new ArrayList<PageItem>();
		numElementosPorPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber() + 1;

		int desde, hasta;

		if (numElementosPorPagina <= totalPaginas) {
			desde = 1;
			hasta = totalPaginas;

		} else if (paginaActual >= totalPaginas - numElementosPorPagina / 2) {
			desde = totalPaginas - numElementosPorPagina + 1;
			hasta = numElementosPorPagina;
		} else {
			desde = paginaActual - numElementosPorPagina / 2;
			hasta = numElementosPorPagina;
		}

		for (int i = 0; i < hasta; i++) {
			pageItems.add(new PageItem(desde + i, paginaActual == desde + i));
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public void setPaginaActual(int paginaActual) {
		this.paginaActual = paginaActual;
	}

	public List<PageItem> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<PageItem> pageItems) {
		this.pageItems = pageItems;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
