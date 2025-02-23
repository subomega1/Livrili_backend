package com.sfar.livrili.Security;

import com.sfar.livrili.Domains.Entities.User;
import com.sfar.livrili.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@RequiredArgsConstructor
public class LivriliUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       if (!userRepository.existsByEmail(email)) {
           throw new UsernameNotFoundException("this email not found :"+email);
       }
        User user = userRepository.findByEmail(email);
        return new LivriliUserDetails(user);
    }
}
