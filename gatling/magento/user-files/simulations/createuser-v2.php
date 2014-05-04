<?php

if (sizeof($argv)!=6) 
	exit ("usage: " . $argv[0] . " <site url> <api user> <api pass> <number of users> <CSV output>\n");

$numUsers=$argv[4];

$outfile=fopen($argv[5], "w");
if ($outfile==FALSE)
	exit("output file " . $argv[5] . " not found.\n");

$proxy=new SoapClient($argv[1] . '/api/soap/?wsdl', 
		array('trace'=>TRUE));
$sessionId = $proxy->login($argv[2], $argv[3]);
var_dump($sessionId);

$outarr=array();
$outarr[]="shopperEmail,shopperPass,addressId"; // title line

$ctr=0;
while($ctr++ < $numUsers) {
	$csline=array();
	$csline[]= (microtime(true)*10000) . '@x.com'; // email
	$csline[]= '12345678'; // password
	print("\n" . trim($csline[0] . ' ' . $csline[1]));
	
	// create customer
	try {
		$customerEntity=array('email' => trim($csline[0]), 'firstname' => 'Shopper',
			'lastname' => 'Mage-Gatling', 'password' => trim($csline[1]), 
			'website_id' => '1', 'store_id' => '1', 'group_id' => '1' );
		$custId=$proxy->call($sessionId, 'customer.create', array($customerEntity));

		// add address and capture address id
		$customerAddress=array('firstname' => 'Shopper', 'lastname' => 'Mage-Gatling',
			'street' => array('123 Some St', 'Ste 123'), 'city' => 'Austin', 
			'country_id' => 'US', 'region' => 'Texas', 'region_id' => '57',
			'postcode' => '78701', 'telephone' => '5125551212', 
			'is_default_billing' => TRUE, 'is_default_shipping' => TRUE);
		$addrId=$proxy->call($sessionId, 'customer_address.create', 
			array('customerId' => $custId, 
				'addressData' => $customerAddress));

		$outarr[]=$csline[0] . "," . $csline[1] . "," . $addrId;
		
		$custInfo=$proxy->call($sessionId, 'customer.info', $custId);
		var_dump($custInfo);
	}
	catch (Exception $ex) {
		print($ex->getMessage() . "\n");
	}
}

// write to output
foreach ($outarr as $x => $line) {
	fwrite($outfile, $line . "\n");	
}
fclose($outfile);

?>

