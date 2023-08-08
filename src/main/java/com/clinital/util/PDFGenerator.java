package com.clinital.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.DesktopManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.clinital.payload.response.ApiResponse;
import com.clinital.security.config.azure.AzureServices;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.*;


public class PDFGenerator {

 @Autowired
 private AzureServices azureapadter;
 @Autowired
 private ObjectMapper Mapper;
 
    public void generatePdf(Object obj,String filename,String folder) {
        try {
            // Create a new document
            Document document = new Document();

            // Create a new PDF writer
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            PdfWriter writer = PdfWriter.getInstance(document, out);
            
            // Open the document for writing
            document.open();

            // Add a paragraph to the document
            document.add((Element) obj);

            // Close the document
            document.close();

            // Flush the writer
            writer.flush();

            
            // Get the PDF content as an input stream
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            
            azureapadter.upload(in, out, filename, folder);
            // Close the input stream
            in.close();
        } catch (Exception e) {
            // Handle the exception
        }
    }
    public ResponseEntity<?> GenartePdfLocaly(Object obj,String filename,String Folder) throws IOException{
        try {
            File pdfFolder = new File("/Documents/"+Folder+"/"+filename+".pdf");
            // pdfFolder.createNewFile();
            // if (!pdfFolder.exists()) {
                
            // }
           
            Files.createDirectories(Paths.get("/Documents/"+Folder));
       
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFolder));
            document.open();
            document.add(new Paragraph(Mapper.writeValueAsString(obj)));
            document.close();
            ResponseEntity.status(200).build();
            
            return ResponseEntity.ok(new ApiResponse(true, "File has been created successfully", obj)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
}


