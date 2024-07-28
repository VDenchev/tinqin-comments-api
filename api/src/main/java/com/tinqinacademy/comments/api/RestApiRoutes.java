package com.tinqinacademy.comments.api;

public class RestApiRoutes {

  public static final String ROOT = "/api/v1";

  public static final String HOTEL = ROOT + "/hotel";
  public static final String SYSTEM = ROOT + "/system";
  public static final String COMMENT = "/comment";

  public static final String GET_COMMENTS = HOTEL + "/{roomId}" + COMMENT;
  public static final String CREATE_COMMENT = HOTEL + "/{roomId}" + COMMENT;
  public static final String UPDATE_COMMENT = HOTEL + COMMENT + "/{commentId}";
  public static final String UPDATE_COMMENT_BY_ADMIN = SYSTEM + COMMENT + "/{commentId}";
  public static final String DELETE_COMMENT = SYSTEM + "/comment/{commentId}";
}