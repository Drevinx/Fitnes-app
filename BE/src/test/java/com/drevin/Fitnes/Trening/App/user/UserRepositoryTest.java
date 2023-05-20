package com.drevin.Fitnes.Trening.App.user;

import com.drevin.Fitnes.Trening.App.repository.orm.user.Role;
import com.drevin.Fitnes.Trening.App.repository.orm.user.RoleRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.user.User;
import com.drevin.Fitnes.Trening.App.repository.orm.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class UserRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void createRoles() {
        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");
        Role userPremium = new Role("ROLE_VIP_USER");

        roleRepository.saveAll(List.of(admin, user, userPremium));

        long count = roleRepository.count();
        assertEquals(3, count);

        Role adminRepo = roleRepository.findByName("ROLE_ADMIN");
        assertEquals(admin,adminRepo);

        Role userRepo = roleRepository.findByName("ROLE_USER");
        assertEquals(user,userRepo);

        Role userPreRepo = roleRepository.findByName("ROLE_VIP_USER");
        assertEquals(userPremium,userPreRepo);
    }

    @Test
    public void createUsers(){
        String userName = "TestName";
        User user = new User(userName,"Password","email@mail.com",true);
        userRepository.save(user);

        long count = userRepository.count();
        assertEquals(1,count);

        User userRepo = userRepository.findByUsername(userName);
        assertEquals(user,userRepo);

        assertEquals(user.getRoles(),null);

    }

    @Test
    public void addRoleToUser(){

        User user = userRepository.findByUsername("TestName");
        Role role = roleRepository.findByName("ADMIN");

        user.addRole(role);
        User updateUser = userRepository.save(user);
        assertEquals(updateUser.getRoles().size(),1);
        assertTrue(updateUser.getRoles().contains(role));

        assertFalse(updateUser.getRoles().contains(new Role("pop")));


    }


}