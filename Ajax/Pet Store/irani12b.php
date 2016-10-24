<?php
      $con = mysql_connect("localhost","iranipaimanf697","") or
            die("Failed to connect to database " . mysql_error());
      $branchid = $_GET["branchid"];
      mysql_select_db("iranipaimanf697", $con);
      $sql_command = "SELECT SMID,BRANCHID,SALEID " .
                  " FROM SALEMADE WHERE BRANCHID =" . $branchid . ";";
      $result = mysql_query($sql_command);
      $returnValue = "";
      while($row = mysql_fetch_array($result))
      {
            $returnValue .= "<TR>";
            $returnValue .= "<TD>" . $row['SMID'] . "</TD>" ;
            $returnValue .= "<TD>" . $row['BRANCHID'] . "</TD>" ;
            $returnValue .= "<TD>" . $row['SALEID'] . "</TD>" ;
            $returnValue .= "</TR>";
      }
      echo $returnValue;
      mysql_close($con);
?>

 