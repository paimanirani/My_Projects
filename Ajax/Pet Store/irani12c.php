<?php
      $con = mysql_connect("localhost","iranipaimanf697","")
            or die("Failed to connect to database");
      mysql_select_db("iranipaimanf697", $con);
      $bid = $_GET['bid'];
      $sid = $_GET['sid'];

      $sql_command ="INSERT INTO SALEMADE(BRANCHID,SALEID) VALUES(".$bid.",".$sid.");";
      if (mysql_query($sql_command))
           echo "<script type='text/javascript'>alert('Sale Made added');</script>";
      else
echo "<script type='text/javascript'>alert('Failed to insert');</script>";
      mysql_close($con);
?>
