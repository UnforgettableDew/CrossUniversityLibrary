package com.crossuniversity.securityservice.utils;

public class SwaggerConstant {

    public static final String SUCCESSFUL_DESCRIPTION =
            "The request was successful. " +
                    "The server has processed the request and the operation was completed without errors.";

    public static final String CREATED_DESCRIPTION = "The resource has been successfully created. " +
            "The server has processed the request to create a new resource, " +
            "and the operation was completed without errors.";


    public static final String NO_CONTENT_DESCRIPTION = "The request was successful, " +
            "but there is no additional content to return. " +
            "The server has processed the request, and no response body is provided.";

    public static final String BAD_REQUEST_DESCRIPTION =
            "The server could not understand the request due to invalid or malformed parameters.";

    public static final String UNAUTHORIZED_DESCRIPTION =
            "Access to this resource requires authentication. " +
                    "Please log in to your account to gain the necessary permissions and access the requested resource.";

    public static final String FORBIDDEN_DESCRIPTION =
            "Access to the requested resource is forbidden. " +
                    "The authenticated user lacks the necessary permissions to perform the requested operation.";

    public static final String NOT_FOUND_DESCRIPTION =
            "The requested resource could not be found on the server. " +
                    "No data exists for this specific request. " +
                    "Please verify the URL and ensure that the resource exists.";

    public static final String BAD_REQUEST_EXCEPTION =
            """
                    {
                      "message": "Bad request example message",
                      "httpStatus": "BAD_REQUEST",
                      "timestamp": "2024-01-04T11:57:16.835Z",
                      "path": "http://exception/example/path"
                    }
                    """;

    public static final String UNAUTHORIZED_EXCEPTION =
            """
                    {
                      "message": "Unauthorized example message",
                      "httpStatus": "UNAUTHORIZED",
                      "timestamp": "2024-01-04T11:57:16.835Z",
                      "path": "http://exception/example/path"
                    }
                    """;

    public static final String FORBIDDEN_EXCEPTION =
            """
                    {
                      "message": "Forbidden example message",
                      "httpStatus": "FORBIDDEN",
                      "timestamp": "2024-01-04T11:57:16.835Z",
                      "path": "http://exception/example/path"
                    }""";

    public static final String NOT_FOUND_EXCEPTION =
            """
                    {
                      "message": "Not found example message",
                      "httpStatus": "NOT_FOUND",
                      "timestamp": "2024-01-04T11:57:16.835Z",
                      "path": "http://exception/example/path"
                    }""";

    public static final String LIBRARY_TEACHER_EXAMPLE =
            """
                    {
                      "title": "library1_title",
                      "topic": "History",
                      "libraryAccess": false
                    }
                    """;

    public static final String LIBRARY_STUDENT_EXAMPLE =
            """
                    {
                      "title": "library1_title",
                      "topic": "History"
                    }
                    """;

    public static final String LIBRARY_EXAMPLE =
            """
                    {
                          "id": 3,
                          "title": "library1_title",
                          "topic": "History",
                          "libraryAccess": false
                        }""";


    public static final String DOCUMENT_EXAMPLE =
            """
                    {
                        "id": 4,
                        "title": "Document 4",
                        "topic": "Math Science",
                        "description": "Description for Document 4",
                        "owner": {
                          "id": 1,
                          "userName": "student1_name",
                          "email": "student1kpi@kpi.ua"
                        }
                      }""";

    public static final String UNIVERSITY_REQUEST_EXAMPLE =
            """
                    {
                      "title": "title_example",
                      "domain": "domain.example"
                    }""";

    public static final String UNIVERSITY_FULL_EXAMPLE =
            """
                    {
                      "id": 1,
                      "title": "title_example",
                      "domain": "domain.example"
                    }""";

    public static final String DOCUMENT_UPDATE_EXAMPLE =
            """
                    {
                        "id": 4,
                        "title": "Document 4",
                        "topic": "Math Science",
                        "description": "Description for Document 4"
                      }""";
    public static final String LIBRARY_LIST_EXAMPLE =
            """
                    {
                      "libraries": [
                        {
                          "id": 3,
                          "title": "Student1_library1",
                          "topic": "History",
                          "libraryAccess": false
                        },
                        {
                          "id": 5,
                          "title": "Student2_library2",
                          "topic": "Math",
                          "libraryAccess": false
                        }
                      ]
                    }""";

    public static final String DOCUMENT_LIST_EXAMPLE =
            """
                    [
                      {
                        "id": 4,
                        "title": "Document 4",
                        "topic": "Math Science",
                        "description": "Description for Document 4",
                        "owner": {
                          "id": 1,
                          "userName": "student1_name",
                          "email": "student1kpi@kpi.ua"
                        }
                      },
                      {
                        "id": 5,
                        "title": "Document 5",
                        "topic": "Math",
                        "description": "Description for Document 5",
                        "owner": {
                          "id": 2,
                          "userName": "teacher1_name",
                          "email": "teacher1kpi@kpi.ua"
                        }
                      }
                    ]""";
    public static final String BRIEF_PROFILE_LIST_EXAMPLE =
            """
                    [
                      {
                        "id": 1,
                        "userName": "teacher1_name",
                        "email": "teacher1kpi@kpi.ua"
                      },
                      {
                        "id": 2,
                        "userName": "student1_name",
                        "email": "student1kpi@kpi.ua"
                      }
                    ]
                    """;

    public static final String JWT_TOKEN_EXAMPLE =
            """
                    {
                      "access_token": "access_token_example",
                      "refresh_token": "refresh_token_example"
                    }""";

    public static final String CREDENTIALS_EXAMPLE =
            """
                    {
                      "email": "email_example@kpi.ua",
                      "password": "password_example"
                    }""";

    public static final String USER_PROFILE_EXAMPLE =
            """
                    {
                      "id": 1,
                      "userName": "User Name",
                      "email": "email@example.com",
                      "space": 1024,
                      "university": {
                        "id": 1,
                        "title": "university_title",
                        "domain": "university_domain"
                      },
                      "ownLibraries": [
                        {
                          "id": 3,
                          "title": "library1_title",
                          "topic": "library1_topic",
                          "libraryAccess": false
                        }
                      ],
                      "subscribedLibraries": [
                          {
                            "id": 2,
                            "title": "library2_title",
                            "topic": "library2_topic",
                            "libraryAccess": true
                          }
                        ]
                    }""";

}
