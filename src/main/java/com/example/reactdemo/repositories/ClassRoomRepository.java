package com.example.reactdemo.repositories;

import com.example.reactdemo.dtos.ClassroomDTO;
import com.example.reactdemo.models.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.List;

/**
 * 
 * @author binhtn1
 *
 */
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Integer> {

    @Query(value = "select c.id from user_classroom uc\n"
            + "join class_room c on uc.classroom_id = c.id\n"
            + "where uc.classroom_id in (:list)\n"
            + "group by c.id", nativeQuery = true)
    List<Integer> existsClassRoomsByIdIn(String list);

    List<ClassRoom> findAllByUsers_UsernameAndDeleteFlagFalse(String username);

    ClassRoom findByIdAndDeleteFlagFalse(int classRoomId);

    @Query("SELECT new com.example.reactdemo.dtos.ClassroomDTO (CR.id, CR.name, COUNT(AU.id), CR.amountStudent, CR.startDate, CR.endDate,CR.deleteFlag)  "
            + "FROM ClassRoom CR LEFT JOIN CR.users AU " + "where (AU.role.id = 2 OR AU.role.id is null)"
            + "GROUP BY CR.id, CR.name, CR.amountStudent, CR.startDate, CR.endDate,CR.deleteFlag")
    List<ClassroomDTO> findAllClassroom();

    @Query(value = "{CALL GET_AVAILABLE_CLASSROOM(:username)}", nativeQuery = true)
    List<Tuple> findAllClassroomByUsername(String username);

    @Query(value = "SELECT COUNT(AU.id) as current_amount_student "
            + "FROM application_user AU JOIN user_classroom UC ON AU.id = UC.user_id JOIN class_room CR ON UC.classroom_id = CR.id "
            + "WHERE AU.role_id = 2 AND CR.delete_flag = 0 and CR.id = :classId", nativeQuery = true)
    int getCurrentStudentInClassroom(int classId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE class_room SET delete_flag = 1 WHERE id = :classRoomId", nativeQuery = true)
    int deleteClassRoomById(int classRoomId);

    @Query("SELECT CR.id FROM ClassRoom CR LEFT JOIN CR.users AU "
            + "where AU.role.id = 2 AND CR.deleteFlag = false and CR.id = :classId and AU.username = :username")
    String existsClassRegistered(int classId, String username);

    @Query("SELECT new com.example.reactdemo.dtos.ClassroomDTO (CR.id, CR.name, COUNT(AU.id), CR.amountStudent, CR.startDate, CR.endDate,CR.deleteFlag)  "
            + "FROM ClassRoom CR LEFT JOIN CR.users AU "
            + "where AU.role.id = 2 AND CR.deleteFlag = false and AU.username = :username "
            + "GROUP BY CR.id, CR.name, CR.amountStudent, CR.startDate, CR.endDate,CR.deleteFlag")
    List<ClassroomDTO> findAllRegisteredClassrooms(String username);
}
