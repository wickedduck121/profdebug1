package com.example.profdebug1.profdebug1.controllers

import com.example.profdebug1.profdebug1.services.PdfStudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/api/report')
class ReportController {

    private final PdfStudentService studReportService

    @Autowired
    ReportController(PdfStudentService studReportServiceLoc){
        studReportService = studReportServiceLoc
    }

    @GetMapping('/student')
    String studentReport(){
    studReportService.createReport()
    }

}
