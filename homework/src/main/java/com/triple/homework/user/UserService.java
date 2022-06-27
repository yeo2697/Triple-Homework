package com.triple.homework.user;

import java.util.*;

public interface UserService {
    Optional<User> selectById(Long idx);

    void insert(User user) throws Exception;
}
