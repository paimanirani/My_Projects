<?php  
      $con = mysql_connect("localhost","iranipaimanf697","") or
            die("Failed to connect to database " . mysql_error());
mysql_select_db("iranipaimanf697", $con);
$id = $_GET['id'];
      $name = $_GET['name'];
      $address = $_GET['address'];
      $phone = $_GET['phone'];
      $email = $_GET['email'];
echo "<script type='text/javascript'>alert('$id');</script>";
echo "<script type='text/javascript'>alert('$name');</script>";
echo "<script type='text/javascript'>alert('$address');</script>";
echo "<script type='text/javascript'>alert('$phone');</script>";
echo "<script type='text/javascript'>alert('$email');</script>";
 $sql_command = "UPDATE BRANCH SET NAME ='".$name."',ADDRESS='".$address."',PHONE='".$phone."',EMAIL='".$email."'WHERE BRANCHID=".$id.";"; 
      if (mysql_query($sql_command))
          echo "<script type='text/javascript'>alert('Data Updated');</script>";
      else
          echo "<script type='text/javascript'>alert('Failed to update');</script>";
      mysql_close($con);
?>

