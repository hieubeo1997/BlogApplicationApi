package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//override lại method loadUserByUserName lấy dữ liệu user từ trong database để xác thực
@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    public JwtUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(s);
        if(user != null){
            //Nếu đúng trả về đối tượng đã đc cấu hình trong CustomUserDetails
            return CustomUserDetails.build(user);
        }else {
            throw new UsernameNotFoundException("This username" + s +" has not existed!");
        }
    }
}
