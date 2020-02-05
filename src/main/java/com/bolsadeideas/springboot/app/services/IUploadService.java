package com.bolsadeideas.springboot.app.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

	public Resource load(String fileName) throws MalformedURLException;

	public String copy(MultipartFile multiPartFile) throws IOException;

	public boolean delete(String fileName);

	public Path getPath(String path);

	public void deleteAll();

	public void init() throws IOException;
}
