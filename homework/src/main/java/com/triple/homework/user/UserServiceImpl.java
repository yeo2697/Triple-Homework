package com.triple.homework.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> selectById(Long idx){
        return userRepository.findById(idx);
    }


    @Override
    @Transactional
    public void insert(User user) throws Exception{
        if(!userRepository.existsByUserIdAndDelYn(user.getUserId(), user.getDelYn())){
            userRepository.save(user);
        }else{
            throw new Exception("409");
        }
    }
}
