/* -------------------------------------------------------------------------
 * Desarrollo de sistemas distribuidos 2020-2021
 * Páctica 4: Node.JS
 * Autor: Raquel Molina Reche
 *
 * Ejecutar el servicio: 
 *     $ node servidor.js
 * 
 * Consideraciones: El servicio mongodb debe estar activo
 * -------------------------------------------------------------------------
 */

//-------Modulos requeridos------------------------
var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");

var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};


//------------VARIABLES DEL SERVICIO-------------
//SENSORES
var LUM_ACTUAL = 150; //valor optimo
var TEMP_ACTUAL = 25;  //valor optimo
var HUMEDAD_ACTUAL = 50; //valor optimo en tanto por ciento
var CALIDADAIRE_ACTUAL = 10; //valor optimo
var MOVIMIENTO = false;

//ACTUADORES
var persiana = false;  //false bajada - true subida
var aireAcondicionado = false; //false apagado - true encendido
var calefaccion = false;  //false apagada - true encendida
var humidificador = false;  //false apagado - true encendido
var purificador = false;  //false apagado - true encendido
var luces = false;  //false apagado - true encendido

//UMBRALES
//Maximos
var UMBRAL_TEMP_MAX = 32;
var UMBRAL_LUM_MAX = 200;
var UMBRAL_HUM_MAX = 60;
var UMBRAL_CALIDADAIRE_MAX = 200;

//Minimos
var UMBRAL_TEMP_MIN = 5;
var UMBRAL_LUM_MIN = 100;
var UMBRAL_HUM_MIN = 40;
var UMBRAL_CALIDADAIRE_MIN = 0;

var valorCambioTemp = 15;

//----------------------------------------------------------------------------
//------------------SERVIDOR------------------------

var httpServer = http.createServer(
	function(request, response) {

		var uri = url.parse(request.url).pathname;

		if( uri == "/"){
			uri = "/usuario.html";
		}
		else if(uri == "/sensor") {
			uri = "/sensores.html";
		}
		else{
			output = "Error: La url no es valida";
		}

		var fname = path.join(process.cwd(), uri);
		fs.exists(fname, function(exists) {
			if (exists) {
				fs.readFile(fname, function(err, data){
					if (!err) {
						var extension = path.extname(fname).split(".")[1];
						var mimeType = mimeTypes[extension];
						response.writeHead(200, mimeType);
						response.write(data);
						response.end();
					}
					else {
						response.writeHead(200, {"Content-Type": "text/plain"});
						response.write('Error de lectura en el fichero: '+uri);
						response.end();
					}
				});
			}
			else{
				console.log("Peticion invalida: "+uri);
				response.writeHead(200, {"Content-Type": "text/plain"});
				response.write('404 Not Found\n');
				response.end();
			}
		});
	}
);


MongoClient.connect("mongodb://localhost:27017/", { useUnifiedTopology: true }, function(err, db) {
	httpServer.listen(8080);
	var io = socketio(httpServer);

	//Array de clientes
	var allClients = new Array();
	var cont=1;

	//En la base de datos se maneja el historial de cambios producidos
	var dbo = db.db("mibd");

	dbo.createCollection("historialCambios", function(err, collection){

		//Cuando un cliente se conecta
		io.sockets.on('connection',
		function(client) {

			var id = "Cliente-"+cont;
			cont++;

			//Se incluye el cliente en el array de clientes
			allClients.push({address:client.request.connection.remoteAddress, port:client.request.connection.remotePort, id:id});
			console.log('New connection from ' + client.request.connection.remoteAddress + ':' + client.request.connection.remotePort);


			//Emision de información del cliente
			client.emit('my-address', {host:client.request.connection.remoteAddress, port:client.request.connection.remotePort, id:id});


			//Emision de la lista de clientes actualizada tras la conexión de un cliente nuevo
			io.sockets.emit('all-connections', allClients);


			//Se le envían los valores actuales al cliente que se acaba de conectar
			client.on('output-evt', function (data) {
				client.emit('output-evt',  {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces});
			});


			//COMPROBACIÓN DEL AGENTE POR SI EL CLIENTE SE CONECTA DESPUÉS Y YA SE HAN PRODUCIDO ALARMAS
			var resAgentInicio = agent();

			//Si el agente comprueba que hay que mandar una alarma se manda a todos los clientes
			if( resAgentInicio.mensajes.length > 0 ){
				io.sockets.emit('all-alarm', resAgentInicio.mensajes);
			}


			//Emision de los valores para la actualizacion de datos
			io.sockets.emit('values', {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces} );


			//PETICIONES DE USUARIOS

			//Recepción de un cambio en el estado del Aire acondicionado
			client.on('AC', function (data) {
				if(aireAcondicionado){
					//Se modifica el estado del AC
					aireAcondicionado = false;
				}
				else{
					//Se modifica el estado del AC
					aireAcondicionado = true;
				}

				var insertion =  {client: data.info.client, change: data.info.change, port: data.info.port, time: data.info.time};
				collection.insertOne(insertion, {safe:true}, function(err, result) {});

				//COMPROBACIÓN DEL AGENTE
				var resAgent = agent();

				console.log('Se modifica el estado del aire acondicionado');
				//Se notifica a todos los suscriptores de la modificacion
				io.sockets.emit('all-values', {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces} );

				//Si el agente comprueba que hay que mandar una alarma se manda a todos los clientes
				if( resAgent.mensajes.length > 0 ){
					io.sockets.emit('all-alarm', resAgent.mensajes);
				}
			});


			//Recepción de un cambio en el estado de la persiana
			client.on('persiana', function (data) {
				if(persiana){
					//Se modifica el estado de la persiana
					persiana = false;
				}
				else{
					persiana = true;
				}

				var insertion =  {client: data.info.client, change: data.info.change, port: data.info.port, time: data.info.time};
				collection.insertOne(insertion, {safe:true}, function(err, result) {});


				//COMPROBACIÓN DEL AGENTE
				var resAgent = agent();

				console.log('Se modifica el estado de la persiana');
				//Se notifica a todos los suscriptores de la modificacion
				io.sockets.emit('all-values', {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces} );

				//Si el agente comprueba que hay que mandar una alarma se manda a todos los clientes
				if( resAgent.mensajes.length > 0 ){
					io.sockets.emit('all-alarm', resAgent.mensajes);
				}
			});

			//Recepción de un cambio en el estado de la CALEFACCION
			client.on('calefaccion', function (data) {
				if(calefaccion){
					//Se modifica el estado
					calefaccion = false;
				}
				else{
					calefaccion = true;
				}

				var insertion =  {client: data.info.client, change: data.info.change, port: data.info.port, time: data.info.time};
				collection.insertOne(insertion, {safe:true}, function(err, result) {});

				//COMPROBACIÓN DEL AGENTE
				var resAgent = agent();

				console.log('Se modifica el estado de la calefaccion');
				//Se notifica a todos los suscriptores de la modificacion
				io.sockets.emit('all-values', {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces} );

				//Si el agente comprueba que hay que mandar una alarma se manda a todos los clientes
				if( resAgent.mensajes.length > 0 ){
					io.sockets.emit('all-alarm', resAgent.mensajes);
				}
			});

			//Recepción de un cambio en el estado de el humidificador
			client.on('humidificador', function (data) {
				if(humidificador){
					//Se modifica el estado
					humidificador = false;
				}
				else{
					humidificador = true;
				}

				var insertion =  {client: data.info.client, change: data.info.change, port: data.info.port, time: data.info.time};
				collection.insertOne(insertion, {safe:true}, function(err, result) {});

				//COMPROBACIÓN DEL AGENTE
				var resAgent = agent();

				console.log('Se modifica el estado del humidificador');
				//Se notifica a todos los suscriptores de la modificacion
				io.sockets.emit('all-values', {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces} );

				//Si el agente comprueba que hay que mandar una alarma se manda a todos los clientes
				if( resAgent.mensajes.length > 0 ){
					io.sockets.emit('all-alarm', resAgent.mensajes);
				}
			});

			//Recepción de un cambio en el estado de el purificador
			client.on('purificador', function (data) {
				if(purificador){
					//Se modifica el estado
					purificador = false;
				}
				else{
					purificador = true;
				}

				var insertion =  {client: data.info.client, change: data.info.change, port: data.info.port, time: data.info.time};
				collection.insertOne(insertion, {safe:true}, function(err, result) {});

				//COMPROBACIÓN DEL AGENTE
				var resAgent = agent();

				console.log('Se modifica el estado del purificador');
				//Se notifica a todos los suscriptores de la modificacion
				io.sockets.emit('all-values', {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces} );

				//Si el agente comprueba que hay que mandar una alarma se manda a todos los clientes
				if( resAgent.mensajes.length > 0 ){
					io.sockets.emit('all-alarm', resAgent.mensajes);
				}
			});

			//Recepción de un cambio en el estado de las luces
			client.on('luces', function (data) {
				if(luces){
					//Se modifica el estado
					luces = false;
				}
				else{
					luces = true;
				}

				var insertion =  {client: data.info.client, change: data.info.change, port: data.info.port, time: data.info.time};
				collection.insertOne(insertion, {safe:true}, function(err, result) {});

				//COMPROBACIÓN DEL AGENTE
				var resAgent = agent();

				console.log('Se modifica el estado de las luces');
				//Se notifica a todos los suscriptores de la modificacion
				io.sockets.emit('all-values', {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces} );

				//Si el agente comprueba que hay que mandar una alarma se manda a todos los clientes
				if( resAgent.mensajes.length > 0 ){
					io.sockets.emit('all-alarm', resAgent.mensajes);
				}
			});


			//PETICIONES DE SENSORES

			client.on('sensor-changes', function(data) {
				LUM_ACTUAL = data.luminosidadNew;
				TEMP_ACTUAL = data.temperaturaNew;
				HUMEDAD_ACTUAL = data.humedadNew;
				CALIDADAIRE_ACTUAL = data.calidadAireNew;
				 MOVIMIENTO = data.movimientoNew;


				console.log('Se modifica el estado de los sensores');
				var insertion =  {client:data.info.client, change: data.info.change, port:data.info.port,time: data.info.time};
				collection.insertOne(insertion, {safe:true}, function(err, result) {});

				//COMPROBACIÓN DEL AGENTE
				var resAgent = agent();

				//Se notifica a todos los suscriptores de la modificacion
				io.sockets.emit('all-values', {temperatura: TEMP_ACTUAL, luminosidad: LUM_ACTUAL,humedad: HUMEDAD_ACTUAL, calidadAire: CALIDADAIRE_ACTUAL, movimiento: MOVIMIENTO, estadoAC: aireAcondicionado,  estadoPersiana: persiana, estadoCalefaccion: calefaccion, estadoHumidificador: humidificador, estadoPurificador: purificador, estadoLuces: luces} );

				//Si el agente comprueba que hay que mandar una alarma se manda a todos los clientes
				if( resAgent.mensajes.length > 0 ) {
					io.sockets.emit('all-alarm', resAgent.mensajes);
				}

			});

			//Si recibe una peticion para conocer el historial de los cambios
			client.on('event-history', function () {
				//Mandar cambios en el historial
				collection.find().toArray(function(err, results){
					client.emit('event-history', results);
				});

			});

			//Recepcion de desconexion de un cliente
			client.on('disconnect', function() {
				console.log("El cliente "+client.request.connection.remoteAddress+" se va a desconectar");
				//console.log(allClients);

				//Se elimina al cliente del array de clientes suscritos
				var index = -1;
				for(var i = 0; i<allClients.length;i++){
					//console.log("Hay "+allClients[i].port);
					if(allClients[i].address == client.request.connection.remoteAddress
						&& allClients[i].port == client.request.connection.remotePort){
						index = i;
					}
				}

				if (index != -1) {
					allClients.splice(index, 1);
					//Emision de la lista de clientes actualizada
					io.sockets.emit('all-connections', allClients);
				}else{
					console.log("EL CLIENTE NO SE HA ENCONTRADO!")
				}
				console.log('El cliente '+client.request.connection.remoteAddress+' se ha desconectado');
			});

		});

	});
});

console.log("Servicio MongoDB iniciado");


//----------------------------------------------------------------------------
//--------------AGENTE----------------------
function agent(){

	var mens = [];
	var result = { cambio:false ,  mensajes: mens};

	//SOBREPASO DE UMBRALES
	//Temperatura max
	if( TEMP_ACTUAL > UMBRAL_TEMP_MAX){
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÁXIMO DE TEMPERATURA");
		console.log("  Agente: se sobrepasa el limite máximo de temperatura!");

	}
	//Temperatura min
	if( TEMP_ACTUAL < UMBRAL_TEMP_MIN){
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÍNIMO DE TEMPERATURA");
		console.log("   Agente: se sobrepasa el limite mínimo de temperatura!");

	}
	//Luminosidad max
	if( LUM_ACTUAL > UMBRAL_LUM_MAX){
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÁXIMO DE LUMINOSIDAD");
		console.log("   Agente: se sobrepasa el limite máximo de luminosidad!");

	}
	//Luminosidad min
	if( LUM_ACTUAL < UMBRAL_LUM_MIN){
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÍNIMO DE LUMINOSIDAD");
		console.log("   Agente: se sobrepasa el limite mínimo de luminosidad!");

	}
	//Humedad max
	if( HUMEDAD_ACTUAL > UMBRAL_HUM_MAX){
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÁXIMO DE HUMEDAD");
		console.log("   Agente: se sobrepasa el limite máximo de humedad!");

	}
	//Humedad min
	if( HUMEDAD_ACTUAL < UMBRAL_HUM_MIN){
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÍNIMO DE HUMEDAD");
		console.log("   Agente: se sobrepasa el limite mínimo de humedad!");

	}
	//Calidad aire max
	if( CALIDADAIRE_ACTUAL > UMBRAL_CALIDADAIRE_MAX){
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÁXIMO DE CALIDAD DEL AIRE");
		console.log("   Agente: se sobrepasa el limite máximo de calidad del aire!");

	}
	//Calidad aire  min
	if( CALIDADAIRE_ACTUAL < UMBRAL_CALIDADAIRE_MIN){
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÍNIMO DE CALIDAD DEL AIRE");
		console.log("   Agente: se sobrepasa el limite mínimo de calidad del aire!");

	}



	//EVENTOS INCOMPATIBLES

	//Si la temperatura y la luminosidad sobrepasan los umbrales máximos se cierra la persiana
	if( TEMP_ACTUAL > UMBRAL_TEMP_MAX && LUM_ACTUAL > UMBRAL_LUM_MAX && persiana){
		persiana = false;
		result.cambio = true;
		result.mensajes.push("SOBREPASADOS LÍMITES DE LUZ Y TEMPERATURA => CERRANDO PERSIANA...");
		console.log("   Agente: se cierra la persiana porque se sobre pasan los limites máximos de luz y temperatura!");
	}


	//Si el aire acondicionado esta activado y la calefaccion tambien salta una alarma
	//Se deja encendido el aparato que concuerde con la temperatura actual
	if( aireAcondicionado && calefaccion){

		//Si la temperatura actua es mayor que la temperatura de cambio establecida
		// se apaga la calefaccion y se deja el aire
		if( TEMP_ACTUAL > valorCambioTemp){
			calefaccion = false;
			result.cambio = true;
			result.mensajes.push("AIRE ACONDICIONADO Y CALEFACCION ENCENDIDOS A LA VEZ => APAGANDO CALEFACCIÓN...");
			console.log("   Agente: se detecta que se ha encendido el aire acondicionado y la calefaccion a la vez pero la temperatura actual no requiere de calefaccion!");

		}
		else{
			aireAcondicionado = false;
			result.cambio = true;
			result.mensajes.push("AIRE ACONDICIONADO Y CALEFACCION ENCENDIDOS A LA VEZ => APAGANDO AIRE ACONDICIONADO...");
			console.log("   Agente: se detecta que se ha encendido el aire acondicionado y la calefaccion a la vez pero la temperatura actual no requiere de aire acondicionado!");

		}
	}

	//Humedad max
	if( (HUMEDAD_ACTUAL > UMBRAL_HUM_MAX) && humidificador ){
		humidificador = false;
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÁXIMO DE HUMEDAD => APAGANDO HUMIDIFICADOR...");
		console.log("   Agente: se sobrepasa el limite máximo de humedad!");

	}
	//Humedad min
	if( (HUMEDAD_ACTUAL < UMBRAL_HUM_MIN) && !humidificador){
		humidificador = true;
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÍNIMO DE HUMEDAD => ENCENDIENDO HUMIDIFICADOR...");
		console.log("   Agente: se sobrepasa el limite mínimo de humedad!");

	}
	//Calidad aire max
	if( (CALIDADAIRE_ACTUAL > UMBRAL_CALIDADAIRE_MAX) && purificador){
		purificador = false;
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÁXIMO DE CALIDAD DEL AIRE => APAGANDO PURIFICADOR...");
		console.log("   Agente: se sobrepasa el limite máximo de calidad del aire!");

	}
	//Calidad aire  min
	if( (CALIDADAIRE_ACTUAL < UMBRAL_CALIDADAIRE_MIN) && !purificador){
		purificador = true;
		result.cambio = true;
		result.mensajes.push("SOBREPASADO LÍMITE MÍNIMO DE CALIDAD DEL AIRE => ENCENDIENDO HUMIDIFICADOR...");
		console.log("   Agente: se sobrepasa el limite mínimo de calidad del aire!");

	}

	//EVENTOS DE ALARMA

	//Movimiento
	//Si el sensor de movimiento recibe un movimiento,  la luminosidad es baja y las luces estan apagadas, enciende las luces
	if( MOVIMIENTO && (LUM_ACTUAL < (UMBRAL_LUM_MIN+20.0)) && !luces ){
		luces = true;
		result.cambio = true;
		result.mensajes.push("SE HA DETECTADO MOVIMIENTO CON UNA LUMINOSIDAD BAJA => ENCENDIENDO LUCES...");
		console.log("   Agente: se detecta una luminosidad baja!");

	}

	//Luces
	//Si se enciende la luz pero la luminosidad es alta se produce un aviso
	if( luces && (LUM_ACTUAL > (UMBRAL_LUM_MAX-(UMBRAL_LUM_MIN/2))) ){
		result.cambio = true;
		result.mensajes.push("SE HA ENCENDIDO LA LUZ PERO LA LUMINOSIDAD ACTUAL ES ALTA");
		console.log("   Agente: se detecta que se ha encendido la luz pero la luminosidad actual es alta!");
	}

	//Si el aire acondicionado esta activado pero la tempertatura es relativamente baja
	if( aireAcondicionado && (TEMP_ACTUAL < 15)){
		result.cambio = true;
		result.mensajes.push("AIRE ACONDICIONADO ENCENDIDO CON UNA TEMPERATURA ACTUAL BAJA");
		console.log("   Agente: se detecta que se ha encendido el aire acondicionado con una temperatura baja!");

	}

	//Si la calefaccion esta activada pero la tempertatura es relativamente alta
	if( calefaccion && (TEMP_ACTUAL > 15)){
		result.cambio = true;
		result.mensajes.push("CALEFACCION ENCENDIDA CON UNA TEMPERATURA ACTUAL ALTA ");
		console.log("   Agente: se detecta que esta encendida la calefaccion con una temperatura alta!");

	}



	return result;
}