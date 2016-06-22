<?php 

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;

class PujiController extends Controller {

    /**
     * 
     */
    public function addMember($name)
    {

        $names = array("jacky", "Mary", "john");
//        return view('puji.addMember',compact('names'));
        return response()->json(array('name' => $names));//Response::json(array('names' => $names->toArray()));
    }

}