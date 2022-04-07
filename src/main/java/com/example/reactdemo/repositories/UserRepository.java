package com.example.reactdemo.repositories;

import com.example.reactdemo.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author binhtn1
 *
 */
@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    ApplicationUser findByUsernameAndDeleteFlagFalse(String username);

    boolean existsByPhoneAndDeleteFlagFalse(String phone);

    @Query(value = "SELECT concat(ifNull(REGEXP_SUBSTR(username,'[0-9]+'),'0'),'0') DIV 10 FROM application_user WHERE username like :name order by  username DESC limit 1;\n"
            + "", nativeQuery = true)
    Integer getLastNumberOfAccountName(@Param("name") String name);
    
    

    @Modifying
    @Transactional
    @Query(value = "UPDATE application_user SET delete_flag = 1 WHERE username = :username", nativeQuery = true)
    int deleteUserByUsername(String username);

    /**
     * Xóa User multiple usernames
     * usernames
     * @param ids
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE application_user SET delete_flag = 1 WHERE id IN ?1", nativeQuery = true)
    void deleteUserByIds(List<Integer> ids);

    @Query(value = "select * from application_user where concat(first_name, ' ' , last_name)"
            + " like %:search%  and delete_flag = 0 and role_id <> 1", nativeQuery = true)
    List<ApplicationUser> findAll(String search);

    @Query(value = "SELECT * FROM application_user WHERE delete_flag = 0 AND role_id <> 1 AND id IN(:ids)", nativeQuery = true)
    List<ApplicationUser> findAllByIds(List<Integer> ids);
    
    @Query(value = "SELECT * FROM application_user WHERE delete_flag = 0 AND role_id <> 1 AND id IN(:ids)", nativeQuery = true)
    public List<ApplicationUser> getUserPrintData(List<Integer> ids);
}
