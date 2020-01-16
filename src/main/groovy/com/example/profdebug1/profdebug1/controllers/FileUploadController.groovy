package com.example.profdebug1.profdebug1.controllers

import com.example.profdebug1.profdebug1.entities.Student
import com.example.profdebug1.profdebug1.entities.StudentFromTable
import com.example.profdebug1.profdebug1.repositories.StudentRepository
import com.example.profdebug1.profdebug1.services.WordService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Path
import java.text.SimpleDateFormat

@Slf4j
@Controller
@RequestMapping("/api/files")
class FileUploadController {

    private final WordService wordService
    private final StudentRepository repo

    @Autowired
    FileUploadController(WordService wordServiceLoc, StudentRepository repoLoc)
    {
        wordService = wordServiceLoc
        repo = repoLoc
    }

    @PostMapping
    String submitDoc(@RequestParam("file") MultipartFile fileLoc, ModelMap modelMap){
        log.info("got submit")
        log.info(fileLoc.originalFilename)
        String path = "DocxFiles/"+fileLoc.originalFilename
        File file = new File(path)
        file << fileLoc.bytes
        modelMap.addAttribute("file", fileLoc)
       ArrayList<StudentFromTable> wordList = wordService.wrd(path)
        def suffix1 = ".docx"
        def suffix2 = ".doc"
        String fileName = "";
        if (fileLoc.originalFilename.endsWith(suffix1)) {
            fileName = fileLoc.originalFilename.substring(0, fileLoc.originalFilename.size()-suffix1.size())
        }
        else if(fileLoc.originalFilename.endsWith(suffix2)){
            fileName = fileLoc.originalFilename.substring(0, fileLoc.originalFilename.size()-suffix2.size())
        }
        else{
            fileName = fileLoc.originalFilename
        }
        for (int i = 0; i < wordList.size()-1; i++) {
            def pattern = "yyyy-MM-dd hh:mm:ss"
            Date dt = new SimpleDateFormat(pattern).parse("2010-01-01 00:00:00")
            Student stud = new Student()
            stud.pib = wordList[i].name
            //log.info(fileLoc.getName())
            stud.groupE = fileName
            stud.address = " "
            stud.code  = wordList[i].code
            stud.gender = true



            //stud.idStud = studentLoc.idStud
            stud.date = dt
            stud.prof = false

            repo.save(stud)
        }
"ok"

    }

}
