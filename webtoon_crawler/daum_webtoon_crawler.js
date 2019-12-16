let request = require('request');
let cheerio = require('cheerio');
let fs = require('fs');

var titles = []
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

const getImageUrls = (articleId, no) => {
    request({url: `http://webtoon.daum.net/data/pc/webtoon/viewer_images/${articleId}`}, function (error, response, body) {
        body = JSON.parse(body);
        console.log(body)
        //for(let i = 0; i < body.data.length; i++)
        //    downloadImage(body.data[i].url, no, i);
    });
}

const getArticlesIds = (url) => {
    request({url: url}, function (error, response, body) {
        body = JSON.parse(body);
        
        var sql = "insert into daum_webtoon_thumb (title, author, thumb) values ?";
        var values = [];
        
        for(let i = 0; i < body.data.length; i++){
            var value = [];
            value.push(body.data[i].title);
            titles.push(body.data[i].title);
            console.log(body.data[i].title)
            value.push(body.data[i].cartoon.artists[0].name);
            console.log(body.data[i].cartoon.artists.name);
            value.push(body.data[i].pcRecommendImage.url);
            console.log(body.data[i].pcRecommendImage.url);
            values.push(value);
        }
        connection.query(sql, [values], function (err, result) {
            if (err) throw err;
            console.log("Number of records inserted: " + result.affectedRows);
          });

        console.log("complete");



        /*for(let i = 0; i < body.data.webtoon.webtoonEpisodes.length; i++) {
            if(body.data.webtoon.webtoonEpisodes[i].serviceType === "free")
                getImageUrls(body.data.webtoon.webtoonEpisodes[i].id, body.data.webtoon.webtoonEpisodes[i].episode);
        }*/
    });
}


var weekend = ['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun']
for(var week in weekend){
    getArticlesIds("http://webtoon.daum.net/data/pc/webtoon/view/".concat(weekend[week]));
}
