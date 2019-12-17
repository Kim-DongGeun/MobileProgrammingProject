<?php 
    ini_set('memory_limit', '-1');
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $title = $_GET['title'];
    $platform = $_GET['platform'];


    if($platform == 'Naver'){
        $stmt = $con->prepare("select * from naver_serial_webtoon_inside where title='$title'");
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
                    'image_data'=>$image_data,
                ));
            }
    
            header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("naver_serial_webtoon_inside"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
    else {

    }
        
    $stmt = $con->prepare("select * from daum_webtoon_inside where title='$title'");
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
		        'image_data'=>$inside,
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("naver_serial_webtoon_inside"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }
?>

