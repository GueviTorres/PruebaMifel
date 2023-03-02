package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.backend.apirest.encrypt.StringEncrypt;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Region;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;
import com.bolsadeideas.springboot.backend.apirest.models.services.IUploadFileService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadService;

	@Autowired
	private StringEncrypt scr;
	// private final Logger log =
	// LoggerFactory.getLogger(ClienteRestController.class);

	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping("/clientes/{nombre}")
	public ResponseEntity<?> show(@PathVariable String nombre) {

		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cliente = clienteService.findByName(nombre);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cliente == null) {
			response.put("mensaje", "El cliente ".concat(nombre.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}


	@GetMapping("/clientes/pokemon/{nombre}")
	public Object pokemon(@PathVariable String nombre) {

		RestTemplate restemplate = new RestTemplate();
		String uri = "https://pokeapi.co/api/v2/pokemon/";
		Object objeto = restemplate.getForObject(uri + nombre, Object.class);
		return objeto;
	}

	@GetMapping("/clientes/encriptar/{palabra}")
	public Object encriptar(@PathVariable String palabra) throws Exception {

		String key = "92AE31A79FEEB2A3"; // llave
		String iv = "0123456789ABCDEF"; // vector de inicialización
		String cleartext = palabra;
		//System.out.println("Texto encriptado: " + scr.encrypt(key, iv, cleartext));

		return scr.encrypt(key, iv, cleartext);
	}

	@GetMapping("/clientes/desencriptar/{encriptado}")
	public Object desencriptar(@PathVariable String encriptado) throws Exception {

		String key = "92AE31A79FEEB2A3"; // llave
		String iv = "0123456789ABCDEF"; // vector de inicialización

		//System.out.println("Texto desencriptado: " + scr.decrypt(key, iv, scr.encrypt(key, iv, palabra)));

		return scr.decrypt(key, iv, encriptado);
	}
}
