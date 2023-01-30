package com.driano.minhasFinancas.api.resource;

import com.driano.minhasFinancas.api.dto.UsuarioDTO;
import com.driano.minhasFinancas.exception.ErroAutenticacao;
import com.driano.minhasFinancas.exception.RegraNegocioException;
import com.driano.minhasFinancas.model.entity.Usuario;
import com.driano.minhasFinancas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private UsuarioService service;

    public UsuarioResource(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar( @RequestBody UsuarioDTO dto ) {
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            //String token = jwtService.gerarToken(usuarioAutenticado);
            //TokenDTO tokenDTO = new TokenDTO( usuarioAutenticado.getNome(), token);
            return ResponseEntity.ok(usuarioAutenticado);
        }catch (ErroAutenticacao e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity salvar( @RequestBody UsuarioDTO dto) {

        Usuario usuario = Usuario.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .senha(dto.getSenha())
                    .build();

        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
