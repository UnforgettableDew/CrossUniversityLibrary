package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.mapper.LibraryMapper;
import com.crossuniversity.securityservice.repository.LibraryRepository;
import com.crossuniversity.securityservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final MailService mailService;
    private final LibraryMapper libraryMapper;
    private final LibraryRepository libraryRepository;
    @Autowired
    public TestController(MailService mailService,
                          LibraryMapper libraryMapper,
                          LibraryRepository libraryRepository) {
        this.mailService = mailService;
        this.libraryMapper = libraryMapper;
        this.libraryRepository = libraryRepository;
    }

    @GetMapping("/hello")
    public String hello(){
        return "HELLO";
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendEmail(String context){
        mailService.sendEmail("sagittariusdew@gmail.com", "CrossUniLibrary", context);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping("/dto-test")
//    public ResponseEntity<LibraryDTO> libraryToDTO(){
//        Library library = libraryRepository.findById(6L).get();
//
//
//        LibraryDTO libraryDTO = LibraryDTO.builder()
//                .title("test")
//                .build();
//
//        libraryMapper.updateEntity(libraryDTO, library);
//
//        libraryRepository.save(library);
//
//        LibraryDTO updatedLibraryDTO = libraryMapper.mapToDTO(library);
//        return new ResponseEntity<>(updatedLibraryDTO, HttpStatus.OK);
//    }
}
