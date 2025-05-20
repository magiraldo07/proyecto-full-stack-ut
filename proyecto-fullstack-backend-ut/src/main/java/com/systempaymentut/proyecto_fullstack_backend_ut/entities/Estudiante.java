package com.systempaymentut.proyecto_fullstack_backend_ut.entities;

//import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //marca la clase estudiante como una entidad para sincronizarse en una base de datos
@Builder //con builder permite que todos los constructores se generaon con un patron de diseño llamado builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {
    //public, private, protected
    //metodos accesores get obtener / set establecer

    @Id
    private String id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String codigo;

    private String programaId;

    private String foto;
}