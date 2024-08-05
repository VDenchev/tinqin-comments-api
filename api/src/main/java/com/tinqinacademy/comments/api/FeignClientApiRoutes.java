package com.tinqinacademy.comments.api;

public class FeignClientApiRoutes {

  public static final String GET_COMMENTS = "GET  " + RestApiRoutes.GET_COMMENTS +
      "?pageNumber={pageNumber}&pageSize={pageSize}";
  public static final String CREATE_COMMENT = "POST " + RestApiRoutes.CREATE_COMMENT;
  public static final String UPDATE_COMMENT = "PUT " + RestApiRoutes.UPDATE_COMMENT;
  public static final String UPDATE_COMMENT_BY_ADMIN = "PUT " + RestApiRoutes.UPDATE_COMMENT_BY_ADMIN;
  public static final String DELETE_COMMENT = "DELETE " + RestApiRoutes.DELETE_COMMENT;
}
