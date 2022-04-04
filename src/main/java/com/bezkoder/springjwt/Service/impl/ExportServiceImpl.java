package com.bezkoder.springjwt.Service.impl;

import com.bezkoder.springjwt.Service.ExportService;
import com.bezkoder.springjwt.Utils.ErrorCode;
import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.GetListTicketRequest;
import com.bezkoder.springjwt.payload.response.TicketTopPageResponse;
import com.bezkoder.springjwt.repository.DepartmentRepository;
import com.bezkoder.springjwt.repository.TicketRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.CustomException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class ExportServiceImpl implements ExportService {


    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentRepository departmentRepository;


    private static XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    @Override
    public void exportExcel(GetListTicketRequest getListTicketRequest) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Export Data");

        List<TicketTopPageResponse> topPageResponses = new ArrayList<>();
        List<TicketEntity> lst = new ArrayList<>();
        TicketTopPageResponse topPageResponse;
        Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndIsDeletedFalse(getListTicketRequest.getUserId());
        if (!userEntityOpt.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addError(ErrorCode.ERR_USER_NOT_FOUND)
                    .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
        }
        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            if (getListTicketRequest.getDepId() == 0) {
                lst = ticketRepository.findAll();
            } else {
                Optional<List<TicketEntity>> lstOpt;
                //Trưởng phòng
                if (userEntity.getRole().equals("2")) {
                    lstOpt = ticketRepository.findAllByDepId(userEntity.getDepEntity().getDepId(), getListTicketRequest.getUserId());
                } else {
                    //User
                    lstOpt = ticketRepository.findAllByUserId(getListTicketRequest.getUserId());
                }
                if (lstOpt.isPresent()) {
                    lst = lstOpt.get();
                }

//                Optional<List<TicketEntity>> lstOpt;
//                if (getListTicketRequest.getDepId() != 0) {
//                    lstOpt = ticketRepository.findAllByDepId(userEntity.getDepEntity().getDepId(), getListTicketRequest.getUserId());
//                    if (lstOpt.isPresent()) {
//                        lst = lstOpt.get();
//                    }
//                }
                if (getListTicketRequest.getDepId() != 0) {
                    Optional<DepEntity> depOpt = departmentRepository.findByDepId(getListTicketRequest.getDepId());
                    List<TicketEntity> lstObj = new ArrayList<>();
                    if (depOpt.isPresent()) {
                        for (TicketEntity ticket : lst) {
                            List<TicketDepRelationEntity> lstDep = ticket.getListDeps();
                            if (lstDep.stream().anyMatch(e -> e.getDepEntity().getDepId() == getListTicketRequest.getDepId())) {
                                lstObj.add(ticket);
                            }
                        }
                        if (lstObj.size() > 0) {
                            try {
                                lst.removeAll(lstObj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//                        lst.removeIf(t -> (t.getListDeps().stream().filter(e->e.getDepEntity().getDepId() != getListTicketRequest.getDepId()).);
                    } else {
                        throw new CustomException(HttpStatus.BAD_REQUEST)
                                .addError(ErrorCode.ERR_DEPARTMENT_NOT_FOUND)
                                .withMessage(ErrorCode.ERR_DEPARTMENT_NOT_FOUND.getMessage());
                    }

                }
            }

        }
        int rownum = 0;
        Cell cell;
        Row row;
        //
        XSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rownum);


        // Name
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Full Name");
        cell.setCellStyle(style);
        // Job
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Job");
        cell.setCellStyle(style);
        // Level
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Level");
        cell.setCellStyle(style);
        // Status
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Status");
        cell.setCellStyle(style);
        // Start
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Start");
        cell.setCellStyle(style);
        // Deadline
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Deadline");
        cell.setCellStyle(style);
        // Manager
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Manager");
        cell.setCellStyle(style);
        // Description
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Description");
        cell.setCellStyle(style);

        // Data
        for (TicketEntity emp : lst) {
            rownum++;
            row = sheet.createRow(rownum);

            String userAttact = "";
            // Name
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(emp.getFullName());
            // Job (B)
            JobLevelRelationEntity jobLevelRelationEntity = emp.getJobLevelRelation().get(0);
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(jobLevelRelationEntity.getJobEntity().getJobName());
            // Level (C)
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(jobLevelRelationEntity.getLevelEntity().getLevelName());
            // Status (D)
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(emp.getStatus());
            // Start (E)
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue(emp.getStart().toString());
            // Deadline (F)
            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue(emp.getDeadline().toString());
            // Manager (G)
            List<TicketUserRelationEntity> ticketUserList = emp.getPIC();
            for (TicketUserRelationEntity relation : ticketUserList) {
                UserEntity entity = relation.getUserEntity();
                userAttact += entity.getUsername() + ",";
            }
            //dùng substring cắt chuỗi
            userAttact = userAttact.substring(0, userAttact.length() - 1);
            cell = row.createCell(6, CellType.NUMERIC);
            cell.setCellValue(userAttact);
            //Description (H)
            cell = row.createCell(7, CellType.NUMERIC);
            cell.setCellValue(emp.getDescription());
        }

        File file = new File("C:/demo/" + new Date().getTime() + ".xlsx");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());
    }
}


