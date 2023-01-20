package com.mirrow.rotoplas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

@SuppressWarnings("unused")
public class MainBK {

	public static void main(String[] args) throws IOException {
		
		File[] files;

		// Crea un JFileChooser
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);

		// Muestra el JFileChooser
		int returnVal = fc.showOpenDialog(fc);

		// Acción que se toma una vez que el usuario acepta
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			files = fc.getSelectedFiles();

			// Se crea un BufferedReader para leer el archivo
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(files[0]), "UTF-8"));

			// Se crea una lista para almacenar las lineas leídas
			List<String> fileContents = new ArrayList<String>();

			String st;
			
			AsientoEnc encabezadoPrevio = new AsientoEnc();
			
			//Genero una instancia de la clase DbConnection
			DbConnection dbConnection = new DbConnection();
			
			dbConnection.startConnection();
			
			try {
				dbConnection.setStatement(dbConnection.getConnection().createStatement());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("f");
				System.exit(0);
			}
			
			// Recorre el archivo hasta que no tenga mas información
			while ((st = br.readLine()) != null) {
				
				
				
				// Si la linea en la que estamos no contiene el texto indicado, la salteamos
				if (!st.contains("|  ")) {
					
					// skip
					continue;
				}
				
				if (st.contains("|  0") || st.contains("|   1") || st.contains("|   2") || st.contains("|   3") || st.contains("|   4") || st.contains("|   5") || st.contains("|   6") || st.contains("|   7") || st.contains("|   8") || st.contains("|   9")) {
					fileContents.add(st);
					
					AsientoEnc encabezado = new AsientoEnc();
					
					String sFecha = st.substring(12, 22).replace(".", "/");
							
					encabezado.setNroAsiento(st.substring(3, 11));
					encabezado.setFechaAsiento(encabezado.stringToDate(sFecha));
					encabezado.setNDoc(st.substring(31, 41));
					encabezado.setReferencia(st.substring(42, 55).trim());
					encabezado.setConcepto(st.substring(56, 106).trim());
					encabezadoPrevio= encabezado;

					try {
						System.out.println("INSERT INTO AsientosEnc (NroAsiento, FechaAsiento, NDoc, Referencia, Concepto) VALUES (" + encabezado.getNroAsiento() + ", " + encabezado.dateToString(encabezado.getFechaAsiento()) + ", '" + encabezado.getNDoc() + "', '" + encabezado.getReferencia() + "', '" + encabezado.getConcepto() + "')");
						
						try {
							dbConnection.getStatement().executeUpdate("INSERT INTO AsientosEnc (NroAsiento, FechaAsiento, NDoc, Referencia, Concepto) VALUES (" + encabezado.getNroAsiento() + ", #" + encabezado.dateToString(encabezado.getFechaAsiento()) + "#, '" + encabezado.getNDoc() + "', '" + encabezado.getReferencia() + "', '" + encabezado.getConcepto() + "')");
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("a");
							System.exit(0);
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("b");
						System.exit(0);
					}
					

				} else if(st.contains("|     0") || st.contains("|     1") || st.contains("|     2") || st.contains("|     3") || st.contains("|     4") || st.contains("|     5") || st.contains("|     6") || st.contains("|     7") || st.contains("|     8") || st.contains("|     9")) {
					fileContents.add(st);
					
					AsientoDetalle detalle = new AsientoDetalle();
					
					detalle.setNroAsiento(encabezadoPrevio.getNroAsiento());
					detalle.setFechaAsiento(encabezadoPrevio.getFechaAsiento());
					detalle.setNroRenglon(st.substring(6, 9).trim());
					detalle.setNroCta(st.substring(10, 16).trim());
					detalle.setNombreCta(st.substring(18, 28).trim().replace("'", "''"));
					detalle.setNIF(st.substring(39, 52).trim());
					detalle.setImpDebe(st.substring(54, 70).trim().replace(",", ""));
					detalle.setImpHaber(st.substring(72, 89).trim().replace(",", ""));
					detalle.setMoneda(st.substring(91, 94).trim());
					detalle.setConcepto(st.substring(96, 106).trim());

					
					try {			
						System.out.println("INSERT INTO AsientoDetalle (NroAsiento, FechaAsiento, NroRenglon, NroCta, NombreCta, NIF, ImpDebe, ImpHaber, Moneda, Concepto) VALUES (" + encabezadoPrevio.getNroAsiento() + ", #" + encabezadoPrevio.dateToString(encabezadoPrevio.getFechaAsiento()) + "#, '" + detalle.getNroRenglon() + "', '" + detalle.getNroCta() + "', '" + detalle.getNombreCta() + "', '" + detalle.getNIF() + "', '" + detalle.getImpDebe() + "', '" + detalle.getImpHaber() + "', '" + detalle.getMoneda() + "', '" + detalle.getConcepto() + "')");
						
						try {
							dbConnection.getStatement().executeUpdate("INSERT INTO AsientoDetalle (NroAsiento, FechaAsiento, NroRenglon, NroCta, NombreCta, NIF, ImpDebe, ImpHaber, Moneda, Concepto) VALUES (" + encabezadoPrevio.getNroAsiento() + ", #" + encabezadoPrevio.dateToString(encabezadoPrevio.getFechaAsiento()) + "#, " + detalle.getNroRenglon() + ", '" + detalle.getNroCta() + "', '" + detalle.getNombreCta() + "', '" + detalle.getNIF() + "', " + detalle.getImpDebe() + ", " + detalle.getImpHaber() + ", '" + detalle.getMoneda() + "', '" + detalle.getConcepto() + "')");
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("c");
							System.exit(0);
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("d");
						System.exit(0);
					}
					
				} else {
					
					// skip
					continue;
					
				}
				
				
			}
			
			// Cierro BufferedReader así puedo reusar el archivo
		    br.close();
		    dbConnection.closeConnection();
		    
		    /*
		  // Creo un BufferedWriter
		    BufferedWriter bw = new BufferedWriter(new FileWriter(files[0]));

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
