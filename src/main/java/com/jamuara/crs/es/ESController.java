package com.jamuara.crs.es;

import com.jamuara.crs.common.location.dto.Location;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@RestController()
@RequestMapping("es")
public class ESController {
    @Autowired
    public ESService esService;

    @PostMapping("upload")
    public ResponseEntity<?> uploadData(@RequestParam("file") MultipartFile file) {
        try(Reader reader = new InputStreamReader(file.getInputStream())) {
            CsvToBean<Location> csvToBean = new CsvToBeanBuilder<Location>(reader)
                    .withType(Location.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Location> locations = csvToBean.parse();

            esService.jamuaraBulkUpload(locations, "airports");
        } catch (IOException e) {
//            throw new RuntimeException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong: " + e.getMessage());
        }
        return ResponseEntity.ok("data uploaded successfully");
    }
}
