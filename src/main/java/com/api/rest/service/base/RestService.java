package com.api.rest.service.base;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RestService<T> {


    public ResponseEntity<List<T>> getAll();

    public ResponseEntity<T> get(Integer id);

    public ResponseEntity<T> add(T entity, HttpServletRequest request);

    public ResponseEntity<T> patchUpdate(Integer id, T entityUpdates);

    public ResponseEntity<T> putUpdate(Integer id, T entityUpdates);

    public ResponseEntity<T> delete(Integer id);


}
