package com.sis.service;

import com.sis.dto.section.SectionDTO;
import com.sis.dto.section.SectionRequestDTO;
import com.sis.entity.*;
import com.sis.entity.mapper.SectionMapper;
import com.sis.repository.SectionRepository;
import com.sis.repository.specification.SectionSpecification;
import com.sis.util.PageQueryUtil;
import com.sis.util.PageResult;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class SectionService extends BaseServiceImp<Section> {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private TimetableService timetableService;
    private final StudentEnrollmentService studentEnrollmentService;

    @Override
    public JpaRepository<Section, Long> Repository() {
        return sectionRepository;
    }

    public PageResult<SectionDTO> search(PageQueryUtil pageUtil, SectionRequestDTO sectionRequestDTO) {
        Page<Section> sectionPage;
        String searchValue = sectionRequestDTO.getSearchValue();

        Long filterCollege = sectionRequestDTO.getFilterCollege();

        Long filterDepartment = sectionRequestDTO.getFilterDepartment();

        Long filterAcademicYear = sectionRequestDTO.getFilterAcademicYear();

        Long filterAcademicTerm = sectionRequestDTO.getFilterAcademicTerm();

        Long filterCourse = sectionRequestDTO.getFilterCourse();

        Long filterStudyType = sectionRequestDTO.getFilterStudyType();

        Long filterMajor = sectionRequestDTO.getFilterMajor();

        Pageable pageable = PageRequest.of(pageUtil.getPage() - 1, pageUtil.getLimit(), constructSortObject(sectionRequestDTO));
        if ((searchValue != null && !searchValue.trim().isEmpty()) || filterCollege != null ||
                filterDepartment != null || filterAcademicYear != null || filterAcademicTerm != null ||
                filterCourse != null || filterStudyType != null || filterMajor != null) {
            SectionSpecification sectionSpecification = new SectionSpecification(searchValue, filterCollege, filterDepartment,
                    filterAcademicYear, filterAcademicTerm, filterCourse, filterStudyType, filterMajor);

            sectionPage = sectionRepository.findAll(sectionSpecification, pageable);
        } else {
            sectionPage = sectionRepository.findAll(pageable);
        }
        PageResult<Section> pageResult = new PageResult<>(sectionPage.getContent(), (int) sectionPage.getTotalElements(),
                pageUtil.getLimit(), pageUtil.getPage());

        return sectionMapper.toDataPage(pageResult);
    }

    private Sort constructSortObject(SectionRequestDTO sectionRequestDTO) {
        if (sectionRequestDTO.getSortDirection() == null) {
            return Sort.by(Sort.Direction.ASC, "college");
        }
        return Sort.by(Sort.Direction.valueOf(sectionRequestDTO.getSortDirection()), sectionRequestDTO.getSortBy());
    }

    public Section findSection(String sectionNumber, College college, Department department) {
        return this.sectionRepository.findSectionBySectionNumberAndCollegeAndDepartment(
                sectionNumber, college, department);
    }

    public int countBySection(Section section) {
        return this.studentEnrollmentService.countBySection(section);
    }


    //Abdo.Amr
    public Collection<Section> findStudentSections(AcademicYear academicYear, AcademicTerm academicTerm, Student student) {
        Collection<Section> sections = this.studentEnrollmentService.findStudentSections(academicYear, academicTerm, student);
        return sections;
    }

    //Abdo.Amr
    public Section findStudentSection(long academicYearId, long academicTermId, long studentId, long courseId) {
        Section section = this.studentEnrollmentService.findStudentSection(academicYearId, academicTermId, studentId, courseId);
        return section;
    }

    //Abdo.Amr
    public ArrayList<SectionDTO> findFacultyMemberSections(long academicYearId, long academicTermId, long facultyMemberId) {
        ArrayList<Long> sectionIds = this.timetableService.findFacultyMemberSections(academicYearId, academicTermId, facultyMemberId);
        ArrayList<Section> sections = new ArrayList<>();
        ArrayList<SectionDTO> sectionDTOs = new ArrayList<>();

        if (sectionIds != null && sectionIds.size() > 0) {
            for (long id : sectionIds) {
                Section section = this.findById(id);
                sections.add(section);
            }
            sectionDTOs = this.sectionMapper.toDTOs(sections);
            return sectionDTOs;
        }
        return null;
    }


}
