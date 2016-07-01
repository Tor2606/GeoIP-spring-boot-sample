
<%--
  Created by IntelliJ IDEA.
  User: Igor
  Date: 29.06.2016
  Time: 18:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<script src="http://code.jquery.com/jquery-3.0.0.min.js" type="text/javascript" ></script>



<body>

<h1>Тест GeoIp</h1>

<h3>Enter id</h3><br />
<input type="text" id="ip"/><br /><br />

<button id="load">Load bcinfo</button><br /><br />

<div id="results">Answer</div>

</body>


<script type="text/javascript">

  $(document).ready(function(){
    $('#load').click(function(){
      var ip = document.getElementById("ip").value;
      $.get('test/ajax?ip='.concat(ip), {}, function(json){  // загрузку JSON данных из файла example.json

        $('#results').html('');
        // заполняем DOM элемент данными из JSON объекта
        $('#results').append('Country: '   + json.countryCode + '<br/>')
                .append('Browser: ' + json.browserInfo + '<br/>');
      });
    })
  });

</script>
</html>
