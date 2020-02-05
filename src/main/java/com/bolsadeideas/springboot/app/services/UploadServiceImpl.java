package com.bolsadeideas.springboot.app.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements IUploadService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final String UPLOAD_PATH = "uploads";

	@Override
	public Resource load(String fileName) throws MalformedURLException {

		Path pathFoto = getPath(fileName);
		Resource recurso = null;

		recurso = new UrlResource(pathFoto.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen.");
		}

		return recurso;
	}

	@Override
	public String copy(MultipartFile multipartFile) throws IOException {

		String uniqueNameFile = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

		Path rootPath = getPath(uniqueNameFile);

//				byte[] bytes = file.getBytes();
//				Path rutaCompleta = Paths.get(rutaRecursos + "//" + file.getOriginalFilename());
//				Files.write(rutaCompleta, bytes);
//				
		Files.copy(multipartFile.getInputStream(), rootPath);// alternativa para copiar archivo:

		return uniqueNameFile;

	}

	@Override
	public boolean delete(String fileName) {

		Path rootFoto = getPath(fileName);
		File foto = rootFoto.toFile();

		if (foto != null && foto.exists() && foto.canRead()) {

			if (foto.delete()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String fileName) {

		Path pathFoto = Paths.get(UPLOAD_PATH).resolve(fileName).toAbsolutePath();

		log.info("pathFoto: " + pathFoto.toString());
		log.info("pathFoto.URI: " + pathFoto.toUri().toString());

		return pathFoto;
	}

	@Override
	public void deleteAll() {

		FileSystemUtils.deleteRecursively(Paths.get(UPLOAD_PATH).toFile());

	}

	@Override
	public void init() throws IOException {

		Files.createDirectory(Paths.get(UPLOAD_PATH));

	}

}
