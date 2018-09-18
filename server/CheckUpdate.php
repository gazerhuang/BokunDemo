<?php
$a = '2';
$data = array('code' => 1, 'msg' => '检测到版本更新，修复了不存在的问题，并把版本号改成了2', 'lastVision' => $a, 'address' => 'http://www.gazerhuang.top/downloads/'. $a .'.apk');  
$fail = array('code' => 2, 'msg' => '错误');  
$res_success = json_encode($data);  
$res_fail = json_encode($fail); 
header('Content-Type:application/json');

echo $res_success;
?>