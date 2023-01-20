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
public class Main {

	public static void main(String[] args) throws IOException {

		File[] files;
		int segmento = 0;
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
			List<String> headerList = new ArrayList<String>();

			List<String> bodyList = new ArrayList<String>();

			for (File file : files) {

				// Se crea un BufferedReader para leer el archivo
				Charset inputCharset = Charset.forName("ISO-8859-1");

				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), inputCharset));

				// Variable que va a almacenar los renglones
				String st;

				// Recorre el archivo hasta que no tenga mas información
				while ((st = br.readLine()) != null) {

					if (st.contains("|  0") || st.contains("|   1") || st.contains("|   2") || st.contains("|   3") || st.contains("|   4") || st.contains("|   5") || st.contains("|   6") || st.contains("|   7") || st.contains("|   8") || st.contains("|   9")) {
						
						AsientoEnc encabezado = new AsientoEnc();

						String sFecha = st.substring(12, 22).replace(".", "/");

						encabezado.setNroAsiento(st.substring(3, 11));

						encabezado.setFechaAsiento(encabezado.stringToDate(sFecha));

						encabezado.setNDoc(st.substring(31, 41));

						encabezado.setReferencia(st.substring(42, 55).trim());

						encabezado.setConcepto(st.substring(56, 106).trim());

						encabezadoPrevio = encabezado;

						headerList.add(st);

					} else if(st.contains("|     0") || st.contains("|     1") || st.contains("|     2") || st.contains("|     3") || st.contains("|     4") || st.contains("|     5") || st.contains("|     6") || st.contains("|     7") || st.contains("|     8") || st.contains("|     9")) {

						bodyList.add(st);

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
							for (String string : headerList) {

								AsientoEnc encabezado = new AsientoEnc();

								String sFecha = string.substring(12, 22).replace(".", "/");

								encabezado.setNroAsiento(string.substring(3, 11));

								encabezado.setFechaAsiento(encabezado.stringToDate(sFecha));

								encabezado.setNDoc(string.substring(31, 41));

								encabezado.setReferencia(string.substring(42, 55).trim());

								encabezado.setConcepto(string.substring(56, 106).trim());

								encabezadoPrevio = encabezado;


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
							for (String string : bodyList) {

								AsientoDetalle detalle = new AsientoDetalle();

								detalle.setNroAsiento(encabezadoPrevio.getNroAsiento());

								detalle.setFechaAsiento(encabezadoPrevio.getFechaAsiento());

								detalle.setNroRenglon(string.substring(6, 9).trim());

								detalle.setNroCta(string.substring(10, 16).trim());

								detalle.setNombreCta(string.substring(18, 38).trim().replace("'", "''"));

								detalle.setNIF(string.substring(39, 52).trim());

								detalle.setImpDebe(string.substring(54, 70).trim().replace(",", ""));

								detalle.setImpHaber(string.substring(72, 89).trim().replace(",", ""));

								detalle.setMoneda(string.substring(91, 94).trim());

								detalle.setConcepto(string.substring(96, 106).trim());

								sumaDebe += Double. parseDouble(detalle.getImpDebe());

								sumaHaberes += Double. parseDouble(detalle.getImpHaber());

								try {

									Statement statement = connection.createStatement();

									System.out.println("INSERT INTO AsientoDetalle (NroAsiento, FechaAsiento, NroRenglon, NroCta, NombreCta, NIF, ImpDebe, ImpHaber, Moneda, Concepto) VALUES (" + encabezadoPrevio.getNroAsiento() + ", #" + encabezadoPrevio.dateToString(encabezadoPrevio.getFechaAsiento()) + "#, '" + detalle.getNroRenglon() + "', '" + detalle.getNroCta() + "', '" + detalle.getNombreCta() + "', '" + detalle.getNIF() + "', '" + detalle.getImpDebe() + "', '" + detalle.getImpHaber() + "', '" + detalle.getMoneda() + "', '" + detalle.getConcepto() + "')");

									statement.executeUpdate("INSERT INTO AsientoDetalle (NroAsiento, FechaAsiento, NroRenglon, NroCta, NombreCta, NIF, ImpDebe, ImpHaber, Moneda, Concepto) VALUES (" + encabezadoPrevio.getNroAsiento() + ", #" + encabezadoPrevio.dateToString(encabezadoPrevio.getFechaAsiento()) + "#, " + detalle.getNroRenglon() + ", '" + detalle.getNroCta() + "', '" + detalle.getNombreCta() + "', '" + detalle.getNIF() + "', " + detalle.getImpDebe() + ", " + detalle.getImpHaber() + ", '" + detalle.getMoneda() + "', '" + detalle.getConcepto() + "')");

									registrosProcesados += 1;
									
									statement.close();

								} catch (Exception e) {

									e.printStackTrace();

									System.exit(0);
								}

							}

							Statement statement = connection.createStatement();

							System.out.println("INSERT INTO AsientosTotales (Mes, Segmento, DebeEsperado, DebeProcesado, HaberEsperado, HaberProcesado, CabecerasEsperadas, CabecerasProcesadas, RegistrosEsperados, RegistrosProcesados) VALUES ('" + mes + "', " + segmento + ", " + debeEsperado + ", " + haberEsperado + ", " + sumaDebe + ", " + sumaHaberes + ", " + cabecerasEsperadas + ", " + cabecerasProcesadas + ", " + registrosEsperados + ", " + registrosProcesados + ")");

							statement.executeUpdate("INSERT INTO AsientosTotales (Mes, Segmento, DebeEsperado, DebeProcesado, HaberEsperado, HaberProcesado, CabecerasEsperadas, CabecerasProcesadas, RegistrosEsperados, RegistrosProcesados) VALUES ('" + mes + "', " + segmento + ", '" + debeEsperado + "', '" + haberEsperado + "', '" + sumaDebe + "', '" + sumaHaberes + "', '" + cabecerasEsperadas + "', '" + cabecerasProcesadas + "', '" + registrosEsperados + "', '" + registrosProcesados + "')");

							statement.close();
							
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						break;

					}else {
						br.readLine();
					}

				}
				
				// Cierro BufferedReader así puedo reusar el archivo
				br.close();

			}

		}
	}
}
