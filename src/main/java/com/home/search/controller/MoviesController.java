package com.home.search.controller;

import com.home.search.response.Result;
import com.home.search.json.JSONValidate;
import com.home.search.responsitory.MoviesRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soncd on 07/12/2018
 */

@RestController
public class MoviesController {

    @Autowired
    private MoviesRepository moviesRepository;

    @PostMapping(value = "/movies/indexing", consumes = "application/json; charset=utf-8")
    public ResponseEntity indexMovies(@RequestBody String data) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("movies");
            List<JSONObject> jsonDocuments = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonData = JSONValidate.validate(jsonArray.getJSONObject(i), "docId", "name", "director",
                            "rating", "votes", "year", "link", "duration", "torrent", "image", "description", "similar", "tags", "actors");
                    jsonDocuments.add(jsonData);
                } catch (Exception e) {
                    log.error("create movies title = " + jsonObject.getString("name"));
                }
            }

            moviesRepository.indexMovies(jsonDocuments);
            return ResponseEntity.ok().body("builder success");

        } catch (Exception e) {
            log.error("builder document error = " + e.getMessage());
            return ResponseEntity.badRequest().body("builder documents error = " + e.getMessage());

        }
    }

    @PutMapping(value = "/movies/update", consumes = "application/json; charset=utf-8")
    public ResponseEntity updateMovie(@RequestParam("docId") long docId, @RequestBody String data) {
        try {
            JSONObject jsonMovies = JSONValidate.validate(data, "docId", "name", "director",
                    "rating", "votes", "year", "link", "duration", "torrent", "image", "description", "similar", "tags", "actors");
            moviesRepository.updateMovies(docId, jsonMovies);
            return ResponseEntity.ok().body("update movie success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/movies/updates", consumes = "application/json; charset=utf-8")
    public ResponseEntity updateMovies(@RequestBody String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("documents");
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonData = JSONValidate.validate(jsonArray.getJSONObject(i), "docId", "name", "director",
                            "rating", "votes", "year", "link", "duration", "torrent", "image", "description", "similar", "tags", "actors");
                    long docId = jsonData.getLong("docId");
                    moviesRepository.updateMovies(docId, jsonData);
                } catch (Exception e) {
                    log.error("create document title = " + jsonObject.getString("name"));
                }
            }
            return ResponseEntity.ok().body("update document success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/movies/search", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> searchMovies(@RequestParam("query") String query, @RequestParam("from") int from) {
        try {
            Result result = moviesRepository.queryMovies(query, from);
            return result.getTotalHits() > 0 ? ResponseEntity.ok().body(result) : ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/movies/filter", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> filterMovies(@RequestParam(name = "from", defaultValue = "0") int from,
                                               @RequestParam(name = "similar", defaultValue = "") String similar,
                                               @RequestParam(name = "country", defaultValue = "") String country,
                                               @RequestParam(name = "year", defaultValue = "") String year,
                                               @RequestParam(name = "domain", defaultValue = "") String domain) {
        try {
            Result result = moviesRepository.filterMovies(from, similar, country, year, domain);
            return result.getTotalHits() > 0 ? ResponseEntity.ok().body(result) : ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/movies/delete", consumes = "application/json; charset=utf-8")
    public ResponseEntity delete(@RequestParam("id") long id) {
        try {
            moviesRepository.deleteMovie(id);
            return ResponseEntity.ok().body("delete success");
        } catch (Exception e) {
            log.error("API DELETE error= " + e.getMessage());
            return ResponseEntity.badRequest().body("delete document error");
        }
    }

    @GetMapping(value = "/movies/suggest", consumes = "application/json; charset=utf-8")
    public ResponseEntity<List<String>> suggest(@RequestParam("query") String query) {
        try {

            return ResponseEntity.ok().body(moviesRepository.suggestMovies(query));
        } catch (Exception e) {
            log.error("API Suggest movies error= " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private final Logger log = LoggerFactory.getLogger(MoviesController.class);
}
