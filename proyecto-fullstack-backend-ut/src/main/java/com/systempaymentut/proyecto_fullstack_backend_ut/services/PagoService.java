package com.systempaymentut.proyecto_fullstack_backend_ut.services;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.systempaymentut.proyecto_fullstack_backend_ut.entities.Estudiante;
import com.systempaymentut.proyecto_fullstack_backend_ut.entities.Pago;
import com.systempaymentut.proyecto_fullstack_backend_ut.enums.PagoStatus;
import com.systempaymentut.proyecto_fullstack_backend_ut.enums.TypePago;
import com.systempaymentut.proyecto_fullstack_backend_ut.repository.EstudianteRepository;
import com.systempaymentut.proyecto_fullstack_backend_ut.repository.PagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional // permite que todos los elementos de esta clase se ejecuten dentro de una
               // transaccion
public class PagoService {
    // se empieza a trabajar todo lo que son inyecciones de dependencia

    @Autowired
    // esto es la inyeccion de dependiencias de PagoRepository para interactuar con
    // la base de datos de pagos
    private PagoRepository pagoRepository;

    /*
     * inyeccion de dependencias de EstudianteRepository para obtener informacion
     * de los estudiantes desde la base de datos
     */
    @Autowired
    private EstudianteRepository estudianteRepository;

    /**
     * metodo para guardar el pago en la base de datos y almacenar un archivo pdf en
     * el servidor
     * 
     * @param file     archivo pdf que se subira al servidor
     * @param cantidad monto del pago realizado por el estudiante
     * 
     * @param date     fecha en la que se l¿realizo el pago
     * @param codigo   codigo del estudiante que pago
     * @return objeto del pago guardado en la base de datos
     * @throws IOExceptio excepcion lanzada si ocurre un error al manejar el file
     *                    pdf
     * 
     */
    // metodo para pago

    public Pago savePago(MultipartFile file, double cantidad, TypePago type, LocalDate date, String codigoEstudiante) // multipartfile
                                                                                                                      // permite
                                                                                                                      // manejar
                                                                                                                      // un
                                                                                                                      // archivo
            throws IOException {
        /**
         * construir la ruta donde se guardara el archivo dentro del sistema
         * System.getProperty("user.home") obtener la ruta dentro de nuestro directorio
         * personaldel usuario del actual sistena operativo
         * Paths.get : construir una ruta dentro del directorio personal en la carpeta
         * "enset-data/pagos"
         * 
         */

        Path folderPath = Paths.get(System.getProperty("user.home"), "enset-data", "pagos");

        // verificar si la carpeta ya existe si no la debe crear
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        // generamos un nombre unico para el archivo usando UUID(identificador unico
        // universal)
        String fileName = UUID.randomUUID().toString();

        // construimos la ruta completa del archivo agregando la extension .pdf
        Path filePath = Paths.get(System.getProperty("user.home"), "enset-data", "pagos", fileName + ".pdf");

        // guardamos el archivo recibido en la ubicacion especificada dentro del sistema
        // ede archivos
        Files.copy(file.getInputStream(), filePath);

        // buscamos el estuduiannte qye reliza el pago con su codigo
        Estudiante estudiante = estudianteRepository.findByCodigo(codigoEstudiante);

        // creamos un nuevo objeto Pago utilizando el patron de diseño builder
        Pago pago = Pago.builder()
                .type(type)
                .status(PagoStatus.CREADO) // estado inicial del pago
                .fecha(date)
                .estudiante(estudiante)
                .cantidad(cantidad)
                .file(filePath.toUri().toString()) // ruta del archivo pdf almacenado
                .build(); // construccion final del objeto

        return pagoRepository.save(pago);

    }

    public byte[] getArchivoPorId(Long pagoId) throws IOException {

        // busca un objeto pago en la base de datos por id
        Pago pago = pagoRepository.findById(pagoId).get();

        /**
         * pago.getFile: obtiene la URI del archivo guardado como una cadena de texto
         * URI.create: va a convertir la cadena de texto en un objeto URI
         * Path.Of : convierte la URI en un path para poder acceder al archivo
         * Files.readAllBytes: lee el contenido del archivo y lo devuelve en un array
         * vector de bytes
         * Esto va a permitir obtener el contenido del archivo para su posterior uso por
         * ejemplo
         * descargarlo
         */

        return Files.readAllBytes(Path.of(URI.create(pago.getFile())));

    }

    public Pago actualizarPagoPorStatus(PagoStatus status, Long id) {

        // busca un objeto pago en la base de datos por id
        Pago pago = pagoRepository.findById(id).get();

        // actualiza el estado del pago(validado o rechazado)
        pago.setStatus(status);

        // guarda el objeto Pago actualizado en la bd y lo devuelve
        return pagoRepository.save(pago);

    }

}
