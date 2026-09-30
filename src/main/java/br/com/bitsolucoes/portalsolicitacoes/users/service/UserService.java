package br.com.bitsolucoes.portalsolicitacoes.users.service;

import br.com.bitsolucoes.portalsolicitacoes.common.exception.ResourceNotFoundException;
import br.com.bitsolucoes.portalsolicitacoes.users.dto.RegisterRequest;
import br.com.bitsolucoes.portalsolicitacoes.users.dto.UserResponse;
import br.com.bitsolucoes.portalsolicitacoes.users.model.User;
import br.com.bitsolucoes.portalsolicitacoes.users.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(RegisterRequest input) {
        String username = input.username().trim();
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Usuário já cadastrado.");
        }

        User user = User.create(username, passwordEncoder.encode(input.password()), input.fullName().trim());
        try {
            return UserResponse.from(repository.save(user));
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("Usuário já cadastrado.");
        }
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }
}
