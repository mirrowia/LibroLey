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
public class Main20230120 {

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
			
			
			for (File file : files) {

				// Se crea un BufferedReader para leer el archivo
				Charset inputCharset = Charset.forName("ISO-8859-1");
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), inputCharset));
	
				// Se crea una lista para almacenar las lineas leídas
				List<String> fileContents = new ArrayList<String>();
				
				// Variable que va a almacenar los renglones
				String st;
				
				
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
			            
						// Recorre el archivo hasta que no tenga mas información
						while ((st = br.readLine()) != null) {
							
							if (st.contains("|  0") || st.contains("|   1") || st.contains("|   2") || st.contains("|   3") || st.contains("|   4") || st.contains("|   5") || st.contains("|   6") || st.contains("|   7") || st.contains("|   8") || st.contains("|   9")) {
								
								cabecerasProcesadas += 1;
								
								fileContents.add(st);
								
								AsientoEnc encabezado = new AsientoEnc();
								
								String sFecha = st.substring(12, 22).replace(".", "/");
										
								encabezado.setNroAsiento(st.substring(3, 11));
								
								encabezado.setFechaAsiento(encabezado.stringToDate(sFecha));
								
								encabezado.setNDoc(st.substring(31, 41));
								
								encabezado.setReferencia(st.substring(42, 55).trim());
								
								encabezado.setConcepto(st.substring(56, 106).trim());
								
								encabezadoPrevio = encabezado;
			
								try {
										
									Statement statement = connection.createStatement();
									
									System.out.println("INSERT INTO AsientosEnc (NroAsiento, FechaAsiento, NDoc, Referencia, Concepto) VALUES (" + encabezado.getNroAsiento() + ", " + encabezado.dateToString(encabezado.getFechaAsiento()) + ", '" + encabezado.getNDoc() + "', '" + encabezado.getReferencia() + "', '" + encabezado.getConcepto() + "')");
										
									statement.executeUpdate("INSERT INTO AsientosEnc (NroAsiento, FechaAsiento, NDoc, Referencia, Concepto) VALUES (" + encabezado.getNroAsiento() + ", #" + encabezado.dateToString(encabezado.getFechaAsiento()) + "#, '" + encabezado.getNDoc() + "', '" + encabezado.getReferencia() + "', '" + encabezado.getConcepto() + "')");
										
									statement.close();
									
								} catch (Exception e) {
										
									e.printStackTrace();
										
									System.exit(0);
									
								}

							} else if(st.contains("|     0") || st.contains("|     1") || st.contains("|     2") || st.contains("|     3") || st.contains("|     4") || st.contains("|     5") || st.contains("|     6") || st.contains("|     7") || st.contains("|     8") || st.contains("|     9")) {
								
								registrosProcesados += 1;
								
								fileContents.add(st);
								
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
								
								sumaDebe += Double. parseDouble(detalle.getImpDebe());
								
								sumaHaberes += Double. parseDouble(detalle.getImpHaber());
																	
								try {
									
									Statement statement = connection.createStatement();

									System.out.println("INSERT INTO AsientoDetalle (NroAsiento, FechaAsiento, NroRenglon, NroCta, NombreCta, NIF, ImpDebe, ImpHaber, Moneda, Concepto) VALUES (" + encabezadoPrevio.getNroAsiento() + ", #" + encabezadoPrevio.dateToString(encabezadoPrevio.getFechaAsiento()) + "#, '" + detalle.getNroRenglon() + "', '" + detalle.getNroCta() + "', '" + detalle.getNombreCta() + "', '" + detalle.getNIF() + "', '" + detalle.getImpDebe() + "', '" + detalle.getImpHaber() + "', '" + detalle.getMoneda() + "', '" + detalle.getConcepto() + "')");
									
									statement.executeUpdate("INSERT INTO AsientoDetalle (NroAsiento, FechaAsiento, NroRenglon, NroCta, NombreCta, NIF, ImpDebe, ImpHaber, Moneda, Concepto) VALUES (" + encabezadoPrevio.getNroAsiento() + ", #" + encabezadoPrevio.dateToString(encabezadoPrevio.getFechaAsiento()) + "#, " + detalle.getNroRenglon() + ", '" + detalle.getNroCta() + "', '" + detalle.getNombreCta() + "', '" + detalle.getNIF() + "', " + detalle.getImpDebe() + ", " + detalle.getImpHaber() + ", '" + detalle.getMoneda() + "', '" + detalle.getConcepto() + "')");
										
									statement.close();
									
								} catch (Exception e) {
										
									e.printStackTrace();
	
									System.exit(0);
								}
								
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
								
								try {
									
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
									
									Statement statement = connection.createStatement();

									System.out.println("INSERT INTO AsientosTotales (Mes, Segmento, DebeEsperado, DebeProcesado, HaberEsperado, HaberProcesado, CabecerasEsperadas, CabecerasProcesadas, RegistrosEsperados, RegistrosProcesados) VALUES ('" + mes + "', " + segmento + ", " + debeEsperado + ", " + haberEsperado + ", " + sumaDebe + ", " + sumaHaberes + ", " + cabecerasEsperadas + ", " + cabecerasProcesadas + ", " + registrosEsperados + ", " + registrosProcesados + ")");
									
									statement.executeUpdate("INSERT INTO AsientosTotales (Mes, Segmento, DebeEsperado, DebeProcesado, HaberEsperado, HaberProcesado, CabecerasEsperadas, CabecerasProcesadas, RegistrosEsperados, RegistrosProcesados) VALUES ('" + mes + "', " + segmento + ", '" + debeEsperado + "', '" + haberEsperado + "', '" + sumaDebe + "', '" + sumaHaberes + "', '" + cabecerasEsperadas + "', '" + cabecerasProcesadas + "', '" + registrosEsperados + "', '" + registrosProcesados + "')");
										
									statement.close();
									
								} catch (Exception e) {
										
									e.printStackTrace();

									System.exit(0);
								}
								
								connection.close();
			
								
								break;
								
							}
							
						}
						
			        }
				 
			        catch(SQLException sqlex){
			        	
			            sqlex.printStackTrace();
			            System.exit(0);
			        }

				// Cierro BufferedReader así puedo reusar el archivo
			    br.close();
			    
			  /*  
			  // Creo un BufferedWriter
			    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
	
			    // Recorro la lista
			    for (String s : fileContents) {
			    	
			        // Escribo la linea al archivo
			        bw.write(s);
			        bw.newLine();
			        
			    }
	
			    // Cierro el Writer
			    bw.close();
			    */
			}
		}
	}
	
}
