package com.systempaymentut.proyecto_fullstack_backend_ut;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.systempaymentut.proyecto_fullstack_backend_ut.entities.Estudiante;
import com.systempaymentut.proyecto_fullstack_backend_ut.entities.Pago;
import com.systempaymentut.proyecto_fullstack_backend_ut.enums.PagoStatus;
import com.systempaymentut.proyecto_fullstack_backend_ut.enums.TypePago;
import com.systempaymentut.proyecto_fullstack_backend_ut.repository.EstudianteRepository;
import com.systempaymentut.proyecto_fullstack_backend_ut.repository.PagoRepository;

@SpringBootApplication
public class ProyectoFullstackBackendUtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoFullstackBackendUtApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(EstudianteRepository estudianteRepository, PagoRepository pagoRepository) {
		return args -> {
			estudianteRepository.save(Estudiante.builder()
					.id(UUID.randomUUID().toString())
					.nombre("Valentina")
					.apellido("Rojas")
					.codigo("1240")
					.programaId("ISI1240")
					.build());

			estudianteRepository.save(Estudiante.builder()
					.id(UUID.randomUUID().toString())
					.nombre("Esteban")
					.apellido("Castillo")
					.codigo("1241")
					.programaId("ISI1241")
					.build());

			estudianteRepository.save(Estudiante.builder()
					.id(UUID.randomUUID().toString())
					.nombre("Paula")
					.apellido("Mejía")
					.codigo("1242")
					.programaId("ISI1242")
					.build());

			estudianteRepository.save(Estudiante.builder()
					.id(UUID.randomUUID().toString())
					.nombre("Andrés")
					.apellido("Salazar")
					.codigo("1243")
					.programaId("ISI1243")
					.build());

			estudianteRepository.save(Estudiante.builder()
					.id(UUID.randomUUID().toString())
					.nombre("Camila")
					.apellido("Herrera")
					.codigo("1244")
					.programaId("ISI1244")
					.build());

			estudianteRepository.save(Estudiante.builder()
					.id(UUID.randomUUID().toString())
					.nombre("Mateo")
					.apellido("Moreno")
					.codigo("1245")
					.programaId("ISI1245")
					.build());
			// obtener tipos de pagos diferentes para cada estudiante
			TypePago tiposPago[] = TypePago.values();

			Random random = new Random();

			estudianteRepository.findAll().forEach(estudiante -> {
				for (int i = 0; i < 10; i++) {
					int index = random.nextInt(tiposPago.length);

					// construir un objeto de pago con valores aleatorios
					Pago pago = Pago.builder()
							.cantidad(1000 + (int) (Math.random() * 20000))
							.type(tiposPago[index])
							.status(PagoStatus.CREADO)
							.fecha(LocalDate.now())
							.estudiante(estudiante)
							.build();

					pagoRepository.save(pago);
				}

			});
		};

	}

}
