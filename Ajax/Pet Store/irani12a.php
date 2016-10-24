<?php
      $con = mysql_connect("localhost","iranipaimanf697","") or
            die("Failed to connect to database " . mysql_error());
      mysql_select_db("iranipaimanf697", $con);
      $sql_command = "SELECT * FROM BRANCH";
      $result = mysql_query($sql_command);
      $returnValue = "<p>Branch:<SELECT id='branchSelect'>";
      while($row = mysql_fetch_array($result))
      {
            $returnValue .= "<OPTION VALUE='" . $row['BRANCHID'] . "'>" . $row['NAME'] . "</OPTION>";
      }
      $returnValue .= "</SELECT></p>";
      echo $returnValue;
      mysql_close($con);
?>