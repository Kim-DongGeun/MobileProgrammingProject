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

 const getImageUrlInside = (URL) => {
  console.log(URL);
  request({url: URL}, function (error, response, body) {
      body = JSON.parse(body);
      var title = "";
      var semi_title = "";
      var inside = "";
      for(let i = 0; i < body.data.webtoon.webtoonEpisodes.length; i++) {
          if(body.data.webtoon.webtoonEpisodes[i].serviceType === "free"){
              title = body.data.webtoon.title;
              semi_title = body.data.webtoon.webtoonEpisodes[i].title;
              inside = body.data.webtoon.webtoonEpisodes[i].thumbnailImage;
              Value.push(title);
              Value.push(semi_title);
              Value.push(inside);
              connection.query("insert into daum_webtoon_inside (title, author, inside) values (?, ?, ?)", Value, function (err, result) {
                  if (err) throw err;
                  console.log("Number of records inserted: " + result.affectedRows);
                });
              //getImageUrls(body.data.webtoon.webtoonEpisodes[i].id, title, semi_title);
          }
           
      }
      //for(let i = 0; i < body.data.length; i++)
      //    downloadImage(body.data[i].url, no, i);
  });
}

 connection.query("select nickname from daum_webtoon_thumb", function (err, result) {
  if (err) throw err;
  console.log("Number of records inserted: " + result.affectedRows);
  for(index in result){
    console.log("http://webtoon.daum.net/data/pc/webtoon/view/".concat(result[index].nickname));
    getImageUrlInside("http://webtoon.daum.net/data/pc/webtoon/view/".concat(result[index].nickname));
  }

});