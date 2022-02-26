package com.sis.dao;

import com.sis.entities.AttendanceDetails;
import com.sis.entities.StudentEnrollment;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Collection;

public interface AttendanceDetailsRepository extends BaseDao<AttendanceDetails>{


    @Query(value="SELECT * FROM attendance_details WHERE  student_id= :studentId and section_id=:sectionId ", nativeQuery = true)
    public ArrayList<AttendanceDetails> findStudentAttendances(long studentId, long sectionId);


}
