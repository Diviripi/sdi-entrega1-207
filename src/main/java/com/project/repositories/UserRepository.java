package com.project.repositories;


import com.project.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, Long> {

    User findByEmail(String email);

    @Query("Update User u set u.money = ?1 where u.id=?2")
    void updateMoney(double money,  long id );

}
