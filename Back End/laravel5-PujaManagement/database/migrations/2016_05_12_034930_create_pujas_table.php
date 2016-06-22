<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;

class CreatePujasTable extends Migration {

	public function up()
	{
		Schema::create('pujas', function(Blueprint $table) {
			$table->increments('pid');
			$table->timestamps();
//			$table->string('year', 4)->nullable();
			$table->tinyInteger('pname')->nullable();
			$table->date('begin_date')->nullable();
			$table->date('end_date')->nullable();
			$table->string('notes', 255)->nullable();
			$table->string('sign_up_table_name', 50)->nullable();
		});
	}

	public function down()
	{
		Schema::drop('pujas');
	}
}