<DOCTYPE html>
<html lang="da">
<head>
  <meta meta http-equiv="Content-Type" 
content="text/html;charset=ISO-8859-10"/>
<style>
  
/*@font-face {
  font-family: '8bit';
  src: url(fonts/prstartk.ttf);
}*/

table {
  font-size: 38px;
  color: #1A237E;
  font-family: '8bit', sans-serif;
  width: 100%;
  border-collapse: collapse;
  text-align: center;
  background-color: #DDDDDD;
}

table, td, th, tr {
  height: 15%;
  text-align: center;
  border: 5px outset green;
  
}
</style>
</head>
<body>
  
  <?php
  $servername = "http://80.240.129.72/phpmyadmin/";
  $username = "root";
  $password = "987654321";
  $dbname = "madkasse";

  

  // Create connection
  $conn = new mysqli($servername, $username, $password, $dbname);

  // Check connection
  if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    }
  mysqli_select_db($conn, $dbname);
  $sql="SELECT brugernavn, highscore FROM brugerinfo ORDER BY highscore DESC limit 10";
  $result = mysqli_query($conn,$sql);
  
  echo "<table>
  <tr>
  <th>#</th>
  <th>Username</th>
  <th>Highscore</th>
  </tr>";
$i = 1;
while($row = mysqli_fetch_array($result)) {
  echo "<tr>";
  echo "<td>" . $i . "</td>";
  echo "<td>" . $row['brugernavn'] . "</td>";
  echo "<td>" . $row['highscore'] . "pts</td>";
  echo "</tr>";
  $i++;
}
echo "</table>";

$conn->close();
?>
</body>
</html>