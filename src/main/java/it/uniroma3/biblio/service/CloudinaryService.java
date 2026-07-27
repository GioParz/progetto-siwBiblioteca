package it.uniroma3.biblio.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

	private final Cloudinary cloudinary;

	public CloudinaryService(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	/**
	 * Carica un'immagine su Cloudinary e restituisce l'URL pubblico (https) da
	 * salvare in Libro.copertinaUrl.
	 */
	public String caricaCopertina(MultipartFile file) throws IOException {

		Map<?, ?> risultato = this.cloudinary.uploader().upload(file.getBytes(),
				ObjectUtils.asMap(
						"folder", "bibliotecAmica/copertine",
						"resource_type", "image"
				));

		return (String) risultato.get("secure_url");
	}
}