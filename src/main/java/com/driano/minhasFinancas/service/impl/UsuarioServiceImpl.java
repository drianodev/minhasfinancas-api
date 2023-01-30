package com.driano.minhasFinancas.service.impl;

import com.driano.minhasFinancas.exception.ErroAutenticacao;
import com.driano.minhasFinancas.exception.RegraNegocioException;
import com.driano.minhasFinancas.model.entity.Usuario;
import com.driano.minhasFinancas.model.repository.UsuarioRepository;
import com.driano.minhasFinancas.service.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;
    //private PasswordEncoder encoder;

    public UsuarioServiceImpl(
            UsuarioRepository repository){//,
            //PasswordEncoder encoder) {
        super();
        this.repository = repository;
        //this.encoder = encoder;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if(!usuario.isPresent()) {
            throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
        }

        //boolean senhasBatem = encoder.matches(senha, usuario.get().getSenha());

        //if(!senhasBatem) {
        if (!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacao("Senha inválida.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
//        criptografarSenha(usuario);
        return repository.save(usuario);
    }

//    private void criptografarSenha(Usuario usuario) {
//        String senha = usuario.getSenha();
//        String senhaCripto = encoder.encode(senha);
//        usuario.setSenha(senhaCripto);
//    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }
}
