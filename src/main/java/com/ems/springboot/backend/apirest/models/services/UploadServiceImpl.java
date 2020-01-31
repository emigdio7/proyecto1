package com.ems.springboot.backend.apirest.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;



import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadServiceImpl implements IUploadService {

	private final Logger log = (Logger) LoggerFactory.getLogger(UploadServiceImpl.class);

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		Path rutaArchivo = getPath(nombreFoto);
		log.info(rutaArchivo.toString());

		Resource recurso = new UrlResource(rutaArchivo.toUri());

		if (!recurso.exists() && recurso.isReadable()) { // verificamos que el recurso exista y que se puda leer
			Path rutaArchivo2 = Paths.get("src/main/resources/static/images").resolve("nouser1.png").toAbsolutePath();

			recurso = new UrlResource(rutaArchivo2.toUri());

			log.error("La imagen no esta disponile: " + nombreFoto);

		}
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {

		String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
		Path rutaArchivo = getPath(nombreArchivo);
		log.info(rutaArchivo.toString());

		Files.copy(archivo.getInputStream(), rutaArchivo);

		return nombreArchivo;
	}

	@Override
	public boolean eliminarFoto(String nombreFoto) {

		if (nombreFoto != null && nombreFoto.length() > 0) {

			Path rutaFotoAnterior = getPath(nombreFoto);// obtener la ruta de la foto existente
			File archivoFotoAnterior = rutaFotoAnterior.toFile();// lo convertimos a un archivo

			if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {// verificamos que se creo el archivo y
																				// que se pueda leeer par luego
																				// eliminarlo
				archivoFotoAnterior.delete();// eliminamos la foto
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		
		return Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
	}

}
