<html>
    <head>
    </head>
    <body>
        
<!--        解析傳到 view 的變數，只有單筆記錄，不是陣列-->
        @foreach ($names as $name)
        <h1>{{ $name }}</h1>
        @endforeach
        
    </body>
</html>