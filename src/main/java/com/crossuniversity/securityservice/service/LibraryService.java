package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.dto.DocumentDTO;
import com.crossuniversity.securityservice.dto.LibraryDTO;
import com.crossuniversity.securityservice.dto.UserBriefProfile;
import com.crossuniversity.securityservice.entity.Document;
import com.crossuniversity.securityservice.entity.Library;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.exception.forbidden.LibraryAccessException;
import com.crossuniversity.securityservice.exception.bad_request.LibraryBadRequestException;
import com.crossuniversity.securityservice.exception.forbidden.DocumentAccessException;
import com.crossuniversity.securityservice.exception.bad_request.DocumentBadRequestException;
import com.crossuniversity.securityservice.exception.not_found.DocumentNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.LibraryNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.UniversityNotFoundException;
import com.crossuniversity.securityservice.exception.not_found.UserNotFoundException;
import com.crossuniversity.securityservice.mapper.BriefProfileMapper;
import com.crossuniversity.securityservice.mapper.DocumentMapper;
import com.crossuniversity.securityservice.mapper.LibraryMapper;
import com.crossuniversity.securityservice.model.DownloadedFile;
import com.crossuniversity.securityservice.repository.DocumentRepository;
import com.crossuniversity.securityservice.repository.LibraryRepository;
import com.crossuniversity.securityservice.repository.UniversityRepository;
import com.crossuniversity.securityservice.repository.UniversityUserRepository;
import com.crossuniversity.securityservice.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class LibraryService {
    private final UniversityUserRepository universityUserRepository;
    private final UniversityRepository universityRepository;
    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;
    private final DocumentMapper documentMapper;
    private final BriefProfileMapper briefProfileMapper;
    private final DocumentRepository documentRepository;
    private final SecurityUtils securityUtils;
    private final S3Service s3Service;

    @Value("${application.aws.bucket}")
    private String bucket;

    @Autowired
    public LibraryService(UniversityUserRepository universityUserRepository,
                          UniversityRepository universityRepository,
                          LibraryRepository libraryRepository,
                          LibraryMapper libraryMapper,
                          DocumentMapper documentMapper,
                          BriefProfileMapper briefProfileMapper,
                          DocumentRepository documentRepository,
                          SecurityUtils securityUtils,
                          S3Service s3Service) {
        this.universityUserRepository = universityUserRepository;
        this.universityRepository = universityRepository;
        this.libraryRepository = libraryRepository;
        this.libraryMapper = libraryMapper;
        this.documentMapper = documentMapper;
        this.briefProfileMapper = briefProfileMapper;
        this.documentRepository = documentRepository;
        this.securityUtils = securityUtils;
        this.s3Service = s3Service;
    }

    public List<LibraryDTO> getOwnLibraries() {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        return libraryMapper.mapToListDTO(universityUser.getOwnLibraries());
    }

    public List<LibraryDTO> getSubscribedLibraries() {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        return libraryMapper.mapToListDTO(universityUser.getSubscribedLibraries());
    }

    public List<LibraryDTO> getUniversityLibraries(Long universityId) {
        if (!universityRepository.existsById(universityId))
            throw new UniversityNotFoundException("University with id = " + universityId + " does not exist");
        return libraryMapper.mapToListDTO(libraryRepository
                .findLibrariesByUniversityIdAndLibraryAccess(universityId, true));
    }

    public List<UserBriefProfile> getOwnersList(Long libraryId) {
        Library library = getLibraryById(libraryId);
        return briefProfileMapper.mapToListDTO(library.getOwners());
    }

    public List<UserBriefProfile> getSubscribersList(Long libraryId) {
        Library library = getLibraryById(libraryId);
        return briefProfileMapper.mapToListDTO(library.getSubscribers());
    }

    public List<DocumentDTO> getDocumentsByLibraryId(Long libraryId) {
        Library library = getLibraryById(libraryId);
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();

        if (!library.isLibraryAccess()) {
            if (checkLibraryOwnerAccess(libraryId, universityUser) ||
                    checkLibrarySubscriberAccess(libraryId, universityUser)) {
                return documentMapper.mapListToDTO(library.getDocuments());
            } else throw new LibraryAccessException("You have not the owner or subscriber of this library");
        } else return documentMapper.mapListToDTO(library.getDocuments());
    }


    public LibraryDTO createLibrary(String title,
                                    String topic,
                                    Boolean libraryAccess) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        Library library = Library.builder()
                .title(title)
                .topic(topic)
                .libraryAccess(libraryAccess)
                .university(universityUser.getUniversity())
                .build();

        libraryRepository.save(library);

        universityUser.addOwnLibrary(library);

        universityUserRepository.save(universityUser);
        return libraryMapper.mapToDTO(library);
    }

    public List<LibraryDTO> subscribeToLibrary(Long libraryId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        Library library = getLibraryById(libraryId);

        if (universityUser.getSubscribedLibraries().contains(library) ||
                universityUser.getOwnLibraries().contains(library))
            throw new LibraryBadRequestException("You are the owner of the library or are already subscribed to it");

        universityUser.addSubscribedLibrary(library);
        universityUserRepository.save(universityUser);
        return libraryMapper.mapToListDTO(universityUser.getSubscribedLibraries());
    }

    public void subscribeUser(Long libraryId, String email) {
        UniversityUser owner = securityUtils.getUserFromSecurityContextHolder();

        if (!checkLibraryOwnerAccess(libraryId, owner))
            throw new LibraryAccessException("You are not the library owner");

        Library library = getLibraryById(libraryId);
        UniversityUser universityUser = getUniversityUserByEmail(email);

        if (universityUser.getSubscribedLibraries().contains(library) ||
                universityUser.getOwnLibraries().contains(library))
            throw new LibraryBadRequestException("User '" + email + "' is owner of the library or is already subscribed to it");

        universityUser.addSubscribedLibrary(library);
        universityUserRepository.save(universityUser);
    }

    public void unsubscribeUser(Long libraryId, String email) {
        UniversityUser owner = securityUtils.getUserFromSecurityContextHolder();

        if (!checkLibraryOwnerAccess(libraryId, owner))
            throw new LibraryAccessException("You are not the library owner");

        Library library = getLibraryById(libraryId);
        UniversityUser universityUser = getUniversityUserByEmail(email);

        if (!universityUser.getSubscribedLibraries().contains(library))
            throw new LibraryBadRequestException("User '" + email + "' is not subscribed to this library");

        universityUser.removeSubscribedLibrary(library);
        universityUserRepository.save(universityUser);
    }

    public List<LibraryDTO> unsubscribeLibrary(Long libraryId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        Library library = getLibraryById(libraryId);

        if (universityUser.getSubscribedLibraries().contains(library))
            throw new LibraryBadRequestException("Library with id = '" + libraryId + "' is not present in your list of subscribed libraries");

        universityUser.removeSubscribedLibrary(library);
        universityUserRepository.save(universityUser);

        return libraryMapper.mapToListDTO(universityUser.getSubscribedLibraries());
    }

    public LibraryDTO updateLibrary(LibraryDTO libraryDTO) {
        UniversityUser owner = securityUtils.getUserFromSecurityContextHolder();

        Long libraryId = libraryDTO.getId();

        if (!checkLibraryOwnerAccess(libraryId, owner))
            throw new LibraryAccessException("You are not the owner of this library");

        Library library = getLibraryById(libraryId);

        Library updatedLibrary = libraryMapper.updateEntity(libraryDTO, library);

        libraryRepository.save(updatedLibrary);

        return libraryMapper.mapToDTO(updatedLibrary);
    }

    public void deleteLibrary(Long libraryId) {
        UniversityUser owner = securityUtils.getUserFromSecurityContextHolder();

        if (!checkLibraryOwnerAccess(libraryId, owner))
            throw new LibraryAccessException("You are not the owner of this library");

        libraryRepository.deleteLibrary(libraryId);
        log.info("Delete from library table");
    }

    public List<LibraryDTO> findLibrariesBy(Long universityId, String title, String topic, String ownerEmail) {
        if (title == null && topic == null && ownerEmail == null)
            return getUniversityLibraries(universityId);

        if (topic == null && ownerEmail == null)
            return libraryMapper.mapToListDTO(libraryRepository.findLibrariesByTitle(title));

        if (title == null && ownerEmail == null)
            return libraryMapper.mapToListDTO(libraryRepository.findLibrariesByTopic(topic));

        if (title == null && topic == null)
            return libraryMapper.mapToListDTO(libraryRepository.findLibrariesByOwner(ownerEmail));

        if (title == null)
            return libraryMapper.mapToListDTO(libraryRepository.findLibrariesByTopicAndOwner(topic, ownerEmail));

        if (topic == null)
            return libraryMapper.mapToListDTO(libraryRepository.findLibrariesByTitleAndOwner(title, ownerEmail));

        if (ownerEmail == null)
            return libraryMapper.mapToListDTO(libraryRepository.findLibrariesByTitleAndTopic(title, topic));

        return libraryMapper.mapToListDTO(libraryRepository.findLibrariesByTitleAndTopicAndOwner(title, topic, ownerEmail));
    }

    public DownloadedFile downloadDocument(Long documentId) throws IOException {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        checkDocumentOwnerAccess(documentId, universityUser);

        Document document = getDocumentById(documentId);

        String filePath = document.getFilePath();
        String fileName = extractFileName(filePath);

        ByteArrayResource resource = new ByteArrayResource(
                s3Service.downloadFile(bucket, filePath)
        );

        return new DownloadedFile(fileName, resource);
    }

    public DocumentDTO uploadDocument(MultipartFile file,
                                      String title, String topic, String description,
                                      Long libraryId) throws IOException {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();

        if (!checkLibraryOwnerAccess(libraryId, universityUser))
            throw new LibraryAccessException("You are not the library owner");

        Library library = libraryRepository.findById(libraryId).orElseThrow();

        String email = universityUser.getUserCredentials().getEmail();
        String path = email + "/" + file.getOriginalFilename();

        Document document = Document.builder()
                .title(title)
                .topic(topic)
                .description(description)
                .filePath(path)
                .fileSize(file.getSize() / 1_000_000.0)
                .libraries(new ArrayList<>())
                .owner(universityUser)
                .build();

        s3Service.uploadFile(bucket, path, file.getBytes());
        universityUser.decreaseSpace(document.getFileSize());

        library.addDocument(document);
        documentRepository.save(document);

        return documentMapper.mapToDTO(document);
    }

    public void addExistedDocumentToLibrary(Long libraryId, Long documentId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        Library library = getLibraryById(libraryId);

        if (!checkLibraryOwnerAccess(libraryId, universityUser))
            throw new LibraryAccessException("You are not the library owner");

        Document document = getDocumentById(documentId);

        if (library.getDocuments().contains(document))
            throw new DocumentBadRequestException("Document with id = '" + documentId + "' is already present in this library");

        library.addDocument(document);

        libraryRepository.save(library);
    }

    public void deleteDocument(Long documentId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();

        if (!checkDocumentOwnerAccess(documentId, universityUser))
            throw new DocumentAccessException("You are not the owner of this document");

        Document document = getDocumentById(documentId);

        s3Service.deleteFile(bucket, document.getFilePath());

        universityUser.increaseSpace(document.getFileSize());

        documentRepository.deleteDocumentById(documentId);
    }

    public void removeExistedDocumentFromLibrary(Long libraryId, Long documentId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();

        if (!checkLibraryOwnerAccess(libraryId, universityUser))
            throw new LibraryAccessException("You are not the library owner");

        Library library = getLibraryById(libraryId);
        Document document = getDocumentById(documentId);

        if (library.getDocuments().contains(document))
            throw new DocumentBadRequestException("Document with id = '" + documentId + "' is not present in this library");

        library.removeDocument(document);
        libraryRepository.save(library);

        List<Library> libraries = document.getLibraries();
        if (libraries.isEmpty())
            documentRepository.deleteDocumentById(documentId);
    }

    public DocumentDTO updateDocument(DocumentDTO documentDTO) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();

        if (!checkDocumentOwnerAccess(documentDTO.getId(), universityUser))
            throw new DocumentAccessException("You are not the owner of this document");

        Document document = getDocumentById(documentDTO.getId());

        Document updatedDocument = documentMapper.updateEntity(documentDTO, document);

        documentRepository.save(updatedDocument);
        return documentMapper.mapToDTO(updatedDocument);
    }

    private UniversityUser getUniversityUserByEmail(String email) {
        return universityUserRepository.findUniversityUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User email = '" + email + "' not found"));
    }

    private Library getLibraryById(Long libraryId) {
        return libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException("Library with id = '" + libraryId + "' does not exist"));
    }

    private Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(() ->
                new DocumentNotFoundException("Document with id = '" + documentId + "' does not exist"));
    }

    private boolean checkLibraryOwnerAccess(Long libraryId, UniversityUser universityUser) {
        boolean flag = false;
        for (Library library : universityUser.getOwnLibraries()) {
            if (library.getId().equals(libraryId))
                return true;
        }
        return flag;
    }

    private boolean checkLibrarySubscriberAccess(Long libraryId, UniversityUser universityUser) {
        for (Library library : universityUser.getSubscribedLibraries()) {
            if (library.getId().equals(libraryId))
                return true;
        }
        return false;
    }

    private boolean checkDocumentOwnerAccess(Long documentId, UniversityUser universityUser) {
        for (Document document : universityUser.getDocuments()) {
            if (document.getId().equals(documentId))
                return true;
        }
        return false;
    }

    private String extractFileName(String path) {
        String regex = "[^/]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);

        if (matcher.find())
            return matcher.group();
        else throw new DocumentBadRequestException("Could not extract file name");
    }
}
