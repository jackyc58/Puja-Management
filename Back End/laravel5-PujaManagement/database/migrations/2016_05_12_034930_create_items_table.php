<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;

class CreateItemsTable extends Migration {

	public function up()
	{
		Schema::create('items', function(Blueprint $table) {
			$table->increments('id');
			$table->smallInteger('no');
			$table->string('item', 100);
		});
	}

	public function down()
	{
		Schema::drop('items');
	}
}