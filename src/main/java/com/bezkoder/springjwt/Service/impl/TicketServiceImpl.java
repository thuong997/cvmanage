package com.bezkoder.springjwt.Service.impl;

import com.bezkoder.springjwt.Service.TicketService;
import com.bezkoder.springjwt.Utils.ErrorCode;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.GetListTicketRequest;
import com.bezkoder.springjwt.payload.request.TicketCreateRequest;
import com.bezkoder.springjwt.payload.request.TicketDetailRequest;
import com.bezkoder.springjwt.payload.request.TicketEditRequest;
import com.bezkoder.springjwt.payload.response.TicketResponse;
import com.bezkoder.springjwt.payload.response.TicketTopPageResponse;
import com.bezkoder.springjwt.repository.*;
import com.bezkoder.springjwt.security.CustomException;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    TicketDepartmentRepository ticketDepartmentRepository;
    @Autowired
    FileServiceImpl fileService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TicketUserRepository ticketUserRepository;
    @Autowired
    TicketJobLevelRepository ticketJobLevelRepository;
    @Autowired
    JobRespository jobRespository;
    @Autowired
    LevelRepository levelRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<TicketTopPageResponse> getAllTicket() {
        return null;
    }

    @Override
    public List<TicketTopPageResponse> getAllTicket(GetListTicketRequest getRequest) {
        List<TicketTopPageResponse> topPageResponses = new ArrayList<>();
        List<TicketEntity> lst = new ArrayList<>();
        TicketTopPageResponse topPageResponse;
        Optional<UserEntity> userOpt = userRepository.findByUserIdAndIsDeletedFalse(getRequest.getUserId());
        if (userOpt.isPresent()) {
            UserEntity userEntity = userOpt.get();
            if (userEntity.getRole().equals("0")) {
                lst = ticketRepository.findAll();
            } else {
                Optional<List<TicketEntity>> lstOpt;
                //Trưởng phòng
                if (userEntity.getRole().equals("2")) {
                    lstOpt = ticketRepository.findAllByDepId(userEntity.getDepEntity().getDepId(), getRequest.getUserId());
                } else {
                    //User
                    lstOpt = ticketRepository.findAllByUserId(getRequest.getUserId());
                }
                if (lstOpt.isPresent()) {
                    lst = lstOpt.get();
                }
            }
            for (TicketEntity t : lst) {

                List<Integer> lstPic = new ArrayList<>();
                List<TicketUserRelationEntity> tu = t.getPIC();
                for (TicketUserRelationEntity ticketUserRelation : tu) {
                    lstPic.add(ticketUserRelation.getUserEntity().getUserId());
                }

                List<Integer> lstDep = new ArrayList<>();
                List<TicketDepRelationEntity> td = t.getListDeps();
                for (TicketDepRelationEntity ticketDepRelation : td) {
                    lstDep.add(ticketDepRelation.getDepEntity().getDepId());
                }

                topPageResponse = TicketTopPageResponse.builder()
                        .jobId(t.getJobLevelRelation().get(0).getJobEntity().getJobId())
                        .levelId(t.getJobLevelRelation().get(0).getLevelEntity().getLevelId())
                        .pic(lstPic)
                        .priority(t.getPriority())
                        .status(t.getStatus())
                        .start(t.getStart())
                        .depId(lstDep)
                        .id(t.getTicketId())
                        .cvUrl(t.getCvUrl())
                        .deadline(t.getDeadline()).build();

                topPageResponses.add(topPageResponse);
            }
            if (getRequest.getDepId() != 0) {
                topPageResponses.removeIf((TicketTopPageResponse t) -> !(t.getDepId().contains(getRequest.getDepId())));
            }
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addError(ErrorCode.ERR_USER_NOT_FOUND)
                    .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
        }

        return topPageResponses;
    }


    @Override
    public TicketResponse ticketDetail(TicketDetailRequest detail) {

        Integer userId = detail.getUserId();

        Integer id = detail.getTicketId();

        Optional<UserEntity> userOpt = userRepository.findByUserIdAndIsDeletedFalse(userId);
        if (userOpt.isPresent()) {
            UserEntity userEntity = userOpt.get();
            if (userEntity.getRole().toLowerCase(Locale.ROOT).equals("0")) {
                Optional<TicketEntity> ticketOpt = ticketRepository.findByTicketId(id);
                if (ticketOpt.isPresent()) {
                    TicketEntity ticket = ticketOpt.get();
                    TicketResponse response = new TicketResponse();
                    response.setFullName(ticket.getFullName());
                    response.setStatus(ticket.getStatus());
                    response.setStart(ticket.getStart().toString());
                    response.setDeadline(ticket.getDeadline().toString());
                    response.setCvUrl(ticket.getCvUrl());
                    response.setDescription(ticket.getDescription());
                    response.setPriority(ticket.getPriority());

                    //Get Job and Level
                    List<JobLevelRelationEntity> jobLevelRelation = ticket.getJobLevelRelation();
                    response.setJobId(jobLevelRelation.get(0).getJobEntity().getJobId());
                    response.setLevelId(jobLevelRelation.get(0).getLevelEntity().getLevelId());
                    //Get List User assign (PIC)
                    List<TicketUserRelationEntity> ticketUserList = ticket.getPIC();
                    List<Integer> userIdList = new ArrayList<Integer>();
                    for (TicketUserRelationEntity relation : ticketUserList) {
                        UserEntity entity = relation.getUserEntity();
                        userIdList.add(entity.getUserId());
                    }
                    response.setPic(userIdList.toArray(new Integer[0]));

                    // Get List Department
                    List<TicketDepRelationEntity> ticketDepList = ticket.getListDeps();
                    List<Integer> depIdList = new ArrayList<Integer>();
                    for (TicketDepRelationEntity relation : ticketDepList) {
                        DepEntity depEntity = relation.getDepEntity();
                        depIdList.add(depEntity.getDepId());
                    }
                    response.setDepartment(depIdList.toArray(new Integer[0]));

                    // Trả dữ liệu về cho Controller
                    return response;
                } else {
                    throw new CustomException(HttpStatus.BAD_REQUEST)
                            .addError(ErrorCode.ERR_TICKET_NOT_FOUND)
                            .withMessage(ErrorCode.ERR_TICKET_NOT_FOUND.getMessage());
                }
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST).addError(ErrorCode.ERR_USER_NOT_ADMIN).withMessage(ErrorCode.ERR_USER_NOT_ADMIN.getMessage());
            }
        }

        throw new CustomException(HttpStatus.BAD_REQUEST).addError(ErrorCode.ERR_USER_NOT_FOUND).withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
    }


    @Override
    @Transactional
    public void createdTicket(TicketCreateRequest ticketRequest) {
        // Validate Job + Level
        String newJobStr = ticketRequest.getNewJob();
        String newLevelStr = ticketRequest.getNewLevel();

        JobEntity jobEntity;
        LevelEntity levelEntity;

        if (StringUtils.isEmpty(newJobStr)) {
            jobEntity = jobRespository.findByJobId(ticketRequest.getJobId());
            if (jobEntity == null)
                throw new CustomException(HttpStatus.NOT_FOUND)
                        .addError(ErrorCode.ERR_JOB_NOT_FOUND)
                        .withMessage(ErrorCode.ERR_JOB_NOT_FOUND.getMessage());
        } else {
            JobEntity temp = jobRespository.findByJobName(newJobStr.trim());
            if (temp != null) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_DUPLICATE_JOB)
                        .withMessage(ErrorCode.ERR_DUPLICATE_JOB.getMessage());
            } else {
                // Khởi tạo Job mới vào lưu vào DB
                jobEntity = new JobEntity(newJobStr.trim());
                jobRespository.save(jobEntity);
            }
        }
        if (StringUtils.isEmpty(newLevelStr)) {
            levelEntity = levelRepository.findByLevelId(ticketRequest.getLevelId());
            if (levelEntity == null)
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_LEVEL_NOT_FOUND)
                        .withMessage(ErrorCode.ERR_LEVEL_NOT_FOUND.getMessage());
        } else {
            LevelEntity temp = levelRepository.findByLevelName(newLevelStr.trim());
            if (temp != null) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_DUPLICATE_LEVEL)
                        .withMessage(ErrorCode.ERR_DUPLICATE_LEVEL.getMessage());
            } else {
                // Khởi tạo Level mới vào lưu vào DB
                levelEntity = new LevelEntity(newLevelStr.trim());
                levelRepository.save(levelEntity);
            }
        }

        // Validate Start - Deadline
        LocalDate startDate = LocalDate.parse(ticketRequest.getStart());
        LocalDate deadlineDate = LocalDate.parse(ticketRequest.getDeadline());
        if (startDate.isAfter(deadlineDate)) {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addError(ErrorCode.ERR_DATE)
                    .withMessage(ErrorCode.ERR_DATE.getMessage());
        }


        // Lưu thông tin cơ bản ticket

        if (StringUtils.isNotEmpty(ticketRequest.getFullName()) &&
                StringUtils.isNotEmpty(ticketRequest.getDescription()) &&
                StringUtils.isNotEmpty(ticketRequest.getStatus()) &&
                StringUtils.isNotEmpty(ticketRequest.getPriority())) {


            TicketEntity ticketEntity = TicketEntity.builder()
                    .fullName(ticketRequest.getFullName())
                    .status(ticketRequest.getStatus())
                    .priority(ticketRequest.getPriority())
                    .start(startDate)
                    .deadline(deadlineDate)
                    .cvUrl(ticketRequest.getCvUrl())
                    .description(ticketRequest.getDescription()).build();
            ticketEntity.setCreatedBy(ticketRequest.getEmail());

            // Lưu thông tin vào bảng ticket
            ticketRepository.save(ticketEntity);
            // Lưu thông tin vào bảng job_level
            ticketJobLevelRepository.save(new JobLevelRelationEntity(ticketEntity, jobEntity, levelEntity));
            // Lưu danh sách phòng ban của ticket
            Optional<List<DepEntity>> listDepsOpt = departmentRepository.findByDepIdIn(ticketRequest.getDepartment());
            if (listDepsOpt.isPresent()) {
                List<DepEntity> lstDeps = listDepsOpt.get();
                if (lstDeps.size() != ticketRequest.getDepartment().length) {
                    throw new CustomException(HttpStatus.BAD_REQUEST)
                            .addError(ErrorCode.ERR_INVALID_DATA)
                            .withMessage(ErrorCode.ERR_INVALID_DATA.getMessage());
                } else {
                    List<TicketDepRelationEntity> lstDepTicket = new ArrayList<>();
                    for (DepEntity dep : lstDeps) {
                        TicketDepRelationEntity obj = new TicketDepRelationEntity(ticketEntity, dep);
//                        obj.setDepEntity(dep);
//                        obj.setTicketEntity(ticketEntity);
                        lstDepTicket.add(obj);
                    }

                    // Lưu thông tin vào bảng ticket_department
                    ticketDepartmentRepository.saveAll(lstDepTicket);
                }
            } else {
                throw new CustomException(HttpStatus.NOT_FOUND)
                        .addError(ErrorCode.ERR_DEPARTMENT_NOT_FOUND);
            }

            // Lưu danh sách User được assign đến ticket
            Optional<List<UserEntity>> listUsersOpt = userRepository.findByUserIdIn(ticketRequest.getPic());
            if (listUsersOpt.isPresent()) {
                List<UserEntity> lstUsers = listUsersOpt.get();
                if (lstUsers.size() != ticketRequest.getPic().length) {
                    throw new CustomException(HttpStatus.BAD_REQUEST)
                            .addError(ErrorCode.ERR_INVALID_DATA)
                            .withMessage(ErrorCode.ERR_INVALID_DATA.getMessage());
                } else {
                    List<TicketUserRelationEntity> lstUserTicket = new ArrayList<>();
                    for (UserEntity user : lstUsers) {
                        TicketUserRelationEntity obj = new TicketUserRelationEntity(ticketEntity, user);
//                        obj.setUserEntity(dep);
//                        obj.setTicketEntity(ticketEntity);
                        lstUserTicket.add(obj);
                    }

                    // Lưu thông tin vào bảng Ticket_user
                    ticketUserRepository.saveAll(lstUserTicket);

                }
            } else {
                throw new CustomException(HttpStatus.NOT_FOUND)
                        .addError(ErrorCode.ERR_USER_NOT_FOUND);
            }
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addErrors(Collections.singletonList(ErrorCode.ERR_INVALID_DATA));
        }
    }

    @Override
    @Transactional
    public void editTicket(int ticketId, TicketEditRequest editRequest) {
        Optional<TicketEntity> ticketEntityOptional = ticketRepository.findByTicketId(ticketId);
        if (!ticketEntityOptional.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addError(ErrorCode.ERR_TICKET_NOT_FOUND)
                    .withMessage(ErrorCode.ERR_TICKET_NOT_FOUND.getMessage());
        } else {
            TicketEntity ticketEntity = ticketEntityOptional.get();

            // Validate Start - Deadline
            LocalDate startDate = LocalDate.parse(editRequest.getStart());
            LocalDate deadlineDate = LocalDate.parse(editRequest.getDeadline());
            if (startDate.isAfter(deadlineDate)) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_DATE)
                        .withMessage(ErrorCode.ERR_DATE.getMessage());
            }
            if (StringUtils.isNotEmpty(editRequest.getFullName()) &&
                    StringUtils.isNotEmpty(editRequest.getDescription()) &&
                    StringUtils.isNotEmpty(editRequest.getStatus()) &&
                    StringUtils.isNotEmpty(editRequest.getPriority()) &&
                    StringUtils.isNotEmpty(editRequest.getDescription())
            ) {
                String isChange = compare(ticketEntity, editRequest);
                ticketEntity.setDescription(editRequest.getDescription());
                ticketEntity.setFullName(editRequest.getFullName());
                ticketEntity.setStatus(editRequest.getStatus());
                ticketEntity.setPriority(editRequest.getPriority());
                ticketEntity.setCvUrl(editRequest.getCvUrl());
                ticketEntity.setUpdatedBy(editRequest.getEmail());
                ticketEntity.setStart(startDate);
                ticketEntity.setDeadline(deadlineDate);
                if (StringUtils.isNotEmpty(isChange.trim())) {
                    ticketEntity.setIsChanged(isChange);
                }
                try {
                    ticketRepository.save(ticketEntity);

                } catch (CustomException e) {
                    e.printStackTrace();
                }
            } else
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addErrors(Collections.singletonList(ErrorCode.ERR_INVALID_DATA));
            // Validate Job + Level
            Optional<List<JobLevelRelationEntity>> jobLevel = ticketJobLevelRepository.findByTicketEntityTicketId(ticketId);
            if (jobLevel.isPresent()) {
//                ticketJobLevelRepository.deleteByTicketEntityTicketId(ticketId);
                ticketJobLevelRepository.deleteAll(jobLevel.get());
                ticketJobLevelRepository.findByTicketEntityTicketId(ticketId);
                JobEntity jobEntity;
                LevelEntity levelEntity;

                if (StringUtils.isEmpty(editRequest.getNewJob().trim())) {
                    jobEntity = jobRespository.findByJobId(editRequest.getJobId());
                    if (jobEntity == null)
                        throw new CustomException(HttpStatus.NOT_FOUND)
                                .addError(ErrorCode.ERR_JOB_NOT_FOUND);
                } else {
                    JobEntity temp = jobRespository.findByJobName(editRequest.getNewJob().trim());
                    if (temp != null) {
                        throw new CustomException(HttpStatus.BAD_REQUEST)
                                .addError(ErrorCode.ERR_DUPLICATE_JOB)
                                .withMessage(ErrorCode.ERR_DUPLICATE_JOB.getMessage());
                    } else {
                        // Khởi tạo Job mới vào lưu vào DB
                        jobEntity = new JobEntity(editRequest.getNewJob().trim());
                        jobRespository.save(jobEntity);
                    }
                }
                if (StringUtils.isEmpty(editRequest.getNewLevel().trim())) {
                    levelEntity = levelRepository.findByLevelId(editRequest.getLevelId());
                    if (levelEntity == null)
                        throw new CustomException(HttpStatus.NOT_FOUND)
                                .addError(ErrorCode.ERR_LEVEL_NOT_FOUND);
                } else {
                    LevelEntity temp = levelRepository.findByLevelName(editRequest.getNewLevel().trim());
                    if (temp != null) {
                        throw new CustomException(HttpStatus.BAD_REQUEST)
                                .addError(ErrorCode.ERR_DUPLICATE_LEVEL)
                                .withMessage(ErrorCode.ERR_DUPLICATE_LEVEL.getMessage());
                    } else {
                        // Khởi tạo Level mới vào lưu vào DB
                        levelEntity = new LevelEntity(editRequest.getNewLevel().trim());
                        levelRepository.save(levelEntity);
                    }
                }

                JobLevelRelationEntity jobLevelRelation = new JobLevelRelationEntity(ticketEntity, jobEntity, levelEntity);
                try {
                    jobLevelRelation.setCreatedAt(LocalDateTime.now());
                    jobLevelRelation.setCreatedBy("Van nguyen");
                    ticketJobLevelRepository.save(jobLevelRelation);

                } catch (CustomException e) {
                    e.printStackTrace();
                }
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addErrors(Collections.singletonList(ErrorCode.ERR_INVALID_DATA));
            }


            // Xóa ticket_department
            Optional<List<TicketDepRelationEntity>> ticketDepOpt = ticketDepartmentRepository.findAllByTicketEntity(ticketEntity);
            if (ticketDepOpt.isPresent()) {
                try {
                    ticketDepartmentRepository.deleteAll(ticketDepOpt.get());
                } catch (CustomException e) {
                    e.printStackTrace();
                }

            }
            Optional<List<DepEntity>> listDepsOpt = departmentRepository.findByDepIdIn(editRequest.getDepartment());
            if (listDepsOpt.isPresent()) {

                List<DepEntity> lstDeps = listDepsOpt.get();
                if (lstDeps.size() != editRequest.getDepartment().length) {
                    throw new CustomException(HttpStatus.BAD_REQUEST)
                            .addError(ErrorCode.ERR_INVALID_DATA)
                            .withMessage(ErrorCode.ERR_INVALID_DATA.getMessage());
                } else {
                    List<TicketDepRelationEntity> lstDepTicket = new ArrayList<>();
                    for (DepEntity dep : lstDeps) {
                        TicketDepRelationEntity obj = new TicketDepRelationEntity(ticketEntity, dep);
//                            obj.setDepEntity(dep);
//                            obj.setTicketEntity(ticketEntity);
                        obj.setCreatedBy("Van Nguyen");
                        obj.setCreatedAt(LocalDateTime.now());
                        lstDepTicket.add(obj);
                    }

                    // Lưu thông tin vào bảng ticket_department
                    try {
                        ticketDepartmentRepository.saveAll(lstDepTicket);
                    } catch (CustomException e) {
                        e.printStackTrace();
                    }
                }
            }


            // Xóa ticket_user


            Optional<List<TicketUserRelationEntity>> ticketUserOpt = ticketUserRepository.findAllByTicketEntity(ticketEntity);
            if (ticketUserOpt.isPresent()) {
                try {
                    ticketUserRepository.deleteAll(ticketUserOpt.get());
                } catch (CustomException e) {
                    e.printStackTrace();
                }
                // Lưu danh sách User được assign đến ticket
            }
            Optional<List<UserEntity>> listUsersOpt = userRepository.findByUserIdIn(editRequest.getPic());
            if (listUsersOpt.isPresent()) {
                List<UserEntity> lstUsers = listUsersOpt.get();
                if (lstUsers.size() != editRequest.getPic().length) {
                    throw new CustomException(HttpStatus.BAD_REQUEST)
                            .addError(ErrorCode.ERR_INVALID_DATA)
                            .withMessage(ErrorCode.ERR_INVALID_DATA.getMessage());
                } else {
                    List<TicketUserRelationEntity> lstUserTicket = new ArrayList<>();
                    for (UserEntity user : lstUsers) {
                        TicketUserRelationEntity obj = new TicketUserRelationEntity(ticketEntity, user);
                        obj.setCreatedBy("Văn Nguyễn");
                        obj.setCreatedAt(LocalDateTime.now());
//                            obj.setUserEntity(user);
//                            obj.setTicketEntity(ticketEntity);
                        lstUserTicket.add(obj);
                    }

                    // Lưu thông tin vào bảng Ticket_user
                    try {
                        ticketUserRepository.saveAll(lstUserTicket);

                    } catch (CustomException e) {
                        e.printStackTrace();
                    }

                }

            }

        }

    }

    public String compare(TicketEntity entity, TicketEditRequest edit) {
        String diff = "";
        if (!entity.getFullName().equals(edit.getFullName()))
            diff += "FullNname";
        if (StringUtils.isNotEmpty(entity.getCvUrl()) && StringUtils.isNotEmpty(edit.getCvUrl())) {
            if (!entity.getCvUrl().equals(edit.getCvUrl()))
                diff += " CvUrl";
        }

        if (!entity.getStatus().equals(edit.getStatus()))
            diff += " Status";
        if (!entity.getPriority().equals(edit.getPriority()))
            diff += " Priority";
        if (!entity.getStart().toString().equals(edit.getStart()))
            diff += " Start";
        if (!entity.getDeadline().toString().equals(edit.getDeadline()))
            diff += " Deadline";
        List<Integer> listDep = new ArrayList<>();
        List<TicketDepRelationEntity> lstTicketDep = entity.getListDeps();
        for (TicketDepRelationEntity d :
                lstTicketDep) {
            int id = d.getDepEntity().getDepId();
            listDep.add(id);
        }
        List<Integer> listUser = new ArrayList<>();
        List<TicketUserRelationEntity> lstTicketUser = entity.getPIC();
        for (TicketUserRelationEntity d :
                lstTicketUser) {
            int id = d.getUserEntity().getUserId();
            listUser.add(id);
        }

        if (!listDep.equals(Arrays.asList(edit.getDepartment())))
            diff += " Departments";
        if (!listUser.equals(Arrays.asList(edit.getPic())))
            diff += " PIC";
        int jobId = entity.getJobLevelRelation().get(0).getJobEntity().getJobId();
        int levelId = entity.getJobLevelRelation().get(0).getLevelEntity().getLevelId();
        if (jobId != edit.getJobId())
            diff += " Job";
        if (levelId != edit.getLevelId())
            diff += " Level";

        return diff;

    }

}

