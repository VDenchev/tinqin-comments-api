package com.tinqinacademy.comments.api.exceptions;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String entityName, UUID id) {
    super(String.format("%s with an id of %s not found", entityName, id));
  }
}
