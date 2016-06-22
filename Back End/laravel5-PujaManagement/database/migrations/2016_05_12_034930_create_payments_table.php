<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;

class CreatePaymentsTable extends Migration {

	public function up()
	{
		Schema::create('payments', function(Blueprint $table) {
			$table->increments('id');
			$table->timestamps();
			$table->string('gid', 255);
			$table->string('pname', 10);
			$table->string('cashier', 10);
			$table->string('cuid', 255);
			$table->string('year', 4);
			$table->date('pay_date');
			$table->bigInteger('amount')->unsigned();
			$table->string('notes', 255);
			$table->boolean('pay_finish');
		});
	}

	public function down()
	{
		Schema::drop('payments');
	}
}