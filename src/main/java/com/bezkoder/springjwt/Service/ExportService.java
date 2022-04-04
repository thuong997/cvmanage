package com.bezkoder.springjwt.Service;

import com.bezkoder.springjwt.payload.request.GetListTicketRequest;

import java.io.IOException;

public interface ExportService {
    void exportExcel(GetListTicketRequest getListTicketRequest) throws IOException;

}
