package pe.edu.upeu.AutoAndes.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AutoAndes.dto.servicio.ServicioRequestDTO;
import pe.edu.upeu.AutoAndes.dto.servicio.ServicioResponseDTO;
import pe.edu.upeu.AutoAndes.entity.Servicio;
import pe.edu.upeu.AutoAndes.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AutoAndes.exception.ReglaNegocioException;
import pe.edu.upeu.AutoAndes.repository.ServicioRepository;
import pe.edu.upeu.AutoAndes.service.ServicioService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> listarTodos() {
        return servicioRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> listarActivos() {
        return servicioRepository.findAll().stream()
                .filter(s -> s.getActivo() != null && s.getActivo() == 1)
                .map(this::mapearADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponseDTO obtenerPorId(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con ID: " + id));
        return mapearADTO(servicio);
    }

    @Override
    public ServicioResponseDTO crear(ServicioRequestDTO dto) {
        if (servicioRepository.existsByCodigoIgnoreCase(dto.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un servicio registrado con el código: " + dto.getCodigo());
        }

        Servicio servicio = new Servicio();
        servicio.setCodigo(dto.getCodigo().toUpperCase().trim());
        servicio.setNombre(dto.getNombre().trim());
        servicio.setPrecio(dto.getPrecio());
        servicio.setDuracionMin(dto.getDuracionMin());
        servicio.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);

        Servicio guardado = servicioRepository.save(servicio);
        return mapearADTO(guardado);
    }

    @Override
    public ServicioResponseDTO actualizar(Long id, ServicioRequestDTO dto) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con ID: " + id));

        if (servicioRepository.existsByCodigoIgnoreCaseAndIdNot(dto.getCodigo(), id)) {
            throw new ReglaNegocioException("Ya existe otro servicio con el código: " + dto.getCodigo());
        }

        servicio.setCodigo(dto.getCodigo().toUpperCase().trim());
        servicio.setNombre(dto.getNombre().trim());
        servicio.setPrecio(dto.getPrecio());
        servicio.setDuracionMin(dto.getDuracionMin());
        if (dto.getActivo() != null) {
            servicio.setActivo(dto.getActivo());
        }

        Servicio actualizado = servicioRepository.save(servicio);
        return mapearADTO(actualizado);
    }

    @Override
    public ServicioResponseDTO cambiarEstado(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con ID: " + id));

        int nuevoEstado = (servicio.getActivo() != null && servicio.getActivo() == 1) ? 0 : 1;
        servicio.setActivo(nuevoEstado);
        Servicio actualizado = servicioRepository.save(servicio);
        return mapearADTO(actualizado);
    }

    private ServicioResponseDTO mapearADTO(Servicio s) {
        return new ServicioResponseDTO(
                s.getId(),
                s.getCodigo(),
                s.getNombre(),
                s.getPrecio(),
                s.getDuracionMin(),
                s.getActivo()
        );
    }
}
