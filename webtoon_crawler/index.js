var mysql      = require('mysql');
var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : 'asdasd2134',
  port     : 3306,
  database : 'my_db'
});

connection.connect();

connection.query('SELECT * from daum_webtoon_thumb', function(err, rows, fields) {
  if (!err)
    console.log('The solution is: ', rows);
  else
    console.log('Error while performing Query.', err);
});

connection.end();