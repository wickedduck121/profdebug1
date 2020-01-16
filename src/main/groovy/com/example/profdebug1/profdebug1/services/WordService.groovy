package com.example.profdebug1.profdebug1.services

import com.example.profdebug1.profdebug1.entities.StudentFromTable
import groovy.util.logging.Slf4j
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFTable
import org.apache.poi.xwpf.usermodel.XWPFTableCell
import org.apache.poi.xwpf.usermodel.XWPFTableRow
import org.springframework.stereotype.Service

@Slf4j
@Service
class WordService {

    ArrayList<StudentFromTable> wrd(String path) {
        ArrayList<StudentFromTable> st = new ArrayList<>()
        FileInputStream fileInputStream = new FileInputStream(path)
        XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(fileInputStream))
        List<XWPFTable> tab = docxFile.getTables()
    log.info("got to while")
        for (int i=0;i<tab[0].numberOfRows;i++){
            XWPFTableRow row = tab[0].getRow(i)
            List<XWPFTableCell> cells = row.tableCells
            String nameLoc = cells[0].text
            String codeLoc = cells[1].text
            StudentFromTable stLoc = new StudentFromTable(nameLoc, codeLoc)
            st.push(stLoc)
        }
        log.info("got from while")
        log.info(st.toString())
    st

}
}
