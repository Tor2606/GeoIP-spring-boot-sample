
<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title>Test CountryBrowserInfo server</title>
  <script src="http://code.jquery.com/jquery-3.0.0.min.js" type="text/javascript" ></script>
</head>


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
                .append('Browser: ' + json.userAgent + '<br/>');
      });
    })
  });

</script>
</html>
