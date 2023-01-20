package com.mirrow.rotoplas;

import java.util.Date;

public class AsientoDetalle {
	
	private String NroAsiento;
	private Date FechaAsiento;
	private String NroRenglon;
	private String NroCta;
	private String NombreCta;
	private String NIF;
	private String ImpDebe;
	private String ImpHaber;
	private String Moneda;
	private String Concepto;

	//Getters and Setters
	
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
	public String getNroRenglon() {
		return NroRenglon;
	}
	public void setNroRenglon(String nroRenglon) {
		NroRenglon = nroRenglon;
	}
	public String getNroCta() {
		return NroCta;
	}
	public void setNroCta(String nroCta) {
		NroCta = nroCta;
	}
	public String getNombreCta() {
		return NombreCta;
	}
	public void setNombreCta(String nombreCta) {
		NombreCta = nombreCta;
	}
	public String getNIF() {
		return NIF;
	}
	public void setNIF(String nIF) {
		NIF = nIF;
	}
	public String getImpDebe() {
		return ImpDebe;
	}
	public void setImpDebe(String impDebe) {
		ImpDebe = impDebe;
	}
	public String getImpHaber() {
		return ImpHaber;
	}
	public void setImpHaber(String impHaber) {
		ImpHaber = impHaber;
	}
	public String getMoneda() {
		return Moneda;
	}
	public void setMoneda(String moneda) {
		Moneda = moneda;
	}
	public String getConcepto() {
		return Concepto;
	}
	public void setConcepto(String concepto) {
		Concepto = concepto;
	}
	
	
}
