package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.dto.cliente.ClienteResponseDTO;
import pe.edu.upeu.AutoAndes.entity.Cliente;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toDTO(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getDocumento(),
                cliente.getNombres(),
                cliente.getTelefono()
        );
    }
}
