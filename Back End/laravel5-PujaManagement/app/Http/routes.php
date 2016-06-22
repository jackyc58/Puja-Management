<?php
    

    /*************************************************/
    /*                  Puja member                  */
    /*************************************************/
    // Get New GID
    Route::post('puja/getNewGid', function () { 
        return Response::json(array('gid' => (String)Uuid::generate(4)));
    });
       
//    // Get household count by gid
//    Route::post('puja/getCountByGid', function () { 
//        //$gid;
//        $count = $usersDB::where('gid', 'like', $gid)->get()->count();
//        return Response::json(array('count' => $count));
//    });
        
    // get household list by gid
    Route::post('puja/getHouseholdList/', function () { 

        $gid = Request::input('gid');


        if (empty($gid))
            return Response::json(array('ret' => 'gid empty!'));
        
        $usersDB = new \App\Models\User;
        
        // check gid exist
        $count = $usersDB::where('gid', 'like', $gid)->get()->count();
        if ($count < 1)
            return Response::json(array());
        
        // get list
        $users = $usersDB::select('uid', 'gid', 'username', 'tel', 'address', "head")->where('gid' ,'like' , $gid )->get();
//        $users = $usersDB::select('uid', 'username')->where('gid' ,'like' , $gid )->get();

        return $users;
    });

    // get household user name by gid
    Route::post('puja/getHouseholdUsers/', function () { 

        $gid = Request::input('gid');


        if (empty($gid))
            return Response::json(array('ret' => 'gid empty!'));
        
        $usersDB = new \App\Models\User;
        
        // check gid exist
        $count = $usersDB::where('gid', 'like', $gid)->get()->count();
        if ($count < 1)
            return Response::json(array());
        
        // get list
        $users = $usersDB::select('username')->where('gid' ,'like' , $gid )->get();
//        $users = $usersDB::select('uid', 'username')->where('gid' ,'like' , $gid )->get();

        return $users;
    });
    
    
    // Add member
    Route::post('puja/addMember/', function () {

        // 
        $gid = Request::input('gid');
        $head = Request::input('head');
        $username = Request::input('username');
        $address = Request::input('address');
        $tel = Request::input('tel');
        $dead = Request::input('dead');
        $born_date = Request::input('born_date');
        $born_time = Request::input('born_time');
        $die_date = Request::input('die_date');
        $die_time = Request::input('die_time');
        $notes = Request::input('notes');
        $email = Request::input('email');
        
        
        // define
        $usersDB = new App\Models\User;

        // default
        $usersDB->uid = (String)Uuid::generate(4);
        $usersDB->password = bcrypt('');
        $usersDB->role_id = 3;
        $usersDB->seen = 0;
        $usersDB->valid = 0;
        $usersDB->confirmed = 0;
        
        // post input
        if (empty($gid)) 
            $usersDB->gid = (String)Uuid::generate(4);
         else 
            $usersDB->gid = $gid;
        
              
        $usersDB->head = $head;
        $usersDB->username = $username;
        $usersDB->address = $address;
        $usersDB->tel = $tel;
        $usersDB->dead = $dead;
        if (!empty($born_date))
            $usersDB->born_date = $born_date;
        
        $usersDB->born_time = $born_time;
        if (!empty($die_date))
            $usersDB->die_date = $die_date;
        
        $usersDB->die_time = $die_time;
        $usersDB->notes = $notes;
        $usersDB->email = $email;

//return Response::json(array(
//    'uid' => $usersDB->uid, 
//    'password' => $usersDB->password, 
//    'role_id' => $usersDB->role_id,
//    'seen' => $usersDB->seen,
//    'valid' => $usersDB->valid,
//    'confirmed' => $usersDB->confirmed,
//    'gid' => $usersDB->gid,
//    'head' => $usersDB->head,
//    'username' => $usersDB->username,
//    'address' => $usersDB->address,
//    'tel' => $usersDB->tel,
//    'dead' => $usersDB->dead,
//    'born_date' => $usersDB->born_date,
//    'born_time' => $usersDB->born_time,
//    'die_date' => $usersDB->die_date,
//    'die_time' => $usersDB->die_time,
//    'notes' => $usersDB->notes,
//    'email' => $usersDB->email 
//    ));
        
        // check gid exist
//        if ($gid != "") {
//            $count = $usersDB::where('gid', 'like', $usersDB->uid )->get()->count();
//            if ($count > 0)
//                return Response::json(array('ret' => 'gid exist!'));
//        }
            
//        
        // save
        $usersDB->save();
        
        // check save success
        $uidCount = $usersDB::where('uid', 'like', $usersDB->uid )->get()->count();
        $gidCount = $usersDB::where('gid', 'like', $usersDB->gid )->get()->count();
        if ($uidCount < 1)
            return Response::json(array('ret' => 'create user fail', 'gidCount' => '0'));
        else
            return Response::json(array('ret' => 'ok', 'gidCount' => (String)$gidCount, 'uid' => $usersDB->uid));

    });
    
    //
    // Modify member
    //
    Route::post('puja/modifyMemberItem', function () {
        
        $gid = Request::input('gid');
        $uid = Request::input('uid');
        $head = Request::input('head');
        $username = Request::input('username');
        $address = Request::input('address');
        $tel = Request::input('tel');
        $dead = Request::input('dead');
        $born_date = Request::input('born_date');
        $born_time = Request::input('born_time');
        $die_date = Request::input('die_date');
        $die_time = Request::input('die_time');
        $notes = Request::input('notes');
        $email = Request::input('email');

        if (empty($born_date))
            $born_date = NULL;
        
        if (empty($die_date))
            $die_date = NULL;
            
        $count = \App\Models\User::where('uid', 'like', $uid)->get()->count();
        
        
        
        if ($count == 1) {
            \App\Models\User::where('uid', 'like', $uid )->update([
                'head'      => $head,
                'username'  => $username,
                'address'   => $address,
                'tel'       => $tel,
                'dead'      => $dead,
                'born_date' => $born_date,
                'born_time' => $born_time,
                'die_date'  => $die_date,
                'die_time'  => $die_time,
                'notes'     => $notes,
                'email'     => $email
            ]);
            
            $gidCount = \App\Models\User::where('gid', 'like', $gid )->get()->count();
            
            return Response::json(array('ret' => 'ok', 'gidCount' => (String)$gidCount));
        } else if ($count > 1) {
            return Response::json(array('ret' => 'Fail: have more modify item.', 'gidCount' => '0'));
        } else
            return Response::json(array('ret' => 'Not found', 'gidCount' => '0'));
        
    });
        
    Route::post('puja/getMemberItem', function () {
        $uid = Request::input('uid');
             
        $pujas = \App\Models\User::select('username', 'email', 'head', 'address', 'tel', 'dead', 'born_date', 'born_time', 'die_date', 'die_time', 'notes')->where('uid' ,'like' ,$uid )->get();
                
        return $pujas;
    });

    Route::post('puja/deleteMemberItem', function () {
        $uid = Request::input('uid');
        $gid = Request::input('gid');
        
        $deletedRows = \App\Models\User::where('uid', 'like', $uid )->delete();
        
        $gidCount = \App\Models\User::where('gid', 'like', $gid )->get()->count();
        
        return Response::json(array('ret' => "OK", 'gidCount' => (String)$gidCount));
    });
    



    /*************************************************/
    /*                  Puja                         */
    /*************************************************/
    Route::post('puja/getPujaList', function () {
        $pujas = \App\Models\Puja::all()->toJson();

        return $pujas;
    });

    Route::post('puja/getPujaListSimplify', function () {
        $pujas = \App\Models\Puja::select('pid', 'pname', 'begin_date')->get()->toJson();

        return $pujas;
    });
    
    Route::post('puja/getPujaItem', function () {
        $pid = Request::input('pid');
             
        $pujas = \App\Models\Puja::select('pid', 'pname', 'begin_date', 'end_date', 'notes')->where('pid' ,'like' ,$pid )->get()->toJson();
                
        return $pujas;
    });
    
    Route::post('puja/addPuja', function () {
        $pname = Request::input('pname');
        $begin_date = Request::input('begin_date');
        $end_date = Request::input('end_date');
        $notes = Request::input('notes');

        // Add Item
        $pujaDB = new \App\Models\Puja;
        
        if (!empty($begin_date))
            $pujaDB->begin_date = $begin_date;
        
        if (!empty($end_date))
            $pujaDB->end_date = $end_date;

        $pujaDB->pname = $pname;
        $pujaDB->notes = $notes;

        $pujaDB->save();
        
        // get count
        $allCount = \App\Models\Puja::all()->count();
        
        return Response::json(array('ret' => 'ok', 'allCount' => (String)$allCount));
    });

    
    Route::post('puja/deletePujaItem', function () {
        $pid = Request::input('pid');
        
        $deletedRows = \App\Models\Puja::where('pid', 'like', $pid )->delete();
        
        $allCount = \App\Models\Puja::all()->count();
        
        return Response::json(array('ret' => "OK", 'allCount' => (String)$allCount));
    });


    //
    // Modify Puja Item
    //
    Route::post('puja/modifyPujaItem', function () {
        $pid = Request::input('pid');
        $pname = Request::input('pname');
        $begin_date = Request::input('begin_date');
        $end_date = Request::input('end_date');
        $notes = Request::input('notes');

        if (empty($begin_date))
            $begin_date = NULL;
        
        if (empty($end_date))
            $end_date = NULL;
            
        $pidCount = \App\Models\Puja::where('pid', 'like', $pid)->get()->count();
        
        
        
        if ($pidCount == 1) {
            \App\Models\Puja::where('pid', 'like', $pid )->update([
                'pname'         => $pname,
                'begin_date'    => $begin_date,
                'end_date'      => $end_date,
                'notes'         => $notes
            ]);
            
            $allCount = \App\Models\Puja::all()->count();
            
            return Response::json(array('ret' => 'ok', 'allCount' => (String)$allCount));
        } else
            return Response::json(array('ret' => 'Not found', 'allCount' => '0'));
        
    });    
    

    /*************************************************/
    /*                  Search                       */
    /*************************************************/
    Route::post('puja/search/', function () {
        $username = Request::input('username');
        
        $usersDB = new \App\Models\User;
        $s_ret = array();
        
        // check user exist
        $count = $usersDB::where('username', 'like', $username)->get()->count();
        if ($count < 1)
            return Response::json(array());
        
        // get Users list by username
        $users = $usersDB::select('uid', 'gid', 'username', 'address')->where('username' ,'like' , $username )->get();


        // get username
        foreach($users as $user)//從取得的資料中把每筆資料放到 $data 中
        {
            $heads = $usersDB::select('username', 'address')->where('gid' , 'like', $user->gid )->where('head' , 1)->get();
            
            // get address
            $head_username = "";
            $address = "";
            foreach($heads as $head) {
                $head_username = $head->username;
                $address = $head->address;
                
                if (empty($address))
                    $address = $user->address;
            }

            array_push($s_ret, array('uid' => $user->uid, 'gid' => $user->gid, 'username' => $user->username, 'head_username' => $head_username, 'address' => $address));
        }

        return Response::json($s_ret);

    });


    /*************************************************/
    /*                  Sign Up Items                */
    /*************************************************/
    // Add sign up item
    Route::post('puja/addSignUpItem/', function () {

        // 
        $pname = Request::input('pname');
        $signup_date = Request::input('signup_date');
        $signup_uid = Request::input('signup_uid');
        $signup_username = Request::input('signup_username');
        $gid = Request::input('gid');
        $item_id = Request::input('item_id');
        $item_name = Request::input('item_name');
        $item_liver = Request::input('item_liver');
        $item_dier = Request::input('item_dier');
        $item_money = Request::input('item_money');
        $notes = Request::input('notes');
        
      
        // define
        $signUpsDB = new App\Models\Signup;
        
        $signUpsDB->pname = $pname;
        $signUpsDB->signup_date = $signup_date;
        $signUpsDB->signup_uid = $signup_uid;
        $signUpsDB->signup_username = $signup_username;
        $signUpsDB->gid = $gid;
        $signUpsDB->item_id = $item_id;
        $signUpsDB->item_name = $item_name;
        
        if (!empty($item_liver))
            $signUpsDB->item_liver = $item_liver;
        
        if (!empty($item_dier))
            $signUpsDB->item_dier = $item_dier;
        
        if (!empty($item_money))
            $signUpsDB->item_money = $item_money;
        
        $signUpsDB->notes = $notes;

        // save
        $signUpsDB->save();
        
        // check save success
        return Response::json(array('ret' => 'ok'));

    });
    
    // get sign up items list by uid&pname
    Route::post('puja/getSignUpItemLists/', function () { 

        $uid = Request::input('uid');
        $pname = Request::input('pname');

//        if (empty($uid))
//            return Response::json(array('ret' => 'uid empty!', 'count' => '0'));
//        if (empty($pname))
//            $pname = '0';
        
        $signUpsDB = new \App\Models\Signup;
        
        // check gid exist
//        $count = $signUpsDB::where('signup_uid', 'like', $uid)->where('pname', 'like', $pname)->get()->count();
//        if ($count < 1)
//            return Response::json(array('ret' => 'Item is empty!', 'count' => $count));
        
        // get list
        $items = $signUpsDB::select('sid', 'item_id', 'item_liver', 'item_dier', 'item_money', 'notes')->where('signup_uid' ,'like' , $uid )->where('pname', 'like', $pname)->get();

        return $items;
    });
    
    // get sign up item by sid
    Route::post('puja/getSignUpItem/', function () { 

        $sid = Request::input('sid');

        if (empty($sid))
            return Response::json(array('ret' => 'sid empty!'));
        
        $signUpsDB = new \App\Models\Signup;
        
        // check gid exist
        $count = $signUpsDB::where('sid', 'like', $sid)->get()->count();
        if ($count < 1)
            return Response::json(array('ret' => 'Item not found!'));
        
        // get list
        $item = $signUpsDB::select('item_id', 'item_liver', 'item_dier', 'item_money', 'notes')->where('sid' ,'like' , $sid )->get();
        

        return $item;
    });


    //
    // Modify Sign up Item
    //
    Route::post('puja/modifySignUpItem', function () {
   
        // 
        $sid = Request::input('sid');
        $pname = Request::input('pname');
        $signup_date = Request::input('signup_date');
        $signup_uid = Request::input('signup_uid');
        $signup_username = Request::input('signup_username');
        $gid = Request::input('gid');
        $item_id = Request::input('item_id');
        $item_name = Request::input('item_name');
        $item_liver = Request::input('item_liver');
        $item_dier = Request::input('item_dier');
        $item_money = Request::input('item_money');
        $notes = Request::input('notes');

        if (empty($item_liver))
            $item_liver = NULL;
        
        if (empty($item_dier))
            $item_dier = NULL;
        
        if (empty($item_money))
            $item_money = NULL;
        
        if (empty($notes))
            $notes = NULL;
        
            
        $sidCount = \App\Models\Signup::where('sid', 'like', $sid)->get()->count();
        
        if ($sidCount == 1) {
            \App\Models\Signup::where('sid', 'like', $sid )->update([
                'pname'             => $pname,
                'signup_date'       => $signup_date,
                'signup_uid'        => $signup_uid,
                'signup_username'   => $signup_username,
                'gid'               => $gid,
                'item_id'           => $item_id,
                'item_name'         => $item_name,
                'item_liver'        => $item_liver,
                'item_dier'         => $item_dier,
                'item_money'        => $item_money,
                'notes'             => $notes
            ]);
            
            
            return Response::json(array('ret' => 'ok'));
        } else
            return Response::json(array('ret' => 'Not found'));
        
    });    

    Route::post('puja/deleteSignUpItem', function () {
        $sid = Request::input('sid');
        
        $deletedRows = \App\Models\Signup::where('sid', 'like', $sid )->delete();
        
        return Response::json(array('ret' => "OK"));
    });


    //Route::get('puja/addMember/{name}', 'pujaController@addMember');
//    Route::get('puja/readDBAll', function () {
//        return \App\Models\User::all()->toJson();
//    });
//    
//    Route::get('puja/getDB', function () {
//        $users = \App\Models\User::select('username', 'email')->where('username' ,'like' ,'jackyc')->get()->toJson();
//        return $users;
//    });
//    
//    Route::get('puja/writeDB', function () {
//        $uName = '張家';
//        $email = 'jk3@hotmail.com';
//        $user = new App\Models\User;
//        
//        // check Email exist
//        $count = $user::where('email', 'like', $email)->get()->count();
//        if ($count > 0)
//            return Response::json(array('ret' => 'Email exist!'));
//        
//        // save
//        $user->username = $uName;
//        $user->email = $email;
//        $user->password = bcrypt('');
//        $user->role_id = 3;
//        $user->seen = 0;
//        $user->valid = 0;
//        $user->confirmed = 0;
//        $user->save();
//        
//        // check save success
//        $count = $user::where('email', 'like', $email)->get()->count();
//        if ($count < 1)
//            return Response::json(array('ret' => 'save fail'));
//        else
//            return Response::json(array('ret' => 'success'));
//    });
//    
//    Route::get('puja/modDB', function () {
//        //$user = \App\Models\User;
//        $email = 'jk3@hotmail.com';
//        
//        $count = \App\Models\User::where('email', 'like', $email)->get()->count();
//        if ($count == 1) {
//            \App\Models\User::where('email', 'like', $email)->update(['username' => 'Hello']);
//            
//            return Response::json(array('ret' => 'success'));
//        } else if ($count > 1) {
//            return Response::json(array('ret' => 'Fail: have more modify item.'));
//        } else
//            return Response::json(array('ret' => 'Not find'));
//        
//    });
//    
//    Route::get('puja/delDB', function () {
//        $email = 'jk3@hotmail.com';
//        $deletedRows = \App\Models\User::where('email', 'like', $email)->delete();
//        return Response::json(array('ret' => $deletedRows));
//    });
//    
    
    
    Route::get('puja/uuid/', function () {
        $email = Request::input('email');//使用 Request 來得到 post 過來的資料。可以到官網 5.0 的文件試Request
        $password = Request::input('password');

        //return Response::json(array('uuid' => (string)Uuid::generate(4), 'email' => $email, 'password' => $password));
        return Response::json(array('uuid' => (string)Uuid::generate(4)));
    });
    
   Route::post('puja/uuid/', function () {
        $email = Request::input('email');//使用 Request 來得到 post 過來的資料。可以到官網 5.0 的文件試Request
        $password = Request::input('password');

        return Response::json(array('uuid' => (string)Uuid::generate(4), 'email' => $email, 'password' => $password));
//        return Response::json(array('uuid' => '12345'));
    });
    
    
//    
    Route::get('puja/test', function () {
//        $users = \App\Models\User::select('username', 'email')->where('username' ,'like' ,'jackyc')->get();
        $txt1 = 'he';
        return $txt1.'1ee';
    });

    
//$photos = \App\Models\User::where('id','>',0)->orderBy('by_user','asc')->get();
//$photos = \App\Models\User::find($user_id)->toJson();
//$photos = \App\Models\User::find($user_id)->user_name;    
//$photos = \App\Models\User::where('id','like','%yahoo%')->get();
//$photos = \App\Models\User::where('email','like','%yahoo%')->get()->toJson();   
//$photos = \App\Models\User::where('email','like','%yahoo%')->orderBy('email','desc')->get(); 
//$photos = \App\Models\User::where('email','like',"%".$email."%")->get();
//$photos = \App\Models\User::where('email','like','%yahoo%')->get()->count();
//$photos = \App\Models\User::where('user_name','=',$user)->photos()->get();
    
// get data
//    Route::post("API/getDataWithTime", function(){
//        $array = array();//用來把每筆資料中所需的資料存成陣列
//        $resultArray = array();//把每筆上面的陣列再存成陣列
//        
//            $size = Request::input("size");
//        
//        $dataArray = \App\Models\MikeData::OrderBy('sensor_time','desc')->take($size)->get();
//        foreach($dataArray as $tmp)
//        {
//            $array['time'] = $tmp->sensor_time;//把每筆資料放到陣列中
//            $array['data'] = $tmp->sensor_data; 
//            $resultArray['content'][] = $array;//再包一層陣列 
//        }
//        
//        return Response::json($resultArray);//把陣列以 json 格式回傳
//    });
//
//	Route::post("API/getData", function(){
//        $tmp = array();//[ 1, 2, 3, 4...]
//        
//        $size = Request::input("size");//手機端若有傳 size 的話，可以據以回傳數筆資料
//        
//        $dataArray = \App\Models\MikeData::OrderBy('created_at','desc')->take(5)->get();//根據資料庫收到的時間做 desc 排序，告訴資料庫要取得 5 筆資料
//        foreach($dataArray as $data)//從取得的資料中把每筆資料放到 $data 中
//        {
//            array_push($tmp, $data->sensor_data);//從 $data 中取得該筆資料的 sensor_data 欄位的資料放到陣列 $tmp 中
//        }
//        
//        return Response::json($tmp);//把 $tmp 陣列以 json 方式回傳
////        return Response::json(Auth::guard('api')->user());//測試回傳目前連線的使用者名稱
//    });
//    
//	
//	
//// DELETE
//   Route::post("API/deleteData", function(){
////        \App\MikeData::Destroy(1);//根據 id 來刪除資料，這行可以刪除 id=1 的資料
//        $datas = \App\Models\MikeData::where('sensor_data','<',20)->get();//示範刪除 sensor_data < 20 的資料
//        $tmp = array();
//        foreach($datas as $data)//只是先把 id 記下來
//        {
//            array_push($tmp,$data->id);
//        }
//        
//        \App\MikeData::where('sensor_data','<',20)->delete();//實際刪除 sensor_data < 20 的，只需要這一行
//        
//        return Response::json($tmp);//回傳被刪除的資料的 id
//    });
    
    


// *********************************************************************************************************************************
Route::group(['middleware' => ['web']], function () {

	// Home
	Route::get('/', [
		'uses' => 'HomeController@index', 
		'as' => 'home'
	]);
	Route::get('language/{lang}', 'HomeController@language')->where('lang', '[A-Za-z_-]+');


	// Admin
	Route::get('admin', [
		'uses' => 'AdminController@admin',
		'as' => 'admin',
		'middleware' => 'admin'
	]);

	Route::get('medias', [
		'uses' => 'AdminController@filemanager',
		'as' => 'medias',
		'middleware' => 'redac'
	]);

 

	// Blog
	Route::get('blog/order', ['uses' => 'BlogController@indexOrder', 'as' => 'blog.order']);
	Route::get('articles', 'BlogController@indexFront');
	Route::get('blog/tag', 'BlogController@tag');
	Route::get('blog/search', 'BlogController@search');

	Route::put('postseen/{id}', 'BlogController@updateSeen');
	Route::put('postactive/{id}', 'BlogController@updateActive');

	Route::resource('blog', 'BlogController');

	// Comment
	Route::resource('comment', 'CommentController', [
		'except' => ['create', 'show']
	]);

	Route::put('commentseen/{id}', 'CommentController@updateSeen');
	Route::put('uservalid/{id}', 'CommentController@valid');


	// Contact
	Route::resource('contact', 'ContactController', [
		'except' => ['show', 'edit']
	]);


	// User
	Route::get('user/sort/{role}', 'UserController@indexSort');

	Route::get('user/roles', 'UserController@getRoles');
	Route::post('user/roles', 'UserController@postRoles');

	Route::put('userseen/{user}', 'UserController@updateSeen');

	Route::resource('user', 'UserController');

	// Authentication routes...
	Route::get('auth/login', 'Auth\AuthController@getLogin');
	Route::post('auth/login', 'Auth\AuthController@postLogin');
	Route::get('auth/logout', 'Auth\AuthController@getLogout');
	Route::get('auth/confirm/{token}', 'Auth\AuthController@getConfirm');

	// Resend routes...
	Route::get('auth/resend', 'Auth\AuthController@getResend');

	// Registration routes...
	Route::get('auth/register', 'Auth\AuthController@getRegister');
	Route::post('auth/register', 'Auth\AuthController@postRegister');

	// Password reset link request routes...
	Route::get('password/email', 'Auth\PasswordController@getEmail');
	Route::post('password/email', 'Auth\PasswordController@postEmail');

	// Password reset routes...
	Route::get('password/reset/{token}', 'Auth\PasswordController@getReset');
	Route::post('password/reset', 'Auth\PasswordController@postReset');

});