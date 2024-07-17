package com.Promotion.PromotionManagement.Security;

        import com.Promotion.PromotionManagement.Models.UserInfo;
        import com.Promotion.PromotionManagement.Repository.UserInfoRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.authentication.BadCredentialsException;
        import org.springframework.security.core.GrantedAuthority;
        import org.springframework.security.core.authority.SimpleGrantedAuthority;
        import org.springframework.security.core.userdetails.User;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.core.userdetails.UsernameNotFoundException;
        import org.springframework.stereotype.Service;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserInfoRepository userInfoRepository;
     private static final Logger logger =LoggerFactory.getLogger(UserDetailsService.class);
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Find the user by email (username)
        List<UserInfo> user= userInfoRepository.findByusername(username);

        if (user.size()!=0) {
            UserInfo users = user.get(0);
             logger.info("the user object ##############################{}",user);
            // Create a list of authorities (roles)
            List<GrantedAuthority> authorities = new ArrayList<>();

            // Add the user's role as a granted authority
//            SimpleGrantedAuthority sga = new SimpleGrantedAuthority(users.getRole());
//            authorities.add(sga);

            // Create and return a UserDetails object representing the user
            // UserDetails contains user details required for authentication
            return new User(users.getUsername(), users.getPassword(), authorities);
        } else {
            // Throw an exception if the user is not found
            throw new BadCredentialsException("User Details not found with this username: " + username);
        }
    }
}