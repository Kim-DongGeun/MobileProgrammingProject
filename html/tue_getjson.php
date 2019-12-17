<?php 
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');
        
    
    $platform = $_GET['platform'];

    if($platform == 'Naver'){
        $stmt = $con->prepare('select * from naver_serial_webtoon_thumb where week="1"');

        $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array(); 
        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
            array_push($data, 
                array('title'=>$title,
                'semi_title'=>$semi_title,
                'author'=>$author,
                'image'=>$image,
                'week'=>$week
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("naver_serial_webtoon_thumb"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }
    }
    else{
        $stmt = $con->prepare('select * from daum_webtoon_thumb where weekend="tue"');

        $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array(); 
        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
            array_push($data, 
                array('title'=>$title,
                'author'=>$author,
                'image'=>$thumb,
                'week'=>$weekend
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("naver_serial_webtoon_thumb"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }
    }

    
?>

