<html>
<head>
<script src="jquery-1.11.1.js"></script>
<script>
$(document).ready(function(){
    $("#edit").click(function() {
var id = $(".classID").attr("id");
        $.get("edit.php", {id:id, name:$('#Name').val(), address:$('#Address').val(),
                          phone:$('#Phone').val(), email:$('#Email').val()
                         },
            function(result){
                  $("#div1").html(result);
            });
    });
});
</script>
</head>
<?php  
      $con = mysql_connect("localhost","iranipaimanf697","") or
            die("Failed to connect to database " . mysql_error());
 $output = '';  
mysql_select_db("iranipaimanf697", $con);
      $sql_command = "SELECT * FROM BRANCH;";  
 $result = mysql_query($sql_command); 
echo '<form name="Edit" method="post" action="edit.php">
            <table border="2" cellspacing="1" cellpadding="0">
                <tr>
                    <th>Branch ID</th>
                    <th>Name</th>
                    <th>Address</th>
                    <th>Phone</th>
                   <th>Email</th>
                   <th>Button</th>
                </tr>';
while ($row = mysql_fetch_assoc($result)) {
    echo '<tr>
                 <td><p class ="classID" id="'.$row['BRANCHID'].'"><strong> '.$row['BRANCHID'].'</p></td>
                <td><input name="Name" type="text" id="Name" value="'.$row['NAME'].'"></td>
                <td><input name="Address" type="text" id="Address" value="'.$row["ADDRESS"].'"></td>
                <td><input name="Phone" type="text" id="Phone" value="'.$row['PHONE'].'"></td>
                <td><input name="Email" type="text" id="Email" value="'.$row['EMAIL'].'"></td>
               <td ><input type="button" id="edit" value="Edit"></td>
            </tr>';
}
echo     ' </table>
        </form><div id="div1"></div>';
 mysql_close($con);
 ?>  
</html>