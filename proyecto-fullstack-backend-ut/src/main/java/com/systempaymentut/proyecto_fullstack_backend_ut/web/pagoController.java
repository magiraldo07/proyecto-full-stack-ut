package com.systempaymentut.proyecto_fullstack_backend_ut.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.systempaymentut.proyecto_fullstack_backend_ut.entities.Estudiante;
import com.systempaymentut.proyecto_fullstack_backend_ut.entities.Pago;
import com.systempaymentut.proyecto_fullstack_backend_ut.enums.PagoStatus;
import com.systempaymentut.proyecto_fullstack_backend_ut.enums.TypePago;
import com.systempaymentut.proyecto_fullstack_backend_ut.repository.EstudianteRepository;
import com.systempaymentut.proyecto_fullstack_backend_ut.repository.PagoRepository;
import com.systempaymentut.proyecto_fullstack_backend_ut.services.PagoService;

//definia la clase como un controllador rest
@RestController
@CrossOrigin("*") // para que esta api sea accesible desde cualqier dominio

public class pagoController {
    /* Sesion 4- desarrollo final de backend y pruebas con swagger */

    // hacemos inyeccion de depencia
    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PagoService pagoService;

    // METODOS PARA EL MANEJO DE ESTUDIANTES

    // METODO QUE ME DEVUELVA LA LISTA CON TODOS LOS ESTUDIANTES
    @GetMapping("/estudiantes")
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll(); // retorna todos los estudiantes desde la basae de datos
    }

    // metodo que devuelva un estudiante en especifico por su codigo
    @GetMapping("/estudiantes/{codigo}")
    public Estudiante listarEstudiantePorCodigo(@PathVariable String codigo) {
        return estudianteRepository.findByCodigo(codigo); // busca estudiante por su codigo y retorna el estudiante por
                                                          // el codigo
    }

    // metodo que lista estudiantes segun el programa academico
    @GetMapping("/estudiantesPorPrograma")
    public List<Estudiante> listarEstudiantesPorPrograma(@RequestParam String programaId) {
        return estudianteRepository.findByProgramaId(programaId);
        // requestparam es cuando tiene varios actores
    }

    // METODOS PARA LOS PAGOS

    // metodo que devuelva la lista con todos los pagos registrados
    @GetMapping("/pagos")
    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    // metodo que devuelva un pago en especifico segun su id
    @GetMapping("/pagos/{id}")
    public Pago listarPagoPorId(@PathVariable Long id) {
        return pagoRepository.findById(id).get();// busca un pago por su ID

    }

    // metodo que lista los pagos hechos por un estudiante en especifico segun su
    // codigo
    @GetMapping("/estudiantes/{codigo}/pagos")
    public List<Pago> listarPagosPorCodigoEstudiante(@PathVariable String codigo) { // es path variable porque solo es
                                                                                    // un actor
        return pagoRepository.findByEstudianteCodigo(codigo);
    }

    // metodo que eliste los pagos segun estado
    @GetMapping("/paagosPorStatus")
    public List<Pago> listarPagosPorStatus(@RequestParam PagoStatus status) {

        return pagoRepository.findByStatus(status);
    }

    // metodo que permite listar los pagos segun su tipo)(=EFECTIVO, CHEQUE,
    // TRANFERENCIA, DEPOSITO)
    @GetMapping("/pagos/porTipo")
    public List<Pago> listarPagoPorType(@RequestParam TypePago type) {

        return pagoRepository.findByType(type);
    }

    // METODO PARA ACTUALIZAR UN ESTADO DE PAGO
    @PutMapping("pagos/{pagoId}/actualizarPago")
    public Pago actualizarStatusDePago(@RequestParam PagoStatus status, @PathVariable Long pagoId) {
        return pagoService.actualizarPagoPorStatus(status, pagoId); // llama al servicio actualizar el estado de pago
    }

    // metodo para registrr un pago con archivo adjunto pdf como comprobante
    @PostMapping(path = "/pagos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Pago guardarPago(
            @RequestParam("file") MultipartFile file, // archivo adjunto
            double cantidad,
            TypePago type,
            LocalDate date,
            String codigoEstudiante) throws IOException {
        return pagoService.savePago(file, cantidad, type, date, codigoEstudiante); // guarda el pago en la base de datos
    }

    // metodo que devuelve un archivo asociado a un pago en formato pdf
    @GetMapping(value="/pagoFile/{pagoId}",produces=MediaType.APPLICATION_PDF_VALUE)

    public byte[] listarArchivoPorId(@PathVariable Long pagoId) throws IOException {// si algo sale mal es el throws con ioexception
        return pagoService.getArchivoPorId(pagoId);
    }

    

}
