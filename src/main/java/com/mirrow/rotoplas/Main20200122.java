package com.mirrow.rotoplas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

@SuppressWarnings("unused")
public class Main20200122 {

	public static void main(String[] args) throws IOException {

		File[] files;
		int segmento = 0;
		int ejercicio = 0;
		int cabecerasEsperadas = 0;
		int registrosEsperados = 0;
		int cabecerasProcesadas = 0;
		int registrosProcesados = 0;
		String debeEsperado = "";
		String haberEsperado = "";
		double sumaDebe = 0;
		double sumaHaberes = 0;


		// Crea un JFileChooser
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);

		// Muestra el JFileChooser
		int returnVal = fc.showOpenDialog(fc);

		// Acción que se toma una vez que el usuario acepta
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			files = fc.getSelectedFiles();
			AsientoEnc encabezadoPrevio = new AsientoEnc();

			// Se crea una lista para almacenar las lineas que se van a leer
			List<AsientoEnc> headerList = new ArrayList<AsientoEnc>();

			List<AsientoDetalle> bodyList = new ArrayList<AsientoDetalle>();

			for (File file : files) {
						
				// Se crea un BufferedReader para leer el archivo
				Charset inputCharset = Charset.forName("ISO-8859-1");

				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), inputCharset));
				
				BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file), inputCharset));

				// Variable que va a almacenar los renglones
				String st;
				
				String anio;
				
				// Recorre el archivo hasta encontrar anio
				while ((anio = br1.readLine()) != null) {
					
					if (anio.contains("Ejercicio:          ")) {
						
						ejercicio = Integer.parseInt(anio.substring(20,24)) ;
						
						br1.close();
						
						break;
						
					}
					
				}
				
				// Recorre el archivo hasta que no tenga mas información
				while ((st = br.readLine()) != null) {

					if (st.contains("|  0") ||  st.contains("|   1") || st.contains("|   2") || st.contains("|   3") || st.contains("|   4") || st.contains("|   5") || st.contains("|   6") || st.contains("|   7") || st.contains("|   8") || st.contains("|   9")) {
						
						AsientoEnc encabezado = new AsientoEnc();

						encabezado.setNroAsiento(st.substring(3, 11));

						encabezado.setFechaAsiento(encabezado.stringToDate(st.substring(12, 22).replace(".", "/")));

						encabezado.setNDoc(st.substring(31, 41));

						encabezado.setReferencia((st.substring(42, 55).trim()).replace("'", "''"));

						encabezado.setConcepto((st.substring(56, 106).trim()).replace("'", "''"));

						encabezadoPrevio = encabezado;

						headerList.add(encabezado);

					} else if(st.contains("|     0") || st.contains("|     1") || st.contains("|     2") || st.contains("|     3") || st.contains("|     4") || st.contains("|     5") || st.contains("|     6") || st.contains("|     7") || st.contains("|     8") || st.contains("|     9")) {

						AsientoDetalle detalle = new AsientoDetalle();

						detalle.setNroAsiento(encabezadoPrevio.getNroAsiento());

						detalle.setFechaAsiento(encabezadoPrevio.getFechaAsiento());

						detalle.setNroRenglon(st.substring(6, 9).trim());

						detalle.setNroCta(st.substring(10, 16).trim());

						detalle.setNombreCta(st.substring(18, 38).trim().replace("'", "''"));

						detalle.setNIF(st.substring(39, 52).trim());

						detalle.setImpDebe(st.substring(54, 70).trim().replace(",", ""));

						detalle.setImpHaber(st.substring(72, 89).trim().replace(",", ""));

						detalle.setMoneda(st.substring(91, 94).trim());

						detalle.setConcepto(st.substring(96, 106).trim());
						
						bodyList.add(detalle);

					}else if(st.contains("|Registros ")){ //Calculo los registros totales esperados

						if(st.contains("Cabecera")) {

							cabecerasEsperadas = Integer.parseInt(st.substring(45, 53).trim().replace(",", ""));

						} else if(st.contains("Posición")) {

							registrosEsperados = Integer.parseInt(st.substring(45, 53).trim().replace(",", ""));

						}

					}else if(st.contains("|** Total") ) { // Calculo los montos totales de debe y haber

						st = br.readLine();

						debeEsperado = st.substring(52, 70).trim().replace(",", "");

						haberEsperado = st.substring(71, 89).trim().replace(",", "");
						
						DecimalFormat df = new DecimalFormat("#.##");

						String sumaDebeString = df.format(sumaDebe);

						String sumaHaberesString = df.format(sumaHaberes);

						sumaDebe = Double. parseDouble(sumaDebeString);

						sumaHaberes = Double. parseDouble(sumaHaberesString);
						
						segmento = Integer.parseInt(encabezadoPrevio.dateToString(encabezadoPrevio.getFechaAsiento()).substring(0, 2));
						
						String mes = "";

						switch(segmento) {

						case 1: {

							mes = "Enero";
							break;

						}

						case 2: {

							mes = "Febrero";
							break;

						}

						case 3: {

							mes = "Marzo";
							break;

						}

						case 4: {

							mes = "Abril";
							break;

						}

						case 5: {

							mes = "Mayo";
							break;

						}

						case 6: {

							mes = "Junio";
							break;

						}

						case 7: {

							mes = "Julio";
							break;

						}

						case 8: {

							mes = "Agosto";
							break;

						}

						case 9: {

							mes = "Septiembre";
							break;

						}

						case 10: {

							mes = "Octubre";
							break;

						}

						case 11: {

							mes = "Noviembre";
							break;

						}

						case 12: {

							mes = "Diciembre";
							break;

						} default: {

							mes = "???";
							break;
						}

						}

						//Genero una instancia de la clase Connection

						try {

							Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

						}
						catch(ClassNotFoundException cnfex) {

							System.out.println("Problema al cargar o registrar el controlador MS Access JDBC");

							cnfex.printStackTrace();

						}

						// Abro la conexión a la base de datos
						try {

							String msAccDB = "RotoPlasDatos.accdb";

							String dbURL = "jdbc:ucanaccess://" + msAccDB; 

							//Creo y obtengo conexión usando la clase DriverManager

							Connection connection = DriverManager.getConnection(dbURL); 

							//Recorro los encabezados
							for (AsientoEnc encabezado : headerList) {
								
								//encabezadoPrevio = encabezado;

								try {

									Statement statement = connection.createStatement();

									System.out.println("INSERT INTO AsientosEnc (NroAsiento, FechaAsiento, NDoc, Referencia, Concepto) VALUES (" + encabezado.getNroAsiento() + ", " + encabezado.dateToString(encabezado.getFechaAsiento()) + ", '" + encabezado.getNDoc() + "', '" + encabezado.getReferencia() + "', '" + encabezado.getConcepto() + "')");

									statement.executeUpdate("INSERT INTO AsientosEnc (NroAsiento, FechaAsiento, NDoc, Referencia, Concepto) VALUES (" + encabezado.getNroAsiento() + ", #" + encabezado.dateToString(encabezado.getFechaAsiento()) + "#, '" + encabezado.getNDoc() + "', '" + encabezado.getReferencia() + "', '" + encabezado.getConcepto() + "')");

									cabecerasProcesadas += 1;
									
									statement.close();

								} catch (Exception e) {

									e.printStackTrace();

									System.exit(0);

								}

							}

							//Recorro el cuerpo de los asintos
							for (AsientoDetalle detalle : bodyList) {
								
								sumaDebe += Double. parseDouble(detalle.getImpDebe());

								sumaHaberes += Double. parseDouble(detalle.getImpHaber());

								try {

									Statement statement = connection.createStatement();

									System.out.println("INSERT INTO AsientoDetalle (NroAsiento, FechaAsiento, NroRenglon, NroCta, NombreCta, NIF, ImpDebe, ImpHaber, Moneda, Concepto) VALUES (" + detalle.getNroAsiento() + ", #" + detalle.dateToString(detalle.getFechaAsiento()) + "#, '" + detalle.getNroRenglon() + "', '" + detalle.getNroCta() + "', '" + detalle.getNombreCta() + "', '" + detalle.getNIF() + "', '" + detalle.getImpDebe() + "', '" + detalle.getImpHaber() + "', '" + detalle.getMoneda() + "', '" + detalle.getConcepto() + "')");

									statement.executeUpdate("INSERT INTO AsientoDetalle (NroAsiento, FechaAsiento, NroRenglon, NroCta, NombreCta, NIF, ImpDebe, ImpHaber, Moneda, Concepto) VALUES (" + detalle.getNroAsiento() + ", #" + detalle.dateToString(detalle.getFechaAsiento()) + "#, " + detalle.getNroRenglon() + ", '" + detalle.getNroCta() + "', '" + detalle.getNombreCta() + "', '" + detalle.getNIF() + "', " + detalle.getImpDebe() + ", " + detalle.getImpHaber() + ", '" + detalle.getMoneda() + "', '" + detalle.getConcepto() + "')");

									registrosProcesados += 1;
									
									statement.close();

								} catch (Exception e) {

									e.printStackTrace();

								}

							}

							Statement statement = connection.createStatement();
							
							sumaDebeString = df.format(sumaDebe);
							
							sumaHaberesString = df.format(sumaDebe);
							
							System.out.println("INSERT INTO AsientosTotales (Ejercicio, Mes, Segmento, DebeEsperado, DebeProcesado, HaberEsperado, HaberProcesado, CabecerasEsperadas, CabecerasProcesadas, RegistrosEsperados, RegistrosProcesados) VALUES (" + ejercicio + ", '" + mes + "', " + segmento + ", '" + debeEsperado + "', '" + haberEsperado + "', '" + sumaDebeString + "', '" + sumaHaberesString + "', '" + cabecerasEsperadas + "', '" + cabecerasProcesadas + "', '" + registrosEsperados + "', '" + registrosProcesados + "')");
							
							statement.executeUpdate("INSERT INTO AsientosTotales (Ejercicio, Mes, Segmento, DebeEsperado, DebeProcesado, HaberEsperado, HaberProcesado, CabecerasEsperadas, CabecerasProcesadas, RegistrosEsperados, RegistrosProcesados) VALUES (" + ejercicio + ", '" + mes + "', " + segmento + ", '" + debeEsperado + "', '" + haberEsperado + "', '" + sumaDebeString + "', '" + sumaHaberesString + "', '" + cabecerasEsperadas + "', '" + cabecerasProcesadas + "', '" + registrosEsperados + "', '" + registrosProcesados + "')");

							statement.close();
							
							//Inicializo las variables
							segmento = 0;
							ejercicio = 0;
							cabecerasEsperadas = 0;
							registrosEsperados = 0;
							cabecerasProcesadas = 0;
							registrosProcesados = 0;
							sumaDebe = 0;
							sumaHaberes = 0;
							st = null;
							
						}catch (Exception e) {
							e.printStackTrace();
						}
						

						break;

					}
					
					
				}
				
				// Cierro BufferedReader así puedo reusar el archivo
				br.close();

			}

		}
	}
}
