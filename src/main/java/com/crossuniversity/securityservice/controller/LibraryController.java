package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.DocumentDTO;
import com.crossuniversity.securityservice.dto.LibraryDTO;
import com.crossuniversity.securityservice.dto.UserBriefProfile;
import com.crossuniversity.securityservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.crossuniversity.securityservice.utils.ResponseCode.*;
import static com.crossuniversity.securityservice.utils.SwaggerConstant.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;


    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Operation(
            summary = "Retrieve libraries owned by the authenticated user",
            description = "This API endpoint allows authenticated users to obtain a list of libraries associated with their account. " +
                    "The response includes relevant details about each owned library, " +
                    "presented in the form of LibraryDTO (Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/my")
    public ResponseEntity<List<LibraryDTO>> getOwnLibraries() {
        return new ResponseEntity<>(libraryService.getOwnLibraries(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve libraries to which the authenticated user is subscribed",
            description = "This API endpoint enables authenticated users to obtain a list of libraries to which they are subscribed. " +
                    "The response includes relevant details about each subscribed library, " +
                    "presented in the form of LibraryDTO (Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/subscribed")
    public ResponseEntity<List<LibraryDTO>> getSubscribedLibraries() {
        return new ResponseEntity<>(libraryService.getSubscribedLibraries(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all libraries associated with a specified university",
            description = "This API endpoint is accessible to both authorized and unauthorized users, " +
                    "allowing them to retrieve a list of all open libraries associated with a particular university. " +
                    "The response includes relevant details about each library, " +
                    "presented in the form of LibraryDTO (Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<LibraryDTO>> getUniversityLibraries(
            @Parameter(description = "ID of the university to retrieve libraries for")
            @PathVariable Long universityId) {
        return new ResponseEntity<>(libraryService.getUniversityLibraries(universityId)
                .stream()
                .map(LibraryDTO::parseEntityToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve information about the owners of a specific library",
            description = "This API endpoint, accessible only to authenticated users, " +
                    "allows them to obtain a list of brief profiles for the owners of libraries associated with their account. " +
                    "The response includes relevant details about each library owner, " +
                    "presented in the form of UserBriefProfile instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = BRIEF_PROFILE_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/{libraryId}/owners")
    public ResponseEntity<List<UserBriefProfile>> getOwnersList(
            @Parameter(description = "ID of the library to retrieve owners for")
            @PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getOwnersList(libraryId), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve information about the subscribers of a specific library",
            description = "This API endpoint, accessible only to authenticated users, " +
                    "allows them to obtain a list of brief profiles for the subscribers of libraries associated with their account. " +
                    "The response includes relevant details about each library subscriber, " +
                    "presented in the form of UserBriefProfile instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = BRIEF_PROFILE_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/{libraryId}/subscribers")
    public ResponseEntity<List<UserBriefProfile>> getSubscribersList(
            @Parameter(description = "ID of the library to retrieve subscribers for")
            @PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getSubscribersList(libraryId), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve documents in a library subscribed or owned by the authenticated user",
            description = "This API endpoint, accessible only to authenticated users, " +
                    "allows them to obtain a list of documents in a library to which they are subscribed or that they own. " +
                    "The response includes relevant details about each document, " +
                    "presented in the form of DocumentDTO (Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = DOCUMENT_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/{libraryId}/documents")
    private ResponseEntity<List<DocumentDTO>> getDocumentsByLibraryId(
            @Parameter(description = "ID of the library to retrieve documents for")
            @PathVariable Long libraryId) throws AccessException {
        return new ResponseEntity<>(libraryService.getDocumentsByLibraryId(libraryId), HttpStatus.OK);
    }

    @Operation(
            summary = "Search for libraries based on specific criteria",
            description = "This API endpoint is accessible to all users, " +
                    "allowing them to search for libraries based on specific criteria such as title, topic, or owner email within a " +
                    "specified university. The response includes relevant details about each matching library, " +
                    "presented in the form of LibraryDTO (Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping("/university/{universityId}/findBy")
    public ResponseEntity<List<LibraryDTO>> findByTopic(
            @Parameter(description = "ID of the university for which libraries are searched.")
            @PathVariable Long universityId,
            @Parameter(description = "Filter libraries by title")
            @RequestParam(required = false) String title,
            @Parameter(description = "Filter libraries by topic")
            @RequestParam(required = false) String topic,
            @Parameter(description = "Filter libraries by owner email")
            @RequestParam(required = false) String ownerEmail) {
        return new ResponseEntity<>(libraryService.findLibrariesBy(universityId, title, topic, ownerEmail)
                .stream()
                .map(LibraryDTO::parseEntityToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Operation(
            summary = "Download a document from a library subscribed or owned by the authenticated user",
            description = "This API endpoint, accessible only to authenticated users, " +
                    "enables them to download a document from a library to which they are subscribed or that they own. " +
                    "The response includes the document file as a downloadable resource.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(mediaType = APPLICATION_OCTET_STREAM_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @GetMapping(value = "/document/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @Parameter(description = "ID of the document to be downloaded")
            @PathVariable Long documentId) throws MalformedURLException {
        Resource resource = libraryService.downloadDocument(documentId);
        return ResponseEntity.ok()
                .contentType(APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }

    @Operation(
            summary = "Create a library for a teacher",
            description = "This API endpoint is designed for authenticated users with the role of a teacher or higher," +
                    " ensuring that users with the role of a student cannot access it. Users with the role of a teacher" +
                    " or higher can utilize this endpoint to create a new library with the option of open or restricted " +
                    "access. The response includes relevant details about the created library, presented in the form of " +
                    "LibraryDTO (Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = CREATED,
                            description = CREATED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/create-teacher")
    public ResponseEntity<LibraryDTO> createTeacherLibrary(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    examples = @ExampleObject(value = LIBRARY_TEACHER_EXAMPLE)
            ))
            @RequestBody LibraryDTO libraryDTO) {
        return new ResponseEntity<>(libraryService.createLibrary(libraryDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create a library for a student",
            description = "This API endpoint is accessible to authenticated users, allowing them to create a new library " +
                    "specifically for students. Libraries created by students always have restricted access. The response " +
                    "includes relevant details about the created library, " +
                    "presented in the form of LibraryDTO (Data Transfer Object) instances",
            responses = {
                    @ApiResponse(
                            responseCode = CREATED,
                            description = CREATED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/create-student")
    public ResponseEntity<LibraryDTO> createStudentLibrary(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    examples = @ExampleObject(value = LIBRARY_STUDENT_EXAMPLE)
            ))
            @RequestBody LibraryDTO libraryDTO) {
        libraryDTO.setLibraryAccess(false);
        return new ResponseEntity<>(libraryService.createLibrary(libraryDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Upload a document to a user's library",
            description = "This API endpoint is accessible to authenticated users, allowing them to upload a document " +
                    "to their library. Users can provide details such as the file, title, topic, description, and the " +
                    "library ID where the document should be uploaded. The response includes relevant details about the " +
                    "uploaded document, presented in the form of DocumentDTO (Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = CREATED,
                            description = CREATED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = DOCUMENT_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping(value = "/{libraryId}/document/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDTO> uploadDocument(
            @Parameter(description = "The document file to be uploaded")
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "The title of the document")
            @RequestParam String title,
            @Parameter(description = "The topic of the document")
            @RequestParam String topic,
            @Parameter(description = "The description of the document")
            @RequestParam String description,
            @Parameter(description = "ID of the library to which the document will be uploaded")
            @PathVariable Long libraryId) throws IOException, AccessException {

        return new ResponseEntity<>(libraryService
                .uploadDocument(file, title, topic, description, libraryId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Subscribe to a library",
            description = "This API endpoint is accessible to authenticated users, enabling them to subscribe to a library. " +
                    "Users can provide the library ID to which they want to subscribe. The response includes the updated list" +
                    " of libraries to which the user is subscribed, presented in the form of LibraryDTO " +
                    "(Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("/{libraryId}/subscribe")
    public ResponseEntity<List<LibraryDTO>> subscribeToLibrary(
            @Parameter(description = "ID of the library to which the user wants to subscribe")
            @PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.subscribeToLibrary(libraryId), HttpStatus.OK);
    }

    @Operation(
            summary = "Subscribe a user to a library",
            description = "This API endpoint is accessible to authenticated users, enabling them to subscribe another " +
                    "user to a library they own. Users can provide the library ID and the email of the user they " +
                    "want to subscribe. The response is a standard 204 No Content status, indicating a successful " +
                    "operation without additional response data.",
            responses = {
                    @ApiResponse(
                            responseCode = NO_CONTENT,
                            description = NO_CONTENT_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("{libraryId}/subscribe/{email}")
    public ResponseEntity<?> subscribeUser(
            @Parameter(description = "ID of the library to which the user wants to subscribe another user")
            @PathVariable Long libraryId,
            @Parameter(description = "The email of the user to be subscribed")
            @PathVariable String email) throws AccessException {
        libraryService.subscribeUser(libraryId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Unsubscribe a user from a library",
            description = "This API endpoint is accessible to authenticated users, allowing them to unsubscribe another " +
                    "user from a library they own. Users can provide the library ID and the email of the user they " +
                    "want to unsubscribe. The response is a standard 204 No Content status, indicating a successful " +
                    "operation without additional response data.",
            responses = {
                    @ApiResponse(
                            responseCode = NO_CONTENT,
                            description = NO_CONTENT_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("{libraryId}/unsubscribe/{email}")
    public ResponseEntity<?> unsubscribeUser(
            @Parameter(description = "ID of the library from which the user wants to unsubscribe another user")
            @PathVariable Long libraryId,
            @Parameter(description = "The email of the user to be unsubscribed")
            @PathVariable String email) throws AccessException {
        libraryService.unsubscribeUser(libraryId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Unsubscribe from a library",
            description = "This API endpoint is accessible to authenticated users, allowing them to unsubscribe from " +
                    "a library to which they are subscribed. Users can provide the library ID from which they want to " +
                    "unsubscribe. The response includes the updated list of libraries from which the user is unsubscribed, " +
                    "presented in the form of LibraryDTO (Data Transfer Object) instances.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_LIST_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("/{libraryId}/unsubscribe")
    public ResponseEntity<List<LibraryDTO>> unsubscribeLibrary(
            @Parameter(description = "ID of the library from which the user wants to unsubscribe")
            @PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.unsubscribeLibrary(libraryId), HttpStatus.OK);
    }

    @Operation(
            summary = "Add an existing document to a library",
            description = "This API endpoint is accessible to authenticated users, allowing them to add an existing document " +
                    "from other libraries to libraries they own. Users can provide the library ID and the document ID to be " +
                    "added. The response is a standard 204 No Content status, indicating a successful operation without " +
                    "additional response data.",
            responses = {
                    @ApiResponse(
                            responseCode = NO_CONTENT,
                            description = NO_CONTENT_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("/{libraryId}/document/{documentId}/add")
    public ResponseEntity<?> addExistedDocumentToLibrary(
            @Parameter(description = "ID of the library to which the user wants to add an existing document")
            @PathVariable Long libraryId,
            @Parameter(description = "ID of the existing document to be added")
            @PathVariable Long documentId) throws AccessException {
        libraryService.addExistedDocumentToLibrary(libraryId, documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Remove an existing document from a library",
            description = "This API endpoint is accessible to authenticated users, allowing them to remove an " +
                    "existing document from their library, specifically documents that were added from other " +
                    "libraries. Users can provide the library ID and the document ID to be removed. The response " +
                    "is a standard 204 No Content status, indicating a successful operation without additional response data.",
            responses = {
                    @ApiResponse(
                            responseCode = NO_CONTENT,
                            description = NO_CONTENT_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("/{libraryId}/document/{documentId}/remove")
    public ResponseEntity<?> removeDocumentFromLibrary(
            @Parameter(description = "ID of the library from which the user wants to remove an existing document")
            @PathVariable Long libraryId,
            @Parameter(description = "ID of the existing document to be removed")
            @PathVariable Long documentId) throws AccessException {
        libraryService.removeExistedDocumentFromLibrary(libraryId, documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Update library information",
            description = "This API endpoint is accessible to authenticated users, allowing them to update the " +
                    "title and topic of a library they own. Users can provide a LibraryDTO (Data Transfer Object) " +
                    "containing the updated information. The response includes the updated LibraryDTO instance representing " +
                    "the modified library.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = LIBRARY_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("/update")
    public ResponseEntity<LibraryDTO> updateLibrary(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    examples = @ExampleObject(value = LIBRARY_EXAMPLE)
            ))
            @RequestBody LibraryDTO libraryDTO) throws AccessException {
        return new ResponseEntity<>(libraryService.updateLibrary(libraryDTO), HttpStatus.OK);
    }

    @Operation(
            summary = "Update document information",
            description = "This API endpoint is accessible to authenticated users, specifically allowing the owner " +
                    "to update information about a document, including the title, topic, and description. Users can " +
                    "provide a DocumentDTO (Data Transfer Object) containing the updated information. The response " +
                    "includes the updated DocumentDTO instance representing the modified document.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = DOCUMENT_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PutMapping("/document/update")
    public ResponseEntity<DocumentDTO> updateDocument(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    examples = @ExampleObject(value = DOCUMENT_UPDATE_EXAMPLE)
            ))
            @RequestBody DocumentDTO documentDTO) throws AccessException {
        return new ResponseEntity<>(libraryService.updateDocument(documentDTO), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a library",
            description = "This API endpoint is accessible to authenticated users, specifically allowing the owner to delete " +
                    "a library. Users can provide the library ID to be deleted. The response is a standard 204 No " +
                    "Content status, indicating a successful operation without additional response data.",
            responses = {
                    @ApiResponse(
                            responseCode = NO_CONTENT,
                            description = NO_CONTENT_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @DeleteMapping("/{libraryId}/delete")
    public ResponseEntity<?> deleteLibrary(
            @Parameter(description = "ID of the library to be deleted")
            @PathVariable Long libraryId) throws AccessException {
        libraryService.deleteLibrary(libraryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Delete a document",
            description = "This API endpoint is accessible to authenticated users, specifically allowing the owner to delete " +
                    "a document. Users can provide the library ID to be deleted. The response is a standard 204 No " +
                    "Content status, indicating a successful operation without additional response data.",
            responses = {
                    @ApiResponse(
                            responseCode = NO_CONTENT,
                            description = NO_CONTENT_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = UNAUTHORIZED,
                            description = UNAUTHORIZED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNAUTHORIZED_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = FORBIDDEN,
                            description = FORBIDDEN_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = FORBIDDEN_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @DeleteMapping("/document/{documentId}/delete")
    public ResponseEntity<?> deleteDocument(
            @Parameter(description = "ID of the document to be deleted")
            @PathVariable Long documentId) throws AccessException {
        libraryService.deleteDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
