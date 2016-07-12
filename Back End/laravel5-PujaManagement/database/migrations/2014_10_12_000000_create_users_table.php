<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUsersTable extends Migration {

	/**
	 * Run the migrations.
	 *
	 * @return void
	 */
	public function up()
	{
		Schema::create('users', function(Blueprint $table)
		{
			$table->increments('id');
			$table->string('uid', 36)->unique()->nullable();
			$table->string('gid', 36)->nullable();
			$table->string('username', 30);
			$table->string('email', 50)->nullable();
			$table->string('password', 64);
			$table->boolean('head')->nullable();
			$table->string('address', 255)->nullable();
			$table->string('tel', 50)->nullable();
			$table->boolean('dead')->nullable();
			$table->date('born_date')->nullable();
			$table->tinyInteger('born_time')->nullable();
			$table->date('die_date')->nullable();
			$table->tinyInteger('die_time')->nullable();
			$table->string('notes', 255)->nullable();
			$table->integer('role_id')->unsigned();
			$table->boolean('seen')->default(false);
			$table->boolean('valid')->default(false);
			$table->boolean('confirmed')->default(false);
			$table->string('confirmation_code')->nullable();
			$table->timestamps();
			$table->rememberToken();			
		});
	}

	/**
	 * Reverse the migrations.
	 *
	 * @return void
	 */
	public function down()
	{
		Schema::drop('users');
	}

}
