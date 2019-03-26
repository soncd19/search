package com.home.search.controller;

import com.home.search.response.Result;
import com.home.search.json.JSONValidate;
import com.home.search.responsitory.MusicRepository;
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
 * Created by soncd on 21/01/2019
 */

@RestController
public class MusicController {

    @Autowired
    private MusicRepository musicRepository;

    @PostMapping(value = "/musics/indexing", consumes = "application/json; charset=utf-8")
    public ResponseEntity indexMusic(@RequestBody String data) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("musics");
            List<JSONObject> jsonMusics = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonData = JSONValidate.validate(jsonArray.getJSONObject(i), "docId", "name", "singers", "composed", "lyric", "topic",
                            "link", "image", "viewCount", "votes", "similar", "publishedAt", "country");
                    jsonMusics.add(jsonData);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            musicRepository.indexMusic(jsonMusics);
            return ResponseEntity.ok().body("index success");
        } catch (Exception e) {
            log.error("index musics error");
            return ResponseEntity.badRequest().body("builder musics error = " + e.getMessage());
        }
    }

    @GetMapping(value = "/musics/search", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> searchMusic(@RequestParam("query") String query, @RequestParam("from") int from) throws Exception {
        try {
            Result musics = musicRepository.queryMusics(query, from);
            return musics.getTotalHits() > 0 ? ResponseEntity.ok().body(musics) : ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("get music error:" + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/musics/filter", consumes = "application/json; charset=utf-8")
    public ResponseEntity<Result> filterMovies(@RequestParam(name = "from", defaultValue = "0") int from,
                                               @RequestParam(name = "singer", defaultValue = "") String singer,
                                               @RequestParam(name = "country", defaultValue = "") String country,
                                               @RequestParam(name = "topic", defaultValue = "") String topic,
                                               @RequestParam(name = "composed", defaultValue = "") String composed,
                                               @RequestParam(name = "album", defaultValue = "") String album) {
        try {
            Result result = musicRepository.filterMusics(from, singer, country, topic, composed);
            return result.getTotalHits() > 0 ? ResponseEntity.ok().body(result) : ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/musics/suggest", consumes = "application/json; charset=utf-8")
    public ResponseEntity<List<String>> suggest(@RequestParam("query") String query) {
        try {
            return ResponseEntity.ok().body(musicRepository.suggestMusic(query));
        } catch (Exception e) {
            log.error("API Suggest music error= " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/testhome", consumes = "application/json; charset=utf-8")
    public ResponseEntity test(@RequestBody String data) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(data);
            System.out.println(jsonObject);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            log.error("index musics error");
            return ResponseEntity.badRequest().body("builder musics error = " + e.getMessage());
        }
    }

    private final Logger log = LoggerFactory.getLogger(MusicController.class);
}
