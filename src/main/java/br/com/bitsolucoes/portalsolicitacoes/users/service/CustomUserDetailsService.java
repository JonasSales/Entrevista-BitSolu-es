package br.com.bitsolucoes.portalsolicitacoes.users.service;

import br.com.bitsolucoes.portalsolicitacoes.users.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.findByUsername(username);
            return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                    .password(user.getPasswordHash())
                    .disabled(!user.isActive())
                    .authorities("USER")
                    .build();
        } catch (RuntimeException exception) {
            throw new UsernameNotFoundException("Credenciais inválidas.", exception);
        }
    }
}
