package com.sis.dto;


import com.sis.dto.college.CollegeDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO extends BaseDTO {

    private String code;
    private String nameAr;
    private String nameEn;
    private Float theoreticalHours;
    private Float exercisesHours;
    private Float practicalHours;
    private Float totalHours;
    private Integer weeks;
    private Float finalGrade;
    private Float finalExamGrade;
    private Float practicalGrade;
    private Float oralGrade;
    private Float midGrade;
    private CollegeDTO collegeDTO;


}
