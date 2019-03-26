1. Run Elasticsearch before start Search Server
+ cd /Elasticsearch/bin && ./elasticsarch
2. Build Search Server with maven
+  mvn clean install
3. Run Search Server
+ java -Dnetworkaddress.cache.ttl=60 -jar -Dspring.profiles.activate=production $ES_HOME/target/search.jar

4 Index multiple movies
- API: POST v1/movies/indexing
- DATA:
 
 {
         "indexId": "JqhwZGgBPVpd0r_LnRg-",
         "docId": 1547105335209,
         "name": "Avengers 2: Đế Chế Ultron",
         "link": "https://fimfast.com/avengers-age-of-ultron",
         "image": "https://icdn.fimfast.com/ff/2018/09/27/05b7922f8e1b6504_998729a4ac1df0be_40039153804699473.jpg",
         "viewCount": 1447873,
         "similar": [
             "Hành Động",
             "Phiêu Lưu",
             "Viễn Tưởng"
         ],
         "director": "",
         "rating": 7.4,
         "votes": 610463,
         "year": 2015,
         "liveLink": "https://fimfast.com/avengers-age-of-ultron",
         "duration": "141 Phút",
         "description": "Avengers: Age of Ultron lấy khởi đầu từ nhân vật Tony Stark - siêu anh hùng Iron Man. Khi chàng tỷ phú tái khởi động dự án gìn giữ hòa bình bị ngưng hoạt động từ l...",
         "country": "Mỹ",
         "englishName": "Tên tiếng Anh: Avengers 2: Age of Ultron",
         "torrent": "",
         "tags": [
             "avengers 2 de che ultron",
             "avengers 2 de che ultron full"
         ],
         "actors": [
            "Samuel L. Jackson"
         ]
     }
- Response: index success

5 Query movies
- API: POST v1/movies/search?query=""?from=""
- Response : 
        {
            "totalHits": 95,
            "maxScore": 16.976757,
            "documents": [
                {
                    "version": -1,
                    "score": 16.976757,
                    "payload": {
                        "englishName": "A Great Way To Care 2 (2013)",
                        "image": "http://cdn.bomtan.org/images/film/giai-ma-nhan-tam-2-8035.jpg",
                        "country": "Phim Hồng Kông",
                        "similar": [
                            "phim tâm lý",
                            "phim hình sự"
                        ],
                        "year": 2013,
                        "docId": 1548433503606,
                        "director": [
                            "La Vĩnh Hiền "
                        ],
                        "link": "http://bomtan.org/phim-giai-ma-nhan-tam-2-8035.html",
                        "torrent": [],
                        "rating": "5",
                        "description": "Phim Giải Mã Nhân Tâm 2: Phim Giải Mã Nhân Tâm 2 tiếp tục thành công của phần 1, trong phần phim Giải Mã Nhân Tâm 2 này bác sĩ Cao Lập Nhân sẽ có mối tình tay ba với hai nhân vật do Mông Gia Tuệ và ...",
                        "tags": [
                            "giải mã nhân tâm 2 vietsub",
                            "giải mã nhân tâm 2 thuyết minh",
                            "giải mã nhân tâm 2 trọn bộ",
                            "xem phim giải mã nhân tâm 2",
                            "a great way to care 2"
                        ],
                        "duration": "25 Tập (43 phút / tập)",
                        "actors": [
                            "Dương Di",
                            " Mông Gia Tuệ",
                            " Phương Trung Tín"
                        ],
                        "domain": "bomtan.org",
                        "name": "Giải Mã Nhân Tâm 2",
                        "liveLink": "http://bomtan.org/xem-phim/giai-ma-nhan-tam-2-8035/tap-1.html",
                        "votes": 8,
                        "viewCount": 63874
                    }
                }
            ]
        }

6. Filter Movies
- API: GET v1/movies/filter?from=0&country=Hồng Kông&year=2013&similar=Phim tâm lý&domain=www.phimmoi.net
- Response
    {
        "totalHits": 95,
        "maxScore": 16.976757,
        "documents": [
            {
                "version": -1,
                "score": 16.976757,
                "payload": {
                    "englishName": "A Great Way To Care 2 (2013)",
                    "image": "http://cdn.bomtan.org/images/film/giai-ma-nhan-tam-2-8035.jpg",
                    "country": "Phim Hồng Kông",
                    "similar": [
                        "phim tâm lý",
                        "phim hình sự"
                    ],
                    "year": 2013,
                    "docId": 1548433503606,
                    "director": [
                        "La Vĩnh Hiền "
                    ],
                    "link": "http://bomtan.org/phim-giai-ma-nhan-tam-2-8035.html",
                    "torrent": [],
                    "rating": "5",
                    "description": "Phim Giải Mã Nhân Tâm 2: Phim Giải Mã Nhân Tâm 2 tiếp tục thành công của phần 1, trong phần phim Giải Mã Nhân Tâm 2 này bác sĩ Cao Lập Nhân sẽ có mối tình tay ba với hai nhân vật do Mông Gia Tuệ và ...",
                    "tags": [
                        "giải mã nhân tâm 2 vietsub",
                        "giải mã nhân tâm 2 thuyết minh",
                        "giải mã nhân tâm 2 trọn bộ",
                        "xem phim giải mã nhân tâm 2",
                        "a great way to care 2"
                    ],
                    "duration": "25 Tập (43 phút / tập)",
                    "actors": [
                        "Dương Di",
                        " Mông Gia Tuệ",
                        " Phương Trung Tín"
                    ],
                    "domain": "bomtan.org",
                    "name": "Giải Mã Nhân Tâm 2",
                    "liveLink": "http://bomtan.org/xem-phim/giai-ma-nhan-tam-2-8035/tap-1.html",
                    "votes": 8,
                    "viewCount": 63874
                }
            }
        ]
    }
 
7. Update movies
-API: PUT v1/movies/update?docId=""
-DATA: data like data post
-Response: update success

8 Delete Document
API: DELETE v1/movies/delete?id=""

9 Suggestion
API: GET v1/movies/suggest?q=""


====> Search music: change movies in API = musics# search
