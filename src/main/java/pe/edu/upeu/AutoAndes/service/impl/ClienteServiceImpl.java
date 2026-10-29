package pe.edu.upeu.AutoAndes.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AutoAndes.dto.cliente.ClienteResponseDTO;
import pe.edu.upeu.AutoAndes.entity.Cliente;
import pe.edu.upeu.AutoAndes.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AutoAndes.repository.ClienteRepository;
import pe.edu.upeu.AutoAndes.service.ClienteService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Override
    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));
        return mapearADTO(cliente);
    }

    private ClienteResponseDTO mapearADTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getDocumento(),
                cliente.getNombres(),
                cliente.getTelefono()
        );
    }
}
