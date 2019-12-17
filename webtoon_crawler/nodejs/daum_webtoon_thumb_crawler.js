let request = require('request');
let cheerio = require('cheerio');
//let fs = require('fs');

 var mysql      = require('mysql');
 var connection = mysql.createConnection({
   host     : 'localhost',
   user     : 'root',
   password : 'asdasd2134',
   port     : 3306,
   database : 'my_db'
 });
 connection.connect();
 const downloadImage = (url, no, i) => {
     request(
         {
             url: url,
             encoding: null
         }, function (error, response, body) {
         fs.writeFile(`${no}_${i}.jpg`, body, null, (err) => {
             if (err) throw err;
             console.log('The file has been saved!');
         });
     });
 }
 const getImageUrls = (articleId, title, semi_title) => {
     request({url: `http://webtoon.daum.net/data/pc/webtoon/viewer_images/${articleId}`}, function (error, response, body) {
         if(error) throw error;
         body = JSON.parse(body);
         for(let i = 0; i < body.data.length; i++){
             let Value = [title, semi_title];
             Value.push(body.data[i].url);
             connection.query("insert into daum_webtoon (title, semi_title, image) values (?, ?, ?)", Value, function (err, result) {
                 if (err) throw err;
                 console.log("Number of records inserted: " + result.affectedRows);
               });
         }
     });
 }
 const getImageUrlInside = (URL) => {
     request({url: URL}, function (error, response, body) {
         body = JSON.parse(body);
         var title = "";
         var semi_title = "";
         var inside = "";
         let Value = [];
         for(let i = 0; i < body.data.webtoon.webtoonEpisodes.length; i++) {
             if(body.data.webtoon.webtoonEpisodes[i].serviceType === "free"){
                 title = body.data.webtoon.title;
                 semi_title = body.data.webtoon.webtoonEpisodes[i].title;
                 inside = body.data.webtoon.webtoonEpisodes[i].thumbnailImage.url;
                 Value.push(title);
                 Value.push(semi_title);
                 Value.push(inside);
                 setTimeout(function(){
                    getImageUrls(body.data.webtoon.webtoonEpisodes[i].id, title, semi_title);
                   }
                    , 2000);
                 

                 /* connection.query("insert into daum_webtoon_inside (title, semi_title, inside) values (?, ?, ?)", Value, function (err, result) {
                     if (err) throw err;
                     console.log("Number of records inserted: " + result.affectedRows);
                   }); */
                //getImageUrls(body.data.webtoon.webtoonEpisodes[i].id, title, semi_title);
                
             }
              
         }
         /* for(let i = 0; i < body.data.length; i++)
             getImageUrls(body.data[i].url , i); */
     });
 }
 const getArticlesIds = (url, week) => {
     request({url: url}, function (error, response, body) {
         body = JSON.parse(body);
         
         var sql = "insert into daum_webtoon_thumb (title, nickname, author, thumb, weekend) values ?";
         var values = [];
         for(let i = 0; i < body.data.length; i++){
             if(body.data.ageGrade != 19){
                 var value = [];
                 var URL = "http://webtoon.daum.net/data/pc/webtoon/view/";
                 value.push(body.data[i].title);
                 value.push(body.data[i].nickname);
                 URL = URL.concat("", body.data[i].nickname);
                 setTimeout(function(){
                     getImageUrlInside(URL);
                    }
                     , 2000);
                 
                 let author = "";
                 for(let j = 0 ; j < body.data[i].cartoon.artists.length; j++){
                     author = author.concat(body.data[i].cartoon.artists[j].name);
                     author = author.concat(" ");
                 }
                 value.push(author);
                 value.push(body.data[i].pcRecommendImage.url);
                 value.push(week)
                 values.push(value);
             }
         }
         /* connection.query(sql, [values], function (err, result) {
             if (err) throw err;
             console.log("Number of records inserted: " + result.affectedRows);
           }); */
         console.log("complete");
         /*for(let i = 0; i < body.data.webtoon.webtoonEpisodes.length; i++) {
             if(body.data.webtoon.webtoonEpisodes[i].serviceType === "free")
                 getImageUrls(body.data.webtoon.webtoonEpisodes[i].id, body.data.webtoon.webtoonEpisodes[i].episode);
         }*/
     });
 }

 var weekend = ['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun']
 var titles = [];
 for(var week in weekend){
     setTimeout(() => {
        getArticlesIds("http://webtoon.daum.net/data/pc/webtoon/list_serialized/".concat(weekend[week]), weekend[week]);    
     }, 2000);
     
 }
 



