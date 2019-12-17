<?php 
    ini_set('memory_limit', '-1');
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $title = $_GET['title'];
    $semi_title = $_GET['semi_title'];

    $stmt = $con->prepare("select * from naver_serial_webtoon where title = '$title' and semi_title = '$semi_title'");   
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array(); 
        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
            array_push($data, 
                array(
                'title'=>$title,
                'semi_title'=>$semi_title,
		'image_data'=>base64_encode($image_data)
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("naver_serial_webtoon"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;

    }
?>
