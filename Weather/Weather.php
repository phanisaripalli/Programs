<?php
use Cmfcmf\OpenWeatherMap;
use Cmfcmf\OpenWeatherMap\Exception as OWMException;

// Must point to composer's autoload file.
require('vendor/autoload.php');

// Language of data (try your own language here!):
$lang = 'en';

// Units (can be 'metric' or 'imperial' [default]):
$units = 'metric';

date_default_timezone_set('Europe/Berlin'); 

// Get OpenWeatherMap object. Don't use caching (take a look into Example_Cache.php to see how it works).
$owm = new OpenWeatherMap();

$city = 'Berlin';

parse_str(implode('&', array_slice($argv, 1)), $_GET);


if (isset($_GET['city'])) {

   $city = $_GET["city"];  

}

echo($city . ' - ' . "\r\n");
echo('Current temperature : ');


try {
    $weather = $owm->getWeather($city, $units, $lang);
} catch(OWMException $e) {
    echo 'OpenWeatherMap exception: ' . $e->getMessage() . ' (Code ' . $e->getCode() . ').';
    echo "\n";
} catch(\Exception $e) {
    echo 'General exception: ' . $e->getMessage() . ' (Code ' . $e->getCode() . ').';
    echo "<br />\n";
}



echo $weather->temperature;
echo "\r\n";

echo "Max. temperature : ";
echo($weather->temperature->max->getValue() . ' °C');

echo "\r\n";
echo "Min. temperature : " ;
echo($weather->temperature->min->getValue() . ' °C');

echo "\r\n";
echo 'Forecast for the next 24 hours : ' . "\r\n";


$forecast = $owm->getWeatherForecast($city, $units, $lang, '', 1);
echo "\n\n\n";

foreach ($forecast as $weather) {
    echo "Weather forecast at " . $weather->time->day->format('d.m.Y') . " from " . $weather->time->from->format('H:i') . " to " . $weather->time->to->format('H:i') . "\r\n";
    echo ">>>>>>>>>> " . $weather->precipitation->getDescription() . "\r\n";
    echo $weather->temperature . "\n";
    echo "\r\n";
}
