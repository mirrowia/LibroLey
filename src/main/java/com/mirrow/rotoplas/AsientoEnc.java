package com.mirrow.rotoplas;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class AsientoEnc {

	private String NroAsiento;

	private Date FechaAsiento;

	private String NDoc;

	private String Referencia;

	private String Concepto;

	// Setters and Getters

	public String getNroAsiento() {
		return NroAsiento;
	}

	public void setNroAsiento(String nroAsiento) {
		NroAsiento = nroAsiento;
	}

	public Date getFechaAsiento() {
		return FechaAsiento;
	}

	public void setFechaAsiento(Date date) {
		FechaAsiento = date;
	}

	public String getNDoc() {
		return NDoc;
	}

	public void setNDoc(String nDoc) {
		NDoc = nDoc;
	}

	public String getReferencia() {
		return Referencia;
	}

	public void setReferencia(String referencia) {
		Referencia = referencia;
	}

	public String getConcepto() {
		
		return Concepto;
	}

	public void setConcepto(String concepto) {
		Concepto = concepto;
	}

	public Date stringToDate(String sFecha) {
		
		Date fecha = null;
		
		try {
			
			fecha = new SimpleDateFormat("dd/MM/yyyy").parse(sFecha);
			
			return fecha;
			
		} catch (ParseException e) {
			
			e.printStackTrace();
			
		}

		return fecha;
	}

	public String dateToString (Date date) {
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		String stringDate = dateFormat.format(date);

		return stringDate;
	}
	
}
