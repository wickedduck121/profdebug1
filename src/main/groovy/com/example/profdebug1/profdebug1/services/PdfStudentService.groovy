package com.example.profdebug1.profdebug1.services

import com.example.profdebug1.profdebug1.entities.Student
import com.example.profdebug1.profdebug1.repositories.StudentRepository
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.text.DateFormat
import java.text.SimpleDateFormat

@Service
class PdfStudentService {

    private static StudentRepository studRepo

    @Autowired
    PdfStudentService(StudentRepository studRepoLoc){
        studRepo = studRepoLoc
    }

    static String createReport(){
        List<Student> students = studRepo.findAll()
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date dateObj = new Date();
        String fileName = "StudentReport"+df.format(dateObj).toString()+".pdf"
        Document document = new Document()
        PdfWriter.getInstance(document, new FileOutputStream(fileName))
        document.open()
        //BaseFont bf = BaseFont.createFont(, 'CP-1251', BaseFont.EMBEDDED)
        //Font fontTimes = new Font(bf, 14)
        Font fontTimes = FontFactory.getFont("fonts/CyrillicRoman.ttf","windows-1251", 14)
        Chunk chunk = new Chunk("Полный отчёт про студентов университета\n", fontTimes)
        Paragraph paraHeader =new Paragraph()
        paraHeader.add(chunk)
        //para.setFont(font)
        paraHeader.setAlignment(Element.ALIGN_CENTER)
        //document.add(chunk)
        document.add(paraHeader)
        PdfPTable table = new PdfPTable(5)
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,"CP1251")

        PdfPCell hCell;
        hCell = new PdfPCell(new Phrase("ФИО", headFont));
        hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hCell);

        hCell = new PdfPCell(new Phrase("Факультет", headFont));
        hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hCell);

        hCell = new PdfPCell(new Phrase("Группа", headFont));
        hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hCell)

        hCell = new PdfPCell(new Phrase("Идент. код", headFont));
        hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hCell)

        hCell = new PdfPCell(new Phrase("Чл. проф.", headFont));
        hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hCell)

        for(Student stud: students){
            PdfPCell cell
            Font bodyFont = FontFactory.getFont(FontFactory.TIMES, 'windows-1251',14)
            cell = new PdfPCell(new Phrase(stud.pib.toString(), bodyFont))
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell)

            cell = new PdfPCell(new Phrase(stud.faculty.abbreviation, bodyFont))
            cell.setPaddingLeft(5)
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT)
            table.addCell(cell)

            cell = new PdfPCell(new Phrase(stud.groupE, bodyFont))
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT)
            cell.setPaddingRight(5)
            table.addCell(cell)

            cell = new PdfPCell(new Phrase(stud.code, bodyFont))
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT)
            cell.setPaddingRight(5)
            table.addCell(cell)

            if(stud.prof) {
                cell = new PdfPCell(new Phrase('+', bodyFont))
            }
            else {
                cell = new PdfPCell(new Phrase('-', bodyFont))
            }
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT)
            cell.setPaddingRight(5)
            table.addCell(cell)
        }
        document.add(table)
        document.close()
        fileName
    }

}
