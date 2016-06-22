<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSignUpTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('signups', function (Blueprint $table) {
			$table->increments('sid');
			$table->timestamps();
			$table->tinyInteger('pname')->nullable();
			$table->date('signup_date')->nullable();
            $table->string('signup_uid', 36)->nullable();
            $table->string('signup_username', 50)->nullable();
            $table->string('gid', 36)->nullable();
            $table->tinyInteger('item_id')->nullable();
            $table->string('item_name', 50)->nullable();
            $table->string('item_liver', 50)->nullable();
            $table->string('item_dier', 50)->nullable();
            $table->integer('item_money')->unsigned()->nullable();
			$table->string('notes', 255)->nullable();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop('signups');
    }
}
